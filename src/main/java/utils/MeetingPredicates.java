package utils;

import interfaces.Meeting;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Custom predicate to be used in Contact Manager class streams to compare meetings.
 *
 * Created by vladimirsivanovs on 11/03/2016.
 */
public class MeetingPredicates {
    public static Predicate<Meeting> isFutureMeeting() {
        Calendar now = new GregorianCalendar();
        return meeting -> meeting.getDate().after(now);
    }

    public static Predicate<Meeting> isPastMeeting() {
        Calendar now = new GregorianCalendar();
        return meeting -> meeting.getDate().before(now);
    }

    public static Predicate<Meeting> isMeetingDate(Calendar date) {
        return meeting -> meeting.getDate().equals(date);
    }

    public static Predicate<Meeting> dummyPredicate() {
        return meeting -> true;
    }

    public static Predicate<Meeting> isMeetingID(int id) {
        return meeting -> meeting.getId() == id;
    }

    public static List<Meeting> filterMeetings(List<Meeting> meetings, Predicate<Meeting> predicate, Predicate<Meeting> predicate2, Comparator<Meeting> comparator) {
        return meetings.stream()
                .filter(predicate)
                .filter(predicate2)
                .sorted(comparator)
                .collect(Collectors.<Meeting>toList());
    }

    public static List<Meeting> getAllFutureMeetings(List<Meeting> meetings) {
        return filterMeetings(meetings, isFutureMeeting(), dummyPredicate(), new DateComparator());
    }

    public static Optional<Meeting> getFutureMeetingByID(List<Meeting> meetings, int id) {
        return filterMeetings(meetings, isFutureMeeting(), isMeetingID(id), new DateComparator()).stream()
                .findFirst();
    }
}
