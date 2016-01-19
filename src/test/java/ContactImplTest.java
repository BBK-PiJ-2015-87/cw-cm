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
    String name = "Name";
    String notes = "note";

    @Before
    public void setUp() {
        id = 50;
        name = "Mark";
        notes = "Note";
        contact = new ContactImpl(id, name, notes);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(id, contact.getId());
    }

    @Test
    public void testGetName() throws Exception {

    }

    @Test
    public void testGetNotes() throws Exception {

    }

    @Test
    public void testAddNotes() throws Exception {

    }
}