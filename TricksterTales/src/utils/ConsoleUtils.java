package utils;

import main.Main;

public class ConsoleUtils {

    public static enum LEVEL {
	NONE, FPS, DEBUG, INFO, count
    };

    public static final LEVEL PRINT_LEVEL = LEVEL.DEBUG;
    public static final int PRINT_LEVELi = PRINT_LEVEL.ordinal();
    public static final int LEVEL_ENUM_COUNT = LEVEL.count.ordinal();

    public static void println(Object obj, LEVEL l) {
	int o = l.ordinal();
	if (o < PRINT_LEVELi || o == LEVEL_ENUM_COUNT)
	    return;
	Main.appendLine(obj);
    }

    public static void println(Object obj) {
	println(obj, LEVEL.DEBUG);
    }

    public static void print(Object obj, LEVEL l) {
	int o = l.ordinal();
	if (o < PRINT_LEVELi || o == LEVEL_ENUM_COUNT)
	    return;
	Main.appendText(obj);
    }

    public static void printFPS(long frames, long ticks) {
	ConsoleUtils.println(
		"TICKS " + ticks + " FRAMES "
			+ StringUtils.formatNumber(frames), LEVEL.FPS);
    }

}
