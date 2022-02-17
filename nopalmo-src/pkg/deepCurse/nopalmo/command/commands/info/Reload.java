package pkg.deepCurse.nopalmo.command.commands.info;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.command.CommandInterface.DualCommandInterface;
import pkg.deepCurse.nopalmo.core.Loader;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

public class Reload implements DualCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] { "reload", "r" };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.DEV;
	}

	@Override
	public String getHelp() {
		return "you should not need help using this command";
	}

	@Override
	public @Nullable HashMap<String, Argument> getArguments() {
		return null;
	}

	@Override
	public void runDualCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {
//		blob.getChannel().sendMessage("Refreshing caches. . .").queue();

		blob.getChannel().sendMessage(Loader.init()).queue();
	}
}
