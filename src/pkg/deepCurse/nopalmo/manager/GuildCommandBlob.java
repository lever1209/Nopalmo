package pkg.deepCurse.nopalmo.manager;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GuildCommandBlob extends CommandBlob {

	@SuppressWarnings("deprecation")
	public GuildCommandBlob(GuildMessageReceivedEvent event) {
		super(event);
		setUser(event.getMessage().getAuthor().getIdLong());
	}
	
	public GuildMessageReceivedEvent getGuildMessageEvent() {
		return (GuildMessageReceivedEvent) this.event;
	}
	
}
