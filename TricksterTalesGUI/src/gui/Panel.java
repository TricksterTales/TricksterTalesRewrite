package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class Panel extends JPanel implements MouseMotionListener {
	
	private static final long serialVersionUID = 1L;
	public Dimension size;
	private boolean drewOnce = false;
	private int x = 0, y = 0, r = 5;
	
	public Panel() {
		size = new Dimension(768, 480);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setBackground(null);
		addMouseMotionListener(this);
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		if(drewOnce == false) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			drewOnce = true;
		}
		g.setColor(getForeground());
		g.fillOval(x-r, y-r, 2*r, 2*r);
	}
	public void mouseDragged(MouseEvent arg0) {
		x = arg0.getX();
		y = arg0.getY();
		repaint();
	}
	public void mouseMoved(MouseEvent arg0) {}

}
