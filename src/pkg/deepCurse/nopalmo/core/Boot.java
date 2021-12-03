package pkg.deepCurse.nopalmo.core;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import pkg.deepCurse.nopalmo.database.DatabaseTools;
import pkg.deepCurse.nopalmo.global.Reactions;
import pkg.deepCurse.nopalmo.listener.GuildMessageReceivedListener;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;
import pkg.deepCurse.simpleLoggingGarbage.core.Log;

public class Boot {

	public static JDA bot;
	public static DatabaseTools databaseTools = null;
	public static GuildCommandManager guildCommandManager = new GuildCommandManager(); // move to master manager
	public static boolean isProd = false;

	public static void main(String[] args) {
		Log.boot("Booting. . .");

		long preBootTime = System.currentTimeMillis();
		
		isProd = args[2].contentEquals("prod");
		
		Log.boot("Connecting to mariadb:nopalmo");
		try {
			databaseTools = new DatabaseTools(args[1]);
			Log.boot("Connected. . .");
		} catch (SQLException e1) {
			e1.printStackTrace();
			Log.boot("Failed to connect. . .\nShutting down. . .");
			System.exit(4);
		}
		
		Log.boot("Init reaction/emote list");
		Reactions.init();
		Log.boot("Initialized reaction/emote list. . .");
		Log.boot("Init guild commands list");
		guildCommandManager.init();
		Log.boot("Initialized guild commands list. . .");
		
		try {
			bot = JDABuilder.createDefault(args[0]).setChunkingFilter(ChunkingFilter.NONE)
					.setMemberCachePolicy(MemberCachePolicy.NONE).enableIntents(GatewayIntent.GUILD_MEMBERS)
					.setActivity(Activity.watching("Loading users...")).setIdle(true)
					.addEventListeners(new GuildMessageReceivedListener()).build().awaitReady();
		} catch (LoginException e) {
			Log.crash(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		bot.getPresence().setActivity(Activity.listening("Infected Mushroom"));
		
		long bootTime = System.currentTimeMillis() - preBootTime;
		
		System.out.println("Taken "+bootTime+"ms to boot");
		
	}
}
