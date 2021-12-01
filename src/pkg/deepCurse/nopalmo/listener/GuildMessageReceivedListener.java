package pkg.deepCurse.nopalmo.listener;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.global.Reactions;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;

public class GuildMessageReceivedListener extends ListenerAdapter {
	
	@Override
	public void onReady(@Nonnull ReadyEvent event) {
		System.out.println("GuildMessageReceivedListener is now ready\n" + event.getGuildAvailableCount() + "/"
				+ event.getGuildTotalCount() + " : " + event.getGuildUnavailableCount() + " <"
				+ event.getResponseNumber() + ">");
	}

	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		Message message = event.getMessage();
		String messageRaw = message.getContentRaw();
		String[] prefixArray = new String[] { DatabaseTools.Tools.Guild.Prefix.getPrefix(event.getGuild().getIdLong()),
				"<@!" + event.getJDA().getSelfUser().getIdLong() + ">" };

		boolean shouldReturn = true;
		for (String i : prefixArray) {
			if (messageRaw.startsWith(i)) {
				shouldReturn = false;
			}
		}

		if (messageRaw.contentEquals(Global.prefix + Global.prefix)
				&& DatabaseTools.Tools.Developers.canPowerOffBot(event.getAuthor().getIdLong())) {
			message.addReaction(Reactions.getReaction("galaxyThumb")).complete();
			
			System.out.println("Shutting down; id " + event.getAuthor().getIdLong() + " used");
			
			// pause thread as last resort
			
			event.getJDA().shutdown();
			System.exit(0);
		}
		
		// TODO add pre manager commands
		
		if (shouldReturn) {
			return;
		}
		
		if (!event.getAuthor().isBot()) {
			Boot.guildCommandManager.startCommand(event);
		}
		
	}

}
