// Child class extending Person, storing student-specific details like age and class section.
public class Student extends Person {
    private int age;
    private String courseSection;

    public Student(String id, String fullName, int age, String courseSection) {
        super(id, fullName);
        setAge(age);
        setCourseSection(courseSection);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age <= 0 || age > 120) {
            throw new IllegalArgumentException("Invalid age! Age must be between 1 and 120.");
        }
        this.age = age;
    }

    public String getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(String courseSection) {
        if (courseSection == null || courseSection.trim().isEmpty()) {
            throw new IllegalArgumentException("Course/Section cannot be empty.");
        }
        this.courseSection = courseSection.trim();
    }

    @Override
    public void displayProfile() {
        super.displayProfile();
        System.out.println("Age  : " + age);
        System.out.println("Class: " + courseSection);
    }
}
