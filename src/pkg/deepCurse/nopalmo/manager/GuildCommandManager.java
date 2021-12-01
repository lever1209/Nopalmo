package pkg.deepCurse.nopalmo.manager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import pkg.deepCurse.nopalmo.command.GuildCommand;
import pkg.deepCurse.nopalmo.command.guildCommand.Ping;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools;

public class GuildCommandManager {

	private final Map<String, GuildCommand> guildCommandMap = new HashMap<>();
	Executor executor = null;

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

	public Collection<GuildCommand> getguildCommandMap() {
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

		executor.execute(() -> {
			long commandStartTime = System.currentTimeMillis();

			try {
				
				ArrayList<String> newArguments = new ArrayList<String>();
				
				ArrayList<String[]> extractedFlags = new ArrayList<String[]>();
				
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
