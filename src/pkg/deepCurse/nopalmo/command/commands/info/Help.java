package pkg.deepCurse.nopalmo.command.commands.info;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.global.Tools;
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

		boolean isDevEnabled = argumentMap.get("dev") != null;

		final List<HelpPage> deniedPages = new ArrayList<HelpPage>();
		deniedPages.add(HelpPage.DEV);
		deniedPages.add(HelpPage.EGG);
		deniedPages.add(HelpPage.TESTING);

		// System.out.println(argumentMap.size()+":"+devEnabled+"\n"+argumentMap.toString());

		if (argumentMap.isEmpty() || (isDevEnabled && argumentMap.size() == 1)) {
			EmbedBuilder embed = new EmbedBuilder().setTitle(isDevEnabled ? "^Commands:" : "Commands:");

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
				if (deniedPages.contains(i) && argumentMap.get("dev") == null) {

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

			blob.getChannel().sendMessageEmbeds(embed.build()).queue();

			return;
		} else if (argumentMap.get("commandName") != null) {

			GuildCommandInterface command = manager.getCommand(argumentMap.get("commandName").getWildCardString());

			if (command != null && ((deniedPages.contains(command.getHelpPage()) && isDevEnabled)
					|| !deniedPages.contains(command.getHelpPage()))) {
				if (!command.isNSFW()) {
				EmbedBuilder eB = new EmbedBuilder();

				eB.setColor(0);
				StringBuilder sB = new StringBuilder();

				eB.setTitle("Help results for: " + command.getCommandName());
				if (command.getHelp() != null) {
					eB.addField("Help info:", command.getHelp(), false);
				} else {
					eB.addField("Help info:", "This command does not contain help information", false);
				}

				eB.addField("Usage:", command.getUsage(), false);

				eB.setFooter("Page: " + command.getHelpPage().toString());

				if (command.getCommandCalls().length > 1) {
					for (int i = 1; i < command.getCommandCalls().length; i++) {
						sB.append("`"+command.getCommandCalls()[i]+"` ");
					}
					sB.append("\n");
				}
				
				if (command.isNSFW()) {
					sB.append("Is nsfw: "+command.isNSFW());
				}

				if (command.getRequiredPermission() != null) {
					sB.append("Required Permission: " + command.getRequiredPermission().getName() + "\n");
				}

				if (command.getTimeout() > 0) {
					sB.append("Usage Timeout: " + command.getTimeout() + "\n");
				}

				sB.append("Premium: " + command.isPremium() + "\n");
				eB.addField("Misc", sB.toString(), false);
				blob.getChannel().sendMessageEmbeds(eB.build()).queue();
				} else {
					blob.getChannel().sendMessage("Sorry, but you are not allowed to view information about that command here, try somewhere more private").queue();
				}
			} else {
				// Tools.wrongUsage(blob.getChannel(), command);
				blob.getChannel().sendMessage(
						"Sorry, but there is no command named: " + argumentMap.get("commandName").getWildCardString())
						.queue();
			}
		}

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
			blob.getChannel().sendMessage("DEV FLAG USED").queue();
		}).setPrefixRequirement(true).setAutoStartRunnable(true).setPermissionLevel("infopermission"));

		return args;
	}

}
