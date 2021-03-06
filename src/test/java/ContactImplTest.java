import interfaces.Contact;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for ContactImpl.
 *
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class ContactImplTest {
    Contact contact;
    Contact contact2;
    int id;
    String name;
    String notes;

    @Before
    public void setUp() {
        id = 50;
        name = "Mark";
        notes = "Note.";
        contact = new ContactImpl(id, name, notes);
        contact2 = new ContactImpl(id, name, notes);
    }

    @Test
    public void shouldReturnCorrectId() {
        assertEquals(id, contact.getId());
    }

    @Test
    public void shouldReturnCorrectName() {
        assertEquals(name, contact.getName());
    }

    @Test
    public void shouldReturnNullIfNoName() {
        contact = new ContactImpl(id, null, notes);

        assertNull(contact.getName());
    }

    @Test
    public void shouldReturnCorrectNotes() {
        assertEquals(notes, contact.getNotes());
    }

    @Test
    public void shouldReturnEmptyStringIfNotesAreNotProvided() {
        contact = new ContactImpl(id, name, null);

        assertNotNull(contact.getNotes());
        assertEquals("", contact.getNotes());
    }

    @Test
    public void shouldTrimWhiteSpacesInProvidedNotes() {
        contact = new ContactImpl(id, name, "    ");

        assertEquals("", contact.getNotes());
    }

    @Test
    public void shouldTrimWhiteSpacesWhenAddingNotes() {
        contact = new ContactImpl(id, name, null);
        contact.addNotes("    ");

        assertEquals("", contact.getNotes());
    }

    @Test
    public void shouldTrimLeadingWhiteSpacesInNotes() throws Exception {
        contact = new ContactImpl(id, name, null);
        contact.addNotes("    Note.");

        assertEquals("Note.", contact.getNotes());
    }

    @Test
    public void shouldTrimTrailingWhiteSpacesInNotes() throws Exception {
        contact.addNotes("Note.   ");

        assertEquals(notes + " " + "Note.", contact.getNotes());
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

    @Test
    public void contactsShouldBeEqualWhenAllFieldsAreSame() throws Exception {
        assertEquals(contact.hashCode(), contact2.hashCode());
        assertEquals(contact, contact2);
    }

    @Test
    public void contactsShouldBeEqualWhenIdEquals() throws Exception {
        contact2 = new ContactImpl(50, new String("Mark"), new String("Note."));

        assertTrue(contact.hashCode() == contact2.hashCode());
        assertEquals(contact, contact2);
    }

    @Test
    public void contactsShouldBeEqualIfNullNamesButSameIDs() throws Exception {
        contact2 = new ContactImpl(50, null, new String("NewNote."));
        contact  = new ContactImpl(50, null, new String("NewNote."));

        assertEquals(contact.hashCode(), contact2.hashCode());
        assertEquals(contact, contact2);
    }

    @Test
    public void contactsShouldBeEqualIfNullNamesAndNotesButSameIDs() throws Exception {
        contact2 = new ContactImpl(50, null, null);
        contact  = new ContactImpl(50, null, null);

        assertEquals(contact.hashCode(), contact2.hashCode());
        assertEquals(contact, contact2);
    }

    @Test
    public void contactsShouldNotBeEqualIfDifferentID() throws Exception {
        contact2 = new ContactImpl(51, new String("Mark"), new String("Note."));

        assertNotEquals(contact.hashCode(), contact2.hashCode());
        assertNotEquals(contact, contact2);
    }

    @Test
    public void contactsShouldNotBeEqualIfDifferentIdAndNullNameAndNotes() throws Exception {
        contact = new ContactImpl(50, null, null);
        contact2 = new ContactImpl(51, null, null);
        assertNotEquals(contact.hashCode(), contact2.hashCode());
        assertNotEquals(contact, contact2);
    }
}