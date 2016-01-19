import interfaces.Contact;

/**
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class ContactImpl implements Contact {
    private int id;
    private String name;
    private String notes;

    /**
     *
     * @param id unique ID of the contact
     * @param name of the contact
     * @param notes about the contact
     */
    public ContactImpl(int id, String name, String notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return null;
    }

    public String getNotes() {
        return null;
    }

    public void addNotes(String note) {

    }

}
