package pkg.deepCurse.nopalmo.command.commands;

import java.util.HashMap;

import pkg.deepCurse.nopalmo.command.CommandInterface.DualCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

public class Git implements DualCommandInterface {
	
	@Override
	public void runDualCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {
		blob.getChannel().sendMessage("Heres the link: https://github.com/lever1209/nopalmo").queue();
	}
	
	@Override
	public String[] getCommandCalls() {
		return new String[] { "git", "source", "github" };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.Info;
	}

}
