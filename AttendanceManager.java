// Core controller managing students, CRUD operations, overloaded record displays, and attendance summaries.
import java.util.ArrayList;

public class AttendanceManager {
    public static final int ABSENCE_WARNING_THRESHOLD = 3;

    private final ArrayList<Student> students;
    private final ArrayList<AttendanceRecord> records;

    public AttendanceManager() {
        this.students = new ArrayList<>();
        this.records = new ArrayList<>();
        loadSampleData();
    }

    private void loadSampleData() {
        Student student1 = new Student("2026-001", "Maria Santos", 17, "Grade 11 - ICT A");
        Student student2 = new Student("2026-002", "Juan Dela Cruz", 17, "Grade 11 - ICT A");
        Student student3 = new Student("2026-003", "Ana Reyes", 16, "Grade 11 - ICT A");

        students.add(student1);
        students.add(student2);
        students.add(student3);

        records.add(new DailyAttendance(
                student1, "November 12, 2026", AttendanceRecord.STATUS_PRESENT,
                "OOP Java Programming", "Active in laboratory"
        ));
        records.add(new DailyAttendance(
                student1, "November 13, 2026", AttendanceRecord.STATUS_LATE,
                "OOP Java Programming", "Arrived 10 minutes late"
        ));
        records.add(new EventAttendance(
                student1, "November 14, 2026", AttendanceRecord.STATUS_PRESENT,
                "ICT Coding Seminar", "School AVR", "Completed workshop"
        ));
        records.add(new DailyAttendance(
                student2, "November 12, 2026", AttendanceRecord.STATUS_ABSENT,
                "OOP Java Programming", "Sick leave"
        ));
        records.add(new DailyAttendance(
                student2, "November 13, 2026", AttendanceRecord.STATUS_PRESENT,
                "OOP Java Programming", "None"
        ));
        records.add(new DailyAttendance(
                student3, "November 14, 2026", AttendanceRecord.STATUS_PRESENT,
                "OOP Java Programming", "None"
        ));
    }

    public boolean registerStudent(Student student) {
        if (student == null || findStudentById(student.getId()) != null) {
            return false;
        }
        students.add(student);
        return true;
    }

