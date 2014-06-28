package main;

import gui.Frame;
import gui.Panel;

public class Main {
	
	public static Frame frame;
	public static Panel panel;
	
	public static void main(String[] args) {
		frame = new Frame();
		panel = frame.getPanel();
		frame.setVisible(true);
		while(!frame.isClosed()) {}
		frame.dispose();
		System.out.println("Done");
	}

}
