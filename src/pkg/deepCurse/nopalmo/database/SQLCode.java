package pkg.deepCurse.nopalmo.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLCode {
	
	
	
	public static String getMessage(String action, int errorCode) {
		switch (errorCode) {
		case 1062:
			System.err.println("Failed to execute; errorCode=" + errorCode + "; ER_DUP_ENTRY; On action " + action);
			return "";
		case 1054:
			System.err.println("Failed to execute; errorCode=" + errorCode + "; ER_BAD_FIELD_ERROR; On action " + action);
			return "";
		default:
			System.err.println("Failed to execute; errorCode=" + errorCode + "; Unknown code; On action " + action);
			return "";
		}
	}
	
	@Deprecated
	public static void sqlTranslate(String action, SQLException e) {
		SQLCode.getMessage(action, e.getErrorCode());
	}

	public static void sqlTranslate(PreparedStatement pstmt, SQLException e) {
		SQLCode.getMessage(pstmt.toString(), e.getErrorCode());
	}
	
}
