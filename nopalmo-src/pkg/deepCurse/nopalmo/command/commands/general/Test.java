package pkg.deepCurse.nopalmo.command.commands.general;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

public class Test implements GuildCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] { "test" };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.TESTING;
	}

	@Override
	public String getHelp() {
		return "A command used to test various things";
	}

	@Override
	public boolean isNSFW() {
		return false;
	}

	@Override
	public int getPremiumLevel() {
		return 0;
	}

	@Override
	public void runGuildCommand(CommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
		blob.getChannel().sendMessage("Tested").queue();
	}

	@Override
	public @Nullable HashMap<String, Argument> getArguments() {
		return null;
	}

}
