import interfaces.Contact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Workstation on 19/01/16.
 */

@XmlRootElement(name = "contact")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactImpl implements Contact, Serializable {

    @XmlAttribute
    private int id;

    private String name;
    private String notes;

    //for XML marshalling purpose
    private ContactImpl() {}

    public ContactImpl(int id, String name, String notes) {
        this.id = id;
        this.name = name;
        this.notes = (notes == null) ? "" : notes.trim();
    }

    /**
     * Returns the ID of the contact.
     *
     * @return the ID of the contact.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the contact.
     *
     * @return the name of the contact.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns our notes about the contact, if any.
     * <p>
     * If we have not written anything about the contact, the empty
     * string is returned.
     *
     * @return a string with notes about the contact, maybe empty.
     */
    @Override
    public String getNotes() {
        return notes;
    }

    /**
     * Add notes about the contact.
     * If notes are not empty, notes will be added to the existing ones after
     * a space. Leading and trailing white spaces are trimmed.
     *
     * @param note the notes to be added
     */
    @Override
    public void addNotes(String note) {
        note = note.trim();
        notes = (notes.equals("")) ? note : notes + " " + note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactImpl contact = (ContactImpl) o;
        return id == contact.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ContactImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
