import interfaces.Contact;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
        notes = "Note";
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
    public void testGetNotes() throws Exception {

    }

    @Test
    public void testAddNotes() throws Exception {

    }
}