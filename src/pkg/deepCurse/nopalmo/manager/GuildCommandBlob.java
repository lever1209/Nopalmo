package pkg.deepCurse.nopalmo.manager;

import java.util.ArrayList;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GuildCommandBlob {
	
	private GuildCommandManager commandManager = null;
	private ArrayList<String> args = null;
	
	private long userID = 0;
	private long channelID = 0;
	
	private GuildMessageReceivedEvent event = null;

	public GuildCommandBlob(GuildMessageReceivedEvent event) {
		this.event = event;
		setUserID(event.getAuthor().getIdLong());
		setChannelID(event.getChannel().getIdLong());
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	public GuildCommandBlob setArgs(ArrayList<String> newArguments) {
		this.args = newArguments;
		return this;
	}

	public GuildCommandBlob setUserID(long userID) {
		this.userID = userID;
		return this;
	}

	public long getUserID() {
		return this.userID;
	}

	public GuildCommandManager getCommandManager() {
		return commandManager;
	}

	public void setCommandManager(GuildCommandManager commandManager) {
		this.commandManager = commandManager;
	}

	public long getChannelID() {
		return channelID;
	}

	public void setChannelID(long channelID) {
		this.channelID = channelID;
	}

	public GuildMessageReceivedEvent getEvent() {
		return event;
	}

}
