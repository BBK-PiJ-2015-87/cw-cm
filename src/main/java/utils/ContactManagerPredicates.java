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
    /**
     * Dummy predicate.
     * @return always true
     */
    public static Predicate<Meeting> dummyPredicate() {
        return meeting -> true;
    }

    public static Predicate<Meeting> isFutureMeeting(Calendar date) {
        return meeting -> meeting.getDate().after(date) && meeting instanceof FutureMeeting;
    }

    public static Predicate<Meeting> isPastMeeting(Calendar date) {
        return meeting -> meeting.getDate().before(date) && meeting instanceof PastMeeting;
    }

    public static Predicate<Meeting> meetingOn(Calendar date) {
        return meeting -> meeting.getDate().equals(date);
    }

    public static Predicate<Meeting> meetingWithID(int id) {
        return meeting -> meeting.getId() == id;
    }

    public static Predicate<Meeting> meetingWithContact(Contact contact) {
        return meeting -> meeting.getContacts().contains(contact);
    }

    public static Predicate<Contact> contactWithName(String name) {
        return contact -> contact.getName().contains(name);
    }

    public static Predicate<Contact> contactWithID(List<Integer> ids) {
        return contact -> ids.contains(contact.getId());
    }
}
