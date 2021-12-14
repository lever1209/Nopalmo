package pkg.deepCurse.nopalmo.command.commands.info;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.command.CommandInterface.DualCommandInterface;
import pkg.deepCurse.nopalmo.global.Reactions;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;
import pkg.deepCurse.nopalmo.manager.StatusManager;
import pkg.deepCurse.nopalmo.utils.LogHelper;

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
		StringBuilder sB = new StringBuilder();

		LogHelper.log("Init reaction/emote list", getClass());
		sB.append("Init reaction/emote list\n");
		try {
			Reactions.init();
			LogHelper.log("Initialized reaction/emote list. . .", getClass());
			sB.append("Initialized reaction/emote list. . .\n");
		} catch (Exception e) {
			LogHelper.log("Failed to initialize reaction/emote list. . .\n" + e + "\n", getClass());
			sB.append("Failed to initialize reaction/emote list. . .\n" + e + "\n");
		}

		LogHelper.log("Init command list", getClass());
		sB.append("Init command list\n");
		try {
			blob.getCommandManager().init();
			LogHelper.log("Initialized command list. . .", getClass());
			sB.append("Initialized command list. . .\n");
		} catch (Exception e) {
			LogHelper.log("Failed to initialize command list. . .\n" + e + "\n", getClass());
			sB.append("Failed to initialize command list. . .\n" + e + "\n");
		}

		LogHelper.log("Init status list", getClass());
		sB.append("Init status list\n");
		try {
			StatusManager.init();
			LogHelper.log("Initialized status list. . .", getClass());
			sB.append("Initialized status list. . .\n");
		} catch (Exception e) {
			LogHelper.log("Failed to initialize status list. . .\n" + e + "\n", getClass());
			sB.append("Failed to initialize status list. . .\n" + e + "\n");
		}

		blob.getChannel().sendMessage(sB.toString()).queue();

	}

}
