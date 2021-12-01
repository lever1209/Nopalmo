package pkg.deepCurse.nopalmo.manager;

import java.util.ArrayList;

public class CommandBlob {
	
	private String modifiedRaw = null;
	private String modified = null;
	
	private ArrayList<String> args = null;
	
	public String getModifiedMessageContents() {
		return this.modified;
	}
	
	public String getModifiedRawMessageContents() {
		return this.modifiedRaw;
	}
	
	public ArrayList<String> getArgs() {
		return args;
	}
	
}
