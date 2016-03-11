package utils;

import interfaces.Meeting;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static utils.MeetingPredicates.dummyPredicate;
import static utils.MeetingPredicates.isFutureMeeting;
import static utils.MeetingPredicates.meetingHasID;

/**
 * Created by vladimirsivanovs on 11/03/2016.
 */
public class MeetingFilters {

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
        return filterMeetings(meetings, isFutureMeeting(), meetingHasID(id), new DateComparator()).stream()
                .findFirst();
    }
}
