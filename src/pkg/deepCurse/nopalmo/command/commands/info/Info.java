package pkg.deepCurse.nopalmo.command.commands.info;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.command.CommandInterface.PrivateCommandInterface;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Users;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

public class Info implements GuildCommandInterface, PrivateCommandInterface {

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
			blob.getChannel().sendMessage(!Users.dump(blob.getAuthorID()).isEmpty() ? Users.dump(blob.getAuthorID())
					: "Sorry, but this user does not exist in the database").queue();
		}).setPrefixRequirement(true).setAutoStartRunnable(true).setSkipOriginalTaskOnRunnable(true).addAliases("u"));

		return args;
	}

	@Override
	public void runPrivateCommand(CommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
	}

	@Override
	public void runGuildCommand(CommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
		blob.getChannel().sendMessage(
				"This command is used to send data stored in the database to whoever is authorized to view it, to view your own data, use `"
						+ getCommandName() + " -userdump`")
				.queue();
	}
}
