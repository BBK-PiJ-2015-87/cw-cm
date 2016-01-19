import interfaces.PastMeeting;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class PastMeetingImplTest {
    String notes;
    PastMeeting pastMeeting;

    @Before
    public void setUp() {
        notes = "Note";
        pastMeeting = new PastMeetingImpl(1, Calendar.getInstance(), new HashSet<>(), notes);
    }

    @Test
    public void shouldReturnCorrectNotes() throws Exception {
        assertEquals(notes, pastMeeting.getNotes());
    }
}