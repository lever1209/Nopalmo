package pkg.deepCurse.nopalmo.listener;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.impl.SimpleLoggerFactory;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.DeveloperDB;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.GlobalDB;
import pkg.deepCurse.nopalmo.global.Reactions;

public class MessageReceivedListener extends ListenerAdapter {

	private Logger logger = new SimpleLoggerFactory().getLogger(this.getClass().getSimpleName());

	@Override
	public void onReady(@Nonnull ReadyEvent event) {
		logger.info("MessageReceivedListener is now ready " + event.getGuildAvailableCount() + "/"
				+ event.getGuildTotalCount() + " : " + event.getGuildUnavailableCount() + " <"
				+ event.getResponseNumber() + ">");
	}

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		try {
			Message message = event.getMessage();
			String messageRaw = message.getContentRaw();

			if (messageRaw.contentEquals(GlobalDB.prefix + GlobalDB.prefix)
					&& DeveloperDB.hasPermission(event.getAuthor().getIdLong(), "canpoweroffbot")) {

				message.addReaction(Reactions.getReaction(":eggplant")).complete();

				event.getJDA().shutdown();
				System.exit(7);
			}

			if (!event.getAuthor().isBot()) {
				Boot.commandManager.startCommand(event);
			}
		} catch (Throwable e) {
			event.getChannel().sendMessage(e.toString()).queue();
			e.printStackTrace();
		}
	}
}
