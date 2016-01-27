import interfaces.Meeting;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * Creates a unique int ID based on IDs of meeting objects in the list.
     *
     * @param list to be inspected
     * @return unique ID
     */
    public static int generateNewID(List<Meeting> list) {
        Set<Integer> keys = list.stream()
                .map(x -> x.getId())
                .collect(Collectors.toSet());

        int newId = 0;
        while (keys.contains(newId)){
            newId++;
        }
        return newId;
    }

    /**
     * Tests if meeting is being considered PastMeeting.
     *
     * @param meeting to test
     * @return
     */
    public static boolean isPastMeeting(Meeting meeting) {
        return System.currentTimeMillis() > meeting.getDate().getTimeInMillis();
    }
}
