package utils;

import interfaces.Contact;
import interfaces.FutureMeeting;
import interfaces.Meeting;
import interfaces.PastMeeting;

import java.util.*;
import java.util.function.Predicate;

/**
 * Custom predicate to be used in Contact Manager class streams to compare meetings.
 *
 * Created by vladimirsivanovs on 11/03/2016.
 */
public class ContactManagerPredicates {
    private final static long TIME_THRESHOLD = 5000;
    /**
     * Dummy predicate.
     * @return true
     */
    public static Predicate<Meeting> dummyPredicate() {
        return meeting -> true;
    }

    /**
     * Tests if meeting is a future meeting
     *
     * @param date to compare with
     * @return true if meeting is in future and of FutureMeeting type, false otherwise
     */
    public static Predicate<Meeting> isFutureMeeting(Calendar date) {
        return meeting -> meeting.getDate().after(date) && meeting instanceof FutureMeeting;
    }

    /**
     * Tests if meeting is a past meeting
     *
     * @param date to compare with
     * @return true if meeting is in the past and of PastMeeting type, false otherwise
     */
    public static Predicate<Meeting> isPastMeeting(Calendar date) {
        return meeting -> meeting.getDate().before(date) && meeting instanceof PastMeeting;
    }

    /**
     *
     * @param date
     * @return
     */
    public static Predicate<Meeting> meetingOn(Calendar date) {
        return meeting -> meeting.getDate().equals(date);
    }

    /**
     *
     * @param id
     * @return
     */
    public static Predicate<Meeting> meetingWithID(int id) {
        return meeting -> meeting.getId() == id;
    }

    /**
     *
     * @param contact
     * @return
     */
    public static Predicate<Meeting> meetingWithContact(Contact contact) {
        return meeting -> meeting.getContacts().contains(contact);
    }

    /**
     *
     * @param name
     * @return
     */
    public static Predicate<Contact> contactWithName(String name) {
        return contact -> contact.getName().contains(name);
    }

    /**
     *
     * @param ids
     * @return
     */
    public static Predicate<Contact> contactWithID(List<Integer> ids) {
        return contact -> ids.contains(contact.getId());
    }
}
