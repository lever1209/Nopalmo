package pkg.deepCurse.nopalmo.manager;

import java.util.ArrayList;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DirectCommandBlob {

	private DirectCommandManager commandManager = null;
	private ArrayList<String> args = null;
	private MessageReceivedEvent event = null;
	private JDA bot = null;

	private long userID = 0;
	private long channelID = 0;

	public DirectCommandBlob(MessageReceivedEvent event) {
		setUserID(event.getAuthor().getIdLong());
		setChannelID(event.getChannel().getIdLong());
		this.event = event;
		this.bot = event.getJDA();
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	public TextChannel getChannel() {
		TextChannel textChannel = bot.getTextChannelById(channelID);
		if (textChannel != null) {
			return textChannel;
		}
		return null;
	}

	public DirectCommandBlob setArgs(ArrayList<String> newArguments) {
		this.args = newArguments;
		return this;
	}

	public DirectCommandBlob setUserID(long userID) {
		this.userID = userID;
		return this;
	}

	public long getUserID() {
		return this.userID;
	}

	public DirectCommandManager getCommandManager() {
		return commandManager;
	}

	public void setCommandManager(DirectCommandManager commandManager) {
		this.commandManager = commandManager;
	}

	public long getChannelID() {
		return channelID;
	}

	public void setChannelID(long channelID) {
		this.channelID = channelID;
	}

	public MessageReceivedEvent getEvent() {
		return event;
	}

	public void setEvent(MessageReceivedEvent event) {
		this.event = event;
	}
}
