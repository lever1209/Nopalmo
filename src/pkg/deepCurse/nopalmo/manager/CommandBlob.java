package pkg.deepCurse.nopalmo.manager;

import java.util.ArrayList;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandBlob {

	// CONTAINER/TRANSLATOR CLASS FOR COMMAND BLOBS

	private Event event = null;
	private CommandManager commandManager = null;
	private ArrayList<String> args = null;
	private JDA bot = null;

	private long userID = 0;
	private long channelID = 0;

	public CommandBlob(GuildMessageReceivedEvent event) {
		this.event = event;
		this.bot = event.getJDA();
	}

	public CommandBlob(MessageReceivedEvent event) {
		this.event = event;
		this.bot = event.getJDA();
	}

	public CommandBlob(GuildCommandBlob blob) {
		this.args = blob.getArgs();
		this.channelID = blob.getChannelID();
		this.commandManager = blob.getCommandManager();
		this.event = blob.getEvent();
		this.userID = blob.getUserID();
		this.bot = blob.getEvent().getJDA();
	}

	public CommandBlob(DirectCommandBlob blob) {
		this.args = blob.getArgs();
		this.channelID = blob.getChannelID();
		this.commandManager = blob.getCommandManager();
		this.event = blob.getEvent();
		this.userID = blob.getUserID();
		this.bot = blob.getEvent().getJDA();
	}

	public long getChannelID() {
		return channelID;
	}

	public CommandBlob setChannelID(long channelID) {
		this.channelID = channelID;
		return this;
	}

	public MessageChannel getChannel() {
		TextChannel textChannel = bot.getTextChannelById(channelID);
		if (textChannel != null){
			return textChannel;
		}
		PrivateChannel privateChannel = bot.getPrivateChannelById(channelID);
		if (privateChannel != null){
			return privateChannel;
		}
		return null;
	}

	public MessageChannel getMessageChannel(long channelID) {
		return bot.getTextChannelById(channelID);
	}

	public long getUserID() {
		return userID;
	}

	public JDA getJDA() {
		return bot;
	}

	public CommandBlob setUserID(long userID) {
		this.userID = userID;
		return this;
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	public CommandBlob setArgs(ArrayList<String> args) {
		this.args = args;
		return this;
	}

	public CommandManager getCommandManager() {
		return commandManager;

	}

	public CommandBlob setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
		return this;
	}

	public Event getEvent() {
		if (event instanceof GuildMessageReceivedEvent) {
			return (GuildMessageReceivedEvent) event;
		} else if (event instanceof MessageReceivedEvent) {
			return (MessageReceivedEvent) event;
		} else
			return null;
	}

}
