import interfaces.Meeting;

import java.util.Map;

/**
 * Utility class which contains helper static methods.
 *
 * Created by Vladimirs Ivanovs on 26/01/16.
 */
public class Utils {

    /**
     * Creates a unique int ID based on integer key values of a given map.
     *
     * @param map to be inspected
     * @return unique ID
     */
    public static int generateNewID(Map<Integer, Meeting> map) {
        int newId = 0;
        while (map.containsKey(newId)){
            newId++;
        }
        return newId;
    }
}
