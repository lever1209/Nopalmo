package pkg.deepCurse.nopalmo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import pkg.deepCurse.simpleLoggingGarbage.core.Log;

public class DatabaseTools {

	private static Connection connection;

	public DatabaseTools(String password) {
		connection = createConnection(password);
	}

	public static Connection createConnection(String password) {
		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost/nopalmo";
		String username = "u1d";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			Log.crash(e);
		}

		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			sqlTranslate("Generate connection", e);
		}
		return null;
	}

	private static void sqlTranslate(String action, int errorCode) {
		switch (errorCode) {
		case 1062:
			System.err.println("Failed to execute; errorCode=1062; Duplicate ID; On action " + action);
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
							return null;
						}

					} catch (SQLException e) {
						sqlTranslate(query, e.getErrorCode());
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

				public static void createPrefix(@Nonnull long guildID, @Nullable String prefix)
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
						connection.commit();
					} catch (SQLException e) {
						sqlTranslate(pstmt, e);

						for (int i : new int[] { 1062 }) {
							if (i == e.getErrorCode()) {
								setPrefix(connection, guildID, prefix);
								break;
							}
						}

						try {
							connection.rollback();
						} catch (Exception e2) {
							e.printStackTrace();
						}

					}
				}

				public static void setPrefix(Connection conn, long guildID, String prefix)
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
					} catch (SQLException e) {
						sqlTranslate(pstmt, e);
						try {
							conn.rollback();
						} catch (Exception e2) {
							e.printStackTrace();
						}

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

			public static String getPrefix() {
				Statement st = null;
				ResultSet rs = null;
				String query = "select * from global where id = 'prefix'";
				try {
					st = connection.createStatement();
					rs = st.executeQuery(query);
					if (rs.next()) {
						return rs.getString("val");
					} else {
						// throw new SQLException(null, null, 33); // we need a real catcher here
						System.err
								.println("Failed to execute; errorCode=NO_ROW_FOUND; No row found; On action " + query);
						return null;
					}

				} catch (SQLException e) {
					sqlTranslate(query, e.getErrorCode());
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
