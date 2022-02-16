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
import pkg.deepCurse.nopalmo.command.commands.fun.Stupid;
import pkg.deepCurse.nopalmo.command.commands.general.Prefix;
import pkg.deepCurse.nopalmo.command.commands.general.Test;
import pkg.deepCurse.nopalmo.command.commands.info.Git;
import pkg.deepCurse.nopalmo.command.commands.info.Help;
import pkg.deepCurse.nopalmo.command.commands.info.Info;
import pkg.deepCurse.nopalmo.command.commands.info.Ping;
import pkg.deepCurse.nopalmo.command.commands.info.Reload;
import pkg.deepCurse.nopalmo.command.commands.testing.GuildCommand;
import pkg.deepCurse.nopalmo.command.commands.testing.LiveUpdateTestCommand;
import pkg.deepCurse.nopalmo.command.commands.testing.PrivateCommand;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.DeveloperDB;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.GlobalDB;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.GuildDB;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.UserDB;
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
		commandMap.clear();
		addCommand(new Help(this));// guild
		addCommand(new Ping()); // dual
		addCommand(new Git()); // dual
		addCommand(new Prefix()); // guild
		addCommand(new Test()); // guild
		addCommand(new Info()); // guild direct
		addCommand(new GuildCommand()); // guild
		addCommand(new PrivateCommand()); // private
		addCommand(new Reload()); // dual
