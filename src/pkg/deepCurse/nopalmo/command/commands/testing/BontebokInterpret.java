package pkg.deepCurse.nopalmo.command.commands.testing;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.bontebok.core.BontebokInterpreter;
import pkg.deepCurse.nopalmo.command.CommandInterface.DualCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

public class BontebokInterpret implements DualCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] { "bontebok", "interpret", "int", "bo" };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.TESTING;
	}

	@Override
	public String getHelp() {
		return "This command will interpret a string using the bontebok interpreter";
	}

	@Override
	public @Nullable HashMap<String, Argument> getArguments() {
		return null;
	}

	@Override
	public void runDualCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {

		blob.getChannel().sendMessage("Interpreting. . .").queue();

		String returnValue = BontebokInterpreter.InterpretString(argumentMap.get("null").getWildCardString(), null);

		switch (returnValue.substring(0, 1)) {
//		case "0":
//			blob.getChannel().sendMessage("Operation completed with return value 0").queue();
//			break;
		case "1":
			blob.getChannel().sendMessage("Operation failed with return value 1"
					+ (returnValue.substring(1).isBlank() ? "" : ": " + returnValue.substring(1))).queue();
			break;
		}
	}

}
