package pkg.deepCurse.nopalmo.command.commands.general;

import java.util.HashMap;

import pkg.deepCurse.nopalmo.command.CommandInterface.DualCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

public class Example implements DualCommandInterface{

	@Override
	public String[] getCommandCalls() {
		return new String[] {"owo"};
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.General;
	}

	@Override
	public String getHelp() {
		return "an example command";
	}

	@Override
	public void runDualCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {
		
		blob.getChannel().sendMessage("owo").queue();
		
	}
	
	@Override
	public HashMap<String, Argument> getArguments() {
		HashMap<String, Argument> args = new HashMap<String, Argument>();
		
		args.put("k", new Argument("k", (CommandBlob blob) -> {
			blob.getChannel().sendMessage("Dr. K").queue();
		}).setPrefixRequirement(true).setAutoStartRunnable(true));
		
		return args;
		
	}

}
