package pkg.deepCurse.nopalmo.command;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import net.dv8tion.jda.api.Permission;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

public interface CommandInterface { // TODO rewrite to implement type args?

	public abstract String[] getCommandCalls();

	public default String getCommandName() {
		return getCommandCalls()[0];
	}

	public default boolean isNSFW() {
		return false;
	}

	public default int getPremiumLevel() { // im probably never gonna use this, but ill leave it in for those who want
											// to
		// see how i would implement it
		return 0;
	}

	public abstract HelpPage getHelpPage();

	public enum HelpPage {
		General, Moderation, Fun, Info, Extra, TESTING, DEV, EGG
	}

	public String getHelp();

	public default String getUsage(boolean hasPermissionInfo) {

		StringBuilder sB = new StringBuilder();
		if (getArguments() != null) {
			for (Argument i : getArguments().values()) {
				if (!i.isDeveloper() || (hasPermissionInfo && i.isDeveloper())) {
					sB.append(i.isRequired() ? "<" : "[");
					if (i.getPrefixRequirement()) {
						sB.append(Argument.argumentPrefix);
					}
					sB.append(i.getArgName() + (i.isRequired() ? "> " : "] "));
				}
			}
		}

		return (Global.prefix + getCommandName() + " " + sB.toString()).strip();
	}

	public default int getTimeout() {
		return 0;
	}

	@Nullable
	public HashMap<String, Argument> getArguments();

	public interface DualCommandInterface extends PrivateCommandInterface, GuildCommandInterface {
		@Override
		public default void runGuildCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {
			runDualCommand(blob, argumentMap);
		}

		@Override
		public default void runPrivateCommand(CommandBlob blob, HashMap<String, Argument> argumentMap)
				throws Exception {
			runDualCommand(blob, argumentMap);
		}

		public void runDualCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception;

	}

	public interface PrivateCommandInterface extends CommandInterface {
		public void runPrivateCommand(CommandBlob blob, HashMap<String, Argument> argumentList) throws Exception;
	}

	public interface GuildCommandInterface extends CommandInterface {
		public void runGuildCommand(CommandBlob blob, HashMap<String, Argument> argumentList) throws Exception;

		public default Permission[] getRequiredPermissions() {
			return null;
		}

		public default Permission getRequiredPermission() {
			return null;
		}
	}

}
