package pkg.deepCurse.nopalmo.manager;

import java.util.ArrayList;
import java.util.List;

public class Argument {

	private int requiredArgs = 0;
	private String argName = null;
	private Argument[] subArgs = null;
	private boolean requiresPrefix = false;
	private Boolean isWildcard = false;
	private int position = -1;
	private String wildCardString = null;
	private boolean autoStartRunnable = false;
	private boolean skipOriginalTaskOnRunnable = false;
	private RunnableArg runnableArg = null;
	private String permissionLevel = null;
	private boolean isRequired = false;
	private boolean isDeveloper = false;
	private List<String> aliases = new ArrayList<String>();

	public static final String argumentPrefix = "-"; // This exists for the sole reason of customization and will
														// generally not change, ever, its recommended you keep it to
														// something other than empty to help ensure that what the user
														// entered is an arg and not something else

	/**
	 * @implNote This is an experimental feature that has potential, but i have no
	 *           use for it currently, imagine it akin to redstone, mojang had no
	 *           idea people would build computers or 9x9 piston doors out of it
	 * @param requiredArgs
	 * @param argName
	 * @param subArgs
	 */
	public Argument(int requiredArgs, String argName, Argument[] subArgs) {
		this.requiredArgs = requiredArgs;
		this.argName = argName;
		this.subArgs = subArgs;
	}

	/**
	 * @implNote This is an experimental feature that has potential, but i have no
	 *           use for it currently, imagine it akin to redstone, mojang had no
	 *           idea people would build computers or 9x9 piston doors out of it
	 * @param requiredArgs
	 * @param argName
	 * @param subArgs
	 */
	public Argument(int requiredArgs, String argName, Argument[] subArgs, RunnableArg runnableArg) {
		this.requiredArgs = requiredArgs;
		this.argName = argName;
		this.subArgs = subArgs;
		this.runnableArg = runnableArg;
	}

	/**
	 * @implNote This is an experimental feature that has potential, but i have no
	 *           use for it currently, imagine it akin to redstone, mojang had no
	 *           idea people would build computers or 9x9 piston doors out of it
	 * @param requiredArgs
	 * @param argName
	 * @param subArgs
	 */
	public Argument(String argName) {
		this.argName = argName;
	}

	/**
	 * @implNote This is an experimental feature that has potential, but i have no
	 *           use for it currently, imagine it akin to redstone, mojang had no
	 *           idea people would build computers or 9x9 piston doors out of it
	 * @param requiredArgs
	 * @param argName
	 * @param subArgs
	 */
	public Argument(String argName, RunnableArg runnableArg) {
		this.argName = argName;
		this.runnableArg = runnableArg;
	}

	public int getRequiredArgs() {
		return requiredArgs;
	}

//	public Argument setRequiredArgs(int requiredArgs) {
//		this.requiredArgs = requiredArgs;
//		return this;
//	}

	public String getArgName() {
		return argName;
	}

//	public Argument setArgName(String argName) {
//		this.argName = argName;
//		return this;
//	}

	public Argument[] getSubArgs() {
		return subArgs;
	}

//	public Argument setSubArgs(Argument[] subArgs) {
//		this.subArgs = subArgs;
//		return this;
//	}

	public Argument setIsWildcard(Boolean bool) {

		if (this.position <= -1) {
			throw new IllegalArgumentException("Cannot create a wildcard without a position; set a position first");
		}

		this.isWildcard = bool;
		return this;
	}

	public Boolean getIsWildcard() {
		return isWildcard;
	}

	public boolean isPrefixRequired() {
		return this.requiresPrefix;
	}

	public Argument setPrefixRequirement(Boolean bool) {
		this.requiresPrefix = bool;
		return this;
	}

	public int getPosition() {
		return position;
	}

	public Argument setPosition(int position) {
		this.position = position;
		return this;
	}

	public interface RunnableArg {

		public void run(CommandBlob blob);

	}

	public Argument setWildCardString(String wildCardString) {
		this.wildCardString = wildCardString;
		return this;
	}

	public String getWildCardString() {
		return wildCardString;
	}

	public Argument setAutoStartRunnable(boolean bool) {
		this.autoStartRunnable = bool;
		return this;
	}

	public boolean isAutoStartRunnable() {
		return autoStartRunnable;
	}

	public boolean isSkipOriginalTaskOnRunnable() {
		return skipOriginalTaskOnRunnable;
	}

	public Argument setSkipOriginalTaskOnRunnable(boolean skipOriginalTaskOnRunnable) {
		this.skipOriginalTaskOnRunnable = skipOriginalTaskOnRunnable;
		return this;
	}

	public RunnableArg getRunnableArg() {
		return runnableArg;
	}

	public Argument setPermissionLevel(String string) {
		this.permissionLevel = string;
		this.isDeveloper = true;
		return this;
	}

	public String getPermission() {
		return this.permissionLevel;
	}

	public boolean isRequired() {
		return this.isRequired;
	}

	public Argument setIsRequired(boolean bool) {
		this.isRequired = bool;
		return this;
	}

	public boolean isDeveloper() {
		return isDeveloper;
	}

	public Argument setDeveloper(boolean isDeveloper) {
		this.isDeveloper = isDeveloper;
		return this;
	}

	public Argument addAliases(String... alias) {
		for (String i : alias) {
			this.aliases.add(i);
		}
		return this;
	}

	public List<String> getAliases() {
		return this.aliases;
	}

}
