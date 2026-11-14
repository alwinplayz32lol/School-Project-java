// Base class storing basic personal details and demonstrating encapsulation.
public class Person {
    private String id;
    private String fullName;

    public Person(String id, String fullName) {
        setId(id);
        setFullName(fullName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        this.id = id.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        this.fullName = fullName.trim();
    }

    public void displayProfile() {
        System.out.println("ID   : " + id);
        System.out.println("Name : " + fullName);
    }
}
