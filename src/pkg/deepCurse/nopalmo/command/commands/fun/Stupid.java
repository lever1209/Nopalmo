package pkg.deepCurse.nopalmo.command.commands.fun;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import net.dv8tion.jda.api.entities.Member;
import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

public class Stupid implements GuildCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] { "stupid", "dumb" };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.Fun;
	}

	@Override
	public String getHelp() {
		return "This calls someone stupid, stupid";
	}

	@Override
	public @Nullable HashMap<String, Argument> getArguments() {
		return null;
	}

	@Override
	public void runGuildCommand(CommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
		StringBuilder sB = new StringBuilder();
		for (Member i : blob.getMessage().getMentionedMembers()) {
			sB.append(i.getAsMention() + " ");
		}
		blob.getMessage().delete().queue();
		blob.getChannel().sendMessage(blob.getAuthor().getName() + " calls you stupid! " + sB.toString()).queue();

	}

}
