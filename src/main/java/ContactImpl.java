import interfaces.Contact;

/**
 * Created by Workstation on 19/01/16.
 */
public class ContactImpl implements Contact {
    private int id;
    private String name;
    private String notes;

    public ContactImpl(int id, String name, String notes) {
        this.id = id;
        this.name = name;
        this.notes = (notes == null) ? "" : notes;
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
     * a space.
     *
     * @param note the notes to be added
     */
    @Override
    public void addNotes(String note) {

    }
}
