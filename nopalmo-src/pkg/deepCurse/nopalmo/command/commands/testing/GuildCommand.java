package pkg.deepCurse.nopalmo.command.commands.testing;

import java.util.HashMap;

import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

public class GuildCommand implements GuildCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] { "guild" };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.TESTING;
	}

	@Override
	public String getHelp() {
		return "an example guild command";
	}

	@Override
	public void runGuildCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {

		blob.getChannel().sendMessage("Message sent via command").queue();

	}

	@Override
	public HashMap<String, Argument> getArguments() {
		HashMap<String, Argument> args = new HashMap<String, Argument>();

		args.put("arg", new Argument("arg", (CommandBlob blob) -> {
			blob.getChannel().sendMessage("message sent via argument runnable").queue();
		}).setPrefixRequirement(true).setAutoStartRunnable(true));

		return args;

	}

}
