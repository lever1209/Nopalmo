package pkg.deepCurse.nopalmo.command.guildCommand.info;

import java.util.HashMap;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import pkg.deepCurse.nopalmo.command.GuildCommand;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.GuildCommandBlob;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;

public class Ping extends GuildCommand {

	@Override
	public void runCommand(GuildCommandBlob blob, GuildCommandManager commandManager,
			HashMap<String, Argument> argumentMap) throws Exception {

		GuildMessageReceivedEvent event = blob.getGuildMessageEvent();

		if (argumentMap.isEmpty()) {
			event.getChannel().sendMessage("Pong!\n" + event.getJDA().getGatewayPing() + "ms\n").queue();
			return;
		}

		if (argumentMap.get("all") != null) {

			event.getChannel().sendMessage("Gathering data. . .").queue(msg -> {
				long timeToProcess = System.currentTimeMillis();

				try { // TODO rewrite this block, all tries are not good practice

					String out = "Pong!\n" + "Google: " + services.UptimePing.sendPing("www.google.com") + "ms\n"
							+ "JDA Gateway: " + event.getJDA().getGatewayPing() + "ms\n" + "www.discord.com: "
							+ services.UptimePing.sendPing("www.discord.com") + "ms";

					msg.editMessage(out + "\nTime to process: " + (System.currentTimeMillis() - timeToProcess) + "ms")
							.queue();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

//		if (argumentArray == null || argumentArray.isEmpty()) {
//			
//			return;
//		} else {
//
//			for (Argument i : argumentArray) {
//				if (i.getArgName().contentEquals("all")) {
//					
//				} else {
//					Tools.wrongUsage(event.getChannel(), this);
//				}
//			}
//			return;
//		}
	}

	@Override
	public String[] getCommandCalls() {
		return new String[] { "ping" };
	}

	@Override
	public String getUsage() {
		return Global.prefix+"ping [" + Argument.argumentPrefix + "all]";
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.Info;
	}

	@Override
	public HashMap<String, Argument> getArguments() {
		HashMap<String, Argument> args = new HashMap<String, Argument>();

		args.put("all", new Argument("all").setPrefixRequirement(true));

		return args;
	}

}
