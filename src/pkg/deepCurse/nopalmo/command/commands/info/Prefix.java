package pkg.deepCurse.nopalmo.command.commands.info;

import java.util.HashMap;

import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;

public class Prefix implements GuildCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] { "prefix", };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.General;
	}

	@Override
	public void runGuildCommand(GuildCommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
		
	}

	@Override
	public HashMap<String, Argument> getArguments() {
		HashMap<String, Argument> args = new HashMap<String, Argument>();

		args.put("prefix", new Argument("prefix").setIsWildcard(true));

		return args;
	}

}
