import interfaces.Contact;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for ContactImpl.
 *
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class ContactImplTest {
    Contact contact;
    int id;
    String name;
    String notes;

    @Before
    public void setUp() {
        id = 50;
        name = "Mark";
        notes = "Note.";
        contact = new ContactImpl(id, name, notes);
    }

    @Test
    public void shouldReturnCorrectId() throws Exception {
        assertEquals(id, contact.getId());
    }

    @Test
    public void shouldReturnCorrectName() throws Exception {
        assertEquals(name, contact.getName());
    }

    @Test
    public void shouldReturnCorrectNotes() throws Exception {
        assertEquals(notes, contact.getNotes());
    }

    @Test
    public void shouldReturnEmptyStringIfNotesAreNotProvided() throws Exception {
        contact = new ContactImpl(id, name, null);
        assertNotNull(contact.getNotes());
        assertEquals("", contact.getNotes());
    }

    @Test
    public void shouldAddNoteAfterSpaceIfNotesExist() throws Exception {
        String newNote = "New note.";
        contact.addNotes(newNote);
        assertEquals(notes + " " + newNote, contact.getNotes());
    }

    @Test
    public void shouldReplaceNotesIfTheyAreEmpty() throws Exception {
        String newNote = "New note.";
        contact = new ContactImpl(id, name, null);
        contact.addNotes(newNote);
        assertEquals(newNote, contact.getNotes());
    }
}