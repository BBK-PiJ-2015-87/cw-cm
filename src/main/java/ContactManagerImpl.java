import interfaces.*;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * Created by Workstation on 19/01/16.
 */
public class ContactManagerImpl implements ContactManager {
    private List<FutureMeeting> futureMeetings;
    private List<PastMeeting> pastMeetings;
    private List<Meeting> meetings;
    private Set<Contact> contacts;
    /**
     * Add a new meeting to be held in the future.
     *
     * @param contacts a list of contacts that will participate in the meeting
     * @param date     the date on which the meeting will take place
     * @return the ID for the meeting
     * @throws IllegalArgumentException if the meeting is set for a time in the past,
     *                                  of if any contact is unknown / non-existent
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        return 0;
    }

    /**
     * Returns the PAST meeting with the requested ID, or null if it
     * there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the future
     */
    @Override
    public PastMeeting getPastMeeting(int id) {
        return null;
    }

    /**
     * Returns the FUTURE meeting with the requested ID, or null if
     * there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the past
     */
    @Override
    public FutureMeeting getFutureMeeting(int id) {
        return null;
    }

    /**
     * Returns the meeting with the requested ID, or null if it there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     */
    @Override
    public Meeting getMeeting(int id) {
        return null;
    }

    /**
     * Returns the list of future meetings scheduled with this contact.
     * <p>
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the user’s contacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not
     *                                  exist
     */
    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        return null;
    }

    /**
     * Returns the list of meetings that are scheduled for, or thattook
     * place on, the specified date
     * <p>
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param date the date
     * @return the list of meetings
     */
    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        return null;
    }

    /**
     * Returns the list of past meetings in which this contact has participated.
     * <p>
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the user’s contacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     */
    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        return null;
    }

    /**
     * Create a new record for a meeting that took place in the past.
     *
     * @param contacts a list of participants
     * @param date     the date on which the meeting took place
     * @param text     messages to be added about the meeting.
     * @throws IllegalArgumentException if the list of contacts is
     *                                  empty, or any of the contacts does not exist
     * @throws NullPointerException     if any of the arguments is null
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {

    }

    /**
     * Add notes to a meeting.
     * <p>
     * This method is used when a future meeting takes place, and is
     * then converted to a past meeting (with notes).
     * <p>
     * It can be also used to add notes to a past meeting at a later date.
     *
     * @param id   the ID of the meeting
     * @param text messages to be added about the meeting.
     * @throws IllegalArgumentException if the meeting does not exist
     * @throws IllegalStateException    if the meeting is set for a date in the future
     * @throws NullPointerException     if the notes are null
     */
    @Override
    public void addMeetingNotes(int id, String text) {

    }

    /**
     * Create a new contact with the specified name and notes.
     *
     * @param name  the name of the contact.
     * @param notes notes to be added about the contact.
     * @throws NullPointerException if the name or the notes are null
     */
    @Override
    public void addNewContact(String name, String notes) {

    }

    /**
     * Returns a list containing the contacts that correspond to the IDs.
     *
     * @param ids an arbitrary number of contact IDs
     * @return a list containing the contacts that correspond to the IDs.
     * @throws IllegalArgumentException if any of the IDs does not correspond to a real contact
     */
    @Override
    public Set<Contact> getContacts(int... ids) {
        return null;
    }

    /**
     * Returns a list with the contacts whose name contains that string.
     *
     * @param name the string to search for
     * @return a list with the contacts whose name contains that string.
     * @throws NullPointerException if the parameter is null
     */
    @Override
    public Set<Contact> getContacts(String name) {
        return null;
    }

    /**
     * Save all data to disk.
     * <p>
     * This method must be executed when the program is
     * closed and when/if the user requests it.
     */
    @Override
    public void flush() {

    }
}