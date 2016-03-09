import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test class for methods in Util class.
 *
 * Created by Vladimirs Ivanovs on 26/01/16.
 */
public class UtilsTest {
    Set<Integer> numbers;

    @Test
    public void shouldGenerateCorrectNewNumberWhenSetIsEmpty(){
        numbers = new HashSet<>();

        int newNum= Utils.generateNewNumber(numbers);
        assertThat(newNum, is(0));
    }

    @Test
    public void shouldGenerateCorrectNewNumberWhenNumbersAreConsecutive(){
        //generate numbers from 0 to 15
        numbers = IntStream.rangeClosed(0, 15).boxed().collect(Collectors.toSet());

        int newID = Utils.generateNewNumber(numbers);
        assertThat(newID, is(16));
    }

    @Test
    public void shouldGenerateCorrectNewNumberWhenNumbersStartWithNonZero(){
        //generate numbers from 5 to 15
        numbers = IntStream.rangeClosed(5, 15).boxed().collect(Collectors.toSet());

        int newID = Utils.generateNewNumber(numbers);
        assertThat(newID, is(0));
    }

    @Test
    public void shouldGenerateCorrectNewNumberWhenNumbersNotConsecutive(){
        numbers = new HashSet<>(Arrays.asList(0, 1, 2, 5, 6));

        int newID = Utils.generateNewNumber(numbers);
        assertThat(newID, is(3));
    }

    @Test
    public void shouldGenerateCorrectNewNumberWhenNumbersAreConsecutiveStartWithNonZero(){
        numbers = new HashSet<>(Arrays.asList(1, 2, 5, 6));

        int newID = Utils.generateNewNumber(numbers);
        assertThat(newID, is(0));
    }

    @Test
    public void shouldReturnTrueForFutureDate() throws Exception {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.roll(Calendar.MONTH, true);
        assertTrue(Utils.isFuture(tomorrow));
    }

    @Test
    public void shouldReturnFalseForPastDate() throws Exception {
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.MONTH, false);
        assertFalse(Utils.isFuture(now));
    }
}