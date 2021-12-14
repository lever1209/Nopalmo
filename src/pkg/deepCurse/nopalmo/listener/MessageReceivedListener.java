package pkg.deepCurse.nopalmo.listener;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.global.Reactions;
import pkg.deepCurse.nopalmo.utils.LogHelper;

public class MessageReceivedListener extends ListenerAdapter {

	@Override
	public void onReady(@Nonnull ReadyEvent event) {
		LogHelper.log("MessageReceivedListener is now ready\n" + event.getGuildAvailableCount() + "/"
				+ event.getGuildTotalCount() + " : " + event.getGuildUnavailableCount() + " <"
				+ event.getResponseNumber() + ">", getClass());
	}

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		Message message = event.getMessage();
		String messageRaw = message.getContentRaw();

		if (messageRaw.contentEquals(Global.prefix + Global.prefix)
				&& DatabaseTools.Tools.Developers.canPowerOffBot(event.getAuthor().getIdLong())) {

			message.addReaction(Reactions.getReaction(":eggplant")).complete();

			event.getJDA().shutdown();
			System.exit(7);
		}

		if (!event.getAuthor().isBot()) {
			Boot.commandManager.startCommand(event);
		}
	}
}
