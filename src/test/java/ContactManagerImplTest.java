import interfaces.Contact;
import interfaces.Meeting;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class ContactManagerImplTest {
    ContactManagerImpl cm;
    List<Meeting> meetings;
    Set<Contact> contacts;
    Set<Contact> participants;

    @Before
    public void setUp() throws Exception {
        meetings = new ArrayList<>();
        contacts = new HashSet<>();

        participants = IntStream.rangeClosed(0, 5)
                .boxed()
                .map(id -> new ContactImpl(id, "", ""))
                .collect(Collectors.toSet());

        meetings = IntStream.rangeClosed(0, 5)
                .boxed()
                .map(id -> new MeetingImpl(id, null, null))
                .collect(Collectors.toList());

        cm = new ContactManagerImpl(meetings, contacts);
    }

    @Test
    public void shouldAddFutureMeetingIfFutureDateProvided() throws Exception {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.roll(Calendar.DATE, true);
        int meetingId = cm.addFutureMeeting(participants, tomorrow);
        assertThat(meetingId, is(6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfPastDateProvided() throws Exception {
        Calendar yesterday = Calendar.getInstance();
        yesterday.roll(Calendar.MONTH, false);
        cm.addFutureMeeting(participants, yesterday);
    }
}