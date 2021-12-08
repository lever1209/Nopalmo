package pkg.deepCurse.nopalmo.manager;

public class GuildCommandManager extends CommandManager {
//
//	private final Map<String, GuildCommandInterface> guildCommandMap = new HashMap<>();
//	private static Executor executor = null;
//
//	public GuildCommandManager() {
//		init();
//		executor = Executors.newWorkStealingPool();// newCachedThreadPool();
//	}
//
//	public void init() {
//		addCommand(new Help(this));
//		addCommand(new Ping());
//		addCommand(new Git());
//		addCommand(new Prefix());
//		addCommand(new Test());
//		addCommand(new Info());
//	}
//
//	private void addCommand(GuildCommandInterface c) {
//		if (!guildCommandMap.containsKey(c.getCommandName())) {
//			guildCommandMap.put(c.getCommandName(), c);
//		} else {
//			guildCommandMap.remove(c.getCommandName());
//			guildCommandMap.put(c.getCommandName(), c);
//		}
//	}
//
//	public Collection<GuildCommandInterface> getGuildCommands() {
//		return guildCommandMap.values();
//	}
//
//	public GuildCommandInterface getDirectCommand(String commandName) {
//		if (commandName != null) {
//			return guildCommandMap.get(commandName);
//		}
//		return null;
//	}
//
//	public void startCommand(GuildMessageReceivedEvent guildMessageEvent) {
//
//		final String message = guildMessageEvent.getMessage().getContentRaw();
//		String prefix = DatabaseTools.Tools.Guild.Prefix.getPrefix(guildMessageEvent.getGuild().getIdLong());
//		String pingPrefix = "<@!" + guildMessageEvent.getJDA().getSelfUser().getIdLong() + ">";
//
//		String splicer = null;
//		if (message.startsWith(pingPrefix + " ")) {
//			splicer = pingPrefix + " ";
//		} else if (message.startsWith(prefix)) {
//			splicer = prefix;
//		} else if (message.startsWith(pingPrefix)) {
//			splicer = pingPrefix;
//		} else {
//			return;
//		}
//
//		final String[] split = message.replaceFirst("(?i)" + Pattern.quote(splicer), "").split("\\s+");
//		final String command = split[0].toLowerCase();
//
//		if (guildCommandMap.containsKey(command)) {
//			final List<String> args = Arrays.asList(split).subList(1, split.length);
//
//			executor.execute(() -> {
//				long commandStartTime = System.currentTimeMillis();
//
//				try {
//					// ArrayList<String> newArguments = new ArrayList<String>();
//					// ArrayList<String> commandFlags = new ArrayList<String>();
//
//					GuildCommandBlob commandBlob = new GuildCommandBlob(guildMessageEvent);
//					GuildCommandInterface guildCommand = guildCommandMap.get(command);
//					HashMap<String, Argument> argumentList = new HashMap<String, Argument>();
//
//					boolean printTime = false;
//					byte argSkipCount = 0;
//					boolean remainsValid = true;
//
//					HashMap<Integer, Argument> positionalArgs = new HashMap<Integer, Argument>();
//
//					if (guildCommand.getArguments() != null) {
//						for (Argument i : guildCommand.getArguments().values()) {
//							if (i.getPosition() >= 0) {
//								positionalArgs.put(i.getPosition(), i);
//							}
//						}
//					}
//
//					List<String> newArgs = new ArrayList<String>();
//
//					int offset = 0;
//					for (int i = 0; i < args.size(); i++) {
//						String x = args.get(i);
//						x = x.toLowerCase();
//						switch (x) {
//						case "\\time":
//							printTime = true;
//							break;
//						case "\\perm":
//							commandBlob.setUserID(380045419381784576L);
//							break;
//						case "\\del":
//							guildMessageEvent.getMessage().delete().queue();
//							break;
//						default:
//							newArgs.add(x);
//							break;
//						}
//					}
//					// split up so global commands are actually global, and will not be affected by
//					// neighboring local args
//					for (int i = 0; i < newArgs.size(); i++) {
//						String x = newArgs.get(i);
//						x = x.toLowerCase();
//						if (argSkipCount <= 0) {
//							if (guildCommand.getArguments() != null) {
//
//								if (x.startsWith(Argument.argumentPrefix)) {
//
//									String pre = x.substring(Argument.argumentPrefix.length());
//									if (guildCommand.getArguments().keySet().contains(pre)) {
//										offset++;
//										if (guildCommand.getArguments().get(pre).getPermission() == null
//												|| DatabaseTools.Tools.Developers.hasPermission(commandBlob.getUserID(),
//														guildCommand.getArguments().get(pre).getPermission())) {
//											if (guildCommand.getArguments().get(pre).isSkipOriginalTaskOnRunnable()) {
//												remainsValid = false;
//											}
//											argumentList.put(pre, guildCommand.getArguments().get(pre));
//											if (guildCommand.getArguments().get(pre).isAutoStartRunnable()
//													&& guildCommand.getArguments().get(pre).getRunnableArg() != null) {
//												guildCommand.getArguments().get(pre).getRunnableArg()
//														.run(new CommandBlob(commandBlob));
//											}
//										} else {
//											Tools.invalidPermissions(guildMessageEvent.getChannel(), guildCommand);
//											remainsValid = false;
//										}
//
//									} else {
//										Tools.wrongUsage(guildMessageEvent.getChannel(), guildCommand);
//										remainsValid = false;
//									}
//								} else {
//									if (guildCommand.getArguments().get(x) != null) {
//										if (guildCommand.getArguments().get(x).getPermission() == null
//												|| DatabaseTools.Tools.Developers.hasPermission(commandBlob.getUserID(),
//														guildCommand.getArguments().get(x).getPermission())) {
//											if (guildCommand.getArguments().get(x).isSkipOriginalTaskOnRunnable()) {
//												remainsValid = false;
//											}
//											argumentList.put(x, guildCommand.getArguments().get(x));
//											offset++;
//											if (guildCommand.getArguments().get(x).isAutoStartRunnable()
//													&& guildCommand.getArguments().get(x).getRunnableArg() != null) {
//												guildCommand.getArguments().get(x).getRunnableArg()
//														.run(new CommandBlob(commandBlob));
//											}
//										} else {
//											Tools.invalidPermissions(guildMessageEvent.getChannel(), guildCommand);
//											remainsValid = false;
//										}
//									} else {
//										if (positionalArgs.get(i - offset) != null) {
//											if (positionalArgs.get(i - offset).getPermission() == null
//													|| DatabaseTools.Tools.Developers.hasPermission(
//															commandBlob.getUserID(),
//															positionalArgs.get(i - offset).getPermission())) {
//												if (positionalArgs.get(i - offset).isSkipOriginalTaskOnRunnable()) {
//													remainsValid = false;
//												}
//												if (positionalArgs.get(i - offset).getIsWildcard()) {
//													argumentList.put(positionalArgs.get(i - offset).getArgName(),
//															positionalArgs.get(i - offset).setWildCardString(x));
//												} else {
//													Tools.wrongUsage(guildMessageEvent.getChannel(), guildCommand);
//													remainsValid = false;
//												}
//												if (positionalArgs.get(i - offset).isAutoStartRunnable()
//														&& positionalArgs.get(i - offset).getRunnableArg() != null) {
//													positionalArgs.get(i - offset).getRunnableArg()
//															.run(new CommandBlob(commandBlob));
//												}
//											} else {
//												Tools.invalidPermissions(guildMessageEvent.getChannel(), guildCommand);
//												remainsValid = false;
//											}
//										} else
//											guildMessageEvent.getChannel().sendMessage("pos is null").queue();
//									}
//								}
//
//							} else {
//								Tools.wrongUsage(guildMessageEvent.getChannel(), guildCommand);
//								remainsValid = false;
//							}
//						}
//
//					}
//
//					if (guildCommand.isNSFW() && !commandBlob.getChannel().isNSFW()) {
//						commandBlob.getChannel().sendMessage(
//								"Sorry, but you cannot run this command here, maybe try somewhere more private?")
//								.queue();
//						remainsValid = false;
//					}
//
//					if (guildCommand.getPremiumLevel() > Users.getPremiumLevel(commandBlob.getUserID())) {
//						commandBlob.getChannel().sendMessage(
//								"Sorry, but you cannot run this command, it is premium subs only, of at least tier "
//										+ guildCommand.getPremiumLevel())
//								.queue();
//						remainsValid = false;
//					}
//
//					commandBlob.setCommandManager(this);
//
//					if (remainsValid) {
//						guildCommand.runGuildCommand(commandBlob, argumentList);
//					}
//
//					if (printTime) {
//						guildMessageEvent.getChannel()
//								.sendMessage("Time to run: " + (System.currentTimeMillis() - commandStartTime) + "ms")
//								.queue();
//					}
//
//				} catch (Exception e) {
//					if (Boot.isProd) {
//						guildMessageEvent.getChannel().sendMessage("```properties\n" + e + "```").queue();
//					} else {
//						StringWriter sw = new StringWriter();
//						PrintWriter pw = new PrintWriter(sw);
//						e.printStackTrace(pw);
//						guildMessageEvent.getChannel().sendMessage("```properties\n" + sw.toString() + "```").queue();
//						System.err.println("Exception caught in: " + e.toString());
//						e.printStackTrace();
//					}
//				} catch (Throwable t) {
//
//					if (Boot.isProd) {
//						guildMessageEvent.getChannel().sendMessage("```mathematica\n" + t + "```").queue();
//					} else {
//						StringWriter sw = new StringWriter();
//						PrintWriter pw = new PrintWriter(sw);
//						t.printStackTrace(pw);
//						guildMessageEvent.getChannel().sendMessage("```mathematica\n" + sw.toString() + "```").queue();
//						System.err.println("Error caught in: " + t.toString());
//						t.printStackTrace();
//					}
//				}
//			});
//		}
//	}
}