    public Student findStudentById(String studentId) {
        if (studentId == null) {
            return null;
        }
        String targetId = studentId.trim();
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(targetId)) {
                return student;
            }
        }
        return null;
    }

    public ArrayList<Student> searchStudents(String keyword) {
        ArrayList<Student> matchedStudents = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return matchedStudents;
        }

        String lowerKeyword = keyword.trim().toLowerCase();
        for (Student student : students) {
            if (student.getId().toLowerCase().contains(lowerKeyword)
                    || student.getFullName().toLowerCase().contains(lowerKeyword)
                    || student.getCourseSection().toLowerCase().contains(lowerKeyword)) {
                matchedStudents.add(student);
            }
        }
        return matchedStudents;
    }

    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public boolean updateStudent(String studentId, String newFullName, int newAge, String newCourseSection) {
        Student student = findStudentById(studentId);
        if (student == null) {
            return false;
        }
        student.setFullName(newFullName);
        student.setAge(newAge);
        student.setCourseSection(newCourseSection);
        return true;
    }

    public boolean deleteStudent(String studentId) {
        Student student = findStudentById(studentId);
        if (student == null) {
            return false;
        }
        students.remove(student);

        for (int i = records.size() - 1; i >= 0; i--) {
            if (records.get(i).getStudent().getId().equalsIgnoreCase(student.getId())) {
                records.remove(i);
            }
        }
        return true;
    }

    public void addAttendanceRecord(AttendanceRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Attendance record cannot be null.");
        }
        records.add(record);
    }

    public ArrayList<AttendanceRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    public boolean updateAttendanceRecord(int index, String newStatus, String newRemarks) {
        if (index < 0 || index >= records.size()) {
            return false;
        }
        AttendanceRecord record = records.get(index);
        record.setStatus(newStatus);
        record.setRemarks(newRemarks);
        return true;
    }

    public boolean deleteAttendanceRecord(int index) {
        if (index < 0 || index >= records.size()) {
            return false;
        }
        records.remove(index);
        return true;
    }

    // Overloading #1: Display all attendance records
    public void displayRecords() {
        if (records.isEmpty()) {
            System.out.println("No attendance records found.");
            return;
        }
        for (int i = 0; i < records.size(); i++) {
            System.out.println("[Record #" + (i + 1) + "]");
            records.get(i).displayRecord();
            System.out.println();
        }
    }

    // Overloading #2: Display attendance records for a specific student
    public void displayRecords(String studentId) {
        boolean hasRecord = false;
        for (int i = 0; i < records.size(); i++) {
            AttendanceRecord record = records.get(i);
            if (record.getStudent().getId().equalsIgnoreCase(studentId.trim())) {
                System.out.println("[Record #" + (i + 1) + "]");
                record.displayRecord();
                System.out.println();
                hasRecord = true;
            }
        }
        if (!hasRecord) {
            System.out.println("No attendance records found for Student ID: " + studentId);
        }
    }

    // Overloading #3: Display attendance records for a specific student filtered by status
    public void displayRecords(String studentId, String statusFilter) {
        String normalizedFilter = AttendanceRecord.normalizeStatus(statusFilter);
        boolean hasRecord = false;

        for (int i = 0; i < records.size(); i++) {
            AttendanceRecord record = records.get(i);
            if (record.getStudent().getId().equalsIgnoreCase(studentId.trim())
                    && record.getStatus().equals(normalizedFilter)) {
                System.out.println("[Record #" + (i + 1) + "]");
                record.displayRecord();
                System.out.println();
                hasRecord = true;
            }
        }
        if (!hasRecord) {
            System.out.println("No " + normalizedFilter + " records found for Student ID: " + studentId);
        }
    }

    public int countStudentStatus(String studentId, String targetStatus) {
        int count = 0;
        for (AttendanceRecord record : records) {
            if (record.getStudent().getId().equalsIgnoreCase(studentId.trim())
                    && record.getStatus().equalsIgnoreCase(targetStatus)) {
                count++;
            }
        }
        return count;
    }

    public void displayStudentSummary(Student student) {
        int presentCount = countStudentStatus(student.getId(), AttendanceRecord.STATUS_PRESENT);
        int absentCount = countStudentStatus(student.getId(), AttendanceRecord.STATUS_ABSENT);
        int lateCount = countStudentStatus(student.getId(), AttendanceRecord.STATUS_LATE);
        int totalCount = presentCount + absentCount + lateCount;

        String latestDate = "N/A";
        String latestStatus = "N/A";
        for (int i = records.size() - 1; i >= 0; i--) {
            AttendanceRecord record = records.get(i);
            if (record.getStudent().getId().equalsIgnoreCase(student.getId())) {
                latestDate = record.getAttendanceDate();
                latestStatus = record.getStatus();
                break;
            }
        }

        double attendanceRate = (totalCount == 0)
                ? 0.0
                : ((presentCount + lateCount) * 100.0) / totalCount;

        String standing = (absentCount >= ABSENCE_WARNING_THRESHOLD)
                ? "WARNING (" + absentCount + " absences)"
                : "Good Standing";

        System.out.println("===== ATTENDANCE RECORD =====");
        System.out.println("Student: " + student.getFullName() + " (" + student.getId() + ")");
        System.out.println("Class  : " + student.getCourseSection());
        System.out.println("Date   : " + latestDate);
        System.out.println("Status : " + latestStatus);
        System.out.println();
        System.out.println("Total Present: " + presentCount);
        System.out.println("Total Absent : " + absentCount);
        System.out.println("Total Late   : " + lateCount);
        System.out.println("Total Records: " + totalCount);
        System.out.printf("Attendance %% : %.1f%%%n", attendanceRate);
        System.out.println("Standing     : " + standing);
        System.out.println("=============================");
    }

    public void displayAllClassSummaries() {
        if (students.isEmpty()) {
            System.out.println("No students registered in the system.");
            return;
        }

        int overallPresent = 0;
        int overallAbsent = 0;
        int overallLate = 0;

        System.out.println("====================================================================================");
        System.out.printf("%-10s %-22s %-8s %-8s %-8s %-8s %-8s %-12s%n",
                "ID", "Student Name", "Present", "Absent", "Late", "Total", "Rate", "Standing");
        System.out.println("====================================================================================");

        for (Student student : students) {
            int present = countStudentStatus(student.getId(), AttendanceRecord.STATUS_PRESENT);
            int absent = countStudentStatus(student.getId(), AttendanceRecord.STATUS_ABSENT);
            int late = countStudentStatus(student.getId(), AttendanceRecord.STATUS_LATE);
            int total = present + absent + late;

            overallPresent += present;
            overallAbsent += absent;
            overallLate += late;

            double rate = (total == 0) ? 0.0 : ((present + late) * 100.0) / total;
            String rateFormatted = String.format("%.1f%%", rate);
            String standing = (absent >= ABSENCE_WARNING_THRESHOLD) ? "WARNING" : "Good";

            System.out.printf("%-10s %-22s %-8d %-8d %-8d %-8d %-8s %-12s%n",
                    truncateText(student.getId(), 10),
                    truncateText(student.getFullName(), 22),
                    present,
                    absent,
                    late,
                    total,
                    rateFormatted,
                    standing);
        }

        int overallTotal = overallPresent + overallAbsent + overallLate;
        double overallRate = (overallTotal == 0) ? 0.0 : ((overallPresent + overallLate) * 100.0) / overallTotal;

        System.out.println("====================================================================================");
        System.out.println("CLASS TOTALS -> Present: " + overallPresent
                + " | Absent: " + overallAbsent
                + " | Late: " + overallLate
                + " | Total Records: " + overallTotal
                + String.format(" | Overall Rate: %.1f%%", overallRate));
    }

    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
