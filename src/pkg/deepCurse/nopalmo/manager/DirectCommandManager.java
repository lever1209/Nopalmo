package pkg.deepCurse.nopalmo.manager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import pkg.deepCurse.nopalmo.command.CommandInterface.DirectCommandInterface;
import pkg.deepCurse.nopalmo.command.commands.Ping;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools;
import pkg.deepCurse.nopalmo.global.Tools;

public class DirectCommandManager extends CommandManager {

	private final Map<String, DirectCommandInterface> directCommandMap = new HashMap<>();
	private static Executor executor = null;

	public DirectCommandManager() {
		init();
		executor = Executors.newSingleThreadExecutor();
	}

	public void init() {
		
		addCommand(new Ping());
		
	}

	private void addCommand(DirectCommandInterface c) {
		if (!directCommandMap.containsKey(c.getCommandName())) {
			directCommandMap.put(c.getCommandName(), c);
		} else {
			directCommandMap.remove(c.getCommandName());
			directCommandMap.put(c.getCommandName(), c);
		}
	}

	public Collection<DirectCommandInterface> getDirectCommands() {
		return directCommandMap.values();
	}

	public DirectCommandInterface getCommand(String commandName) {
		if (commandName != null) {
			return directCommandMap.get(commandName);
		}
		return null;
	}

	public void startCommand(MessageReceivedEvent messageReceivedEvent) {

		final String message = messageReceivedEvent.getMessage().getContentRaw();

		final String[] split = message.replaceFirst("(?i)" + Pattern.quote(DatabaseTools.Tools.Global.prefix), "")
				.split("\\s+");
		final String command = split[0].toLowerCase();

		if (directCommandMap.containsKey(command)) {
			final List<String> args = Arrays.asList(split).subList(1, split.length);

			executor.execute(() -> {
				long commandStartTime = System.currentTimeMillis();

				try {
					DirectCommandBlob commandBlob = new DirectCommandBlob(messageReceivedEvent);
					DirectCommandInterface directCommand = directCommandMap.get(command);
					HashMap<String, Argument> argumentList = new HashMap<String, Argument>();

					boolean printTime = false;
					byte argSkipCount = 0;
					boolean remainsValid = true;

					for (String x : args) {
						x = x.toLowerCase();
						switch (x) {
						case "\\time":
							printTime = true;
							break;
						case "\\perm":
							// commandFlags.add("user:380045419381784576");
							commandBlob.setUserID(380045419381784576L);
							break;
						case "\\del":
							messageReceivedEvent.getMessage().delete().queue();
							break;
						default: // in the rewrite process
							if (argSkipCount <= 0) {

								if (directCommand.getArguments() != null) {
									if (x.startsWith(Argument.argumentPrefix)) {

										String pre = x.substring(Argument.argumentPrefix.length());
										
										if (directCommand.getArguments().keySet().contains(pre)) {
											argumentList.put(pre, directCommand.getArguments().get(pre));
										} else {
											Tools.wrongUsage(messageReceivedEvent.getChannel(), directCommand);
											remainsValid = false;
										}
									} else {
										if (directCommand.getArguments().get(x).getPrefixRequirement()) {
											Tools.wrongUsage(messageReceivedEvent.getChannel(), directCommand);
											remainsValid = false;
										} else {
											argumentList.put(x, directCommand.getArguments().get(x));
										}
									}
								}
//
//								if (guildCommand.getArguments() != null) {
//
//									String newArg = x;
//									
//									if (!newArg.startsWith(Argument.argumentPrefix)) {
//										if (guildCommand.getArguments().get(newArg)!=null) {
//											
//										}
//									}
//									
//									if (guildCommand.getArguments().containsKey(newArg)) {
//
//										argumentList.put(guildCommand.getArguments().get(newArg).getArgName(),
//												guildCommand.getArguments().get(newArg));
//									} else
//
//										argumentList.put(newArg, new Argument(newArg));
//								}
							}
						}
					}
					
					commandBlob.setCommandManager(this);
					
					if (remainsValid) {
						directCommand.runDirectCommand(commandBlob, argumentList);
					}

					if (printTime) {
						messageReceivedEvent.getChannel()
								.sendMessage("Time to run: " + (System.currentTimeMillis() - commandStartTime) + "ms")
								.queue();
					}

				} catch (Exception e) {
					if (Boot.isProd) {
						messageReceivedEvent.getChannel().sendMessage("```properties\n" + e + "```").queue();
					} else {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						messageReceivedEvent.getChannel().sendMessage("```properties\n" + sw.toString() + "```").queue();
						System.err.println("Exception caught in: " + e.toString());
						e.printStackTrace();
					}
				} catch (Throwable t) {

					if (Boot.isProd) {
						messageReceivedEvent.getChannel().sendMessage("```mathematica\n" + t + "```").queue();
					} else {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						t.printStackTrace(pw);
						messageReceivedEvent.getChannel().sendMessage("```mathematica\n" + sw.toString() + "```").queue();
						System.err.println("Error caught in: " + t.toString());
						t.printStackTrace();
					}
				}
			});
		}
	}
}
