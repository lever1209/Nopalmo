package pkg.deepCurse.nopalmo.core;

import org.slf4j.Logger;
import org.slf4j.impl.SimpleLoggerFactory;

import pkg.deepCurse.nopalmo.global.Reactions;
import pkg.deepCurse.nopalmo.manager.StatusManager;

public class Loader {

	private static Logger logger = new SimpleLoggerFactory().getLogger(Loader.class.getSimpleName());

	public static String init() {
		StringBuilder sB = new StringBuilder();

		logger.info("Init reaction/emote list");
		sB.append("Init reaction/emote list\n");
		try {
			Reactions.init();
			logger.info("Initialized reaction/emote list. . .");
			sB.append("Initialized reaction/emote list. . .\n");
		} catch (Exception e) {
			logger.info("Failed to initialize reaction/emote list. . .\n" + e + "\n");
			sB.append("Failed to initialize reaction/emote list. . .```properties\n" + e + "```\n");
		}

		logger.info("Init command list");
		sB.append("Init command list\n");
		try {
			Boot.commandManager.init();
			logger.info("Initialized command list. . .");
			sB.append("Initialized command list. . .\n");
		} catch (Exception e) {
			logger.info("Failed to initialize command list. . .\n" + e + "\n");
			sB.append("Failed to initialize command list. . .```properties\n" + e + "```\n");
		}

		logger.info("Init status list");
		sB.append("Init status list\n");
		try {
			StatusManager.init();
			logger.info("Initialized status list. . .");
			sB.append("Initialized status list. . .\n");
		} catch (Exception e) {
			logger.info("Failed to initialize status list. . .\n" + e + "\n");
			sB.append("Failed to initialize status list. . .```properties\n" + e + "```\n");
		}

//		logger.info("Init bontedok settings");
//		sB.append("Init bontedok settings\n");
//		BontebokSettings settings = null;
//		try {
//			settings = new BontebokSettings();
//			logger.info("Initialized bontedok settings. . .");
//			sB.append("Initilaized bontedok settings\n");
//		} catch (Exception e) {
//			logger.info("Failed to initialize bontebok settings. . .\n" + e + "\n");
//			sB.append("Failed to initialize bontebok settings. . .```properties\n" + e + "```\n");
//		}
//		logger.info("Init bontedok functions");
//		sB.append("Init bontedok functions\n");
//		try {
//			Boot.bontebokManager = new BontebokManager(settings);
//			Boot.bontebokManager.init();
//			logger.info("Initialized bontedok functions. . .");
//			sB.append("Initilaized bontedok functions\n");
//		} catch (Exception e) {
//			logger.info("Failed to initialize bontebok functions. . .\n" + e + "\n");
//			sB.append("Failed to initialize bontebok functions. . .```properties\n" + e + "```\n");
//		}
		return sB.toString();
	}
}