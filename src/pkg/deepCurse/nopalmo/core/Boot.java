package pkg.deepCurse.nopalmo.core;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import pkg.deepCurse.nopalmo.listener.GuildMessageReceivedListener;
import pkg.deepCurse.simpleLoggingGarbage.core.Log;

public class Boot {

	public static JDA bot;

	public static void main(String[] args) {
		Log.boot("Booting. . .");

		long preBootTime = System.currentTimeMillis();

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

		long bootTime = System.currentTimeMillis() - preBootTime;

	}

}
