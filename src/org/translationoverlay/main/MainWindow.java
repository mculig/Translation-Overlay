package org.translationoverlay.main;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow {
	
	//Action manager manages all actions elements take on each other
	ActionManager actionMan;
	
	//The frame and panel used for the overlay and rectangle drawing
	private JFrame frame = new JFrame();
	private JPanel textBoxPanel = new JPanel();
	
	//The viewport for the OCR loop
	private Rectangle OCRViewport=new Rectangle();
	
	private AtomicBoolean drawing = new AtomicBoolean(false);
	private AtomicBoolean overlayEnabled = new AtomicBoolean(false);

	//Listeners for the mouse movements
	MouseAdapter clickListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				//System.out.println(e.getX() + "," + e.getY());
				beginDraw(e.getX(), e.getY());
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				//System.out.println(e.getX() + "," + e.getY());
				endDraw();
			}
		}
	};
	
	MouseAdapter dragListener = new MouseAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			if(drawing.get()) {
				resizeRect(e.getX(), e.getY());
				//System.out.println("Dragging to: " + e.getX() + "," + e.getY());
				frame.repaint();
			}
		}
	};
	
	MainWindow(){
		
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLayout(null);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(0,0,0,0));
		
		textBoxPanel.setBackground(new Color(0,0,0,0));
		frame.add(textBoxPanel);
			
		frame.setVisible(true);	
		
		//Add listeners for the mouse work in the window
		frame.addMouseListener(clickListener);
		frame.addMouseMotionListener(dragListener);
		
	}
	
	void closeApp() {
		//Close the window
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	void toggleOverlay() {
		if(overlayEnabled.get()){
			
			overlayEnabled.set(false);
			
			frame.setBackground(new Color(0,0,0,0));
			frame.setAlwaysOnTop(false);
			textBoxPanel.setBorder(BorderFactory.createEmptyBorder());
			frame.repaint();
			
			
		}
		else {
			overlayEnabled.set(true);
			
			frame.setBackground(new Color(0,0,0,30));
			frame.setAlwaysOnTop(true);
			textBoxPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
			frame.repaint();
			
		}
		
	}
	
	void toggleOCRLoop() {
		actionMan.toggleOCRLoop();
	}
	
	public Rectangle getOCRViewport() {
		return OCRViewport;
	}
	
	void beginDraw(int x, int y){
		if(!overlayEnabled.get()) return;
		OCRViewport.x=x;
		OCRViewport.y=y;
		drawing.set(true);
	}
	
	void resizeRect(int x, int y) {	
		if(!overlayEnabled.get()) return;
		OCRViewport.width = x - OCRViewport.x;
		OCRViewport.height = y - OCRViewport.y;
		textBoxPanel.setBounds(OCRViewport);
	}
	
	void endDraw() {
		if(!overlayEnabled.get()) return;
		drawing.set(false);
	}
	

}
