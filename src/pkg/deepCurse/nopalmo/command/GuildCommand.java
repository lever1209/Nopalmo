package pkg.deepCurse.nopalmo.command;

import java.util.HashMap;

import net.dv8tion.jda.api.Permission;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;

public abstract class GuildCommand extends AbstractCommand {

	public abstract void runCommand(GuildCommandBlob blob, HashMap<String, Argument> argumentList) throws Exception;

	public abstract String[] getCommandCalls();

	public Permission[] getRequiredPermissions() {
		return null;
	}

	public Permission getRequiredPermission() {

		return null;
	}
}
