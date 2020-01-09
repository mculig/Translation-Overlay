package org.translationoverlay.OCR;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class OCRTextWindow {
	
	private JFrame OCRFrame = new JFrame();
	private JTextArea OCRLabel = new JTextArea("Click and drag me to reposition overlay output! \n "
			+ "Press F8 to show overlay and click+drag to select area for OCR \n"
			+ "For best results select area where only text will appear if possible! \n"
			+ "Press F9 to start/stop the OCR/Translation loop! \n"
			+ "Press F10 to stop the application!");
	
	private int OCRLabelWidth = 600;
	private int OCRLabelHeight = 200;
	
	//Dragging logic
	private AtomicBoolean dragWindow = new AtomicBoolean(false);
	private int dragInitialX = 0;
	private int dragInitialY = 0;
	
	MouseAdapter clickListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				//System.out.println(e.getX() + "," + e.getY());
				beginDrag(e.getX(), e.getY());
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				//System.out.println(e.getX() + "," + e.getY());
				endDrag();
			}
		}
	};
	
	MouseAdapter dragListener = new MouseAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			dragWindow(e.getX(), e.getY());
		}
	};
	
	public OCRTextWindow(){
		
		OCRFrame.setLayout(null);
		OCRFrame.setUndecorated(true);
		OCRFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		OCRFrame.setBackground(new Color(255,255,255,255));
		OCRFrame.setAlwaysOnTop(true);
		OCRFrame.setBounds(0, 0,  OCRLabelWidth, OCRLabelHeight);
		OCRFrame.add(OCRLabel);
		
		OCRLabel.setOpaque(true);
		OCRLabel.setFocusable(false);
		OCRLabel.setEditable(false);
		OCRLabel.setLineWrap(true);
		OCRLabel.setFont(new Font("Verdana", 1, 20));
		OCRLabel.setBackground(new Color(255,255,255,255));
		OCRLabel.setBounds(0,0,OCRLabelWidth, OCRLabelHeight);
		
		//Add listeners for the mouse work in the window
		OCRLabel.addMouseListener(clickListener);
		OCRLabel.addMouseMotionListener(dragListener);
		
		OCRFrame.setVisible(true);
	}
	
	//Updates the text in the OCR label
	public void updateLabelText(String text) {
		OCRLabel.setText(text);
	}
	
	private void beginDrag(int x, int y){
		dragWindow.set(true);
		dragInitialX=x;
		dragInitialY=y;
	}
	
	private void endDrag() {
		dragWindow.set(false);
	}
	
	private void dragWindow(int x, int y) {
		if(dragWindow.get()) {
			OCRFrame.setLocation(OCRFrame.getX() + x - dragInitialX, OCRFrame.getY() + y -dragInitialY);
		}
	}

}
