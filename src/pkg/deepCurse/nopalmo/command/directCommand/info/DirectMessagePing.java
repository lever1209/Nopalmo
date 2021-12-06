package pkg.deepCurse.nopalmo.command.directCommand.info;

import java.util.HashMap;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import pkg.deepCurse.nopalmo.command.DirectCommand;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.DirectCommandBlob;
import pkg.deepCurse.nopalmo.utils.UptimePing;

public class DirectMessagePing extends DirectCommand {

	@Override
	public void runCommand(DirectCommandBlob blob,
			HashMap<String, Argument> argumentMap) throws Exception {

		MessageReceivedEvent event = blob.getEvent();

		if (argumentMap.isEmpty()) {
			event.getChannel().sendMessage("Pong!\n" + event.getJDA().getGatewayPing() + "ms\n").queue();
			return;
		}

		if (argumentMap.get("all") != null) {

			event.getChannel().sendMessage("Gathering data. . .").queue(msg -> {
				long timeToProcess = System.currentTimeMillis();

				try { // TODO rewrite this block, all tries are not good practice

					String out = "Pong!\n" + "Google: " + UptimePing.sendPing("www.google.com") + "ms\n"
							+ "JDA Gateway: " + event.getJDA().getGatewayPing() + "ms\n" + "www.discord.com: "
							+ UptimePing.sendPing("www.discord.com") + "ms";

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
		return Global.prefix + "ping [" + Argument.argumentPrefix + "all]";
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
