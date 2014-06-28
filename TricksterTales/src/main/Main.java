package main;

import game.GameController;
import game.art.Renderer;
import game.art.Texture;
import game.level.LevelLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import tools.KeyHandler;
import tools.SmartScroller;
import utils.ConsoleUtils;
import utils.MathUtils;
import utils.StringUtils;

public class Main implements Runnable {

    public static final float ASPECT_RATIO = 768 / 480f;
    public static final int WIDTH = 768, HEIGHT = 480;
    public static final float SCALE = 2f;
    public static final int VIRTUAL_WIDTH = (int) Math.floor(WIDTH / SCALE),
	    VIRTUAL_HEIGHT = (int) Math.floor(HEIGHT / SCALE);

    private static boolean updateLocal = true;
    public static String LOCAL = "";

    public static JFrame frame, drawFrame;
    public static JPanel contentPane, drawPane;
    public static Graphics drawGraphics;
    public static BufferedImage drawBuffer;
    public static JTextArea console;
    public static JScrollPane consolePane;
    public static JLabel tickLabel;
    public static ComponentAdapter cadapter;
    public static Thread mainThread;
    public static Main main;
    public static boolean gameDone = false;

    public GameController game;
    public Texture image;

    private long prevTime, curTime;
    private long deltaTime;

    private long timer = 0, fpsTimer = 0;
    private long frames = 0, ticks = 0;

    public static void main(String[] args) {
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	    e.printStackTrace();
	}

	parseTags(args);

	main = new Main();

	frame = new JFrame();
	frame.setLayout(new BorderLayout());
	frame.setMinimumSize(new Dimension(550, (int) (600 / ASPECT_RATIO)));
	frame.setTitle("Trickster Tales Rewrite");

	contentPane = new JPanel();
	contentPane.setLayout(new BorderLayout());
	cadapter = new ComponentAdapter() {
	    public void componentResized(ComponentEvent arg0) {
		contentPane.removeComponentListener(cadapter);

		super.componentResized(arg0);
		Dimension dim = contentPane.getSize();
		float w = dim.width, h = dim.height;
		if (w / h > ASPECT_RATIO) {
		    // Too wide
		    float nw = h * ASPECT_RATIO;
		    contentPane.setBorder(BorderFactory.createMatteBorder(0,
			    (int) ((w - nw) / 2), 0, (int) ((w - nw) / 2),
			    Color.BLACK));
		} else {
		    // Too tall
		    float nh = w / ASPECT_RATIO;
		    contentPane.setBorder(BorderFactory.createMatteBorder(
			    (int) ((h - nh) / 2), 0, (int) ((h - nh) / 2), 0,
			    Color.BLACK));
		}

		contentPane.addComponentListener(cadapter);
	    }
	};
	contentPane.addComponentListener(cadapter);

	console = new JTextArea();
	console.setEditable(false);
	console.setWrapStyleWord(true);
	console.setLineWrap(true);
	console.setAutoscrolls(false);
	console.setFont(new Font("Courier New", Font.PLAIN, 12));
	console.setHighlighter(null);

