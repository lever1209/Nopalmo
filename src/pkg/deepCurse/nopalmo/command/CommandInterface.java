package pkg.deepCurse.nopalmo.command;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import net.dv8tion.jda.api.Permission;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;
import pkg.deepCurse.nopalmo.manager.DirectCommandBlob;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;

public interface CommandInterface { // TODO rewrite to implement type args?

	public abstract String[] getCommandCalls();

	public default String getCommandName() {
		return getCommandCalls()[0];
	}

	public default boolean isNSFW() {
		return false;
	}

	public default boolean isPremium() { // im probably never gonna use this, but ill leave it in for those who want to
		// see how i would implement it
		return false;
	}

	public abstract HelpPage getHelpPage();

	public enum HelpPage {
		General, Moderation, Fun, Info, Extra, TESTING, DEV, EGG
	}

	public String getHelp();

	public default String getUsage() {
		return Global.prefix + getCommandName();
	}
	public default int getTimeout() {
		return 0;
	}

	@Nullable
	public default HashMap<String, Argument> getArguments() {
		return null;
	}

	public interface DualCommandInterface extends DirectCommandInterface, GuildCommandInterface {
		@Override
		public default void runGuildCommand(GuildCommandBlob blob, HashMap<String, Argument> argumentMap)
				throws Exception {
			runDualCommand(new CommandBlob(blob), argumentMap);
		}

		@Override
		public default void runDirectCommand(DirectCommandBlob blob, HashMap<String, Argument> argumentMap)
				throws Exception {
			runDualCommand(new CommandBlob(blob), argumentMap);
		}

		public void runDualCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception;

	}

	public interface DirectCommandInterface extends CommandInterface {
		public void runDirectCommand(DirectCommandBlob blob, HashMap<String, Argument> argumentList) throws Exception;
	}

	public interface GuildCommandInterface extends CommandInterface {
		public void runGuildCommand(GuildCommandBlob blob, HashMap<String, Argument> argumentList) throws Exception;

		public default Permission[] getRequiredPermissions() {
			return null;
		}

		public default Permission getRequiredPermission() {
			return null;
		}
	}

}
