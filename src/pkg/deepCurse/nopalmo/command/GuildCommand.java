package pkg.deepCurse.nopalmo.command;

import net.dv8tion.jda.api.Permission;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;

public abstract class GuildCommand {

	public abstract void run(GuildCommandBlob blob, GuildCommandManager commandManager) throws Exception;

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

	public Permission[] getRequiredPermissions() {
		return null;
	}

	public boolean isPremium() { // im probably never gonna use this, but ill leave it in for those who want to
									// see how i would implement it
		return false;
	}
	
	public abstract HelpPage getHelpPage();
	
	public enum HelpPage {
		General, DEV, EGG, Moderation, Fun, Info
	}
	
}
