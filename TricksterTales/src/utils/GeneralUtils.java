package utils;

public class GeneralUtils {

    public static <T> boolean contains(T[] array, T obj) {
	if (array == null)
	    return false;
	for (T o : array) {
	    if (o == null)
		continue;
	    if (o.equals(obj))
		return true;
	}
	return false;
    }

}
