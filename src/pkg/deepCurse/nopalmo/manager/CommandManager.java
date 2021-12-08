package pkg.deepCurse.nopalmo.manager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import pkg.deepCurse.nopalmo.command.CommandInterface;
import pkg.deepCurse.nopalmo.command.CommandInterface.DualCommandInterface;
import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.command.CommandInterface.PrivateCommandInterface;
import pkg.deepCurse.nopalmo.command.commands.general.Example;
import pkg.deepCurse.nopalmo.command.commands.general.Prefix;
import pkg.deepCurse.nopalmo.command.commands.general.Test;
import pkg.deepCurse.nopalmo.command.commands.info.Git;
import pkg.deepCurse.nopalmo.command.commands.info.Help;
import pkg.deepCurse.nopalmo.command.commands.info.Info;
import pkg.deepCurse.nopalmo.command.commands.info.Ping;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Users;
import pkg.deepCurse.nopalmo.global.Tools;

public class CommandManager {

	private final Map<String, CommandInterface> commandMap = new HashMap<>();
	// private final Map<String, DirectCommandInterface> directCommandMap = new
	// HashMap<>();
	private static Executor executor = null;

	public CommandManager() {
		init();
		executor = Executors.newWorkStealingPool();
	}

	public void init() {
		addCommand(new Help(this));// guild
		addCommand(new Ping()); // dual
		addCommand(new Git()); // dual
		addCommand(new Prefix()); // guild
		addCommand(new Test()); // guild
		addCommand(new Info()); // guild direct
		addCommand(new Example()); // dual
	}

	private void addCommand(CommandInterface c) {
		if (!commandMap.containsKey(c.getCommandName())) {
			commandMap.put(c.getCommandName(), c);
		} else {
			commandMap.remove(c.getCommandName());
			commandMap.put(c.getCommandName(), c);
		}
	}

	public CommandInterface getCommand(String commandName) {
		return commandMap.get(commandName);
	}

	public Collection<CommandInterface> getCommands() {
		return commandMap.values();
	}

