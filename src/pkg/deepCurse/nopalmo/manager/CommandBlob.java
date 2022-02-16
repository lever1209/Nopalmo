package pkg.deepCurse.nopalmo.manager;

import java.util.ArrayList;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandBlob {

	private CommandManager commandManager = null;
	private ArrayList<Argument> args = null;

	private long authorID = 0;
	private long channelID = 0;
	private long guildID = 0;
	private long messageID = 0;

	private Event event = null;
	private JDA bot = null;
	private MessageChannel channel = null;
	private User author = null;
	private Guild guild = null;
	private Member member = null;
	private Message message = null;
//	private BontebokInterpreter interpreter = null;

	private boolean isDeveloper = false;
	private boolean isWebhookMessage = false;
	private boolean isFromGuild = false;

	public CommandBlob(MessageReceivedEvent event, CommandManager commandManager) {
		this.event = event;
		this.bot = event.getJDA();
		this.commandManager = commandManager;

		this.author = event.getAuthor();
		this.authorID = this.author.getIdLong();
		this.channel = event.getChannel();
		this.channelID = this.channel.getIdLong();
		this.message = event.getMessage();
		this.messageID = event.getMessageIdLong();
		this.isFromGuild = event.isFromGuild();
		if (this.isFromGuild) {
			this.guild = event.getGuild();
			this.guildID = this.guild.getIdLong();
			this.member = event.getMember();
			this.isWebhookMessage = event.isWebhookMessage();
		}
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	public ArrayList<Argument> getArgs() {
		return args;
	}

	public void setArgs(ArrayList<Argument> args) {
		this.args = args;
	}

	public long getAuthorID() {
		return authorID;
	}

	public void setAuthorID(long authorID) {
		this.authorID = authorID;
	}

	public long getChannelID() {
		return channelID;
	}

	public void setChannelID(long channelID) {
		this.channelID = channelID;
	}

	public long getGuildID() {
		return guildID;
	}

	public void setGuildID(long guildID) {
		this.guildID = guildID;
	}

	public long getMessageID() {
		return messageID;
	}

	public void setMessageID(long messageID) {
		this.messageID = messageID;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public JDA getJDA() {
		return bot;
	}

	public void setBot(JDA bot) {
		this.bot = bot;
	}

	public MessageChannel getChannel() {
		return channel;
	}

	public void setChannel(MessageChannel messageChannel) {
		this.channel = messageChannel;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public boolean isWebhookMessage() {
		return isWebhookMessage;
	}

	public void setWebhookMessage(boolean isWebhookMessage) {
		this.isWebhookMessage = isWebhookMessage;
	}

	public boolean isFromGuild() {
		return isFromGuild;
	}

	public void setFromGuild(boolean isFromGuild) {
		this.isFromGuild = isFromGuild;
	}

	public boolean isDeveloper() {
		return isDeveloper;
	}

	public void setDeveloper(boolean isDeveloper) {
		this.isDeveloper = isDeveloper;
	}

//	public BontebokInterpreter getInterpreter() {
//		return interpreter;
//	}
//
//	public CommandBlob setInterpreter(BontebokInterpreter interpreter) {
//		this.interpreter = interpreter;
//		return this;
//	}
}
