package pkg.deepCurse.nopalmo.command;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.manager.Argument;

public abstract class AbstractCommand { // TODO rewrite to implement type args?

	public abstract String[] getCommandCalls();

	public String getCommandName() {
		return getCommandCalls()[0];
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
		return null;
	}

	public String getUsage() {
		return null;
	}

	public int getTimeout() {
		return 0;
	}

	@Nullable
	public HashMap<String, Argument> getArguments() {
		return null;
	}

}
