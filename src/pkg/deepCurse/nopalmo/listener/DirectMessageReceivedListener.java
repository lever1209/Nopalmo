package pkg.deepCurse.nopalmo.listener;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;

public class DirectMessageReceivedListener extends ListenerAdapter {

	@Override
	public void onReady(@Nonnull ReadyEvent event) {
		System.out.println("DirectMessageReceivedListener is now ready. . .");
	}

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		Message message = event.getMessage();
		String messageRaw = message.getContentRaw();
		System.out.println(messageRaw + "\n<@!" + event.getJDA().getSelfUser().getIdLong() + ">");
		if (messageRaw.contentEquals(Global.prefix + Global.prefix)
				&& DatabaseTools.Tools.Developers.canPowerOffBot(event.getAuthor().getIdLong())) {

			// message.addReaction(Reactions.getReaction("galaxyThumb")).complete(); TODO re
			// enable

			// message.delete().complete();

			// pause thread as last resort

			event.getJDA().shutdown();
			System.exit(0);
		}

		String[] prefixArray = new String[] { Global.prefix, "<@! " + event.getJDA().getSelfUser().getIdLong() + ">" };
		// FIXME BROKEN PING PREFIX

		boolean shouldReturn = true;
		for (String i : prefixArray) { // TODO switch to [] to skip for loop?

			if (messageRaw.startsWith(i)) {
				// System.out.println("breaking");
				shouldReturn = false;
			}
		}

		// TODO add pre manager commands

		if (shouldReturn) {

			return;
		}

		if (!event.getAuthor().isBot() && !event.isFromGuild()) {
			Boot.directCommandManager.startCommand(event);
		}

	}

}
