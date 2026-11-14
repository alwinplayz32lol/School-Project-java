// Abstract blueprint defining shared attendance fields, status validation, and display structure.
public abstract class AttendanceRecord {
    public static final String STATUS_PRESENT = "PRESENT";
    public static final String STATUS_ABSENT = "ABSENT";
    public static final String STATUS_LATE = "LATE";

    private Student student;
    private String attendanceDate;
    private String status;
    private String remarks;

    public AttendanceRecord(Student student, String attendanceDate, String status, String remarks) {
        setStudent(student);
        setAttendanceDate(attendanceDate);
        setStatus(status);
        setRemarks(remarks);
    }

    public static String normalizeStatus(String statusInput) {
        if (statusInput == null || statusInput.trim().isEmpty()) {
            throw new IllegalArgumentException("Attendance status cannot be empty.");
        }

        String normalized = statusInput.trim().toUpperCase();
        if (normalized.equals("P") || normalized.equals(STATUS_PRESENT)) {
            return STATUS_PRESENT;
        } else if (normalized.equals("A") || normalized.equals(STATUS_ABSENT)) {
            return STATUS_ABSENT;
        } else if (normalized.equals("L") || normalized.equals(STATUS_LATE)) {
            return STATUS_LATE;
        } else {
            throw new IllegalArgumentException(
                "Invalid status! Please enter PRESENT (P), ABSENT (A), or LATE (L)."
            );
        }
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }
        this.student = student;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        if (attendanceDate == null || attendanceDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Attendance date cannot be empty.");
        }
        this.attendanceDate = attendanceDate.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = normalizeStatus(status);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        if (remarks == null || remarks.trim().isEmpty()) {
            this.remarks = "None";
        } else {
            this.remarks = remarks.trim();
        }
    }

    protected void displayCommonInfo() {
        System.out.println("Student : " + student.getFullName() + " (" + student.getId() + ")");
        System.out.println("Class   : " + student.getCourseSection());
        System.out.println("Date    : " + attendanceDate);
        System.out.println("Status  : " + status);
        System.out.println("Remarks : " + remarks);
    }

    public abstract String getRecordType();

    public abstract void displayRecord();
}