	consolePane = new JScrollPane(console,
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	consolePane.getVerticalScrollBar().addAdjustmentListener(
		new SmartScroller(consolePane));
	consolePane.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createEmptyBorder(5, 5, 5, 5),
		BorderFactory.createCompoundBorder(
			BorderFactory.createRaisedBevelBorder(),
			BorderFactory.createLoweredBevelBorder())));
	contentPane.add(consolePane, BorderLayout.CENTER);

	tickLabel = new JLabel("TICKS 0 FRAMES 0");
	tickLabel.setHorizontalAlignment(SwingConstants.CENTER);
	tickLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
	contentPane.add(tickLabel, BorderLayout.SOUTH);

	frame.addWindowListener(new WindowListener() {
	    public void windowActivated(WindowEvent arg0) {
	    }

	    public void windowClosed(WindowEvent arg0) {
	    }

	    public void windowClosing(WindowEvent arg0) {
		Main.gameDone = true;
	    }

	    public void windowDeactivated(WindowEvent arg0) {
	    }

	    public void windowDeiconified(WindowEvent arg0) {
	    }

	    public void windowIconified(WindowEvent arg0) {
	    }

	    public void windowOpened(WindowEvent arg0) {
	    }
	});

	frame.setLocation(0, 0);
	frame.setContentPane(contentPane);
	frame.setResizable(false);
	frame.pack();
	frame.setResizable(true);
	frame.setVisible(true);

	// The drawing window...
	drawPane = new JPanel() {
	    private static final long serialVersionUID = 1L;

	    public void paint(Graphics g) {
		main.drawStuff();
		if (drawBuffer != null)
		    g.drawImage(drawBuffer, 0, 0, Main.WIDTH, Main.HEIGHT, null);
	    }
	};
	drawPane.setPreferredSize(new Dimension(WIDTH - 10, HEIGHT - 10));
	drawPane.setBackground(Color.BLACK);

	drawFrame = new JFrame();
	drawFrame.setSize(1, 1);
	drawFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	drawFrame.setBackground(Color.BLACK);
	drawFrame.setLayout(new BorderLayout());
	drawFrame.add(drawPane, BorderLayout.CENTER);
	drawFrame.setResizable(false);
	drawFrame.pack();
	drawFrame.setLocationRelativeTo(null);
	drawFrame.addWindowListener(new WindowListener() {
	    public void windowActivated(WindowEvent arg0) {
		ConsoleUtils.println("Drawing window activated");
	    }

	    public void windowClosed(WindowEvent arg0) {
		ConsoleUtils.println("Drawing window closed");
	    }

	    public void windowClosing(WindowEvent arg0) {
		ConsoleUtils.println("Drawing window closing");
	    }

	    public void windowDeactivated(WindowEvent arg0) {
		ConsoleUtils.println("Drawing window deactivated");
	    }

	    public void windowDeiconified(WindowEvent arg0) {
		ConsoleUtils.println("Drawing window deiconified");
	    }

	    public void windowIconified(WindowEvent arg0) {
		ConsoleUtils.println("Drawing window iconified");
	    }

	    public void windowOpened(WindowEvent arg0) {
		ConsoleUtils.println("Drawing window opened");
	    }
	});
	drawFrame.addKeyListener(new KeyHandler(main));
	drawFrame.setVisible(true);

	mainThread = new Thread(main);
	mainThread.start();

	ConsoleUtils.println("Start");

	while (true) {
	}
    }

    private void startDeltaTime() {
	deltaTime = 0;
	curTime = prevTime = System.nanoTime();
    }

    private void calcDeltaTime() {
	curTime = System.nanoTime();
	deltaTime = curTime - prevTime;
	prevTime = curTime;
    }

    public void run() {
	ConsoleUtils.println("Thread start");
	startDeltaTime();
	loadContent();
	while (!Main.gameDone) {
	    calcDeltaTime();
	    timer += deltaTime;
	    fpsTimer += deltaTime;
	    while (timer >= MathUtils.FRAME_60) {
		update(1 / 60.0f);
		timer -= MathUtils.FRAME_60;
	    }
	    draw();
	    if (fpsTimer >= MathUtils.SECOND) {
		fpsTimer = 0;
		tickLabel.setText("TICKS " + ticks + " FRAMES "
			+ StringUtils.formatNumber(frames));
		ConsoleUtils.printFPS(frames, ticks);
		ticks = 0;
		frames = 0;
	    }
	}
	ConsoleUtils.println("Frame died");
	unloadContent();
	ConsoleUtils.println("Done");
	System.exit(0);
    }

    private void update(double dt) {
	// Update Here
	game.update(dt);
	// Stop Updating
	ticks++;
    }

    private void draw() {
	// Draw Here

	frame.repaint();
	drawFrame.repaint();

	// Stop Drawing
	frames++;
    }

    private void drawStuff() {
	if (drawGraphics == null)
	    return;
	game.draw();
	
	Renderer.drawTexture(0, 0, 1, image);
	Renderer.drawTexture(150, 40, 5, image);
	Renderer.drawTexture(75, 80, 3, image);

	Renderer.drawOnScreen(drawGraphics);
	Renderer.clear(0x0);
    }

    private void loadContent() {
	if (Main.updateLocal) {
	    String path = ClassLoader.getSystemClassLoader().getResource(".")
		    .getPath();
	    try {
		LOCAL = URLDecoder.decode(path, "UTF-8") + "/";
	    } catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	    }
	}
	LevelLoader.loadContent();

	game = new GameController();
	game.loadContent();

	drawBuffer = new BufferedImage(VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
		BufferedImage.TYPE_INT_ARGB);
	drawGraphics = drawBuffer.getGraphics();

	Renderer.loadContent();

	BufferedImage img = null;
	try {
	    img = ImageIO.read(new File(LOCAL + "res/Sprites/left.png"));
	} catch (Exception e) {
	    e.printStackTrace();
	}

	image = Texture.getTexture(img);
    }

    private void unloadContent() {
	game.unloadContent();

	LevelLoader.unloadContent();
	if (drawGraphics != null) {
	    drawGraphics.dispose();
	    drawGraphics = null;
	}
	drawBuffer = null;
	image = null;
    }

    private static void parseTags(String[] args) {
	if (args == null || args.length == 0)
	    return;
	for (String str : args) {
	    if (!str.startsWith("-"))
		continue;
	    str = str.substring(1);
	    switch (str) {
	    case "eclipse":
		Main.updateLocal = false;
		break;
	    default:
		continue;
	    }
	}
    }

    public static void appendLine(Object obj) {
	if (obj == null) {
	    appendText("\n");
	    return;
	}
	String str = obj.toString();
	appendText(str + "\n");

    }

    public static void appendText(Object obj) {
	if (obj == null)
	    return;
	String str = obj.toString();
	if (str == null || str.equals(""))
	    return;

	synchronized (console) {
	    if (!console.isVisible() || !frame.isVisible())
		return;
	    console.append(str);
	}
    }

}
