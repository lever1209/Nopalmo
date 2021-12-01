package pkg.deepCurse.nopalmo.command.guildCommand;

import java.util.List;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import pkg.deepCurse.nopalmo.command.GuildCommand;
import pkg.deepCurse.nopalmo.database.DatabaseTools;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;

public class Ping extends GuildCommand {

	@Override
	public void run(List<String> args, GuildMessageReceivedEvent guildMessage, GuildCommandManager commandManager)
			throws Exception {
		
		DatabaseTools.Tools.Guild.Prefix.createPrefix(guildMessage.getGuild().getIdLong(), args.get(0));
		
	}

	@Override
	public String[] getCommandCalls() {
		return new String[] {"ping"};
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.Info;
	}
}
