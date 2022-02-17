package pkg.deepCurse.nopalmo.core.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextConnection;
import org.fluentjdbc.DbContextTable;
import org.slf4j.Logger;
import org.slf4j.impl.SimpleLoggerFactory;

import com.mysql.cj.jdbc.MysqlDataSource;

import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.GlobalDB;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.InfractionDB;
import pkg.deepCurse.nopalmo.core.database.NopalmoDBTools.Tools.UserDB;

public class NopalmoDBTools {

	private static Logger logger = new SimpleLoggerFactory().getLogger(NopalmoDBTools.class.getSimpleName());

	private static DbContext context = null;

	private static DbContextTable users = null;
	private static DbContextTable guilds = null;
	private static DbContextTable developers = null;
	private static DbContextTable global = null;
	@SuppressWarnings("unused") // it will remain un implemented for a little bit
	private static DbContextTable actions = null;
	private static DbContextTable infractions = null;

	private static MysqlDataSource dataSource = null;

	public static void init(String dbName, String username, String pass) {
//		logger.info("Connecting. . .");
		context = new DbContext();
		dataSource = new MysqlDataSource();
		dataSource.setDatabaseName(dbName);
		dataSource.setUser(username);
		dataSource.setPassword(pass);

		users = context.table("users");
		guilds = context.table("guilds");
		developers = context.table("developers");
		global = context.table("global");
		actions = context.table("actions");
		infractions = context.table("infractions");

		GlobalDB.prefix = GlobalDB.getGlobalValue("prefix");

//		logger.info("Connected");
	}

	public static void close() throws SQLException {
		dataSource.getConnection().close();
	}

	public class Tools {

		public class UserDB {

			public static boolean getUserBoolean(long userID, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Optional<String> out = users.query().where("userid", userID).singleString(value);
					return out.isPresent() ? out.get().contentEquals("1") : null;
				}
			}

			public static String getUserString(long userID, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Optional<String> out = users.query().where("userid", userID).singleString(value);
					return out.isPresent() ? out.get() : null;
				}
			}

