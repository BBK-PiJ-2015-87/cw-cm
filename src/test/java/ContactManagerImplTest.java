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

        participants = IntStream.range(1, 5)
                .boxed()
                .map(id -> new ContactImpl(id, "", ""))
                .collect(Collectors.toSet());

        cm = new ContactManagerImpl(meetings, contacts);
    }

    @Test
    public void shouldAddFutureMeeting() throws Exception {
        Calendar today = Calendar.getInstance(Locale.UK);
        int meetingId = cm.addFutureMeeting(participants, today);
        assertThat(meetingId, is(1));
    }
}