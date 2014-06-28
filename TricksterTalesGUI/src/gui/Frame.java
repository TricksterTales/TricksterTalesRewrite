package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Frame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private boolean closed = false;
	private JPanel contentPane;
	private Box vertBox;
	private Box horBox;
	private Panel contentPanel;
	private JPanel titlePanel;
	private JLabel titleLabel;
	private JLabel subLabel;

	/**
	 * Create the frame.
	 */
	public Frame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		vertBox = Box.createVerticalBox();
		contentPane.add(vertBox);
		
		horBox = Box.createHorizontalBox();
		horBox.add(Box.createHorizontalGlue());
		
		contentPanel = new Panel();
		contentPanel.setBackground(Color.WHITE);
		
		titlePanel = new JPanel();
		vertBox.add(Box.createVerticalGlue());
		vertBox.add(Box.createVerticalStrut(10));
		vertBox.add(titlePanel);
		vertBox.add(Box.createVerticalStrut(10));
		titlePanel.setBackground(null);
		
		titleLabel = new JLabel("Trickster Tales");
		titleLabel.setForeground(Color.WHITE);
		titlePanel.add(Box.createHorizontalGlue());
		titlePanel.add(titleLabel);
		titlePanel.add(Box.createHorizontalGlue());
		titlePanel.setMaximumSize(new Dimension(1000000000, titlePanel.getBounds().height));
		
		horBox.add(contentPanel);
		horBox.add(Box.createHorizontalGlue());
		vertBox.add(horBox);
		vertBox.add(Box.createVerticalStrut(10));
		subLabel = new JLabel("Made By: Anthony Northrup");
		subLabel.setForeground(Color.WHITE);
		subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		vertBox.add(subLabel);
		vertBox.add(Box.createVerticalStrut(10));
		vertBox.add(Box.createVerticalGlue());
		
		addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {
				getFrame().closed = true;
			}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) { getFrame().closed = false; }
		});
		
		pack();
		setMinimumSize(new Dimension(getBounds().width, getBounds().height));
		setLocationRelativeTo(null);
	}
	
	private Frame getFrame() { return this; }
	public boolean isClosed() { return this.closed; }
	public Panel getPanel() { return this.contentPanel; }
	
	public void update(Graphics g) {
		contentPanel.update(g);
	}

}
