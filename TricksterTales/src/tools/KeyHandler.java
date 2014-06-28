package tools;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Main;

public class KeyHandler implements KeyListener {

    private Main main;

    public KeyHandler(Main main) {
	this.main = main;
    }

    public void keyPressed(KeyEvent arg0) {
    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }

    public void stuffs() {
	String something = main.toString();
	something = something.trim();
    }

}
