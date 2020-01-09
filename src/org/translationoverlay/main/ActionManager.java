package org.translationoverlay.main;

import org.translationoverlay.OCR.OCRController;

public class ActionManager {
	
	private MainWindow mainWindow;
	private OCRController tController;
	
	public ActionManager(MainWindow mainWindow, OCRController tController) {
		this.mainWindow=mainWindow;
		this.tController=tController;
	}
	
	public void closeApp() {
		//End the timer
		tController.endOCRLoop();
		//Close the main window
		mainWindow.closeApp();
	}
	
	public void toggleOCRLoop() {
		tController.toggleOCRLoop();
	}
	
	public void toggleOverlay() {
		mainWindow.toggleOverlay();
	}

}
