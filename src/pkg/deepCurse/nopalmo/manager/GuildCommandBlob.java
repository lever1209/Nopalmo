package pkg.deepCurse.nopalmo.manager;

import java.util.ArrayList;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GuildCommandBlob {
	
	private CommandManager commandManager = null;
	private ArrayList<String> args = null;
	private JDA bot = null;
	
	private long userID = 0;
	private long channelID = 0;
	
	private GuildMessageReceivedEvent event = null;

	public GuildCommandBlob(GuildMessageReceivedEvent event) {
		this.event = event;
		setUserID(event.getAuthor().getIdLong());
		setChannelID(event.getChannel().getIdLong());
		this.bot = event.getJDA();
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	public GuildCommandBlob setArgs(ArrayList<String> newArguments) {
		this.args = newArguments;
		return this;
	}
	
	public TextChannel getChannel() {
		TextChannel textChannel = bot.getTextChannelById(channelID);
		if (textChannel != null){
			return textChannel;
		}
		return null;
	}

	public GuildCommandBlob setUserID(long userID) {
		this.userID = userID;
		return this;
	}

	public long getUserID() {
		return this.userID;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public void setCommandManager(CommandManager commandManager) {
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
