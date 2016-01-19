import interfaces.Contact;
import interfaces.Meeting;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by Workstation on 19/01/16.
 */
public class MeetingImpl implements Meeting {
    private int id;
    private Calendar date;
    private Set<Contact> contacts;

    public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        this.id = id;
        this.date = date;
        this.contacts = contacts;
    }

    /**
     * Returns the id of the meeting.
     *
     * @return the id of the meeting.
     */
    @Override
    public int getId() {
        return  id;
    }

    /**
     * Return the date of the meeting.
     *
     * @return the date of the meeting.
     */
    @Override
    public Calendar getDate() {
        return date;
    }

    /**
     * Return the details of people that attended the meeting.
     * <p>
     * The list contains a minimum of one contact (if there were
     * just two people: the user and the contact) and may contain an
     * arbitraty number of them.
     *
     * @return the details of people that attended the meeting.
     */
    @Override
    public Set<Contact> getContacts() {
        return contacts;
    }
}
