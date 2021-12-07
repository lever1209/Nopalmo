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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import pkg.deepCurse.nopalmo.command.CommandInterface.GuildCommandInterface;
import pkg.deepCurse.nopalmo.command.commands.general.Prefix;
import pkg.deepCurse.nopalmo.command.commands.info.Git;
import pkg.deepCurse.nopalmo.command.commands.info.Help;
import pkg.deepCurse.nopalmo.command.commands.info.Ping;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools;
import pkg.deepCurse.nopalmo.global.Tools;

public class GuildCommandManager extends CommandManager {

	private final Map<String, GuildCommandInterface> guildCommandMap = new HashMap<>();
	private static Executor executor = null;

	public GuildCommandManager() {
		init();
		executor = Executors.newSingleThreadExecutor();
	}

	public void init() {
		addCommand(new Help(this));
		addCommand(new Ping());
		addCommand(new Git());
		addCommand(new Prefix());
	}

	private void addCommand(GuildCommandInterface c) {
		if (!guildCommandMap.containsKey(c.getCommandName())) {
			guildCommandMap.put(c.getCommandName(), c);
		} else {
			guildCommandMap.remove(c.getCommandName());
			guildCommandMap.put(c.getCommandName(), c);
		}
	}

	public Collection<GuildCommandInterface> getGuildCommands() {
		return guildCommandMap.values();
	}

	public GuildCommandInterface getCommand(String commandName) {
		if (commandName != null) {
			return guildCommandMap.get(commandName);
		}
		return null;
	}

	public void startCommand(GuildMessageReceivedEvent guildMessageEvent) { // TODO SPLIT UP AND INHERIT

		final String message = guildMessageEvent.getMessage().getContentRaw();
		String prefix = DatabaseTools.Tools.Guild.Prefix.getPrefix(guildMessageEvent.getGuild().getIdLong());
		String pingPrefix = "<@!" + guildMessageEvent.getJDA().getSelfUser().getIdLong() + ">";

//		String splicer = null;
//		String[] prefixArray = new String[] { DatabaseTools.Tools.Guild.Prefix.getPrefix(guildMessageEvent.getGuild().getIdLong()),
//				"<@!" + guildMessageEvent.getJDA().getSelfUser().getIdLong() + ">" }; // FIXME BROKEN PING PREFIX
//		for () {
//			
//		}

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

		if (guildCommandMap.containsKey(command)) {
			final List<String> args = Arrays.asList(split).subList(1, split.length);

			executor.execute(() -> {
				long commandStartTime = System.currentTimeMillis();

				try {
					// ArrayList<String> newArguments = new ArrayList<String>();
					// ArrayList<String> commandFlags = new ArrayList<String>();

					GuildCommandBlob commandBlob = new GuildCommandBlob(guildMessageEvent);
					GuildCommandInterface guildCommand = guildCommandMap.get(command);
					HashMap<String, Argument> argumentList = new HashMap<String, Argument>();

					boolean printTime = false;
					byte argSkipCount = 0;
					boolean remainsValid = true;
					// int id = 0;

					HashMap<Integer, Argument> positionalArgs = new HashMap<Integer, Argument>();

					for (Argument i : guildCommand.getArguments().values()) {
						if (i.getPosition() >= 0) {
							positionalArgs.put(i.getPosition(), i);
						}

						if (i.isSkipOriginalTaskOnRunnable()) {
							remainsValid = false;
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
							guildMessageEvent.getMessage().delete().queue();
							break;
						default:
							if (argSkipCount <= 0) {
								if (guildCommand.getArguments() != null) {

									if (positionalArgs.get(i) == null) {

										if (x.startsWith(Argument.argumentPrefix)) {

											String pre = x.substring(Argument.argumentPrefix.length());
											if (guildCommand.getArguments().keySet().contains(pre)) {
												argumentList.put(pre, guildCommand.getArguments().get(pre));
												if (guildCommand.getArguments().get(pre).isAutoStartRunnable()
														&& guildCommand.getArguments().get(pre)
																.getRunnableArg() != null) {
													guildCommand.getArguments().get(pre).getRunnableArg()
															.run(new CommandBlob(commandBlob));
												}
											} else {
												Tools.wrongUsage(guildMessageEvent.getChannel(), guildCommand);
												remainsValid = false;
											}
										} else {
											if (guildCommand.getArguments().get(x) != null) {
												if (guildCommand.getArguments().get(x).getPrefixRequirement()) {
													Tools.wrongUsage(guildMessageEvent.getChannel(), guildCommand);
													remainsValid = false;
												} else {
													argumentList.put(x, guildCommand.getArguments().get(x));
													if (guildCommand.getArguments().get(x).isAutoStartRunnable()
															&& guildCommand.getArguments().get(x)
																	.getRunnableArg() != null) {
														guildCommand.getArguments().get(x).getRunnableArg()
																.run(new CommandBlob(commandBlob));
													}
												}
											} else {
												Tools.wrongUsage(guildMessageEvent.getChannel(), guildCommand);
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
						guildCommand.runGuildCommand(commandBlob, argumentList);
					}

					if (printTime) {
						guildMessageEvent.getChannel()
								.sendMessage("Time to run: " + (System.currentTimeMillis() - commandStartTime) + "ms")
								.queue();
					}

				} catch (Exception e) {
					if (Boot.isProd) {
						guildMessageEvent.getChannel().sendMessage("```properties\n" + e + "```").queue();
					} else {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						guildMessageEvent.getChannel().sendMessage("```properties\n" + sw.toString() + "```").queue();
						System.err.println("Exception caught in: " + e.toString());
						e.printStackTrace();
					}
				} catch (Throwable t) {

					if (Boot.isProd) {
						guildMessageEvent.getChannel().sendMessage("```mathematica\n" + t + "```").queue();
					} else {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						t.printStackTrace(pw);
						guildMessageEvent.getChannel().sendMessage("```mathematica\n" + sw.toString() + "```").queue();
						System.err.println("Error caught in: " + t.toString());
						t.printStackTrace();
					}
				}
			});
		}
	}
}
