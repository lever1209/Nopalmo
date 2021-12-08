package pkg.deepCurse.nopalmo.command.commands.info;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.MessageChannel;
import pkg.deepCurse.nopalmo.command.CommandInterface.DualCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;
import pkg.deepCurse.nopalmo.utils.UptimePing;

public class Ping implements DualCommandInterface {

	@Override
	public void runDualCommand(CommandBlob blob, HashMap<String, Argument> argumentMap) throws Exception {

		MessageChannel channel = blob.getChannel();
		
		if (argumentMap.isEmpty()) {
			channel.sendMessage("Pong!\n" + blob.getJDA().getGatewayPing() + "ms\n").queue();
			return;
		}

		if (argumentMap.get("all") != null) {

			channel.sendMessage("Gathering data. . .").queue(msg -> {
				long timeToProcess = System.currentTimeMillis();

				long jdaPing = blob.getJDA().getGatewayPing();
				long googlePing = -1;
				try {
					googlePing = UptimePing.sendPing("www.google.com");
				} catch (Exception e) {
					e.printStackTrace();
				}
				long discordPing = -1;
				try {
					discordPing = UptimePing.sendPing("www.discord.com");
				} catch (Exception e) {
					e.printStackTrace();
				}

				String out = "Ping:\n"
						+ (googlePing > 0 ? "Google: " + googlePing + "ms\n" : "Could not connect to www.google.com\n")
						+ (discordPing > 0 ? "Discord: " + discordPing + "ms\n"
								: "Could not connect to www.discord.com\n")
						+ "JDA-Discord heartbeat: "+jdaPing+"ms";

				msg.editMessage(out + "\nTime to process: " + (System.currentTimeMillis() - timeToProcess) + "ms")
						.queue();
			});
		}
	}

	@Override
	public String[] getCommandCalls() {
		return new String[] { "ping" };
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

	@Override
	public String getHelp() {
		return "Returns the jda heartbeat ping";
	}

}
