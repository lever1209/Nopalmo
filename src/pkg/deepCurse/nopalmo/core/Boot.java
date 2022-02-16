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

	public static void main(String[] args) {

		// TODO using join and a while last time + 15000 < current time, then kill and
		// proceed as a failure

//		settings.actions.put("phoenix-update-confirm", (PhoenixRuntime runtime) -> {
//			LogHelper.log("Received <phoenix-update-confirm>");
//		});

		logger.info("Booting: <" + pid + ">");

		long preBootTime = System.currentTimeMillis();

		isProd = args[2].contentEquals("prod");

		logger.info("Locking System");

		try {
			if (Locks.dirLock(isProd ? "nopalmo.lock" : "chaos.lock")) {
				logger.info("Is locked, shutting down. . .");
				System.exit(3);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		logger.info("System locked. . .");

		try {
			logger.info("Connecting to mariadb:nopalmo");
			NopalmoDBTools.init(isProd ? "nopalmo" : "chaos", "nopalmo", args[1]);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Failed to connect\nShutting down. . .");
			System.exit(4);
		}
		logger.info("Connected. . .");

		try {
//						bot = JDABuilder.createDefault(args[0]).setChunkingFilter(ChunkingFilter.ALL)
//								.setMemberCachePolicy(MemberCachePolicy.ALL).enableIntents(GatewayIntent.GUILD_MEMBERS)
//								.setActivity(Activity.watching("Loading users...")).setIdle(true)
//								.addEventListeners(new GuildMessageReceivedListener())
//								.addEventListeners(new DirectMessageReceivedListener()).build().awaitReady();

			bot = JDABuilder.createDefault(args[0])
					.setActivity(Activity.of(ActivityType.WATCHING, "the loading bar. . ."))
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

		logger.info("Using account: " + bot.getSelfUser().getName());

		bot.getPresence().setStatus(OnlineStatus.ONLINE);
		bot.getPresence().setActivity(Activity.listening("Infected Mushroom"));

		long bootTime = System.currentTimeMillis() - preBootTime;

		logger.info("Taken " + bootTime + "ms to boot");

		logger.info("Starting looping thread");
		Thread loopingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				loop();
			}
		}, "looping-thread");
		loopingThread.start();
		logger.info("Looping thread started. . .");
	}

	public static void loop() {

		long lastTime = System.currentTimeMillis();
		long fifteenMins = lastTime;
		long fiveMins = lastTime;
		long threeMins = lastTime;
		long lastTimeUpdateStatus = lastTime;
		long lastTimeCheckUpdate = lastTime;

		long dynamicWait = Long.parseLong(GlobalDB.getGlobalValue("dynamicwait"));

		while (running) {

			long now = System.currentTimeMillis();

			if (now > lastTime + dynamicWait) { // dynamic wait loop
				lastTime = now;
				try {
					bot.getSelfUser();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (now > lastTimeCheckUpdate + 900000) {
				lastTimeCheckUpdate = now;
			}

			if (now > lastTimeUpdateStatus + dynamicWait
					&& GlobalDB.getGlobalValue("isshufflestatusenabled").contentEquals("true")) {
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
