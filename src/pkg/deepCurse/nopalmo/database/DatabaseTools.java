package pkg.deepCurse.nopalmo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;

public class DatabaseTools {

	private static Connection connection = null;

	public DatabaseTools(String password) throws SQLException, ClassNotFoundException {
		connection = createConnection(password);
		Global.updatePrefix();
	}

	public static Connection createConnection(String password) throws SQLException, ClassNotFoundException {

		String dbName = Boot.isProd ? "nopalmo" : "chaos";

		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost/" + dbName;
		String username = "nopalmo";

		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}

	private static void checkUpdateCounts(PreparedStatement pstmt, int[] updateCounts) {
		checkUpdateCounts(pstmt.toString(), updateCounts);
	}

	@Deprecated
	public static void checkUpdateCounts(String action, int[] updateCounts) {
		for (int i = 0; i < updateCounts.length; i++) {
			if (updateCounts[i] >= 0) {
				System.out.println("Successfully executed; updateCount=" + updateCounts[i] + "; On action " + action);
			} else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
				System.out.println("Successfully executed; updateCount=Statement.SUCCESS_NO_INFO; On action " + action);
			} else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
				System.out.println("Failed to execute; updateCount=Statement.EXECUTE_FAILED; On action " + action);
			}
		}
	}

	public class Tools {

		// these sub classes will represent tables and the methods therein will be for
		// actions within said table

		public class Users {

			public static int getPremiumLevel(long userID) {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from users where userid = " + userID;
				try {
					st = connection.createStatement();
					rs = st.executeQuery(query);
					if (rs.next()) {
						return rs.getInt("premiumlevel");
					} else {
						// throw new SQLException(null, null, 33); // we need a real catcher here
						System.err
								.println("Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
						return 0;
					}
				} catch (SQLException e) {
					SQLCode.getMessage(query, e.getErrorCode());
					return 0;
				} finally { // @formatter:off
					try {if (rs != null)rs.close();} catch (Exception e) {}
					try {if (st != null)st.close();} catch (Exception e) {}
					// @formatter:on
				}
			}

			public static boolean isAdvancedUser(long userID) {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from users where userid = " + userID;
				try {
					st = connection.createStatement();
					rs = st.executeQuery(query);
					if (rs.next()) {
						return rs.getBoolean("advanceduser");
					} else {
						// throw new SQLException(null, null, 33); // we need a real catcher here
						System.err
								.println("Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
						return false;
					}
				} catch (SQLException e) {
					SQLCode.getMessage(query, e.getErrorCode());
					return false;
				} finally { // @formatter:off
					try {if (rs != null)rs.close();} catch (Exception e) {}
					try {if (st != null)st.close();} catch (Exception e) {}
					// @formatter:on
				}
			}

			public static String dump(long userID) {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from users where userid = " + userID;
				try {
					st = connection.createStatement();
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
					return sB.toString();

				} catch (SQLException e) {
					SQLCode.getMessage(query, e.getErrorCode());
					return null;
				} finally { // @formatter:off
					try {if (rs != null)rs.close();} catch (Exception e) {}
					try {if (st != null)st.close();} catch (Exception e) {}
					// @formatter:on
				}
			}

			public static void addUser(long idLong) {
				
			}

		}

		public class Guild {

			public class Prefix {

				@Nullable
				public static String getPrefix(@Nonnull long guildID) {
					Statement st = null;
					ResultSet rs = null;
					String query = "select * from guilds where guildid = " + guildID;
					try {
						st = connection.createStatement();
						rs = st.executeQuery(query);
						if (rs.next()) {
							return rs.getString("prefix");
						} else {
							// throw new SQLException(null, null, 33); // we need a real catcher here
							System.err.println(
									"Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
							return createPrefix(guildID, Global.prefix);
						}
					} catch (SQLException e) {
						SQLCode.getMessage(query, e.getErrorCode());
						return null;
					} finally { // @formatter:off
						try {if (rs != null)rs.close();} catch (Exception e) {}
						try {if (st != null)st.close();} catch (Exception e) {}
						// @formatter:on
					}
				}

				public static String createPrefix(@Nonnull long guildID, @Nullable String prefix)
						throws IllegalArgumentException {

					if (prefix == null || prefix.isEmpty()) {
						// throw new IllegalArgumentException("Input cannot be empty");
						prefix = Global.prefix;
					}

					PreparedStatement pstmt = null;

					String query = "insert into guilds(guildid, prefix) values(?, ?);";
					try {
						pstmt = connection.prepareStatement(query);

						pstmt.setLong(1, guildID);
						pstmt.setString(2, prefix);
						pstmt.addBatch();

						int[] updateCounts = pstmt.executeBatch();
						checkUpdateCounts(pstmt, updateCounts);
						pstmt.close();
						// connection.commit();
						return prefix;
					} catch (SQLException e) {
						SQLCode.sqlTranslate(pstmt, e);
						for (int i : new int[] { 1062 }) {
							if (i == e.getErrorCode()) {
								return setPrefix(guildID, prefix);
							}
						}
						try {
							connection.rollback();
						} catch (Exception e2) {
							e.printStackTrace();
						}
						return null;
					}
				}

				public static String setPrefix(long guildID, String prefix) throws IllegalArgumentException {

					if (prefix.isEmpty()) {
						throw new IllegalArgumentException("Input cannot be empty");
					}

					PreparedStatement pstmt = null;

					String query = "update guilds set prefix = ? where guildid = ?;";
					try {
						pstmt = connection.prepareStatement(query);

						pstmt.setLong(2, guildID);
						pstmt.setString(1, prefix);
						pstmt.addBatch();

						int[] updateCounts = pstmt.executeBatch();
						checkUpdateCounts(pstmt, updateCounts);
						pstmt.close();
						// conn.commit();
						return prefix;
					} catch (SQLException e) {
						SQLCode.sqlTranslate(pstmt, e);
						try {
							connection.rollback();
						} catch (Exception e2) {
							e.printStackTrace();
						}
						return null;
					}
				}
			}
		}

		public class Developers {

			public static boolean getDeveloperBoolean(long userID, String key) {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from developers where userid = " + userID;
				try {
					st = connection.createStatement();
					rs = st.executeQuery(query);
					if (rs.next()) {
						return rs.getBoolean(key);
					} else {
						// throw new SQLException(null, null, 33); // we need a real catcher here
						System.err
								.println("Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
						return false;
					}

				} catch (SQLException e) {
					SQLCode.getMessage(query, e.getErrorCode());
					// System.out.println("eeeeee");
					return false;
				} finally {
					try {
						if (rs != null)
							rs.close();
					} catch (Exception e) {
					}

					try {
						if (st != null)
							st.close();
					} catch (Exception e) {
					}

					// try { if (conn != null) conn.close(); } catch (Exception e) {};
				}
				// return null;
			}

			public static String getDeveloperString(long userID, String key) {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from developers where userid = " + userID;
				try {
					st = connection.createStatement();
					rs = st.executeQuery(query);
					if (rs.next()) {
						return rs.getString(key);
					} else {
						// throw new SQLException(null, null, 33); // we need a real catcher here
						System.err
								.println("Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
						return null;
					}

				} catch (SQLException e) {
					SQLCode.getMessage(query, e.getErrorCode());
					// System.out.println("eeeeee");
					return null;
				} finally {
					try {
						if (rs != null)
							rs.close();
					} catch (Exception e) {
					}

					try {
						if (st != null)
							st.close();
					} catch (Exception e) {
					}

					// try { if (conn != null) conn.close(); } catch (Exception e) {};
				}
				// return null;
			}

			public static boolean canPowerOffBot(long userID) {
				return getDeveloperBoolean(userID, "canPowerOffBot");
			}

			public static boolean canUseInformationCommands(long userID) {
				return getDeveloperBoolean(userID, "canuseinformationcommands");
			}

			public static boolean hasPermission(long userID, String permission) {
				return getDeveloperBoolean(userID, permission);
			}

		}

		public class Global {

			public static String prefix = null;

			public static void updatePrefix() throws SQLException {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from global where id = 'prefix'";
				String msg = "Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query;
				try {
					st = connection.createStatement();
					rs = st.executeQuery(query);
					if (rs.next()) {
						prefix = rs.getString("value");
					} else {
						// throw new SQLException(null, null, 33); // we need a real catcher here

						System.err.println(msg);
						throw new NullPointerException(msg);
					}

				} catch (SQLException e) {
					SQLCode.getMessage(query, e.getErrorCode());
					// System.out.println("eeeeee");
					throw new SQLException(e);
				} finally {
					try {
						if (rs != null)
							rs.close();
					} catch (Exception e) {
					}

					try {
						if (st != null)
							st.close();
					} catch (Exception e) {
					}

					// try { if (conn != null) conn.close(); } catch (Exception e) {};
				}
				// return null;
			}

			public static boolean isShuffleStatusEnabled() {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from global where id = 'isshufflestatusenabled'";
				try {
					st = connection.createStatement();
					rs = st.executeQuery(query);
					if (rs.next()) {
						return rs.getString("value").contentEquals("true");
					} else {
						System.err
								.println("Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
						return false;
					}

				} catch (SQLException e) {
					SQLCode.getMessage(query, e.getErrorCode());
					// System.out.println("eeeeee");
					return false;
				} finally {
					try {
						if (rs != null)
							rs.close();
					} catch (Exception e) {
					}

					try {
						if (st != null)
							st.close();
					} catch (Exception e) {
					}

					// try { if (conn != null) conn.close(); } catch (Exception e) {};
				}
				// return null;
			}

			public static int getDynamicWait() {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from global where id = 'dynamicwait'";
				try {
					st = connection.createStatement();
					rs = st.executeQuery(query);
					if (rs.next()) {
						return rs.getInt("value");
					} else {
						System.err
								.println("Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
						return 45000;
					}

				} catch (SQLException e) {
					SQLCode.getMessage(query, e.getErrorCode());
					// System.out.println("eeeeee");
					return 45000;
				} finally {
					try {
						if (rs != null)
							rs.close();
					} catch (Exception e) {
					}

					try {
						if (st != null)
							st.close();
					} catch (Exception e) {
					}

					// try { if (conn != null) conn.close(); } catch (Exception e) {};
				}
				// return null;
			}

			public static int getEmbedColor() {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from global where id = 'embedcolor'";
				try {
					st = connection.createStatement();
					rs = st.executeQuery(query);
					if (rs.next()) {
						return rs.getInt("value");
					} else {
						System.err
								.println("Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
						return 0;
					}

				} catch (SQLException e) {
					SQLCode.getMessage(query, e.getErrorCode());
					// System.out.println("eeeeee");
					return 0;
				} finally {
					try {
						if (rs != null)
							rs.close();
					} catch (Exception e) {
					}

					try {
						if (st != null)
							st.close();
					} catch (Exception e) {
					}

					// try { if (conn != null) conn.close(); } catch (Exception e) {};
				}
				// return null;
			}

		}

//		public class Reaction { // started off as a good idea but it sucks
//			public static String getReaction(long reactionID) {
//			Statement st = null;
//			ResultSet rs = null;
//			String query = "select * from reactions where id = '"+reactionID+"'";
//			try {
//				st = connection.createStatement();
//				rs = st.executeQuery(query);
//				if (rs.next()) {
//					return rs.getString("val");
//				} else {
//					// throw new SQLException(null, null, 33); // we need a real catcher here
//					System.err
//							.println("Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
//					return null;
//				}
//
//			} catch (SQLException e) {
//				sqlTranslate(query, e.getErrorCode());
//				// System.out.println("eeeeee");
//				return null;
//			} finally {
//				try {
//					if (rs != null)
//						rs.close();
//				} catch (Exception e) {
//				}
//
//				try {
//					if (st != null)
//						st.close();
//				} catch (Exception e) {
//				}
//
//				// try { if (conn != null) conn.close(); } catch (Exception e) {};
//			}
//			// return null;
//		}
//	}

	}

}
