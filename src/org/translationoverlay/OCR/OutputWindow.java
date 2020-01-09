package org.translationoverlay.OCR;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.translationoverlay.config.Config;

public class OutputWindow {
	
	private String instructions = 
			"Click and drag me to reposition overlay output!\n "
			+ "Press F8 to show overlay and click+drag to select area for OCR\n"
			+ "For best results select area where only text will appear if possible!\n"
			+ "Press F9 to start/stop the OCR/Translation loop!\n"
			+ "Press F10 to stop the application!";
	
	private JFrame outputFrame = new JFrame();
	private JTextArea outputTextArea = new JTextArea(instructions);
	
	private int outputWindowWidth;
	private int outputWindowHeight;
	
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
	
	public OutputWindow(Config config){
		
		this.outputWindowWidth = config.outputWindowWidth;
		this.outputWindowHeight = config.outputWindowHeight;
		
		outputFrame.setLayout(null);
		outputFrame.setUndecorated(true);
		outputFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		outputFrame.setBackground(new Color(255,255,255,255));
		outputFrame.setAlwaysOnTop(true);
		outputFrame.setBounds(0, 0,  outputWindowWidth, outputWindowHeight);
		outputFrame.add(outputTextArea);
		
		outputTextArea.setOpaque(true);
		outputTextArea.setFocusable(false);
		outputTextArea.setEditable(false);
		outputTextArea.setLineWrap(true);
		outputTextArea.setFont(new Font("Verdana", 1, 20));
		outputTextArea.setBackground(new Color(255,255,255,255));
		outputTextArea.setBounds(0,0,outputWindowWidth, outputWindowHeight);
		
		//Add listeners for the mouse work in the window
		outputTextArea.addMouseListener(clickListener);
		outputTextArea.addMouseMotionListener(dragListener);
		
		outputFrame.setVisible(true);
	}
	
	//Updates the text in the OCR label
	public void updateLabelText(String text) {
		outputTextArea.setText(text);
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
			outputFrame.setLocation(outputFrame.getX() + x - dragInitialX, outputFrame.getY() + y -dragInitialY);
		}
	}

}
