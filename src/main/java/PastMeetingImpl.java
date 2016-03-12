import interfaces.Contact;
import interfaces.PastMeeting;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    private String notes = "";

    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes) {
        super(id, date, contacts);
        this.notes = notes;
    }

    /**
     * Returns the notes from the meeting.
     * <p>
     * If there are no notes, the empty string is returned.
     *
     * @return the notes from the meeting.
     */
    @Override
    public String getNotes() {
        return notes;
    }
}
