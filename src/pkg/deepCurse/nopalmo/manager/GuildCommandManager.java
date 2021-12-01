package pkg.deepCurse.nopalmo.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import pkg.deepCurse.nopalmo.command.GuildCommand;
import pkg.deepCurse.nopalmo.command.guildCommand.Ping;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;

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
		
		final String[] split = message.replaceFirst("(?i)" + Pattern.quote(Global.getPrefix()), "").split("\\s+");
		final String command = split[0].toLowerCase();
		
		executor.execute(() -> {
			long commandStartTime = System.currentTimeMillis();
			
			
		});
		
	}
	
}
