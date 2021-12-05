package pkg.deepCurse.nopalmo.manager;

import java.util.ArrayList;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;

public abstract class CommandBlob {
	
	private String modifiedRaw = null;
	private String modified = null;
	
	private ArrayList<String> args = null;
	
	protected long userID = 0;
	
	Event event = null;
	
	@Deprecated
	public CommandBlob(Event event) {
		this.event = event;
	}
	
	public Event getEvent() {
		return event;
	}
	
	public String getModifiedMessageContents() {
		return this.modified;
	}
	
	public String getModifiedRawMessageContents() {
		return this.modifiedRaw;
	}
	
	@Deprecated
	public ArrayList<String> getArgs() {
		return args;
	}

	public CommandBlob setUser(long userID) {
		this.userID = userID;
		return this;
	}
	
	public long getUserID() {
		return this.userID;
	}
	
	public CommandBlob setJDA(JDA bot) {
		return this;
		
	}
	
	public CommandBlob setArgs(ArrayList<String> newArguments) {
		this.args = newArguments;
		return this;
	}
		
}
