package game.level;

import java.util.HashMap;

public class LevelInfo {

    public String levelName;

    public HashMap<String, DoorInfo> Doors;
    public HashMap<String, SignInfo> Signs;

    public LevelInfo(String levelName) {
	this.levelName = levelName;
	Doors = new HashMap<String, DoorInfo>();
	Signs = new HashMap<String, SignInfo>();
    }

    public boolean addDoor(String label, int x, int y, String doorDest) {
	if (Doors.containsKey(label))
	    return false;
	Doors.put(label, new DoorInfo(x, y, doorDest, levelName));
	return true;
    }

    public boolean addDoor(String label, int x, int y, String doorDest,
	    String levelDest) {
	if (Doors.containsKey(label))
	    return false;
	Doors.put(label, new DoorInfo(x, y, doorDest, levelDest));
	return true;
    }

    public boolean addSign(String label, int x, int y, String msg) {
	if (Signs.containsKey(label))
	    return false;
	Signs.put(label, new SignInfo(x, y, msg));
	return true;
    }

    public class DoorInfo {
	public int x, y;
	public String levelDest, doorDest;

	public DoorInfo(int x, int y, String doorDest, String levelDest) {
	    this.x = x;
	    this.y = y;
	    this.doorDest = doorDest;
	    this.levelDest = levelDest;
	}
    }

    public class SignInfo {
	public int x, y;
	public String msg;

	public SignInfo(int x, int y, String msg) {
	    this.x = x;
	    this.y = y;
	    this.msg = msg;
	}
    }

}
