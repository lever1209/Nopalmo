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
import pkg.deepCurse.nopalmo.command.commands.info.Git;
import pkg.deepCurse.nopalmo.command.commands.info.Ping;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.global.Tools;

public class DirectCommandManager extends CommandManager {

	private final Map<String, DirectCommandInterface> directCommandMap = new HashMap<>();
	private static Executor executor = null;

	public DirectCommandManager() {
		init();
		executor = Executors.newCachedThreadPool();
	}

	public void init() {
		addCommand(new Ping());
		addCommand(new Git());
		// addCommand(new Help(this));
		
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

	public void startCommand(MessageReceivedEvent messageReceivedEvent) { // TODO SPLIT UP AND INHERIT

		final String message = messageReceivedEvent.getMessage().getContentRaw();
		String prefix = Global.prefix;
		String pingPrefix = "<@!" + messageReceivedEvent.getJDA().getSelfUser().getIdLong() + ">";

		String splicer = null;
		if (message.startsWith(pingPrefix + " ")) {
			splicer = pingPrefix + " ";
		} else if (message.startsWith(prefix)) {
			splicer = prefix;
		} else if (message.startsWith(pingPrefix)) {
			splicer = pingPrefix;
		} else {
			return;
		}

		final String[] split = message.replaceFirst("(?i)" + Pattern.quote(splicer), "").split("\\s+");
		final String command = split[0].toLowerCase();

		if (directCommandMap.containsKey(command)) {
			final List<String> args = Arrays.asList(split).subList(1, split.length);

			executor.execute(() -> {
				long commandStartTime = System.currentTimeMillis();

				try {
					// ArrayList<String> newArguments = new ArrayList<String>();
					// ArrayList<String> commandFlags = new ArrayList<String>();

					DirectCommandBlob commandBlob = new DirectCommandBlob(messageReceivedEvent);
					DirectCommandInterface directCommand = directCommandMap.get(command);
					HashMap<String, Argument> argumentList = new HashMap<String, Argument>();

					boolean printTime = false;
					byte argSkipCount = 0;
					boolean remainsValid = true;
					// int id = 0;

					HashMap<Integer, Argument> positionalArgs = new HashMap<Integer, Argument>();

					if (directCommand.getArguments() != null) {
						for (Argument i : directCommand.getArguments().values()) {
							if (i.getPosition() >= 0) {
								positionalArgs.put(i.getPosition(), i);
							}

							if (i.isSkipOriginalTaskOnRunnable()) {
								remainsValid = false;
							}

						}
					}

					for (int i = 0; i < args.size(); i++) {
						String x = args.get(i);
						x = x.toLowerCase();
						switch (x) {
						case "\\time":
							printTime = true;
							break;
						case "\\perm":
							commandBlob.setUserID(380045419381784576L);
							break;
						case "\\del":
							messageReceivedEvent.getMessage().delete().queue();
							break;
						default:
							if (argSkipCount <= 0) {
								if (directCommand.getArguments() != null) {

									if (positionalArgs.get(i) == null) {

										if (x.startsWith(Argument.argumentPrefix)) {

											String pre = x.substring(Argument.argumentPrefix.length());
											if (directCommand.getArguments().keySet().contains(pre)) {
												argumentList.put(pre, directCommand.getArguments().get(pre));
												if (directCommand.getArguments().get(pre).isAutoStartRunnable()
														&& directCommand.getArguments().get(pre)
																.getRunnableArg() != null) {
													directCommand.getArguments().get(pre).getRunnableArg()
															.run(new CommandBlob(commandBlob));
												}
											} else {
												Tools.wrongUsage(messageReceivedEvent.getChannel(), directCommand);
												remainsValid = false;
											}
										} else {
											if (directCommand.getArguments().get(x) != null) {
												if (directCommand.getArguments().get(x).getPrefixRequirement()) {
													Tools.wrongUsage(messageReceivedEvent.getChannel(), directCommand);
													remainsValid = false;
												} else {
													argumentList.put(x, directCommand.getArguments().get(x));
													if (directCommand.getArguments().get(x).isAutoStartRunnable()
															&& directCommand.getArguments().get(x)
																	.getRunnableArg() != null) {
														directCommand.getArguments().get(x).getRunnableArg()
																.run(new CommandBlob(commandBlob));
													}
												}
											} else {
												Tools.wrongUsage(messageReceivedEvent.getChannel(), directCommand);
												remainsValid = false;
											}
										}
									} else {
										if (positionalArgs.get(i).getIsWildcard()) {
											argumentList.put(positionalArgs.get(i).getArgName(),
													positionalArgs.get(i).setWildCardString(x));
										}
										if (positionalArgs.get(i).isAutoStartRunnable()
												&& positionalArgs.get(i).getRunnableArg() != null) {
											positionalArgs.get(i).getRunnableArg().run(new CommandBlob(commandBlob));
										}
									}
								}
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
