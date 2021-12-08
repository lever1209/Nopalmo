package pkg.deepCurse.nopalmo.core;

import java.sql.SQLException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import pkg.deepCurse.nopalmo.database.DatabaseTools;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.global.Reactions;
import pkg.deepCurse.nopalmo.listener.MessageReceivedListener;
import pkg.deepCurse.nopalmo.manager.CommandManager;
import pkg.deepCurse.nopalmo.manager.StatusManager;
import pkg.deepCurse.nopalmo.utils.Locks;
import pkg.deepCurse.nopalmo.utils.LogHelper;

public class Boot {

	public static JDA bot; // TODO create sharding handler
	public static DatabaseTools databaseTools = null;
//	public static final GuildCommandManager guildCommandManager = new GuildCommandManager(); // move to master manager
//	public static final DirectCommandManager directCommandManager = new DirectCommandManager(); // move to master
//																								// manager

	public static boolean isProd = false;

	public static final long pid = ProcessHandle.current().pid();
	public static boolean running = true;
	public static final CommandManager commandManager = new CommandManager();

	public static void main(String[] args) {
		LogHelper.boot("Booting: <" + pid + ">");

		new Thread(() -> {
			long count = 0;
			while (true) {
				if (count++ > 1000000000) {
					count = 0;
					System.out.println("owo");
				}
			}
		}, "owo");

		LogHelper.boot("Testing Lock. . .");

		try {
			if (Locks.dirLock("nopalmo.lock")) {
				LogHelper.boot("Failed to lock. . .\nShutting down. . .");
				System.exit(3);
			} else {
				LogHelper.boot("Nopalmo is locked. . .");
			}
		} catch (Exception e) {
			LogHelper.crash(e);
		}

		long preBootTime = System.currentTimeMillis();

		isProd = args[2].contentEquals("prod");

		LogHelper.boot("Connecting to mariadb:nopalmo");
		try {
			databaseTools = new DatabaseTools(args[1]);
			LogHelper.boot("Connected. . .");
		} catch (SQLException | ClassNotFoundException e1) {
			e1.printStackTrace();
			LogHelper.boot("Failed to connect\nShutting down. . .");
			System.exit(4);
		}

		LogHelper.boot("Init reaction/emote list");
		Reactions.init();
		LogHelper.boot("Initialized reaction/emote list. . .");
		LogHelper.boot("Init commands list");
		commandManager.init();
		LogHelper.boot("Initialized commands list. . .");

		try {
//			bot = JDABuilder.createDefault(args[0]).setChunkingFilter(ChunkingFilter.ALL)
//					.setMemberCachePolicy(MemberCachePolicy.ALL).enableIntents(GatewayIntent.GUILD_MEMBERS)
//					.setActivity(Activity.watching("Loading users...")).setIdle(true)
//					.addEventListeners(new GuildMessageReceivedListener())
//					.addEventListeners(new DirectMessageReceivedListener()).build().awaitReady();

			bot = JDABuilder.createDefault(args[0])
					.setActivity(Activity.of(ActivityType.WATCHING, "the loading bar. . ."))
					.setStatus(OnlineStatus.DO_NOT_DISTURB)

					.setMaxBufferSize(Integer.MAX_VALUE)

					.setChunkingFilter(ChunkingFilter.ALL).setMemberCachePolicy(MemberCachePolicy.ALL)

					.enableIntents(GatewayIntent.DIRECT_MESSAGE_REACTIONS,

							GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS,
							GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS,

							GatewayIntent.GUILD_MESSAGES // , GatewayIntent.GUILD_VOICE_STATES
					)

					.enableCache(// CacheFlag.CLIENT_STATUS,
							CacheFlag.EMOTE, // CacheFlag.ACTIVITY,
							CacheFlag.MEMBER_OVERRIDES // , CacheFlag.VOICE_STATE
					)

					// .setIdle(true)

					.setAutoReconnect(true)

//					.addEventListeners(new GuildMessageReceivedListener())
//					.addEventListeners(new DirectMessageReceivedListener())
					.addEventListeners(new MessageReceivedListener())

					.setEnableShutdownHook(true)

					.build().awaitReady();

		} catch (Exception e) {
			LogHelper.crash(e);
		}

		bot.getPresence().setStatus(OnlineStatus.ONLINE);
		bot.getPresence().setActivity(Activity.listening("Infected Mushroom"));

		LogHelper.boot("Init status list");
		StatusManager.init();
		LogHelper.boot("Initialized status list. . .");

		long bootTime = System.currentTimeMillis() - preBootTime;

		LogHelper.boot("Taken " + bootTime + "ms to boot");

		LogHelper.boot("Starting loop");
		loop();
	}

	private static void loop() {

		long lastTime = System.currentTimeMillis();
		long fifteenMins = lastTime;
		long fiveMins = lastTime;
		long threeMins = lastTime;
		long lastTimeUpdateStatus = lastTime;
		long lastTimeCheckUpdate = lastTime;

		long dynamicWait = Global.getDynamicWait();

		while (running) {

			long now = System.currentTimeMillis();

			if (now > lastTime + dynamicWait) { // dynamic wait loop
				lastTime = now;
			}

			if (now > lastTimeCheckUpdate + 900000) {
				lastTimeCheckUpdate = now;
			}

			if (now > lastTimeUpdateStatus + dynamicWait && Global.isShuffleStatusEnabled()) {
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
