package pkg.deepCurse.nopalmo.command.commands.info;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.command.CommandInterface.DirectCommandInterface;
import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Users;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;
import pkg.deepCurse.nopalmo.manager.DirectCommandBlob;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;

public class Info implements GuildCommandInterface, DirectCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] { "info", "i" };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.Info;
	}

	@Override
	public String getHelp() {
		return "A command for getting information in and out of the bot";
	}

	@Override
	public @Nullable HashMap<String, Argument> getArguments() {
		HashMap<String, Argument> args = new HashMap<String, Argument>();

		args.put("userdump", new Argument("userdump", (CommandBlob blob) -> {
			blob.getChannel().sendMessage(Users.dump(blob.getUserID())).queue();
		}).setPrefixRequirement(true).setAutoStartRunnable(true)
				.setSkipOriginalTaskOnRunnable(true));

		return args;
	}

	@Override
	public void runDirectCommand(DirectCommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
	}

	@Override
	public void runGuildCommand(GuildCommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
		blob.getChannel().sendMessage("EEE").queue();
	}
}
