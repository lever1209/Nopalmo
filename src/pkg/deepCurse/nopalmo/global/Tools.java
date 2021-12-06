package pkg.deepCurse.nopalmo.global;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import pkg.deepCurse.nopalmo.command.DirectCommand;
import pkg.deepCurse.nopalmo.command.GuildCommand;

public class Tools {

	public static void wrongUsage(TextChannel tc, GuildCommand c) {
		tc.sendMessage("Wrong Command Usage!\n" + c.getUsage()).queue();
	}
	
	public static void wrongUsage(MessageChannel tc, DirectCommand c) {
		tc.sendMessage("Wrong Command Usage!\n" + c.getUsage()).queue();
	}

}
