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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import pkg.deepCurse.nopalmo.command.GuildCommand;
import pkg.deepCurse.nopalmo.command.guildCommand.info.Ping;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools;

public class GuildCommandManager {

	private final Map<String, GuildCommand> guildCommandMap = new HashMap<>();
	private static Executor executor = null;

	public GuildCommandManager() {
		init();
		executor = Executors.newSingleThreadExecutor();
	}

	public void init() {
		addCommand(new Ping());
	}

	private void addCommand(GuildCommand c) {
		if (!guildCommandMap.containsKey(c.getCommandName())) {
			guildCommandMap.put(c.getCommandName(), c);
		} else {
			guildCommandMap.remove(c.getCommandName());
			guildCommandMap.put(c.getCommandName(), c);
		}
	}

	public Collection<GuildCommand> getGuildCommands() {
		return guildCommandMap.values();
	}

	public GuildCommand getCommand(String commandName) {
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
					ArrayList<String> newArguments = new ArrayList<String>();
					// ArrayList<String> commandFlags = new ArrayList<String>();
					// ArrayList<String[]> extractedFlags = new ArrayList<String[]>(); // not needed currently, remnant idea of bash-ish

					GuildCommandBlob commandBlob = new GuildCommandBlob(guildMessage);

					boolean printTime = false;
					byte argSkipCount = 0;

					for (String x : args) {
						switch (x) {
						case "\\time":
							printTime = true;
							break;
						case "\\perm":
							// commandFlags.add("user:380045419381784576");
							commandBlob.setUser(380045419381784576L);
							break;
						case "\\del":
							guildMessage.getMessage().delete().queue();
							break;
						default:
							if (argSkipCount<=0) {
								newArguments.add(x);
							}
						}

					}
					
					commandBlob.setArgs(newArguments);
					
					guildCommandMap.get(command).run(commandBlob, this);

					if (printTime) {
						guildMessage.getChannel()
								.sendMessage("Time to run: " + (commandStartTime - System.currentTimeMillis()) + "ms")
								.queue();
					}

				} catch (Exception e) {
					if (Boot.isProd) {
						guildMessage.getChannel().sendMessage("```\n" + e + "```").queue();
					} else {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						guildMessage.getChannel().sendMessage("```\n" + sw.toString() + "```").queue();
						System.err.println("Exception caught in: " + e.toString());
						e.printStackTrace();
					}
				} catch (Throwable t) {

					if (Boot.isProd) {
						guildMessage.getChannel().sendMessage("```\n" + t + "```").queue();
					} else {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						t.printStackTrace(pw);
						guildMessage.getChannel().sendMessage("```\n" + sw.toString() + "```").queue();
						System.err.println("Error caught in: " + t.toString());
						t.printStackTrace();
					}

				}

			});
		}
	}

}
