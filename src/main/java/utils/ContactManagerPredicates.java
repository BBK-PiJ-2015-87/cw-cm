package utils;

import interfaces.Contact;
import interfaces.Meeting;

import java.util.*;
import java.util.function.Predicate;

/**
 * Custom predicate to be used in Contact Manager class streams to compare meetings.
 *
 * Created by vladimirsivanovs on 11/03/2016.
 */
public class ContactManagerPredicates {

    /**
     *
     * @return
     */
    public static Predicate<Meeting> dummyPredicate() {
        return meeting -> true;
    }

    /**
     *
     * @return
     */
    public static Predicate<Meeting> isFutureMeeting() {
        Calendar now = new GregorianCalendar();
        return meeting -> meeting.getDate().after(now);
    }

    /**
     *
     * @return
     */
    public static Predicate<Meeting> isPastMeeting() {
        Calendar now = new GregorianCalendar();
        return meeting -> meeting.getDate().before(now);
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
    public static Predicate<Meeting> meetingHasID(int id) {
        return meeting -> meeting.getId() == id;
    }

    /**
     *
     * @param contact
     * @return
     */
    public static Predicate<Meeting> meetingHasContact(Contact contact) {
        return meeting -> meeting.getContacts().contains(contact);
    }

    /**
     *
     * @param name
     * @return
     */
    public static Predicate<Contact> contactWithName(String name) {
        return contact -> contact.getName().equals(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public static Predicate<Contact> contactWithID(int id) {
        return contact -> contact.getId() == id;
    }
}
