package game.level;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import main.Main;
import utils.ConsoleUtils;
import utils.StringUtils;

public class LevelLoader {

    public static HashMap<String, Dimension> LEVEL_SIZES;
    public static HashMap<String, String> LEVEL_BLOCKS;
    public static HashMap<String, LevelInfo> LEVEL_DATA;

    static {
	LEVEL_SIZES = new HashMap<String, Dimension>();
	LEVEL_BLOCKS = new HashMap<String, String>();
	LEVEL_DATA = new HashMap<String, LevelInfo>();
    }

    public static void loadContent() {
	// res/Levels/
	File file = new File(Main.LOCAL + "res/Levels/Levels.txt");
	unloadContent();
	if (!file.exists()) {
	    ConsoleUtils.println("Levels not found");
	    return;
	}
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(file));
	    String line, n;
	    int w, h;
	    String[] args;
	    File data, objects;
	    ConsoleUtils.println("Reading levels");
	    while ((line = reader.readLine()) != null) {
		args = StringUtils.parseArguments(line);
		ConsoleUtils.println(line);
		if (args.length != 3)
		    continue;
		n = args[0];
		w = Integer.parseInt(args[1]);
		h = Integer.parseInt(args[2]);
		if (LEVEL_SIZES.containsKey(n)) {
		    ConsoleUtils.println("Level " + n + " already in use");
		    continue;
		} else {
		    LEVEL_SIZES.put(n, new Dimension(w, h));
		}
		data = new File(Main.LOCAL + "res/Levels/LevelData_" + n
			+ ".txt");
		objects = new File(Main.LOCAL + "res/Levels/LevelObjects_" + n
			+ ".txt");
		if (!loadLevel(n, w, h, data, objects)) {
		    ConsoleUtils.println("Loading level " + n + " failed");
		    LEVEL_SIZES.remove(n);
		    LEVEL_BLOCKS.remove(n);
		    LEVEL_DATA.remove(n);
		}
	    }
	    reader.close();
	    ConsoleUtils.println("Done loading levels");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private static boolean loadLevel(String name, int w, int h, File data,
	    File objs) throws Exception {
	if (!data.exists()) {
	    ConsoleUtils.println("Level data for " + name + " not found");
	    return false;
	}
	BufferedReader reader = new BufferedReader(new FileReader(data));
	String line;
	String[] args;
	int curh = 0;
	String leveldata = "";
	ConsoleUtils.println("Loading level data");
	while ((curh < h) && ((line = reader.readLine()) != null)) {
	    ++curh;
	    line = StringUtils.makeLength(line, w, Level.NOTHING);
	    leveldata = line + leveldata;
	}
	if (curh < h) {
	    leveldata = StringUtils.createChars(Level.NOTHING, (h - curh) * w)
		    + leveldata;
	}
	reader.close();
	LEVEL_BLOCKS.put(name, leveldata);
	if (!objs.exists()) {
	    ConsoleUtils.println("No object data for level " + name);
	    return true;
	}
	reader = new BufferedReader(new FileReader(objs));
	LevelInfo linfo = new LevelInfo(name);
	int x, y;
	String msg, label;
	ConsoleUtils.println("Reading level objects");
	while ((line = reader.readLine()) != null) {
	    args = StringUtils.parseArguments(line);
	    ConsoleUtils.println(line);
	    if (args.length < 4) {
		ConsoleUtils.println("Not enough arguments");
		continue;
	    }
	    label = args[1];
	    x = Integer.parseInt(args[2]);
	    y = Integer.parseInt(args[3]);
	    if (x < 0 || x > w) {
		ConsoleUtils.println("Position out of bounds");
		continue;
	    }
	    if (y < 0 || y > h) {
		ConsoleUtils.println("Position out of bounds");
		continue;
	    }
	    switch (args[0].toLowerCase()) {
	    case "door":
		if (args.length == 5) {
		    if (linfo.addDoor(label, x, y, args[4]))
			ConsoleUtils.println("Door added, " + label);
		    else
			ConsoleUtils.println("Door label taken already "
				+ label);
		} else if (args.length == 6) {
		    if (linfo.addDoor(label, x, y, args[4], args[5]))
			ConsoleUtils.println("Door added, " + label);
		    else
			ConsoleUtils.println("Door label taken already "
				+ label);
		} else {
		    ConsoleUtils.println("Bad arguments for door");
		}
		break;
	    case "sign":
		if (args.length != 5) {
		    ConsoleUtils.println("Bad arguments for sign");
		    continue;
		}
		msg = args[4];
		if (linfo.addSign(label, x, y, msg))
		    ConsoleUtils.println("Sign added, " + label + ", \'" + msg
			    + "\'");
		else
		    ConsoleUtils.println("Sign label taken already " + label);
		break;
	    default:
		continue;
	    }
	}
	reader.close();
	LEVEL_DATA.put(name, linfo);
	ConsoleUtils.println("Done loading objects");
	return true;
    }

    public static void unloadContent() {
	ConsoleUtils.println("Unloading level data");
	LEVEL_SIZES.clear();
	LEVEL_BLOCKS.clear();
	LEVEL_DATA.clear();
    }

}
