package pkg.deepCurse.nopalmo.command.commands.general;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;

public class Test implements GuildCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] {"test"};
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
		return true;
	}
	
	@Override
	public int getPremiumLevel() {
		return 1;
	}
	
	@Override
	public void runGuildCommand(GuildCommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
		blob.getEvent().getChannel().sendMessage("Tested").queue();
	}

	@Override
	public @Nullable HashMap<String, Argument> getArguments() {
		return null;
	}

}
