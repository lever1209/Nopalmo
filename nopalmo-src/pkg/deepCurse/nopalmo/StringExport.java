package pkg.deepCurse.nopalmo;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class StringExport {
	private static final String BUNDLE_NAME = StringExport.class
			.getPackageName() + ".Strings"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private StringExport() {
	}
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
