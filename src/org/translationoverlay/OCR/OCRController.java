package org.translationoverlay.OCR;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;

import org.translationoverlay.config.Config;
import org.translationoverlay.main.MainWindow;
import org.translationoverlay.translation.CrowdTranslator;

public class OCRController {
		
	private AtomicBoolean OCRLoopRunning = new AtomicBoolean(false);
	private CrowdTranslator trans;
	private Timer OCRTimer = new Timer();
	private long OCRLoopDelay ;
	private long OCRLoopRefresh;
	private OCRLoop ocrLoop;
	
	//Reference to the main window used to get the viewport for OCR
	private MainWindow mainWindow=null;
	//Reference to the OCR Text Window where the results are displayed
	private OCRTextWindow textWindow;

	public OCRController(MainWindow mainWindow, OCRTextWindow textWindow, CrowdTranslator trans, Config config) {
		this.mainWindow = mainWindow;
		this.textWindow = textWindow;
		this.trans = trans;
		this.OCRLoopDelay=config.OCRLoopDelay;
		this.OCRLoopRefresh=config.OCRLoopRefresh;
	}
	
	public void toggleOCRLoop() {
			if(!OCRLoopRunning.get()) {
				if(mainWindow.getOCRViewport().width<=0 || mainWindow.getOCRViewport().height<=0){
					System.out.println("Invalid OCR Viewport (Rectangle) size!");
				}
				else {
					beginOCRLoop();
					OCRLoopRunning.set(true);
				}
				
			}
			else {
				endOCRLoop();
				OCRLoopRunning.set(false);
			}
	}
	
	private void beginOCRLoop() {
			OCRTimer = new Timer();
			ocrLoop = new OCRLoop(mainWindow.getOCRViewport(), textWindow, trans);
			OCRTimer.scheduleAtFixedRate(ocrLoop, OCRLoopDelay, OCRLoopRefresh);
	}
	
	public void endOCRLoop() {
		if(OCRLoopRunning.get()) {
			OCRTimer.cancel();
			OCRTimer.purge();
			OCRLoopRunning.set(false);
		}	
	}
	
}
