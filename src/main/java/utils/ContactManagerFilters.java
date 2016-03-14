package utils;

import interfaces.Contact;
import interfaces.Meeting;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static utils.ContactManagerPredicates.*;

/**
 * Custom filtering methods to be used as helpers for meeting list filtering.
 *
 * Created by Vladimirs Ivanovs on 11/03/2016.
 */
public class ContactManagerFilters {

    /**
     * Custom method to filter list with 2 predicate.
     *
     * @param meetings list of meetings to be filtered
     * @param predicate first filter
     * @param predicate2 second filter
     * @param comparator comparator to sort
     * @return depends on set up. Can return filtered and sorted list
     */
    public static List<Meeting> customMeetingFilter(List<? super Meeting> meetings, Predicate<Meeting> predicate, Predicate<Meeting> predicate2, Comparator<Meeting> comparator) {
        return meetings.stream()
                .map(meeting -> (Meeting)meeting)
                .filter(predicate)
                .filter(predicate2)
                .sorted(comparator)
                .collect(Collectors.<Meeting>toList());
    }

    /**
     * Custom method to filter contacts.
     *
     * @param contacts set of contacts to be filtered
     * @param predicate
     * @return set of filtered contacts according to predicate
     */
    public static Set<Contact> customContactFilter(Set<Contact> contacts, Predicate<Contact> predicate) {
        return contacts.stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }


    public static List<Meeting> filterAnyMeetingsWithID(List<? super Meeting> meetings, int id) {
        return customMeetingFilter(meetings,  meetingWithID(id), dummyPredicate(), new DateComparator());
    }

    public static List<Meeting> filterAnyMeetingsOnDate(List<? super Meeting> meetings, Calendar date) {
        return customMeetingFilter(meetings,  meetingOn(date), dummyPredicate(), new DateComparator());
    }

    public static List<Meeting> filterFutureMeetingsWithContact(List<? super Meeting> meetings, Contact contact, Calendar now) {
        return customMeetingFilter(meetings, isFutureMeeting(now), meetingWithContact(contact), new DateComparator());
    }

    public static List<? super Meeting> filterPastMeetingsByContact(List<? super Meeting> meetings, Contact contact) {
        return customMeetingFilter(meetings, isPastMeeting(new GregorianCalendar()), meetingWithContact(contact), new DateComparator());
    }

    public static Set<Contact> filterContactsWithName(Set<Contact> contacts, String nameContains) {
        return customContactFilter(contacts, contactWithName(nameContains));
    }

    public static Set<Contact> filterContactsWithId(Set<Contact> contacts, List<Integer> ids) {
        return customContactFilter(contacts, contactWithID(ids));
    }
}
