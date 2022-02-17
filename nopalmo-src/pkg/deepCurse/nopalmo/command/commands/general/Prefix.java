package pkg.deepCurse.nopalmo.command.commands.general;

import java.util.HashMap;

import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.GlobalDB;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.GuildDB;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.UserDB;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

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
	public void runGuildCommand(CommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {

		if (argumentList.get("prefix") != null) {
			GuildDB.setPrefix(blob.getGuildID(), argumentList.get("prefix").getWildCardString());
			blob.getChannel().sendMessage("Set prefix to " + argumentList.get("prefix").getWildCardString()).queue();
			if (!UserDB.isAdvancedUser(blob.getAuthorID()))
				blob.getChannel()
						.sendMessage(
								"Remember: you can always ping me to use any command in case you forget the prefix")
						.queue();

//			if () {
//				
//			}

		} else {
			GuildDB.setPrefix(blob.getGuildID(), GlobalDB.prefix);
			blob.getChannel().sendMessage("Reset prefix to default").queue();
		}

	}

	@Override
	public HashMap<String, Argument> getArguments() {
		HashMap<String, Argument> args = new HashMap<String, Argument>();

		args.put("prefix", new Argument("prefix").setPosition(0).setIsWildcard(true));

		return args;
	}

	@Override
	public String getHelp() {
		return "Sets a prefix for your guild";
	}

}
