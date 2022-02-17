package pkg.deepCurse.kyt.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;

import pkg.deepCurse.kyt.ClassManager;
import pkg.deepCurse.kyt.ClassManager.InternalReloadable;

public class InitLoader {

	public static void main(String[] args) throws ClassNotFoundException,
			IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		File file = new File(System.getProperty("user.dir") + "/bin/");
		ClassManager<String, InternalReloadable<String, ?>> classManager = new ClassManager<>();

//		addClasses(classManager, file);

//		generateList();
		
		
		
	}

	private static void addClasses(
			ClassManager<String, InternalReloadable<String, ?>> classManager,
			File file) throws ClassNotFoundException, IOException {
		classManager.addFile("Socks",
				"pkg.deepCurse.nopalmo.server.socket.Socks", file);
		classManager.addFile("CommandInterface$GuildCommandInterface",
				"pkg.deepCurse.nopalmo.command.CommandInterface$GuildCommandInterface",
				file);
		classManager.addFile("Info",
				"pkg.deepCurse.nopalmo.command.commands.info.Info", file);
		classManager.addFile("Reload",
				"pkg.deepCurse.nopalmo.command.commands.info.Reload", file);
		classManager.addFile("Git",
				"pkg.deepCurse.nopalmo.command.commands.info.Git", file);
		classManager.addFile("Help",
				"pkg.deepCurse.nopalmo.command.commands.info.Help", file);
		classManager.addFile("Ping",
				"pkg.deepCurse.nopalmo.command.commands.info.Ping", file);
		classManager.addFile("PrivateCommand",
				"pkg.deepCurse.nopalmo.command.commands.testing.PrivateCommand",
				file);
		classManager.addFile("GuildCommand",
				"pkg.deepCurse.nopalmo.command.commands.testing.GuildCommand",
				file);
		classManager.addFile("LiveUpdateTestCommand",
				"pkg.deepCurse.nopalmo.command.commands.testing.LiveUpdateTestCommand",
				file);
		classManager.addFile("Purge",
				"pkg.deepCurse.nopalmo.command.commands.testing.Purge", file);
		classManager.addFile("Stupid",
				"pkg.deepCurse.nopalmo.command.commands.fun.Stupid", file);
		classManager.addFile("Test",
				"pkg.deepCurse.nopalmo.command.commands.general.Test", file);
		classManager.addFile("Prefix",
				"pkg.deepCurse.nopalmo.command.commands.general.Prefix", file);
		classManager.addFile("CommandInterface$PrivateCommandInterface",
				"pkg.deepCurse.nopalmo.command.CommandInterface$PrivateCommandInterface",
				file);
		classManager.addFile("CommandInterface$DualCommandInterface",
				"pkg.deepCurse.nopalmo.command.CommandInterface$DualCommandInterface",
				file);
		classManager.addFile("CommandInterface",
				"pkg.deepCurse.nopalmo.command.CommandInterface", file);
		classManager.addFile("CommandInterface$HelpPage",
				"pkg.deepCurse.nopalmo.command.CommandInterface$HelpPage",
				file);
		classManager.addFile("MessageReceivedListener",
				"pkg.deepCurse.nopalmo.listener.MessageReceivedListener", file);
		classManager.addFile("Reactions",
				"pkg.deepCurse.nopalmo.global.Reactions", file);
		classManager.addFile("Tools", "pkg.deepCurse.nopalmo.global.Tools",
				file);
		classManager.addFile("UptimePing",
				"pkg.deepCurse.nopalmo.utils.UptimePing", file);
		classManager.addFile("LogHelper",
				"pkg.deepCurse.nopalmo.utils.LogHelper", file);
		classManager.addFile("Locks", "pkg.deepCurse.nopalmo.utils.Locks",
				file);
		classManager.addFile("Argument",
				"pkg.deepCurse.nopalmo.manager.Argument", file);
		classManager.addFile("StatusManager",
				"pkg.deepCurse.nopalmo.manager.StatusManager", file);
		classManager.addFile("Argument$RunnableArg",
				"pkg.deepCurse.nopalmo.manager.Argument$RunnableArg", file);
		classManager.addFile("CommandLoop",
				"pkg.deepCurse.nopalmo.manager.CommandLoop", file);
		classManager.addFile("CommandManager",
				"pkg.deepCurse.nopalmo.manager.CommandManager", file);
		classManager.addFile("CommandBlob",
				"pkg.deepCurse.nopalmo.manager.CommandBlob", file);
		classManager.addFile("NopalmoDBTools",
				"pkg.deepCurse.nopalmo.core.database.NopalmoDBTools", file);
		classManager.addFile("NopalmoDBTools$Tests",
				"pkg.deepCurse.nopalmo.core.database.NopalmoDBTools$Tests",
				file);
		classManager.addFile("NopalmoDBTools$Tools$GlobalDB",
				"pkg.deepCurse.nopalmo.core.database.NopalmoDBTools$Tools$GlobalDB",
				file);
		classManager.addFile("NopalmoDBTools$Tools$InfractionDB",
				"pkg.deepCurse.nopalmo.core.database.NopalmoDBTools$Tools$InfractionDB",
				file);
		classManager.addFile("NopalmoDBTools$Tools$DeveloperDB",
				"pkg.deepCurse.nopalmo.core.database.NopalmoDBTools$Tools$DeveloperDB",
				file);
		classManager.addFile("NopalmoDBTools$Tools",
				"pkg.deepCurse.nopalmo.core.database.NopalmoDBTools$Tools",
				file);
		classManager.addFile("NopalmoDBTools$Tools$ActionDB",
				"pkg.deepCurse.nopalmo.core.database.NopalmoDBTools$Tools$ActionDB",
				file);
		classManager.addFile("NopalmoDBTools$Tools$UserDB",
				"pkg.deepCurse.nopalmo.core.database.NopalmoDBTools$Tools$UserDB",
				file);
		classManager.addFile("NopalmoDBTools$Tools$GuildDB",
				"pkg.deepCurse.nopalmo.core.database.NopalmoDBTools$Tools$GuildDB",
				file);
		classManager.addFile("Loader", "pkg.deepCurse.nopalmo.core.Loader",
				file);
		classManager.addFile("Boot", "pkg.deepCurse.nopalmo.core.Boot", file);
		classManager.addFile("Boot$1", "pkg.deepCurse.nopalmo.core.Boot$1",
				file);
		classManager.addFile("ClassManager", "pkg.deepCurse.kyt.ClassManager",
				file);
		classManager.addFile("InitLoader",
				"pkg.deepCurse.kyt.loader.InitLoader", file);
		classManager.addFile("ClassManager$InternalReloadable",
				"pkg.deepCurse.kyt.ClassManager$InternalReloadable", file);
	}

	/*
	 * used to generate code for use inside the loader, a temporary solution
	 * until automation is achieved
	 */
	public static File[] generateList() {
		File file = new File(System.getProperty("user.dir") + "/bin/");
		ArrayList<File> files = new ArrayList<File>();
		for (File i : gatherRecursiveFileList(file)) {
			String out = i.getPath().replace("/home/u1d/git/nopalmo/bin/", "")
					.replace("/", ".");
			if (out.endsWith(".class")) {
				files.add(i);
				StringBuilder newOut = new StringBuilder();
				byte pos = 0;
				boolean pFound = false;
				StringBuilder className = new StringBuilder();
				for (int j = out.length() - 1; j >= 0; j--) {
					String cless = "ssalc.";
					if (pos >= 6 || out.charAt(j) != cless.charAt(pos)) {
						newOut.insert(0, out.charAt(j));
						if (out.charAt(j) == '.') {
							pFound = true;
						}
						if (!pFound) {
							className.insert(0, out.charAt(j));
						}
					}
					pos++;
				}
				String e = "classManager.addFile(\"" + className + "\", \""
						+ newOut + "\", file);";
				System.out.println(e);
			}
		}
		return files.toArray(new File[]{});
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