	public void startCommand(MessageReceivedEvent event) { // TODO split up more

		final String message = event.getMessage().getContentRaw();
		String prefix = null;
		if (event.isFromGuild()) {
			prefix = DatabaseTools.Tools.Guild.Prefix.getPrefix(event.getGuild().getIdLong());
		} else {
			prefix = Global.prefix;
		}
		String pingPrefix = "<@!" + event.getJDA().getSelfUser().getIdLong() + ">";

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
		final String commandCall = split[0].toLowerCase();

		if (commandMap.containsKey(commandCall)) {
			final List<String> args = Arrays.asList(split).subList(1, split.length);

			executor.execute(() -> {
				long commandStartTime = System.currentTimeMillis();

				try {
					CommandBlob commandBlob = new CommandBlob(event, this);
					CommandInterface command = commandMap.get(commandCall);
					HashMap<String, Argument> argumentList = new HashMap<String, Argument>();

					boolean printTime = false;
					byte argSkipCount = 0;
					boolean remainsValid = true;

					HashMap<Integer, Argument> positionalArgs = new HashMap<Integer, Argument>();

					if (command.getArguments() != null) {
						for (Argument i : command.getArguments().values()) {
							if (i.getPosition() >= 0) {
								positionalArgs.put(i.getPosition(), i);
							}
						}
					}

					List<String> newArgs = new ArrayList<String>();

					int offset = 0;
					for (int i = 0; i < args.size(); i++) {
						String x = args.get(i);
						x = x.toLowerCase();
						switch (x) {
						case "\\time":
							printTime = true;
							break;
						case "\\perm":
							commandBlob.setAuthorID(380045419381784576L);
							break;
						case "\\del":
							event.getMessage().delete().queue();
							break;
						default:
							newArgs.add(x);
							break;
						}
					}
					// split up so global commands are actually global, and will not be affected by
					// neighboring local args
					for (int i = 0; i < newArgs.size(); i++) {
						String x = newArgs.get(i);
						x = x.toLowerCase();
						if (argSkipCount <= 0) {
							if (command.getArguments() != null) {

								if (x.startsWith(Argument.argumentPrefix)) {

									String pre = x.substring(Argument.argumentPrefix.length());
									if (command.getArguments().keySet().contains(pre)) {
										offset++;
										if (command.getArguments().get(pre).getPermission() == null
												|| DatabaseTools.Tools.Developers.hasPermission(
														commandBlob.getAuthorID(),
														command.getArguments().get(pre).getPermission())) {
											if (command.getArguments().get(pre).isSkipOriginalTaskOnRunnable()) {
												remainsValid = false;
											}
											argumentList.put(pre, command.getArguments().get(pre));
											if (command.getArguments().get(pre).isAutoStartRunnable()
													&& command.getArguments().get(pre).getRunnableArg() != null) {
												command.getArguments().get(pre).getRunnableArg().run(commandBlob);
											}
										} else {
											Tools.invalidPermissions(event.getChannel(), command);
											remainsValid = false;
										}

									} else {
										Tools.wrongUsage(event.getChannel(), command);
										remainsValid = false;
									}
								} else {
									if (command.getArguments().get(x) != null) {
										if (command.getArguments().get(x).getPermission() == null
												|| DatabaseTools.Tools.Developers.hasPermission(
														commandBlob.getAuthorID(),
														command.getArguments().get(x).getPermission())) {
											if (command.getArguments().get(x).isSkipOriginalTaskOnRunnable()) {
												remainsValid = false;
											}
											argumentList.put(x, command.getArguments().get(x));
											offset++;
											if (command.getArguments().get(x).isAutoStartRunnable()
													&& command.getArguments().get(x).getRunnableArg() != null) {
												command.getArguments().get(x).getRunnableArg().run(commandBlob);
											}
										} else {
											Tools.invalidPermissions(event.getChannel(), command);
											remainsValid = false;
										}
									} else {
										if (positionalArgs.get(i - offset) != null) {
											if (positionalArgs.get(i - offset).getPermission() == null
													|| DatabaseTools.Tools.Developers.hasPermission(
															commandBlob.getAuthorID(),
															positionalArgs.get(i - offset).getPermission())) {
												if (positionalArgs.get(i - offset).isSkipOriginalTaskOnRunnable()) {
													remainsValid = false;
												}
												if (positionalArgs.get(i - offset).getIsWildcard()) {
													argumentList.put(positionalArgs.get(i - offset).getArgName(),
															positionalArgs.get(i - offset).setWildCardString(x));
												} else {
													Tools.wrongUsage(event.getChannel(), command);
													remainsValid = false;
												}
												if (positionalArgs.get(i - offset).isAutoStartRunnable()
														&& positionalArgs.get(i - offset).getRunnableArg() != null) {
													positionalArgs.get(i - offset).getRunnableArg().run(commandBlob);
												}
											} else {
												Tools.invalidPermissions(event.getChannel(), command);
												remainsValid = false;
											}
										} else
											event.getChannel().sendMessage("pos is null").queue();
									}
								}

							} else {
								Tools.wrongUsage(event.getChannel(), command);
								remainsValid = false;
							}
						}

					}

					if (command.getPremiumLevel() > Users.getPremiumLevel(commandBlob.getAuthorID())) {
						commandBlob.getChannel().sendMessage(
								"Sorry, but you cannot run this command, it is premium subs only, of at least tier "
										+ command.getPremiumLevel())
								.queue();
						remainsValid = false;
					}

					commandBlob.setCommandManager(this);

					if (event.isFromGuild()) {
						if (command.isNSFW() && !((TextChannel) commandBlob.getChannel()).isNSFW()) {
							commandBlob.getChannel().sendMessage(
									"Sorry, but you cannot run this command here, maybe try somewhere more private?")
									.queue();
							remainsValid = false;
						}
					}

					if (remainsValid) {
						if (command instanceof DualCommandInterface) {
							((DualCommandInterface) command).runDualCommand(commandBlob, argumentList);
						} else if (command instanceof GuildCommandInterface && event.isFromGuild()) {
							((GuildCommandInterface) command).runGuildCommand(commandBlob, argumentList);
						} else if (command instanceof PrivateCommandInterface && !event.isFromGuild()) {
							((PrivateCommandInterface) command).runDirectCommand(commandBlob, argumentList);
						}
					}

					if (printTime) {
						event.getChannel()
								.sendMessage("Time to run: " + (System.currentTimeMillis() - commandStartTime) + "ms")
								.queue();
					}

				} catch (Exception e) {
					if (Boot.isProd) {
						event.getChannel().sendMessage("```properties\n" + e + "```").queue();
					} else {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						event.getChannel().sendMessage("```properties\n" + sw.toString() + "```").queue();
						System.err.println("Exception caught in: " + e.toString());
						e.printStackTrace();
					}
				} catch (Throwable t) {

					if (Boot.isProd) {
						event.getChannel().sendMessage("```mathematica\n" + t + "```").queue();
					} else {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						t.printStackTrace(pw);
						event.getChannel().sendMessage("```mathematica\n" + sw.toString() + "```").queue();
						System.err.println("Error caught in: " + t.toString());
						t.printStackTrace();
					}
				}
			});
		}
	}
}
