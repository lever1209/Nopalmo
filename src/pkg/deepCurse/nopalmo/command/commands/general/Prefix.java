package pkg.deepCurse.nopalmo.command.commands.general;

import java.util.HashMap;

import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Guild;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;

public class Prefix implements GuildCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] { "prefix", };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.General;
	}

	@Override
	public void runGuildCommand(GuildCommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
		
		if (argumentList.get("prefix") != null) {
			Guild.Prefix.setPrefix(
					blob.getEvent().getGuild().getIdLong(), argumentList.get("prefix").getWildCardString());
			blob.getEvent().getChannel().sendMessage("Set prefix to " + argumentList.get("prefix").getWildCardString()).queue();
			blob.getChannel().sendMessage("Remember: you can always ping me to use any command in case you forget the prefix").queue();
		} else {
			Guild.Prefix.setPrefix(
					blob.getEvent().getGuild().getIdLong(), Global.prefix);
			blob.getEvent().getChannel().sendMessage("Reset prefix to default").queue();
		}

	}

	@Override
	public HashMap<String, Argument> getArguments() {
		HashMap<String, Argument> args = new HashMap<String, Argument>();

		args.put("prefix", new Argument("prefix").setPosition(0).setIsWildcard(true));

		return args;
	}

	@Override
	public String getUsage() {
		return Global.prefix + getCommandName() + " <prefix>";
	}

	@Override
	public String getHelp() {
		return "Sets a prefix for your guild";
	}

}