			public static long getUserLong(long userID, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Optional<Number> out = users.query().where("userid", userID).singleLong(value);
					return out.isPresent() ? out.get().longValue() : null;
				}
			}

			public static boolean userExists(long userID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					return users.query().where("userid", userID).singleLong("userid").isPresent();
				}
			}

			public static void setUserBoolean(long userID, String columnName, boolean value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						users.insert().setPrimaryKey("userid", userID).setField(columnName, value).execute();
					} catch (Exception e) {
//					e.printStackTrace();
						try {
							users.query().where("userid", userID).update().setField(columnName, value).execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void setUserString(long userID, String columnName, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						users.insert().setPrimaryKey("userid", userID).setField(columnName, value).execute();
					} catch (Exception e) {
//					e.printStackTrace();
						try {
							users.query().where("userid", userID).update().setField(columnName, value).execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void setUserLong(long userID, String columnName, long value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						users.insert().setPrimaryKey("userid", userID).setField(columnName, value).execute();
					} catch (Exception e) {
//					e.printStackTrace();
						try {
							users.query().where("userid", userID).update().setField(columnName, value).execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void addUser(long userID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						users.insert().setPrimaryKey("userid", userID).execute();
					} catch (Exception e) {

					}
				}
			}

			public static void removeUser(long userID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					users.query().where("userid", userID).executeDelete();
				}
			}

			public static boolean isDirectMessagingEnabled(long userID) {
				return getUserBoolean(userID, "allowdms");
			}

			public static boolean isAdvancedUser(long userID) {
				return getUserBoolean(userID, "advanceduser");
			}

			public static long getPremiumLevel(long userID) {
				return getUserLong(userID, "premiumlevel");
			}

			public static void setDirectMessagingBoolean(long userID, boolean value) {
				setUserBoolean(userID, "allowdms", value);
			}

			public static void setAdvancedUserBoolean(long userID, boolean value) {
				setUserBoolean(userID, "advanceduser", value);
			}

			public static void setPremiumLevel(long userID, int value) {
				setUserLong(userID, "premiumlevel", value);
			}

			/**
			 * this exists because the lib i tried swapping to doesnt have shit to do with
			 * columns, and since its only input is long, it should be fine
			 */
			public static String generateDump(long userID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Statement st = null;
					ResultSet rs = null;
					String query = "select * from users where userid = " + userID;
					try {
						st = users.getConnection().createStatement();
						rs = st.executeQuery(query);
						ResultSetMetaData rsMeta = rs.getMetaData();
						int columnCount = rsMeta.getColumnCount();
						StringBuilder sB = new StringBuilder();
						while (rs.next()) {
							// Object[] values = new Object[columnCount];
							for (int i = 1; i <= columnCount; i++) {
								// values[i - 1] = resultSet.getObject(i);
								sB.append(rsMeta.getColumnLabel(i) + ": " + rs.getString(i) + "\n");
							}
						}
						return sB.toString().isEmpty() ? "No data here" : sB.toString();

					} catch (SQLException e) {
						return null;
					} finally { // @formatter:off
					try {if (rs != null)rs.close();} catch (Exception e) {}
					try {if (st != null)st.close();} catch (Exception e) {}
					// @formatter:on
					}
				}
			}
		}

		public class GuildDB {

			public static boolean getGuildBoolean(long guildID, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Optional<String> out = guilds.query().where("guildid", guildID).singleString(value);
					return out.isPresent() ? out.get().contentEquals("1") : null;
				}
			}

			public static String getGuildString(long guildID, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Optional<String> out = guilds.query().where("guildid", guildID).singleString(value);
					return out.isPresent() ? out.get() : null;
				}
			}

			public static long getGuildLong(long guildID, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Optional<Number> out = guilds.query().where("guildid", guildID).singleLong(value);
					return out.isPresent() ? out.get().longValue() : null;
				}
			}

			public static boolean guildExists(long guildID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					return guilds.query().where("guildid", guildID).singleLong("guildid").isPresent();
				}
			}

			public static void setGuildBoolean(long guildID, String columnName, boolean value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						guilds.insert().setPrimaryKey("guildid", guildID).setField(columnName, value).execute();
					} catch (Exception e) {
//					e.printStackTrace();
						try {
							guilds.query().where("guildid", guildID).update().setField(columnName, value).execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void setGuildString(long guildID, String columnName, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						guilds.insert().setPrimaryKey("guildid", guildID).setField(columnName, value).execute();
					} catch (Exception e) {
//					e.printStackTrace();
						try {
							guilds.query().where("guildid", guildID).update().setField(columnName, value).execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void setGuildLong(long guildID, String columnName, long value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						guilds.insert().setPrimaryKey("guildid", guildID).setField(columnName, value).execute();
					} catch (Exception e) {
//					e.printStackTrace();
						try {
							guilds.query().where("guildid", guildID).update().setField(columnName, value).execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void addGuild(long guildID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						guilds.insert().setPrimaryKey("guildid", guildID).execute();
					} catch (Exception e) {
					}
				}
			}

			public static void removeGuild(long guildID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					guilds.query().where("guildid", guildID).executeDelete();
				}
			}

			public static void setJoinMessage(long guildID, String value) {
				setGuildString(guildID, "joinmessage", value);
			}

			public static void setLeaveMessage(long guildID, String value) {
				setGuildString(guildID, "leavemessage", value);
			}

			public static String getJoinMessage(long guildID) {
				return getGuildString(guildID, "joinmessage");
			}

			public static String getLeaveMessage(long guildID) {
				return getGuildString(guildID, "leavemessage");
			}

			public static String getPrefix(long guildID) {
				return getGuildString(guildID, "prefix");
			}

			public static void setPrefix(long guildID, String value) {
				setGuildString(guildID, "prefix", value);
			}

			public static String getDateObjectFormat(long guildID) {
				return getGuildString(guildID, "dateobjectformat");
			}

			public static void setDateObjectFormat(long guildID, String value) {
				setGuildString(guildID, "dateobjectformat", value);
			}

			public static boolean getUpdateOwnNameWithPrefix(long guildID) {
				return getGuildBoolean(guildID, "updatesownnamewithprefix");
			}

			public static void setUpdateOwnNameWithPrefix(long guildID, boolean value) {
				setGuildBoolean(guildID, "updatesownnamewithprefix", value);
			}

		}

		public class DeveloperDB {

			public static boolean getDeveloperBoolean(long userID, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Optional<String> out = developers.query().where("developerid", userID).singleString(value);
					return out.isPresent() ? out.get().contentEquals("1") : null;
				}
			}

			public static String getDeveloperString(long userID, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Optional<String> out = developers.query().where("developerid", userID).singleString(value);
					return out.isPresent() ? out.get() : null;
				}
			}

			public static long getDeveloperLong(long userID, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Optional<Number> out = developers.query().where("developerid", userID).singleLong(value);
					return out.isPresent() ? out.get().longValue() : null;
				}
			}

			public static boolean developerExists(long userID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					return developers.query().where("developerid", userID).singleLong("developerid").isPresent();
				}
			}

			public static void setDeveloperBoolean(long developerID, String columnName, boolean value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						developers.insert().setPrimaryKey("developerid", developerID).setField(columnName, value)
								.execute();
					} catch (Exception e) {
//					e.printStackTrace();
						try {
							developers.query().where("developerid", developerID).update().setField(columnName, value)
									.execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void setDeveloperString(long developerID, String columnName, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						developers.insert().setPrimaryKey("developerid", developerID).setField(columnName, value)
								.execute();
					} catch (Exception e) {
//					e.printStackTrace();
						try {
							developers.query().where("developerid", developerID).update().setField(columnName, value)
									.execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void setDeveloperLong(long developerID, String columnName, long value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						developers.insert().setPrimaryKey("developerid", developerID).setField(columnName, value)
								.execute();
					} catch (Exception e) {
//					e.printStackTrace();
						try {
							developers.query().where("developerid", developerID).update().setField(columnName, value)
									.execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void addDeveloper(long developerID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						developers.insert().setPrimaryKey("developerid", developerID).execute();
					} catch (Exception e) {

					}
				}
			}

			public static void removeDeveloper(long developerID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					developers.query().where("developerid", developerID).executeDelete();
				}
			}

			public static boolean hasPermission(long userID, String permission) {
				return getDeveloperBoolean(userID, permission);
			}

			public static void setPermission(long userID, String permission, boolean value) {
				setDeveloperBoolean(userID, permission, value);
			}

		}

		public class GlobalDB {

			public static String prefix = null;

			public static String getGlobalValue(String id) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					return global.query().where("id", id).singleString("value").isPresent()
							? global.query().where("id", id).singleString("value").get()
							: null;
				}
			}

//			public static void setGlobalValue(long id, String columnName, String value) {
//				try (DbContextConnection idk = context.startConnection(dataSource)) {
//					try {
//						global.insert().setPrimaryKey("id", id).setField(columnName, value).execute();
//					} catch (Exception e) {
////					e.printStackTrace();
//						try {
//							global.query().where("id", id).update().setField(columnName, value).execute();
//						} catch (Exception e2) {
//							e2.printStackTrace();
//							e.printStackTrace();
//						}
//					}
//				}
//			}

			public static boolean globalValueExists(long id) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					return global.query().where("id", id).singleLong("id").isPresent();
				}
			}

		}

		public class ActionDB {
			// TODO postponed until a later date
		}

		public class InfractionDB {

			public static long createInfraction(long userID, String reason, long expiryDate, long guildID,
					long invokerID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					long time = System.currentTimeMillis();
					infractions.insert().setPrimaryKey("userid", userID).setField("reason", reason)
							.setField("epochdate", time).setField("expirydate", expiryDate).execute();

					return infractions.query().where("epochdate", time).singleLong("epochdate").isPresent()
							? infractions.query().where("epochdate", time).singleLong("epochdate").get().longValue()
							: null;
				}
			}

			/**
			 * do not use unless for bulk testing in the first 10000 ids
			 * 
			 * @param userID
			 * @param reason
			 * @param expiryDate
			 * @param infractionID
			 */
			public static void createInfraction(long userID, String reason, long expiryDate, long guildID,
					long invokerID, long infractionID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					infractions.insert().setPrimaryKey("infractionid", infractionID).setField("reason", reason)
							.setField("epochdate", System.currentTimeMillis()).setField("expirydate", expiryDate)
							.setField("userid", userID).execute();
				}
			}

			public static void deleteInfraction(long infractionID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					infractions.query().where("infractionid", infractionID).executeDelete();
				}
			}

			public static void setInfractionBoolean(long infractionID, String columnName, boolean value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						infractions.insert().setPrimaryKey("infractionid", infractionID).setField(columnName, value)
								.execute();
					} catch (Exception e) {
						try {
							infractions.query().where("infractionid", infractionID).update().setField(columnName, value)
									.execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void setInfractionString(long infractionID, String columnName, String value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						infractions.insert().setPrimaryKey("infractionid", infractionID).setField(columnName, value)
								.execute();
					} catch (Exception e) {
						try {
							infractions.query().where("infractionid", infractionID).update().setField(columnName, value)
									.execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static void setInfractionLong(long infractionID, String columnName, long value) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					try {
						infractions.insert().setPrimaryKey("infractionid", infractionID).setField(columnName, value)
								.execute();
					} catch (Exception e) {
						try {
							infractions.query().where("infractionid", infractionID).update().setField(columnName, value)
									.execute();
						} catch (Exception e2) {
							e2.printStackTrace();
							e.printStackTrace();
						}
					}
				}
			}

			public static HashMap<String, String> getInfractionFromID(long infractionID) {
				if (infractionExists(infractionID)) {
					try (DbContextConnection idk = context.startConnection(dataSource)) {
						HashMap<String, String> map = new HashMap<String, String>();
						Statement st = null;
						ResultSet rs = null;
						String query = "select * from infractions where infractionid = " + infractionID;
						try {
							st = infractions.getConnection().createStatement();
							rs = st.executeQuery(query);
							ResultSetMetaData rsMeta = rs.getMetaData();
							int columnCount = rsMeta.getColumnCount();
							if (rs.next()) {

								for (int i = 1; i <= columnCount; i++) {
									map.put(rsMeta.getColumnLabel(i), rs.getString(i));
								}
							}
							return map.isEmpty() ? null : map;
						} catch (SQLException e) {
							return null;
						} finally { // @formatter:off
						try {if (rs != null)rs.close();} catch (Exception e) {}
						try {if (st != null)st.close();} catch (Exception e) {}
						// @formatter:on
						}
					}
				} else {
					return null;
				}
			}

			public static boolean infractionExists(long infractionID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					return infractions.query().where("infractionID", infractionID).singleLong("infractionID")
							.isPresent();
				}
			}

			public static ArrayList<HashMap<String, String>> getUserInfractions(long userID) {
				try (DbContextConnection idk = context.startConnection(dataSource)) {
					Statement st = null;
					ResultSet rs = null;
					String query = "select * from infractions where userid = " + userID;
					try {
						st = infractions.getConnection().createStatement();
						rs = st.executeQuery(query);
						ResultSetMetaData rsMeta = rs.getMetaData();
						int columnCount = rsMeta.getColumnCount();
						ArrayList<HashMap<String, String>> hashList = new ArrayList<HashMap<String, String>>();
						while (rs.next()) {
							HashMap<String, String> infractionHash = new HashMap<String, String>();
							for (int i = 1; i <= columnCount; i++) {
								infractionHash.put(rsMeta.getColumnLabel(i), rs.getString(i));
							}
							hashList.add(infractionHash);
						}
						return hashList.isEmpty() ? null : hashList;
					} catch (SQLException e) {
						return null;
					} finally { // @formatter:off
					try {if (rs != null)rs.close();} catch (Exception e) {}
					try {if (st != null)st.close();} catch (Exception e) {}
					// @formatter:on
					}
				}
			}

		}

	}

	private static ArrayList<String> eLL = null;
	private static ArrayList<Throwable> eL = null;

	public static void main(String... args) {
		init("chaos", "nopalmo", args[0]);
//	}
//
//	public static void test(Runnable runnable) {

		long userID = 99;
//		long guildID = 99;

		Random random = new Random();
		long minutes = 0;
		long seconds = 20;
		eL = new ArrayList<Throwable>();
		eLL = new ArrayList<String>();
		ArrayList<Long> times = new ArrayList<Long>();
		for (int i = 0; i < 10; i++) {
			times.add(Tests.userLoop((minutes > 0) ? (minutes * 60) : 1 * ((seconds > 0) ? (seconds * 1000) : 1000),
					1000000000, random));
//			runnable.run();
		}
		for (int i = 0; i < 10; i++) {
			times.add(
					Tests.infractionLoop((minutes > 0) ? (minutes * 60) : 1 * ((seconds > 0) ? (seconds * 1000) : 1000),
							1000000000, userID, random));
		}

		if (!eL.isEmpty() && !eLL.isEmpty()) {
			for (int i = 0; i < eL.size(); i++) {
				System.err.println('\n' + eLL.get(i));
				eL.get(i).printStackTrace();
			}
		}

		long total = 0;
		for (long i : times) {
			total += i;
		}
		logger.info("Average of {} tests, with length of {} minutes, {} seconds, is {} : {}", times.size(), minutes,
				seconds, total / times.size(), total);
	}

	private class Tests {

		public static long infractionLoop(long time, long count, long userID, Random random) {

			long userStartTime = System.currentTimeMillis();

			long currentTime = System.currentTimeMillis();
			long targetTime = System.currentTimeMillis() + time;

			long currentCount = 0;
			long targetCount = count;

			for (long i = 1L; true; i++) {

				if (currentTime >= targetTime) {
					logger.info("Breaking: Exceeding target time frame");
					break;
				} else if (currentCount >= targetCount) {
					logger.info("Breaking: Exceeding target count");
					break;
				}

				try {
					InfractionDB.createInfraction(userID, String.valueOf(random.nextInt()), 0, 0, 0, i);
				} catch (Throwable e) {
					eLL.add("onCreate");
					eL.add(e);
				}
				InfractionDB.getInfractionFromID(i);
				InfractionDB.setInfractionLong(i, "expirydate", userStartTime);
				InfractionDB.getInfractionFromID(i);
				try {
					InfractionDB.deleteInfraction(i);
				} catch (Throwable e) {
					eLL.add("onRemove");
					eL.add(e);
				}

				currentTime = System.currentTimeMillis();
				currentCount++;

			}

			logger.info("Time taken: {}", (System.currentTimeMillis() - userStartTime));
			logger.info("Total completed count: {}", currentCount);
//		return System.currentTimeMillis() - userStartTime;
			return currentCount;
		}

		public static long userLoop(long time, long count, Random random) {

			long userStartTime = System.currentTimeMillis();

			long currentTime = System.currentTimeMillis();
			long targetTime = System.currentTimeMillis() + time;

			long currentCount = 0;
			long targetCount = count;

			for (long i = 1L; true; i++) {

				if (currentTime >= targetTime) {
					logger.info("Breaking: Exceeding target time frame");
					break;
				} else if (currentCount >= targetCount) {
					logger.info("Breaking: Exceeding target count");
					break;
				}

				try {
					UserDB.addUser(i);
				} catch (Throwable e) {
					eLL.add("onCreate");
					eL.add(e);
				}

				UserDB.setAdvancedUserBoolean(i, true);
				UserDB.setDirectMessagingBoolean(i, false);
				UserDB.setPremiumLevel(i, 2);
				UserDB.getPremiumLevel(i);
				UserDB.isAdvancedUser(i);
				UserDB.isDirectMessagingEnabled(i);

				try {
					UserDB.removeUser(i);
				} catch (Throwable e) {
					eLL.add("onRemove");
					eL.add(e);
				}

				currentTime = System.currentTimeMillis();
				currentCount++;

			}

			logger.info("Time taken: {}", (System.currentTimeMillis() - userStartTime));
			logger.info("Total completed count: {}", currentCount);
//			return System.currentTimeMillis() - userStartTime;
			return currentCount;
		}

	}
}
