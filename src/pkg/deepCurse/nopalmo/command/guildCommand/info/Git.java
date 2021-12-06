package pkg.deepCurse.nopalmo.command.guildCommand.info;

import java.util.HashMap;

import pkg.deepCurse.nopalmo.command.GuildCommand;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;

public class Git extends GuildCommand {

	@Override
	public void runCommand(GuildCommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
		blob.getEvent().getChannel().sendMessage("Heres the link: https://github.com/lever1209/nopalmo").queue();
	}

	@Override
	public String[] getCommandCalls() {
		// TODO Auto-generated method stub
		return new String[] { "git", "source", "github" };
	}

	@Override
	public HelpPage getHelpPage() {
		// TODO Auto-generated method stub
		return HelpPage.Info;
	}

}
