import interfaces.Contact;
import interfaces.Meeting;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by vladimirsivanovs on 10/03/2016.
 */
public class TESTflush {
    public static void main(String[] args) throws JAXBException {
        ContactManagerImpl cm;
        List<Meeting> pastMeetings;
        List<Meeting> futureMeetings;
        List<Meeting> pastAndFutureMeetings;
        Set<Contact> participantsFuture;
        Set<Contact> participantsPast;
        Set<Contact> contacts;

        pastAndFutureMeetings = new ArrayList<>();
        contacts = new HashSet<>();

        participantsFuture = IntStream.rangeClosed(1, 2)
                .boxed()
                .map(id -> new ContactImpl(id, "name_"+id, "notes_"+id))
                .collect(Collectors.toSet());

        participantsPast = IntStream.rangeClosed(3, 4)
                .boxed()
                .map(id -> new ContactImpl(id, "name_"+id, "notes_"+id))
                .collect(Collectors.toSet());

        futureMeetings = IntStream.rangeClosed(1, 2)
                .boxed()
                .map(id -> new MeetingImpl(id, new GregorianCalendar(2020+id, 0, 10), participantsFuture))
                .collect(Collectors.toList());

        pastMeetings = IntStream.rangeClosed(3, 4)
                .boxed()
                .map(id -> new MeetingImpl(id, new GregorianCalendar(2000-id, 0, 10), participantsPast))
                .collect(Collectors.toList());


        pastAndFutureMeetings.addAll(futureMeetings);
        pastAndFutureMeetings.addAll(pastMeetings);

        contacts.addAll(participantsFuture);
        contacts.addAll(participantsPast);

        cm = new ContactManagerImpl(contacts, pastAndFutureMeetings);


        JAXBContext jaxbContext = JAXBContext.newInstance(ContactManagerImpl.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.marshal(cm, new File("contacts.txt"));
    }

}
