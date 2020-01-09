package org.translationoverlay.main;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.translationoverlay.OCR.OCRController;
import org.translationoverlay.OCR.OCRTextWindow;
import org.translationoverlay.config.Config;
import org.translationoverlay.config.ConfigLoader;
import org.translationoverlay.translation.CrowdTranslator;

public class Main {

	public static void main(String args[]) {
		
		Config config;
		
		ConfigLoader configLoader = new ConfigLoader();

		config = configLoader.getConfig();
		
		MainWindow mainWindow = new MainWindow();

		OCRTextWindow OCRWindow = new OCRTextWindow();

		CrowdTranslator trans = new CrowdTranslator(config);

		OCRController tController = new OCRController(mainWindow, OCRWindow, trans, config);

		ActionManager actionMan = new ActionManager(mainWindow, tController);

		GlobalKeyListener keyListener = new GlobalKeyListener(actionMan);

		// Create the global key listener for the shortcut keys
		try {
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(keyListener);
		} catch (NativeHookException e) {
			e.printStackTrace();
			System.out.println("FATAL: Failed to register hooks for global keyboard shortcuts.");
			System.exit(1); // If we fail to register our hooks the app needs to close
		}
	}

}
