package pkg.deepCurse.nopalmo.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

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
import pkg.deepCurse.nopalmo.server.socket.Socks;
import pkg.deepCurse.nopalmo.utils.LogHelper;
import pkg.deepCurse.phoenixRuntime.core.PhoenixCommandManager;
import pkg.deepCurse.phoenixRuntime.core.PhoenixInterface;
import pkg.deepCurse.phoenixRuntime.core.PhoenixRuntime;
import pkg.deepCurse.phoenixRuntime.core.PhoenixSettings;

public class Boot {

	public static JDA bot; // TODO create sharding handler
	private static Logger logger = new SimpleLoggerFactory().getLogger(Boot.class.getSimpleName());
	public static final CommandManager commandManager = new CommandManager();
//	public static BontebokManager bontebokManager = null;

	public static boolean isProd = false;
	public static final long pid = ProcessHandle.current().pid();
	public static boolean running = true;

	public static void main(String[] args) {

		PhoenixSettings settings = new PhoenixSettings().setAuthentication(args[3]).setCommandSplitRegex(", ")
				.setCommandManager(new PhoenixCommandManager());

		// TODO using join and a while last time + 15000 < current time, then kill and
		// proceed as a failure

		settings.commandManager.addCommand("phoenix-update", (PhoenixRuntime runtime, List<String> commandArgs) -> {
			logger.info("Received <phoenix-update>");

			Socks.sendStringSock(settings.address, settings.commonPort, "phoenix-update-confirm");

			logger.info("Sent <phoenix-update-confirm>");

			if (bot != null) {
				bot.shutdown();
			}

			runtime.shutdown(9);
		});

//		settings.actions.put("phoenix-update-confirm", (PhoenixRuntime runtime) -> {
//			LogHelper.log("Received <phoenix-update-confirm>");
//		});

		PhoenixRuntime runtime = new PhoenixRuntime(settings, new PhoenixInterface() {

			@Override
			public void boot() {
				logger.info("Booting: <" + pid + ">");

				long preBootTime = System.currentTimeMillis();

				isProd = args[2].contentEquals("prod");

				try {
					logger.info("Connecting to mariadb:nopalmo"+args[1]);
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

				logger.info("Starting loop");
				loop();
			}

			public void loop() {

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
		});

		runtime.setLockedRunnable(() -> {
			logger.info("System is locked\nSending <phoenix-update> instead. . . ");

			try {
				Socket cSocket = new Socket("127.0.0.1", settings.commonPort);
				DataOutputStream dOut = new DataOutputStream(cSocket.getOutputStream());
				dOut.writeUTF("phoenix-update");
				dOut.flush();
				dOut.close();
				cSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// settings.getRuntime().shutdown(0);

		});

		runtime.launch();

	}
}
