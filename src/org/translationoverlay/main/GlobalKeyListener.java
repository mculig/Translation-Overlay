package org.translationoverlay.main;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyListener implements NativeKeyListener {
	
	ActionManager actionMan;
	
	GlobalKeyListener(ActionManager actionMan){
		this.actionMan = actionMan;
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
			case NativeKeyEvent.VC_F8:
				//Toggle the visible overlay and enable drawing
				actionMan.toggleOverlay();
				break;
			case NativeKeyEvent.VC_F9:
				//Do OCR
				actionMan.toggleOCRLoop();
				break;
			case NativeKeyEvent.VC_F10:
				//Close the window
				actionMan.closeApp();
				break;
			default:
				//Do nothing
				break;
		}
		
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		// Nothing to do on release
		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// Nothing to do on type
		
	}
	
	

}
