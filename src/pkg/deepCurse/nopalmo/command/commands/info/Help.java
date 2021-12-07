package pkg.deepCurse.nopalmo.command.commands.info;

import java.time.Instant;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;

public class Help implements GuildCommandInterface {

	public final GuildCommandManager manager;

	public Help(GuildCommandManager m) {
		this.manager = m;
	}

	@Override
	public void runGuildCommand(GuildCommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {

		if (argumentMap.isEmpty()) {
			EmbedBuilder embed = new EmbedBuilder().setTitle("Commands:");

			HashMap<HelpPage, String> commandHash = new HashMap<HelpPage, String>();

			for (GuildCommandInterface command : manager.getGuildCommands()) {

				commandHash.put(command.getHelpPage(),
						commandHash.get(command.getHelpPage()) + command.getCommandName());

			}

			StringBuilder sB = new StringBuilder();

			GuildCommandInterface ping = blob.getCommandManager().getCommand("ping");
			if (ping != null) {
				sB.append("`" + ping.getUsage() + "`\n");
			}

			GuildCommandInterface help = blob.getCommandManager().getCommand("help");
			if (help != null) {
				sB.append("`" + help.getUsage() + "`\n");
			}

			embed.addField("Information:", "Commands to take note of:\n" + sB, false);

			// embed.addField("Commands : ", "`"+sB.toString()+"`\n", true);
			// desc.append("`").append(sB).append("`\n");

			// embed.addBlankField(true);
			// embed.setFooter("Command list requested by: "+event.getAuthor().getAsTag(),
			// event.getAuthor().getEffectiveAvatarUrl());

			embed.setFooter(blob.getEvent().getMember().getEffectiveName(),
					blob.getEvent().getMember().getUser().getEffectiveAvatarUrl());
			embed.setTimestamp(Instant.now());
			embed.setColor(0);
			if (embed.isValidLength()) {
				blob.getEvent().getChannel().sendMessageEmbeds(embed.build()).queue();
			} else {
				blob.getEvent().getChannel()
						.sendMessage(
								"Critical error!\nEmbed max size exceeded, please report this to the devs immediately")
						.queue();
			}
			if (new Random().nextLong() == 69420l) { // i wonder who will find this, also, if you read the source to
				// find this, shhhhhhhh - deepCurse
				blob.getEvent().getChannel().sendMessage("we will rise above you humans")
						.queue(msg -> msg.delete().queueAfter(300, TimeUnit.MILLISECONDS));
			}
			return;
		}

		// ##########################################################################################################################

		// ##########################################################################################################################

		// ##########################################################################################################################

		// ##########################################################################################################################

		// ##########################################################################################################################
		try {
			GuildCommandInterface command = manager.getCommand(String.join("", blob.getArgs()));

			// event.getChannel().sendMessage("Command help for `" + command.commandName() +
			// "`:\n\tUsage: "+ command.usageString() + "\n" +
			// command.helpString()).queue();
			if (command.getHelpPage() != HelpPage.EGG) {
				EmbedBuilder eB = new EmbedBuilder();
				eB.setTitle("Help results for: " + command.getCommandName());
				if (command.getHelp() != null) {
					eB.addField("Help info:", command.getHelp(), false);
				}
				eB.addField("Usage:", command.getUsage(), false);
				eB.setFooter("Page: " + command.getHelpPage().toString());
				String alias = "`";
				for (int i = 1; i < command.getCommandCalls().length; i++) {

					if (i == 1) {
						alias += command.getCommandCalls()[i];
					} else {
						alias += ", " + command.getCommandCalls()[i];
					}
				}
				alias += "`";

				String endAilias = "";

				if (!alias.contentEquals("``")) {
					endAilias = "Aliases: " + alias + "\n";
				} else {
					endAilias = "Aliases: none\n";
				}
				eB.setColor(0);
				StringBuilder sB = new StringBuilder();
				sB.append(endAilias);
				try {
					sB.append("Required Permission: " + command.getRequiredPermission().getName() + "\n");
				} catch (NullPointerException e) {
				}
				if (command.getTimeout() > 0) {
					sB.append("Usage Timeout: " + command.getTimeout() + "\n");
				}
				sB.append("Premium: " + command.isPremium() + "\n");
				eB.addField("Misc", sB.toString(), false);
				blob.getEvent().getChannel().sendMessageEmbeds(eB.build()).queue();
			} else {
				throw new NullPointerException("Invalid input");
			}

		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();
			blob.getEvent().getChannel()
					.sendMessage("The command `" + String.join("", blob.getArgs()) + "` does not exist!\n" + "Use `"
							+ Global.prefix + getCommandCalls()[0] + "` for a list of all my commands!")
					.queue();
			return;

		}

		// }
		// https://download.java.net/java/GA/jdk16/7863447f0ab643c585b9bdebf67c69db/36/GPL/openjdk-16_linux-x64_bin.tar.gz
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.Info;
	}

	@Override
	public String[] getCommandCalls() {

		return new String[] { "help", "h" };
	}

	@Override
	public String getHelp() {

		return "The help command, it seems like you already know how to use it. . .";
	}

	@Override
	public String getUsage() {
		return Global.prefix + getCommandCalls()[0] + " [Command name]";
	}

}
