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
import pkg.deepCurse.nopalmo.command.commands.info.Git;
import pkg.deepCurse.nopalmo.command.commands.info.Help;
import pkg.deepCurse.nopalmo.command.commands.info.Ping;
import pkg.deepCurse.nopalmo.command.commands.info.Prefix;
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

	public void startCommand(GuildMessageReceivedEvent guildMessage) {

		final String message = guildMessage.getMessage().getContentRaw();

		final String[] split = message.replaceFirst("(?i)" + Pattern.quote(DatabaseTools.Tools.Global.prefix), "")
				.split("\\s+");
		final String command = split[0].toLowerCase();

		if (guildCommandMap.containsKey(command)) {
			final List<String> args = Arrays.asList(split).subList(1, split.length);

			executor.execute(() -> {
				long commandStartTime = System.currentTimeMillis();

				try {
					// ArrayList<String> newArguments = new ArrayList<String>();
					// ArrayList<String> commandFlags = new ArrayList<String>();

					GuildCommandBlob commandBlob = new GuildCommandBlob(guildMessage);
					GuildCommandInterface guildCommand = guildCommandMap.get(command);
					HashMap<String, Argument> argumentList = new HashMap<String, Argument>();

					boolean printTime = false;
					byte argSkipCount = 0;
					boolean remainsValid = true;
					boolean isWildCard = false;

					for (String x : args) {
						boolean taken = false;
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
							guildMessage.getMessage().delete().queue();
							break;
						default: // in the rewrite process
							if (argSkipCount <= 0) {

								if (guildCommand.getArguments() != null) {
									if (x.startsWith(Argument.argumentPrefix)) {

										String pre = x.substring(Argument.argumentPrefix.length());
										if (guildCommand.getArguments().keySet().contains(pre)) {
											argumentList.put(pre, guildCommand.getArguments().get(pre));
											taken = true;
										} else {
											Tools.wrongUsage(guildMessage.getChannel(), guildCommand);
											remainsValid = false;
										}
									} else {
										// if (!guildCommand.getArguments().get(x).getIsWildcard()) {
										if (guildCommand.getArguments().get(x).getPrefixRequirement()) {
											Tools.wrongUsage(guildMessage.getChannel(), guildCommand);
											remainsValid = false;
										} else {
											argumentList.put(x, guildCommand.getArguments().get(x));
											taken = true;
										}
										// } else {
										// argumentList.put(x, guildCommand.getArguments().get(x));
										// }
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
						guildMessage.getChannel()
								.sendMessage("Time to run: " + (System.currentTimeMillis() - commandStartTime) + "ms")
								.queue();
					}

				} catch (Exception e) {
					if (Boot.isProd) {
						guildMessage.getChannel().sendMessage("```properties\n" + e + "```").queue();
					} else {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						guildMessage.getChannel().sendMessage("```properties\n" + sw.toString() + "```").queue();
						System.err.println("Exception caught in: " + e.toString());
						e.printStackTrace();
					}
				} catch (Throwable t) {

					if (Boot.isProd) {
						guildMessage.getChannel().sendMessage("```mathematica\n" + t + "```").queue();
					} else {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						t.printStackTrace(pw);
						guildMessage.getChannel().sendMessage("```mathematica\n" + sw.toString() + "```").queue();
						System.err.println("Error caught in: " + t.toString());
						t.printStackTrace();
					}
				}
			});
		}
	}
}
