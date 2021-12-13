package pkg.deepCurse.nopalmo.command.commands.info;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import pkg.deepCurse.nopalmo.command.CommandInterface;
import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;
import pkg.deepCurse.nopalmo.manager.CommandManager;

public class Help implements GuildCommandInterface {

	public final CommandManager manager;

	public Help(CommandManager m) {
		this.manager = m;
	}

	@Override
	public void runGuildCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {

		boolean isDevEnabled = argumentMap.get("dev") != null;

		final List<HelpPage> deniedPages = new ArrayList<HelpPage>();
		deniedPages.add(HelpPage.DEV);
		deniedPages.add(HelpPage.EGG);
		deniedPages.add(HelpPage.TESTING);

		if (argumentMap.get("commandName") == null) {
			EmbedBuilder embed = new EmbedBuilder().setTitle(isDevEnabled ? "^Commands:" : "Commands:");

			HashMap<HelpPage, ArrayList<String>> commandHash = new HashMap<HelpPage, ArrayList<String>>();

			// TODO yet another rewrite
			// TODO add command to log a string

			for (HelpPage i : HelpPage.values()) {
				ArrayList<String> commandNameList = commandHash.get(i);
				for (CommandInterface command : manager.getCommands()) {
					if (!(deniedPages.contains(i) && argumentMap.get("dev") == null)) {
						if (command.getHelpPage() == i) {
							if (commandNameList == null) {
								commandNameList = new ArrayList<String>();
							}

							if (!commandNameList.contains(command.getCommandName())) {
								commandNameList.add(command.getCommandName());

								commandHash.put(command.getHelpPage(), commandNameList);

							}
						}
					}
				}
				commandNameList = null;
			}
			
			// blob.getChannel().sendMessage(commandHash.toString()).queue();

			for (HelpPage i : HelpPage.values()) {
				if (commandHash.get(i) != null) {
					StringBuilder sB = new StringBuilder();
					int count = 0;
					for (String j : commandHash.get(i)) {
						if (count >= 3) {
							count = 0;
							sB.append("\n");
						}
						count++;
						sB.append("`" + j + "` ");
					}
					embed.addField(i.toString(), sB.toString(), true);

				}
			}

//			for () {
//				
//			}

//			for (HelpPage i : HelpPage.values()) {
//				if (deniedPages.contains(i) && argumentMap.get("dev") == null) {
//
//				} else if (commandHash.get(i) != null) {
//
//					StringBuilder sB = new StringBuilder();
//
//					int count = 0;
//					for (String j : commandHash.get(i)) {
//						if (++count > 3) {
//							count = 0;
//							sB.append("\n");
//						}
//						sB.append("`" + j + "` ");
//					}
//
//					embed.addField(i.toString(), sB.toString(), true);
//
//				}
//
//			}

			StringBuilder sB = new StringBuilder();

			CommandInterface ping = blob.getCommandManager().getCommand("ping");
			if (ping != null) {
				sB.append("`" + ping.getUsage(isDevEnabled) + "`\n");
			}

			CommandInterface info = blob.getCommandManager().getCommand("info");
			if (info != null) {
				sB.append("`" + info.getUsage(isDevEnabled) + "`\n");
			}

			CommandInterface prefix = blob.getCommandManager().getCommand("prefix");
			if (prefix != null) {
				sB.append("`" + prefix.getUsage(isDevEnabled) + "`\n");
			}

			embed.addField("Information:", "Commands to take note of:\n" + sB, false);

			embed.setFooter(blob.getMember().getEffectiveName(), blob.getAuthor().getEffectiveAvatarUrl());
			embed.setTimestamp(Instant.now());
			embed.setColor(Global.getEmbedColor());

			blob.getChannel().sendMessageEmbeds(embed.build()).queue();

			return;
		} else if (argumentMap.get("commandName") != null) {

			CommandInterface command = manager.getCommand(argumentMap.get("commandName").getWildCardString());

			if (command != null && ((deniedPages.contains(command.getHelpPage()) && isDevEnabled)
					|| !deniedPages.contains(command.getHelpPage()))) {
				if (!blob.isFromGuild() ? true : !(command.isNSFW() && !((TextChannel) blob.getChannel()).isNSFW())) {
					EmbedBuilder eB = new EmbedBuilder();

					eB.setColor(Global.getEmbedColor());
					StringBuilder sB = new StringBuilder();

					eB.setTitle("Help results for: " + command.getCommandName());
					if (command.getHelp() != null) {
						eB.addField("Help info:", command.getHelp(), false);
					} else {
						eB.addField("Help info:", "This command does not contain help information", false);
					}

					eB.addField("Usage:", "`" + command.getUsage(isDevEnabled) + "`", false);

					eB.addField("Page:", command.getHelpPage().toString(), true); // ("Page: " +
																					// command.getHelpPage().toString());
					eB.setFooter(blob.getMember().getEffectiveName(), blob.getAuthor().getEffectiveAvatarUrl());
					eB.setTimestamp(Instant.now());

					if (command.getCommandCalls().length > 1) {
						sB.append("Aliases: ");
						for (int i = 1; i < command.getCommandCalls().length; i++) {
							sB.append("`" + command.getCommandCalls()[i] + "` ");
						}
						sB.append("\n");
					}

					if (command.isNSFW()) {
						sB.append("Is nsfw: " + command.isNSFW() + "\n");
					}

//					if (command.getRequiredPermission() != null) {
//						sB.append("Required Permission: " + command.getRequiredPermission().getName() + "\n");
//					}

					if (command.getTimeout() > 0) {
						sB.append("Usage Timeout: " + command.getTimeout() + "\n");
					}

					sB.append(
							"Premium: " + ((command.getPremiumLevel() < 1) ? "no" : command.getPremiumLevel()) + "\n");
					if (!sB.isEmpty()) {
						eB.addField("Misc", sB.toString(), false);
					}
					blob.getChannel().sendMessageEmbeds(eB.build()).queue();
				} else {
					blob.getChannel().sendMessage(
							"Sorry, but you are not allowed to view information about that command here, try somewhere more private")
							.queue();
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
	public HashMap<String, Argument> getArguments() {
		HashMap<String, Argument> args = new HashMap<String, Argument>();

		args.put("commandName", new Argument("commandName").setPosition(0).setIsWildcard(true));
		args.put("dev", new Argument("dev").setPrefixRequirement(true).setPermissionLevel("infopermission")
				.addAliases("d", "developer", "extra", "shit", "to", "test"));

		return args;
	}

}
