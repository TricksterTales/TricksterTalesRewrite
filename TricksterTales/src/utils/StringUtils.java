package utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class StringUtils {

    public static final NumberFormat nfInt = NumberFormat.getIntegerInstance();

    public static String createChars(char ch, int count) {
	if (count <= 0)
	    return "";
	char[] chars = new char[count];
	Arrays.fill(chars, ch);
	return new String(chars);
    }

    public static String makeLength(String str, int length, char fill) {
	int slength = str.length();
	if (slength == length)
	    return new String(str);
	if (slength < length) {
	    int needed = length - slength;
	    return new String(str + createChars(fill, needed));
	} else {
	    return new String(str.substring(0, length));
	}
    }

    public static String getArgsToEnd(String args, int start, boolean trim) {
	if (start <= 0)
	    return new String(args);
	boolean inWhite = false;
	boolean hitChar = false;
	int argc = 1;
	int i = -1;
	char ch;
	if (trim == false) {
	    i = 0;
	    while (hitChar == false) {
		ch = args.charAt(i);
		if (Character.isWhitespace(ch)) {
		    ++i;
		} else {
		    hitChar = true;
		}
	    }
	    --i;
	} else {
	    args = args.trim();
	}
	int len = args.length();
	while (argc < start) {
	    ++i;
	    if (i >= len)
		return "";
	    ch = args.charAt(i);
	    if (inWhite == false && Character.isWhitespace(ch)) {
		inWhite = true;
	    }
	    if (inWhite == true && !Character.isWhitespace(ch)) {
		inWhite = false;
		++argc;
	    }
	}
	return new String(args.substring(i));
    }

    public static String getArgsToEnd(String args, int start) {
	return getArgsToEnd(args, start, false);
    }

    public static char getEscapeChar(char esc) {
	switch (esc) {
	case 'b':
	    return '\b';
	case 'n':
	    return '\n';
	case 't':
	    return '\t';
	case '\\':
	    return '\\';
	case '\"':
	    return '\"';
	case '\'':
	    return '\'';
	default:
	    return 0;
	}
    }

    public static String[] parseArguments(String str) {
	if (str == null || str.length() == 0)
	    return null;

	ArrayList<String> args = new ArrayList<String>();

	String curArg = "";
	boolean inQuotes = false;
	int i = 0;
	char ch, add;
	str = str.trim();
	int maxi = str.length();

	while (i < maxi) {
	    ch = str.charAt(i);
	    if (inQuotes) {
		if (Character.isWhitespace(ch)) {
		    ch = ' ';
		    curArg = curArg + ch;
		} else if (ch == '\\') {
		    ++i;
		    if (i >= maxi)
			break;
		    ch = str.charAt(i);
		    add = getEscapeChar(ch);
		    if (add == '\b') {
			if (curArg.length() != 0)
			    curArg = curArg.substring(0, curArg.length() - 1);
		    } else if (add != 0) {
			curArg = curArg + add;
		    }
		} else if (ch == '\"') {
		    inQuotes = false;
		    args.add(curArg);
		    curArg = "";
		} else {
		    curArg = curArg + ch;
		}
	    } else {
		if (Character.isWhitespace(ch)) {
		    if (curArg.length() != 0) {
			args.add(curArg);
			curArg = "";
		    }
		} else if (ch == '\\') {
		    ++i;
		    if (i >= maxi)
			break;
		    ch = str.charAt(i);
		    add = getEscapeChar(ch);
		    if (add == '\b') {
			if (curArg.length() != 0)
			    curArg = curArg.substring(0, curArg.length() - 1);
		    } else if (add != 0) {
			curArg = curArg + add;
		    }
		} else if (ch == '\"') {
		    if (curArg.length() != 0) {
			args.add(curArg);
			curArg = "";
		    }
		    inQuotes = true;
		} else {
		    curArg = curArg + ch;
		}
	    }
	    ++i;
	}

	if (curArg.length() != 0) {
	    args.add(curArg);
	    curArg = "";
	}

	return args.toArray(new String[args.size()]);
    }

    public static <T> String getContents(T[] array) {
	if (array == null || array.length == 0) {
	    return "{ }";
	}

	String contents = "";

	for (int i = 0; i < array.length; ++i) {
	    if (contents.equals("")) {
		contents = "{ \'" + array[i];
	    } else {
		contents = contents + "\', \'" + array[i];
	    }
	}

	if (contents.equals("")) {
	    contents = "{ }";
	} else {
	    contents = contents + "\' }";
	}

	return contents;
    }

    public static String formatNumber(int number) {
	return nfInt.format(number);
    }

    public static String formatNumber(long number) {
	return nfInt.format(number);
    }

}
