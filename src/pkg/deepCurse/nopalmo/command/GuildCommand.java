package pkg.deepCurse.nopalmo.command;

import net.dv8tion.jda.api.Permission;

public abstract class GuildCommand extends Command {
	
	public Permission[] getRequiredPermissions() {
		return null;
	}
	
	public Permission getRequiredPermission() {
		// TODO Auto-generated method stub
		return null;
	}
}
