package pkg.deepCurse.nopalmo.core;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.impl.SimpleLoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import pkg.deepCurse.nopalmo.StringExport;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.GlobalDB;
import pkg.deepCurse.nopalmo.listener.MessageReceivedListener;
import pkg.deepCurse.nopalmo.manager.CommandManager;
import pkg.deepCurse.nopalmo.manager.StatusManager;
import pkg.deepCurse.nopalmo.utils.Locks;
import pkg.deepCurse.nopalmo.utils.LogHelper;

public class Boot {

	public static JDA bot; // TODO create sharding handler
	private static Logger logger = new SimpleLoggerFactory().getLogger(Boot.class.getSimpleName());
	public static final CommandManager commandManager = new CommandManager();
//	public static BontebokManager bontebokManager = null;

	public static boolean isProd = false;
	public static final long pid = ProcessHandle.current().pid();
	public static boolean running = true;

	private static final String token = StringExport.getString("Boot.1"); //$NON-NLS-1$
	private static final String databasePassword = StringExport.getString("Boot.0"); //$NON-NLS-1$
	
	public static void boot() {

		// TODO using join and a while last time + 15000 < current time, then kill and
		// proceed as a failure

//		settings.actions.put("phoenix-update-confirm", (PhoenixRuntime runtime) -> {
//			LogHelper.log("Received <phoenix-update-confirm>");
//		});

		logger.info("Booting: <" + pid + ">"); //$NON-NLS-1$ //$NON-NLS-2$

		long preBootTime = System.currentTimeMillis();

		logger.info("Locking System"); //$NON-NLS-1$

		try {
			if (Locks.dirLock(isProd ? "nopalmo.lock" : "chaos.lock")) { //$NON-NLS-1$ //$NON-NLS-2$
				logger.info("Is locked, shutting down. . ."); //$NON-NLS-1$
				System.exit(3);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		logger.info("System locked. . ."); //$NON-NLS-1$

		try {
			logger.info("Connecting to mariadb:nopalmo"); //$NON-NLS-1$
			NopalmoDBTools.init(isProd ? "nopalmo" : "chaos", "nopalmo", databasePassword); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Failed to connect\nShutting down. . ."); //$NON-NLS-1$
			System.exit(4);
		}
		logger.info("Connected. . ."); //$NON-NLS-1$

		try {
//						bot = JDABuilder.createDefault(args[0]).setChunkingFilter(ChunkingFilter.ALL)
//								.setMemberCachePolicy(MemberCachePolicy.ALL).enableIntents(GatewayIntent.GUILD_MEMBERS)
//								.setActivity(Activity.watching("Loading users...")).setIdle(true)
//								.addEventListeners(new GuildMessageReceivedListener())
//								.addEventListeners(new DirectMessageReceivedListener()).build().awaitReady();

			bot = JDABuilder.createDefault(token)
					.setActivity(Activity.of(ActivityType.WATCHING, "the loading bar. . .")) //$NON-NLS-1$
					.setStatus(OnlineStatus.DO_NOT_DISTURB)

					.setMaxBufferSize(Integer.MAX_VALUE)

					.setChunkingFilter(ChunkingFilter.ALL).setMemberCachePolicy(MemberCachePolicy.ALL)

					.enableIntents(GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES,
							GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_MEMBERS,
							GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES)

					.enableCache(CacheFlag.EMOTE, CacheFlag.MEMBER_OVERRIDES)

					// .setIdle(true)

					.setAutoReconnect(true)

					.addEventListeners(new MessageReceivedListener())

					.setEnableShutdownHook(true)

					.build().awaitReady();

		} catch (Exception e) {
			LogHelper.crash(e);
		}

		Loader.init();

		logger.info("Using account: " + bot.getSelfUser().getName()); //$NON-NLS-1$

		bot.getPresence().setStatus(OnlineStatus.ONLINE);
		bot.getPresence().setActivity(Activity.listening("Infected Mushroom")); //$NON-NLS-1$

		long bootTime = System.currentTimeMillis() - preBootTime;

		logger.info("Taken " + bootTime + "ms to boot"); //$NON-NLS-1$ //$NON-NLS-2$

		logger.info("Starting looping thread"); //$NON-NLS-1$
		Thread loopingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				loop();
			}
		}, "looping-thread"); //$NON-NLS-1$
		loopingThread.start();
		logger.info("Looping thread started. . ."); //$NON-NLS-1$
	}

	public static void loop() {

		long lastTime = System.currentTimeMillis();
		long fifteenMins = lastTime;
		long fiveMins = lastTime;
		long threeMins = lastTime;
		long lastTimeUpdateStatus = lastTime;
		long lastTimeCheckUpdate = lastTime;

		long dynamicWait = Long.parseLong(GlobalDB.getGlobalValue("dynamicwait")); //$NON-NLS-1$

		while (running) {

			long now = System.currentTimeMillis();

			if (now > lastTime + dynamicWait) { // TODO revamp dynamic wait into individual tasks part of a runnable iterator based on time
				lastTime = now;
			}

			if (now > lastTimeCheckUpdate + 900000) {
				lastTimeCheckUpdate = now;
			}

			if (now > lastTimeUpdateStatus + dynamicWait
					&& GlobalDB.getGlobalValue("isshufflestatusenabled").contentEquals("true")) { //$NON-NLS-1$ //$NON-NLS-2$
				lastTimeUpdateStatus = now;

				StatusManager.shuffle(bot);

			}

			if (now > fifteenMins + 900000) {
				fifteenMins = now;

			}

			if (now > fiveMins + 300000) {
				fiveMins = now;
			}

			if (now > threeMins + 180000) {
				threeMins = now;
			}

		}

	}
}
