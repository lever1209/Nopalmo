package pkg.deepCurse.nopalmo.command;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;

public abstract class Command {
	
	public abstract void runCommand(GuildCommandBlob blob, GuildCommandManager commandManager,
			HashMap<String, Argument> argumentList) throws Exception;

	public abstract String[] getCommandCalls();

	public String getCommandName() {
		return getCommandCalls()[0];
	}

	public boolean isHidden() {
		return false;
	}

	public boolean isNSFW() {
		return false;
	}

	public boolean isPremium() { // im probably never gonna use this, but ill leave it in for those who want to
									// see how i would implement it
		return false;
	}

	public abstract HelpPage getHelpPage();

	public enum HelpPage {
		General, DEV, EGG, Moderation, Fun, Info
	}

	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Nullable
	public HashMap<String, Argument> getArguments() {
		return null;
	}

	
}
