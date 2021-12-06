package pkg.deepCurse.nopalmo.manager;

public class Argument {

	// README
	//
	// This tool is used simply for now, but it will be worth while if you get used
	// to it
	//
	// this allows extra organization and ease of use throughout the creation of
	// commands
	// instead of one simple list where if args[1] == "all" do code
	// this allows the same functionality, if not more with a little bit of learning
	// you can go back to what it was before, but i honestly believe this system is
	// worthwhile
	// it just needs polish and reports on usage for optimization
	//
	//// TL;DR ITS A NEW FEATURE, GIVE IT TIME

	private int requiredArgs = 0;
	private String argName = null;
	private Argument[] subArgs = null;
	private boolean requiresPrefix = false;
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

	public Argument setPrefixRequirement(Boolean bool) {
		this.requiresPrefix = bool;
		return this;
	}

	public boolean getPrefixRequirement() {
		return this.requiresPrefix;
	}

	public interface RunnableArg {

		public void run();

	}

}