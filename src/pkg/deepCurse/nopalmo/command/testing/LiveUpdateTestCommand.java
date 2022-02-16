package pkg.deepCurse.nopalmo.command.testing;

import java.io.File;
import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.command.CommandInterface.DualCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.ClassManager;
import pkg.deepCurse.nopalmo.manager.ClassManager.InternalReloadable;
import pkg.deepCurse.nopalmo.manager.CommandBlob;
import qj.util.ReflectUtil;

public class LiveUpdateTestCommand implements InternalReloadable<String, String>, DualCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] { "test-update" };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.TESTING;
	}

	@Override
	public String getHelp() {
		return "Used to test the live updater";
	}

	@Override
	public void runDualCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {
		File file = new File(System.getProperty("user.dir") + "/external-src/");
		ClassManager<String, InternalReloadable<String, String>> manager = new ClassManager<String, InternalReloadable<String, String>>();

		manager.addFile("testing", "testing.Testing", file);

		String string = (String) ReflectUtil.getField("string", manager.getClass("testing")).get(null);

		blob.getChannel().sendMessage(string).queue();

	}

	@Override
	public @Nullable HashMap<String, Argument> getArguments() {
		return null;
	}

}
