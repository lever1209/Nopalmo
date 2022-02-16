package pkg.deepCurse.kyt.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import pkg.deepCurse.kyt.ClassManager;
import pkg.deepCurse.kyt.ClassManager.InternalReloadable;
import qj.util.ReflectUtil;

public class InitLoader {

	private static long maxMemUsed;

	public static void main(String[] args) throws ClassNotFoundException,
			IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		// File file = new File(System.getProperty("user.dir") +
		// "/external-src/");
		// ClassManager<String, InternalReloadable<String, ?>> classManager =
		// new ClassManager<>();
		//
		// classManager.addFile("boot", "pkg.deepCurse.nopalmo.core.Boot",
		// file);
		//
		// ReflectUtil.invokeStatic("init", classManager.getClass("boot"));
		generateList();
	}

	/*
	 * used to generate code for use inside the loader, a temporary solution
	 * until automation is achieved
	 */
	public static void generateList() {
		for (File i : gatherRecursiveFileList(
				new File(System.getProperty("user.dir") + "/external-src/"))) {

			String out = i.getPath()
					.replace(System.getProperty("user.dir") + "/external-src/",
							"")
					.replace("/", ".");
			byte pos = 0;
			StringBuilder newOut = new StringBuilder();
			for (int j = out.length()-1; j > 0; j--) {
				String cless = "ssalc.";
				out.charAt(j);
				System.out.println(pos+String.valueOf(out.charAt(j)));
				cless.charAt(pos);
				if (out.charAt(j) == cless.charAt(pos) && pos <= 6) {

				} else {
					newOut.append(out.charAt(j));
				}
				pos++;
			}

			System.out.println(newOut.toString());

		}
	}

	public static File[] gatherRecursiveFileList(File dir) {
		if (!dir.isDirectory()) {
			return new File[]{dir};
		}
		ArrayList<File> fileList = new ArrayList<>();
		if (dir.canRead() && !Files.isSymbolicLink(dir.toPath())) {
			for (File i : dir.listFiles()) {
				if (i.isDirectory()) {
					for (File j : gatherRecursiveFileList(i)) {
						fileList.add(j);
					}
				} else
					fileList.add(i);
			}
		}
		return fileList.toArray(new File[]{});
	}

}
