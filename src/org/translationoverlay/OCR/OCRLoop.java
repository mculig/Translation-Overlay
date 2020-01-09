package org.translationoverlay.OCR;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

import org.translationoverlay.translation.CrowdTranslator;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCRLoop extends TimerTask{

	private CrowdTranslator trans;
	private Tesseract tesseract;
	private String language = "eng";
	
	OCRTextWindow textWindow;
	Rectangle OCRViewport;
	
	public OCRLoop(Rectangle OCRViewport, OCRTextWindow textWindow, CrowdTranslator trans){
		this.textWindow = textWindow;
		this.OCRViewport = OCRViewport;
		this.trans=trans;
		tesseract = new Tesseract();
		tesseract.setDatapath("./ocr_training_data");
		tesseract.setLanguage(language);
	}
	
	@Override
	public void run() {	
			doOCR();
	}

	//Does the actual OCR operation and puts the generated text in the label
	public void doOCR() {
		try {
			Robot robot = new Robot();
			BufferedImage textCap = robot.createScreenCapture(OCRViewport);
			String text = tesseract.doOCR(textCap);
			//Translator test
			text = trans.translate(text);
			textWindow.updateLabelText(text);
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
	}
	
}
