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
	
	public ArrayList<String> getArgs() {
		return args;
	}

	public void setUser(long userID) {
		this.userID = userID;
	}
	
	public long getUserID() {
		return this.userID;
	}
	
	public void setJDA(JDA bot) {
		
	}
	
	public void setArgs(ArrayList<String> newArguments) {
		this.args = newArguments;
	}
	
}
