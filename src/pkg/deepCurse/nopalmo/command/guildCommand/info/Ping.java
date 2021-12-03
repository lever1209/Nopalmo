package pkg.deepCurse.nopalmo.command.guildCommand.info;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import pkg.deepCurse.nopalmo.command.GuildCommand;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.global.Tools;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;

public class Ping extends GuildCommand {

	@Override
	public void run(GuildCommandBlob blob, GuildCommandManager commandManager)
			throws Exception {
		
		GuildMessageReceivedEvent event = blob.getGuildMessageEvent();

		TextChannel channel = event.getChannel();

		channel.sendMessage("You are: " + blob.getUserID()).queue();
		
		if (blob.getArgs().size() == 0) {
			// new Main();
			channel.sendMessage("Pong!\n" + event.getJDA().getGatewayPing() + "ms\n"
			// + "Sorry if the ping is too high, im currently hosting on an under powered
			// laptop out in the countryside...\n"
			// + "This will be fixed in at most 2 days..."
			).queue();
			// long pang = Main.bot.getGatewayPing();

		} else if (blob.getArgs().get(0).contentEquals("all")) {

			channel.sendMessage("Gathering data...").queue(msg -> {
				try {
					long timeToProcess = System.currentTimeMillis();
					
					String out = "Pong!\n" + "Google: " + services.UptimePing.sendPing("www.google.com") + "ms\n"
							+ "JDA Gateway: " + event.getJDA().getGatewayPing() + "ms\n" + "www.discord.com: "
							+ services.UptimePing.sendPing("www.discord.com") + "ms";

					
					msg.editMessage(out + "\nTime to process: " + (timeToProcess - System.currentTimeMillis()) + "ms").queue();
				} catch (Exception e) {

				}

			});
		} else
			Tools.wrongUsage(channel, this);
	}

	@Override
	public String[] getCommandCalls() {
		return new String[] {"ping"};
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.Info;
	}

}
