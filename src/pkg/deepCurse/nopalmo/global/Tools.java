package pkg.deepCurse.nopalmo.global;

import net.dv8tion.jda.api.entities.MessageChannel;
import pkg.deepCurse.nopalmo.command.CommandInterface;

public class Tools {

	public static void wrongUsage(MessageChannel messageChannel, CommandInterface command) {
		messageChannel.sendMessage("Wrong Command Usage!\n" + command.getUsage(false)).queue();
	}

	public static void invalidPermissions(MessageChannel messageChannel, CommandInterface command) {
		messageChannel.sendMessage("Sorry, but you are not allowed to use that! Try this:\n" + command.getUsage(false))
				.queue();
	}

//	public static void wrongUsage(MessageChannel tc, DirectCommandInterface c) {
//		tc.sendMessage("Wrong Command Usage!\n" + c.getUsage()).queue();
//	}

}