//		addCommand(new BontebokInterpret()); // dual
		addCommand(new Stupid()); // guild
		addCommand(new LiveUpdateTestCommand());
	}

	private void addCommand(CommandInterface c) {

		for (String i : c.getCommandCalls()) {
//			if (!commandMap.containsKey(i)) {
			commandMap.put(i, c);
//			}
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
		final String prefix = event.isFromGuild() ? (GuildDB.getPrefix(event.getGuild().getIdLong()) != null
				? GuildDB.getPrefix(event.getGuild().getIdLong())
				: GlobalDB.prefix) : GlobalDB.prefix;
		final String pingPrefix = "<@!" + event.getJDA().getSelfUser().getIdLong() + ">";

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

			UserDB.addUser(event.getAuthor().getIdLong());

			if (event.isFromGuild()) {
				GuildDB.addGuild(event.getGuild().getIdLong());
			}

			final List<String> args = Arrays.asList(split).subList(1, split.length);
			executor.execute(() -> {
				try {
					CommandBlob commandBlob = new CommandBlob(event, this);
					CommandInterface command = commandMap.get(commandCall);

//					commandBlob.setInterpreter(Boot.bontebokManager.getBontebokInterpreter());

					HashMap<String, Argument> argumentMap = new HashMap<String, Argument>();
					HashMap<Integer, Argument> positionalArgs = new HashMap<Integer, Argument>();

					long commandStartTime = System.currentTimeMillis();

					boolean printTime = false;
					byte argSkipCount = 0;
					boolean remainsValid = true;

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

					if (DeveloperDB.developerExists(commandBlob.getAuthorID())) {
						commandBlob.setDeveloper(DeveloperDB.getDeveloperBoolean(commandBlob.getAuthorID(),
								"developercommandpermission"));
					}
					for (int i = 0; i < newArgs.size(); i++) {
						String x = newArgs.get(i);
						x = x.toLowerCase();
						if (argSkipCount <= 0) {
							if (command.getArguments() != null) {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								if (x.startsWith(Argument.argumentPrefix)) {

									String pre = x.substring(Argument.argumentPrefix.length());

									if (!command.getArguments().keySet().contains(pre)) {
										for (Argument arg : command.getArguments().values()) {
											if (arg.getAliases().contains(pre)) {
//												System.out.println("ALIAS FOUND");
												pre = arg.getArgName();
											}
										}
									} // TODO RIP ROOT ALIAS PREFIX IN FAVOR OF "--long-name" "-s (shortname)"
										// "wildcard"

									if (command.getArguments().keySet().contains(pre)) {

										offset++;
										if (command.getArguments().get(pre).getPermission() == null
												|| DeveloperDB.hasPermission(commandBlob.getAuthorID(),
														command.getArguments().get(pre).getPermission())) {
											if (command.getArguments().get(pre).isSkipOriginalTaskOnRunnable()) {
												remainsValid = false;
											}
											argumentMap.put(pre, command.getArguments().get(pre));
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								} else {

									if (!command.getArguments().keySet().contains(x)) {
										for (Argument arg : command.getArguments().values()) {
											if (arg.getAliases().contains(x)) {
//												System.out.println("ALIAS FOUND");
												x = arg.getArgName();
											}
										}
									} // TODO RIP ROOT ALIAS PREFIX IN FAVOR OF "--long-name" "-s (shortname)"
										// "wildcard"

									if (command.getArguments().get(x) != null) {
										if (command.getArguments().get(x).isPrefixRequired()) {
											Tools.wrongUsage(event.getChannel(), command);
											remainsValid = false;
										}
										if (command.getArguments().get(x).getPermission() == null
												|| DeveloperDB.hasPermission(commandBlob.getAuthorID(),
														command.getArguments().get(x).getPermission())) {
											if (command.getArguments().get(x).isSkipOriginalTaskOnRunnable()) {
												remainsValid = false;
											}
											argumentMap.put(x, command.getArguments().get(x));
											offset++;
											if (command.getArguments().get(x).isAutoStartRunnable()
													&& command.getArguments().get(x).getRunnableArg() != null) {
												command.getArguments().get(x).getRunnableArg().run(commandBlob);
											}
										} else {
											Tools.invalidPermissions(event.getChannel(), command);
											remainsValid = false;
										}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

									} else {

										Argument posix = positionalArgs.get(i - offset);

//										if (!command.getArguments().keySet().contains(x)) {
//											for (Argument arg : command.getArguments().values()) {
//												if (arg.getAliases().contains(x)) {
////													System.out.println("ALIAS FOUND");
//													x = arg.getArgName();
//												}
//											}
//										} // TODO RIP ROOT ALIAS PREFIX IN FAVOR OF "--long-name" "-s (shortname)" "wildcard"

										if (posix != null) {
											if (posix.getPermission() == null || DeveloperDB
													.hasPermission(commandBlob.getAuthorID(), posix.getPermission())) {
												if (posix.isSkipOriginalTaskOnRunnable()) {
													remainsValid = false;
												}
												if (posix.getIsWildcard()) {
													argumentMap.put(posix.getArgName(), posix.setWildCardString(x));
												} else {
													Tools.wrongUsage(event.getChannel(), command);
													remainsValid = false;
												}
												if (posix.isAutoStartRunnable() && posix.getRunnableArg() != null) {
													posix.getRunnableArg().run(commandBlob);
												}
											} else {
												Tools.invalidPermissions(event.getChannel(), command);
												remainsValid = false;
											}
										} else
											event.getChannel().sendMessage("pos is null").queue();
									}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								}
							}
						}
					}

					if (command.getPremiumLevel() > UserDB.getPremiumLevel(commandBlob.getAuthorID())) {
						commandBlob.getChannel().sendMessage(
								"Sorry, but you cannot run this command, it is premium subs only, of at least tier "
										+ command.getPremiumLevel())
								.queue();
						remainsValid = false;
					}

					if (Help.deniedPages.contains(command.getHelpPage())) {
						if (!commandBlob.isDeveloper()) {
							commandBlob.getChannel()
									.sendMessage("Sorry, but you are not allowed to run this command. . ").queue();
							remainsValid = false;
						}
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

						if (command.getArguments() == null) {
							StringBuilder sB = new StringBuilder();
							for (String i : newArgs) {
								sB.append(i + " ");
							}
							argumentMap.clear();
							argumentMap.put("null", new Argument("null").setWildCardString(sB.toString().trim()));
						}

						if (command instanceof DualCommandInterface) {
							((DualCommandInterface) command).runDualCommand(commandBlob, argumentMap);
						} else if (command instanceof GuildCommandInterface && event.isFromGuild()) {
							((GuildCommandInterface) command).runGuildCommand(commandBlob, argumentMap);
						} else if (command instanceof PrivateCommandInterface && !event.isFromGuild()) {
							((PrivateCommandInterface) command).runPrivateCommand(commandBlob, argumentMap);
						}

						if (command instanceof GuildCommandInterface && !event.isFromGuild()
								&& !(command instanceof PrivateCommandInterface)) {
							event.getChannel()
									.sendMessage("Sorry, but you need to be in a "
											+ (UserDB.isAdvancedUser(commandBlob.getAuthorID()) ? "guild" : "server")
											+ " to use this command. . .")
									.queue();
						} else if (command instanceof PrivateCommandInterface && event.isFromGuild()
								&& !(command instanceof GuildCommandInterface)) {
							event.getChannel().sendMessage("Sorry, but this command will only run in dms. . .").queue();
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
