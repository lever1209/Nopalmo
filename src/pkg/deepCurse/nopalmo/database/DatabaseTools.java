package pkg.deepCurse.nopalmo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;
import pkg.deepCurse.simpleLoggingGarbage.core.Log;

public class DatabaseTools {

	private static Connection connection = null;

	public DatabaseTools(String password) throws SQLException {
		connection = createConnection(password);
		Global.updatePrefix();
	}

	public static Connection createConnection(String password) throws SQLException {

		String dbName = Boot.isProd ? "nopalmo" : "chaos";

		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost/" + dbName;
		String username = "nopalmo";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			Log.crash(e);
		}

		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			sqlTranslate("Generate connection", e);
			throw new SQLException(e);
		}
	}

	private static void sqlTranslate(String action, int errorCode) {
		switch (errorCode) {
		case 1062:
			System.err.println("Failed to execute; errorCode=" + errorCode + "; ER_DUP_ENTRY; On action " + action);
			break;
		case 1054:
			System.err.println("Failed to execute; errorCode=" + errorCode + "; ER_BAD_FIELD_ERROR; On action " + action);
			break;
		default:
			System.err.println("Failed to execute; errorCode=" + errorCode + "; Unknown code; On action " + action);
			break;
		}
	}

	@Deprecated
	private static void sqlTranslate(String action, SQLException e) {
		sqlTranslate(action, e.getErrorCode());
	}

	private static void sqlTranslate(PreparedStatement pstmt, SQLException e) {
		sqlTranslate(pstmt.toString(), e.getErrorCode());
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

		// these sub classes will represent tables and the methods therein will be for actions within said table
		
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
						sqlTranslate(query, e.getErrorCode());
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
						prefix = ";";
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
						sqlTranslate(pstmt, e);
						for (int i : new int[] { 1062 }) {
							if (i == e.getErrorCode()) {
								return setPrefix(connection, guildID, prefix);
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

				public static String setPrefix(Connection conn, long guildID, String prefix)
						throws IllegalArgumentException {

					if (prefix.isEmpty()) {
						throw new IllegalArgumentException("Input cannot be empty");
					}

					PreparedStatement pstmt = null;

					String query = "update guilds set prefix = ? where guildid = ?;";
					try {
						pstmt = conn.prepareStatement(query);

						pstmt.setLong(2, guildID);
						pstmt.setString(1, prefix);
						pstmt.addBatch();

						int[] updateCounts = pstmt.executeBatch();
						checkUpdateCounts(pstmt, updateCounts);
						pstmt.close();
						conn.commit();
						return prefix;
					} catch (SQLException e) {
						sqlTranslate(pstmt, e);
						try {
							conn.rollback();
						} catch (Exception e2) {
							e.printStackTrace();
						}
						return null;
					}
				}
			}
		}

		public class Developers {

			public static boolean canPowerOffBot(long userID) {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from developers where userid = " + userID;
				try {
					st = connection.createStatement();
					rs = st.executeQuery(query);
					if (rs.next()) {
						return rs.getBoolean("canpoweroffbot");
					} else {
						// throw new SQLException(null, null, 33); // we need a real catcher here
						System.err
								.println("Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
						return false;
					}

				} catch (SQLException e) {
					sqlTranslate(query, e.getErrorCode());
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
					sqlTranslate(query, e.getErrorCode());
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
