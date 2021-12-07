package pkg.deepCurse.nopalmo.command.commands.info;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;

public class Help implements GuildCommandInterface {

	public final GuildCommandManager manager;

	public Help(GuildCommandManager m) {
		this.manager = m;
	}

	@Override
	public void runGuildCommand(GuildCommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {

		if (argumentMap.isEmpty() || argumentMap.get("dev") != null) {
			EmbedBuilder embed = new EmbedBuilder().setTitle("Commands:");

			HashMap<HelpPage, List<String>> commandHash = new HashMap<HelpPage, List<String>>();

			for (GuildCommandInterface command : manager.getGuildCommands()) {
				List<String> commandNameList = commandHash.get(command.getHelpPage());
				if (commandNameList == null) {
					commandNameList = new ArrayList<String>();

				}
				commandNameList.add(command.getCommandName());
				commandHash.put(command.getHelpPage(), commandNameList);
			}

			for (HelpPage i : HelpPage.values()) {
				if ((i == HelpPage.DEV || i == HelpPage.TESTING || i == HelpPage.EGG)
						&& argumentMap.get("dev") == null) {

				} else if (commandHash.get(i) != null) {

					StringBuilder sB = new StringBuilder();

					int count = 0;
					for (String j : commandHash.get(i)) {
						if (++count > 3) {
							sB.append("\n");
							count = 0;
						}
						sB.append("`" + j + "` ");
					}

					embed.addField(i.toString(), sB.toString(), true);

				}

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

			GuildCommandInterface prefix = blob.getCommandManager().getCommand("prefix");
			if (prefix != null) {
				sB.append("`" + prefix.getUsage() + "`\n");
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

			blob.getEvent().getChannel().sendMessageEmbeds(embed.build()).queue();

			return;
		} else if (argumentMap.get("commandName") != null) {

			GuildCommandInterface command = manager.getCommand(argumentMap.get("commandName").getWildCardString());

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
		}

		// ##########################################################################################################################

		// ##########################################################################################################################

		// ##########################################################################################################################

		// ##########################################################################################################################

		// ##########################################################################################################################

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
	
	@Override
	public String getCompleteUsage() {
		return Global.prefix + getCommandCalls()[0] + " [Command name | -dev]";
	}

	@Override
	public HashMap<String, Argument> getArguments() {
		HashMap<String, Argument> args = new HashMap<String, Argument>();

		args.put("commandName", new Argument("commandName").setPosition(0).setIsWildcard(true));
		args.put("dev", new Argument("dev", (CommandBlob blob) -> {
			blob.getChannel().sendMessage("dev").queue();
		}).setPrefixRequirement(true).setAutoStartRunnable(true));

		return args;
	}

}
