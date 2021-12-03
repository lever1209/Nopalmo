package pkg.deepCurse.nopalmo.command.guildCommand.info;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import core.Global;
import core.enums.HelpPage;
import core.listeners.Command;
import core.listeners.CommandManager;
import core.tools.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Help implements Command {
	public final CommandManager manager;

	public Help(CommandManager m) {
		this.manager = m;
	}

	@Override
	public void run(List<String> args, GuildMessageReceivedEvent event, ArrayList<Long> devList) throws Exception {
		// TextChannel channel = event.getChannel();
		// Member user = event.getMember();
		// if (Tools.devIdCheck(null, user.getId(), channel, null)) {
		if (args.size() > 1) {
			Tools.wrongUsage(event.getChannel(), this);
			return;
		}
		if (args.isEmpty()) {
			EmbedBuilder embed = new EmbedBuilder().setTitle("Commands:");

			for (HelpPage i : HelpPage.values()) {
				if (i != HelpPage.DEV) {
					StringBuilder pageData = new StringBuilder();

					for (Command command : manager.getCommands()) {
						if (!command.isHidden() & command.getPage() != HelpPage.EGG) {
							if (command.getPage() == i) {

								if (!pageData.toString().contains(command.getName())) {
									pageData.append("`" + command.getName() + "`\n");
								}

							}
						}
					}
					if (!pageData.toString().isBlank()) {
						embed.addField(i.name() + ": ", pageData.toString(), true);
					}
					// pageData.delete(0, pageData.length());
				}
			}

			StringBuilder sB = new StringBuilder();

			sB.append("`manual`\n");
			sB.append("`ping`\n");
			sB.append("`support`\n");
			// sB.append("`"+Global.Prefix+"\n");

			embed.addField("Information:", "Commands to take note of:\n" + sB, false);

			// embed.addField("Commands : ", "`"+sB.toString()+"`\n", true);
			// desc.append("`").append(sB).append("`\n");

			// embed.addBlankField(true);
			// embed.setFooter("Command list requested by: "+event.getAuthor().getAsTag(),
			// event.getAuthor().getEffectiveAvatarUrl());

			embed.setFooter(event.getMember().getEffectiveName(), event.getMember().getUser().getEffectiveAvatarUrl());
			embed.setTimestamp(Instant.now());
			embed.setColor(Global.embedColor);
			if (embed.isValidLength()) {
				event.getChannel().sendMessage(embed.build()).queue();
			} else {
				event.getChannel()
						.sendMessage(
								"Critical error!\nEmbed max size exceeded, please report this to the devs immediately")
						.queue();
			}
			if (new Random().nextLong() == 69420l) { // i wonder who will find this, also, if you read the source to
				// find this, shhhhhhhh - deepCurse
				event.getChannel().sendMessage("we will rise above you humans")
						.queue(msg -> msg.delete().queueAfter(300, TimeUnit.MILLISECONDS));
			}
			return;
		}
		try {
			Command command = manager.getCommand(String.join("", args));

			// event.getChannel().sendMessage("Command help for `" + command.commandName() +
			// "`:\n\tUsage: "+ command.usageString() + "\n" +
			// command.helpString()).queue();
			if (!command.isHidden() & command.getPage() != HelpPage.EGG) {
				EmbedBuilder eB = new EmbedBuilder();
				eB.setTitle("Help results for: " + command.getName());
				if (command.getHelp() != null) {
					eB.addField("Help info:", command.getHelp(), false);
				}
				eB.addField("Usage:", command.getUsage(), false);
				eB.setFooter("Page: " + command.getPage().toString());
				String alias = "`";
				for (int i = 1; i < command.getCalls().length; i++) {

					if (i == 1) {
						alias += command.getCalls()[i];
					} else {
						alias += ", " + command.getCalls()[i];
					}
				}
				alias += "`";

				String endAilias = "";

				if (!alias.contentEquals("``")) {
					endAilias = "Aliases: " + alias + "\n";
				} else {
					endAilias = "Aliases: none\n";
				}
				eB.setColor(Global.embedColor);
				StringBuilder sB = new StringBuilder();
				sB.append(endAilias);
				try {
					sB.append("Required Permission: " + command.getRequiredPermission().getName()+"\n");
				} catch (NullPointerException e) {
				}if(command.getTimeout()>0) {
				sB.append("Usage Timeout: "+command.getTimeout()+"\n");
				}
				sB.append("Premium: "+command.isPremium()+"\n");
				eB.addField("Misc", sB.toString(), false);
				event.getChannel().sendMessage(eB.build()).queue();
			} else {
				throw new NullPointerException("Invalid input");
			}

		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();
			event.getChannel().sendMessage("The command `" + String.join("", args) + "` does not exist!\n" + "Use `"
					+ Global.Prefix + getCalls()[0] + "` for a list of all my commands!").queue();
			return;

		}

		// }
		// https://download.java.net/java/GA/jdk16/7863447f0ab643c585b9bdebf67c69db/36/GPL/openjdk-16_linux-x64_bin.tar.gz
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public HelpPage getPage() {
		return HelpPage.Info;
	}

	@Override
	public String[] getCalls() {

		return new String[] { "help", "h" };
	}

	@Override
	public String getHelp() {

		return "The help command, it seems like you already know how to use it. . .";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.MESSAGE_WRITE;
	}

	@Override
	public String getName() {
		return getCalls()[0];
	}

	@Override
	public String getUsage() {
		return Global.Prefix + getCalls()[0] + " [Command name]";
	}

	@Override
	public boolean isPremium() {
		return false;
	}
	@Override
	public int getTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}
}
