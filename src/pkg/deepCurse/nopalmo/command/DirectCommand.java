package pkg.deepCurse.nopalmo.command;

import java.util.HashMap;

import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.DirectCommandBlob;

public abstract class DirectCommand extends AbstractCommand {

	public abstract void runCommand(DirectCommandBlob blob, HashMap<String, Argument> argumentList) throws Exception;

}
