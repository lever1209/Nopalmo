package pkg.deepCurse.nopalmo.command.commands.testing;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.manager.Argument;
import pkg.deepCurse.nopalmo.manager.CommandBlob;

public class Purge implements GuildCommandInterface {

	@Override
	public String[] getCommandCalls() {
		return new String[] { "p" };
	}

	@Override
	public HelpPage getHelpPage() {
		return HelpPage.TESTING;
	}

	@Override
	public String getHelp() {
		return "Purges the channel of messages using many useful arguments to control the mode";
	}

	@Override
	public @Nullable HashMap<String, Argument> getArguments() {
		return null;
	}

	@Override
	public void runGuildCommand(CommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
	}

//	@Override
//	public void runGuildCommand(CommandBlob blob, HashMap<String, Argument> argumentList) throws Exception {
//
//		if (args.size() == 1) {
//			if (!blob.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
//				blob.getChannel().sendMessage("You don't have the **MESSAGE_MANAGE** permission!").queue();
//				return;
//			}
//			int num = 0;
//			try {
//				num = Integer.parseInt(args.get(0));
//			} catch (NumberFormatException nfe) {
//				throw new NumberFormatException("Enter a number dumbass");
//			}
//			blob.getMessage().delete().complete();
//			int currentNum = num / 100;
//			if (currentNum == 0) {
//				List<Message> msg = event.getChannel().getHistory().retrievePast(num).complete();
//				event.getChannel().purgeMessages(msg);
//				// event.getChannel().sendMessage("Successfully purged `" + num + "`
//				// messages.").queue();
//				return;
//			}
//			try {
//				for (int i = 0; i <= currentNum; i++) {
//					if (i == num) {
//						List<Message> msg = event.getChannel().getHistory().retrievePast(num).complete();
//						event.getChannel().purgeMessages(msg);
//						// event.getChannel().sendMessage("Successfully purged `" + num + "`
//						// messages.").queue();
//					} else {
//						List<Message> msg = event.getChannel().getHistory().retrievePast(100).complete();
//						event.getChannel().purgeMessages(msg);
//						num -= 100;
//					}
//				}
//			} catch (Exception e) {
//				if (Main.mode == BotMode.DEV) {
//					StringWriter sw = new StringWriter();
//					PrintWriter pw = new PrintWriter(sw);
//					e.printStackTrace(pw);
//					event.getChannel().sendMessage("```\n" + sw.toString() + "```").queue();
//					System.out.println("Error caught in: " + e.toString());
//					e.printStackTrace();
//				} else {
//					event.getChannel().sendMessage("```\n" + e + "```").queue(); // sends limited message
//					e.printStackTrace();
//				}
//				return;
//			}
//		} else if (args.size() == 2) {
//
//			if (args.get(0).contentEquals("-id") | args.get(0).contentEquals("-i")) {
//				long msgID = Long.parseLong(args.get(1));
//				blob.getChannel().retrieveMessageById(msgID).complete();
//
//				for (Message i : blob.getChannel().getIterableHistory()) {
//					if (i.getIdLong() == msgID) {
//						break;
//					} else {
//						i.delete().queue();
//						Thread.sleep(1000);
//					}
//				}
//				// i need a better way to get args, flags, and args of flags
//			} else if (args.get(0).contentEquals("-u") | args.get(0).contentEquals("-user")) {
//
//				long userID = Long.parseLong(args.get(1).replaceAll("[^0-9]", ""));
//				if (blob.getJDA().getUserById(userID) == null) {
//					throw new NullPointerException("Null user id");
//				}
//
//				for (Message i : blob.getChannel().getIterableHistory()) {
//					if (i.getAuthor().getIdLong() == userID) {
//						i.delete().complete();
//						Thread.sleep(1000);
//					}
//
//				}
//
//			} else {
//				pkg.deepCurse.nopalmo.global.Tools.wrongUsage(blob.getChannel(), this);
//			}
//
//		} else {
//			pkg.deepCurse.nopalmo.global.Tools.wrongUsage(blob.getChannel(), this);
//		}
//
//	}

}
