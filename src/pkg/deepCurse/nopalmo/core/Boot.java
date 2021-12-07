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
import pkg.deepCurse.nopalmo.listener.DirectMessageReceivedListener;
import pkg.deepCurse.nopalmo.listener.GuildMessageReceivedListener;
import pkg.deepCurse.nopalmo.manager.DirectCommandManager;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;
import pkg.deepCurse.nopalmo.utils.Locks;
import pkg.deepCurse.nopalmo.utils.LogHelper;

public class Boot {

	public static JDA bot; // TODO create sharding handler
	public static DatabaseTools databaseTools = null;
	public static final GuildCommandManager guildCommandManager = new GuildCommandManager(); // move to master manager
	public static final DirectCommandManager directCommandManager = new DirectCommandManager(); // move to master
																								// manager

	public static boolean isProd = false;

	public static final long pid = ProcessHandle.current().pid();
	public static boolean running = true;

	public static void main(String[] args) {
		LogHelper.boot("Booting: <" + pid + ">");

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
		LogHelper.boot("Init guild commands list");
		guildCommandManager.init();
		LogHelper.boot("Initialized guild commands list. . .");

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

					.addEventListeners(new GuildMessageReceivedListener())
					.addEventListeners(new DirectMessageReceivedListener())

					.setEnableShutdownHook(true)

					.build();

		} catch (Exception e) {
			LogHelper.crash(e);
		}

		bot.getPresence().setStatus(OnlineStatus.ONLINE);
		bot.getPresence().setActivity(Activity.listening("Infected Mushroom"));

		long bootTime = System.currentTimeMillis() - preBootTime;

		LogHelper.boot("Taken " + bootTime + "ms to boot");

		// LogHelper.boot("Starting loop");
		// loop();
	}

	private static void loop() {

		long lastTime = System.currentTimeMillis();
		long fifteenMins = lastTime;
		long fiveMins = lastTime;
		long threeMins = lastTime;
		long lastTimeUpdateStatus = lastTime;
		long lastTimeCheckUpdate= lastTime;
		
		long dynamicWait = 0;
		
		while (running) {
			
			long now = System.currentTimeMillis();
			
			if (now > lastTime + dynamicWait) { // dynamic wait loop
				
			}
			
			if (now > lastTimeCheckUpdate + 900000) {
				
			}
			
			if (now > lastTimeUpdateStatus + dynamicWait && Global.isShuffleStatusEnabled()) {
				
			}
			
			if (now > fifteenMins + 900000) {
				
			}
			
			if (now > fiveMins + 300000) {
				
			}
			
			if (now > threeMins + 180000) {
				
			}
			
		}
	}

}
