package pkg.deepCurse.nopalmo.command.commands.info;

import java.util.HashMap;

import pkg.deepCurse.nopalmo.command.CommandInterface.DualCommandInterface;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
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

	@Override
	public HashMap<String, Argument> getArguments() {
		HashMap<String, Argument> args = new HashMap<String, Argument>();

		args.put("test", new Argument("test", (CommandBlob blob) -> {
			
			blob.getChannel().sendMessage("This is the automatically running argument inside of " + this.getCommandName()).queue();
			
		}).setPrefixRequirement(true).setAutoStartRunnable(true));

		return args;
	}

	@Override
	public String getHelp() {
		return "Posts my github link";
	}
	
	@Override
	public String getCompleteUsage() {
		return Global.prefix + getCommandName() + " [-test]";
	}
	
}
