import java.util.Calendar;
import java.util.Set;

/**
 * Utility class which contains helper static methods for generating unique numbers.
 *
 * Created by Vladimirs Ivanovs on 26/01/16.
 */
public class Utils {
    /**
     * Creates a unique number which doesn't exist in the provided set of integers.
     * If provided set of numbers is empty, then new 0 is retrbed.
     *
     * @param numbers set of existing numbers
     * @return unique integer or 0 if set is empty
     */
    public static int generateNewNumber(Set<Integer> numbers) {
        int newNum = 0;
        if (!numbers.isEmpty()){
            while (numbers.contains(newNum)){
                newNum++;
            }
        }
        return newNum;
    }


    /**
     * Tests if provided date is in future, compared to now.
     *
     * @param date to be tested
     * @return true if date is in the future, false otherwise
     */
    public static boolean isFuture(Calendar date) {
        Calendar now = Calendar.getInstance();
        if (date.compareTo(now) == 1) {
            return true;
        }
        return false;
    }
}
