// Subclass of AttendanceRecord, formatting and displaying daily subject class attendance.
public class DailyAttendance extends AttendanceRecord {
    private String subject;

    public DailyAttendance(Student student, String attendanceDate, String status, String subject, String remarks) {
        super(student, attendanceDate, status, remarks);
        setSubject(subject);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be empty.");
        }
        this.subject = subject.trim();
    }

    @Override
    public String getRecordType() {
        return "Daily Class Attendance";
    }

    @Override
    public void displayRecord() {
        System.out.println("===== DAILY ATTENDANCE RECORD =====");
        System.out.println("Type    : " + getRecordType());
        System.out.println("Subject : " + subject);
        displayCommonInfo();
        System.out.println("===================================");
    }
}
