package pkg.deepCurse.nopalmo.command;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;

public abstract class GuildCommand {

	public abstract void run(List<String> args, GuildMessageReceivedEvent guildMessage, GuildCommandManager commandManager) throws Exception;

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
