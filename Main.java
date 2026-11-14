// Terminal user interface handling menu navigation, input validation, and user interactions.
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AttendanceManager manager = new AttendanceManager();

    public static void main(String[] args) {
        System.out.println("Welcome to the Attendance Monitoring System!");
        System.out.println("[Info] Loaded 3 sample students and 6 sample attendance records for testing.");

        boolean isRunning = true;
        while (isRunning) {
            displayMainMenu();
            int choice = readInt("Enter your choice: ");
            System.out.println();

            switch (choice) {
                case 1:
                    handleRegisterStudent();
                    break;
                case 2:
                    handleViewStudents();
                    break;
                case 3:
                    handleRecordSingleDailyAttendance();
                    break;
                case 4:
                    handleBatchDailyAttendance();
                    break;
                case 5:
                    handleRecordEventAttendance();
                    break;
                case 6:
                    handleDisplayAllRecords();
                    break;
                case 7:
                    handleSearchStudent();
                    break;
                case 8:
                    handleFilterRecordsByStatus();
                    break;
                case 9:
                    handleCalculateSummaries();
                    break;
                case 10:
                    handleUpdateOrDeleteMenu();
                    break;
                case 0:
                    isRunning = false;
                    System.out.println("Thank you for using the Attendance Monitoring System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid menu choice. Please select between 0 and 10.");
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("==================================================");
        System.out.println("     ATTENDANCE MONITORING SYSTEM (JAVA OOP)");
        System.out.println("==================================================");
        System.out.println("[1] Register New Student");
        System.out.println("[2] View All Registered Students");
        System.out.println("[3] Record Daily Attendance (Single Student)");
        System.out.println("[4] Record Daily Attendance (Whole Class Roll Call)");
        System.out.println("[5] Record Event Attendance");
        System.out.println("[6] Display All Attendance Records");
        System.out.println("[7] Search Student & View Records");
        System.out.println("[8] Filter Student Records by Status");
        System.out.println("[9] Calculate Attendance Summaries");
        System.out.println("[10] Update or Delete Records");
        System.out.println("[0] Exit System");
        System.out.println("==================================================");
    }

    private static void handleRegisterStudent() {
        System.out.println("--- REGISTER NEW STUDENT ---");
        String studentId = readNonEmptyString("Enter Student ID (e.g., 2026-004): ");

        if (manager.findStudentById(studentId) != null) {
            System.out.println("Registration failed: A student with ID '" + studentId + "' already exists.");
            return;
        }

        String fullName = readNonEmptyString("Enter Full Name: ");
        int age = readValidAge("Enter Age: ");
        String courseSection = readNonEmptyString("Enter Course/Section (e.g., Grade 11 - ICT A): ");

        try {
            Student newStudent = new Student(studentId, fullName, age, courseSection);
            if (manager.registerStudent(newStudent)) {
                System.out.println("\nStudent successfully registered!");
                newStudent.displayProfile();
            } else {
                System.out.println("Registration failed.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    private static void handleViewStudents() {
        System.out.println("--- REGISTERED STUDENTS ---");
        ArrayList<Student> studentList = manager.getAllStudents();
        if (studentList.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        for (int i = 0; i < studentList.size(); i++) {
            System.out.println("Student #" + (i + 1));
            studentList.get(i).displayProfile();
            System.out.println("---------------------------");
        }
    }

    private static void handleRecordSingleDailyAttendance() {
        System.out.println("--- RECORD DAILY ATTENDANCE ---");
        if (manager.getAllStudents().isEmpty()) {
            System.out.println("No students registered. Please register a student first.");
            return;
        }

        displayQuickStudentList();
        Student selectedStudent = promptExistingStudent();
        if (selectedStudent == null) {
            return;
        }

        String date = readNonEmptyString("Enter Date (e.g., November 14, 2026): ");
        String subject = readNonEmptyString("Enter Subject Name: ");
        String status = readValidStatus("Enter Status [PRESENT (P) / ABSENT (A) / LATE (L)]: ");
        String remarks = readOptionalString("Enter Remarks (optional, press Enter to skip): ");

        try {
            AttendanceRecord record = new DailyAttendance(selectedStudent, date, status, subject, remarks);
            manager.addAttendanceRecord(record);
            System.out.println("\nDaily attendance recorded successfully!");
            record.displayRecord();
        } catch (IllegalArgumentException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    private static void handleBatchDailyAttendance() {
        System.out.println("--- WHOLE CLASS ROLL CALL (DAILY ATTENDANCE) ---");
        ArrayList<Student> studentList = manager.getAllStudents();
        if (studentList.isEmpty()) {
            System.out.println("No students registered. Please register a student first.");
            return;
        }

        String date = readNonEmptyString("Enter Class Date (e.g., November 14, 2026): ");
        String subject = readNonEmptyString("Enter Subject Name: ");
        System.out.println("Mark each student as P (Present), A (Absent), or L (Late).\n");

        int recordedCount = 0;
        for (int i = 0; i < studentList.size(); i++) {
            Student student = studentList.get(i);
            System.out.println("[" + (i + 1) + "/" + studentList.size() + "] "
                    + student.getFullName() + " (" + student.getId() + ")");
            String status = readValidStatus("  Status [P/A/L]: ");
            String remarks = readOptionalString("  Remarks (optional): ");

            AttendanceRecord record = new DailyAttendance(student, date, status, subject, remarks);
            manager.addAttendanceRecord(record);
            recordedCount++;
        }

        System.out.println("\nRoll call complete! Recorded daily attendance for " + recordedCount + " student(s).");
    }

    private static void handleRecordEventAttendance() {
        System.out.println("--- RECORD EVENT ATTENDANCE ---");
        if (manager.getAllStudents().isEmpty()) {
            System.out.println("No students registered. Please register a student first.");
            return;
        }

        displayQuickStudentList();
        Student selectedStudent = promptExistingStudent();
        if (selectedStudent == null) {
            return;
        }

        String date = readNonEmptyString("Enter Event Date (e.g., November 14, 2026): ");
        String eventName = readNonEmptyString("Enter Event Name: ");
        String venue = readNonEmptyString("Enter Venue: ");
        String status = readValidStatus("Enter Status [PRESENT (P) / ABSENT (A) / LATE (L)]: ");
        String remarks = readOptionalString("Enter Remarks (optional, press Enter to skip): ");

        try {
            AttendanceRecord record = new EventAttendance(selectedStudent, date, status, eventName, venue, remarks);
            manager.addAttendanceRecord(record);
            System.out.println("\nEvent attendance recorded successfully!");
            record.displayRecord();
        } catch (IllegalArgumentException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    private static void handleDisplayAllRecords() {
        System.out.println("--- ALL ATTENDANCE RECORDS ---");
        manager.displayRecords();
    }

    private static void handleSearchStudent() {
        System.out.println("--- SEARCH FOR A STUDENT ---");
        String keyword = readNonEmptyString("Enter Student ID, Name, or Class to search: ");
        ArrayList<Student> matches = manager.searchStudents(keyword);

        if (matches.isEmpty()) {
            System.out.println("No student matched '" + keyword + "'.");
            return;
        }

        System.out.println("Found " + matches.size() + " matching student(s):\n");
        for (Student student : matches) {
            manager.displayStudentSummary(student);
            System.out.println("\nDetailed Records for " + student.getFullName() + ":");
            manager.displayRecords(student.getId());
        }
    }

    private static void handleFilterRecordsByStatus() {
        System.out.println("--- FILTER STUDENT ATTENDANCE BY STATUS ---");
        if (manager.getAllStudents().isEmpty()) {
            System.out.println("No students registered.");
            return;
        }

        displayQuickStudentList();
        Student selectedStudent = promptExistingStudent();
        if (selectedStudent == null) {
            return;
        }

        String statusFilter = readValidStatus("Enter Status to Filter [PRESENT (P) / ABSENT (A) / LATE (L)]: ");
        System.out.println();
        manager.displayRecords(selectedStudent.getId(), statusFilter);
    }

    private static void handleCalculateSummaries() {
        System.out.println("--- ATTENDANCE SUMMARIES ---");
        manager.displayAllClassSummaries();

        if (!manager.getAllStudents().isEmpty()) {
            String studentId = readOptionalString("\nEnter Student ID for detailed card (or press Enter to go back): ");
            if (!studentId.isEmpty()) {
                Student student = manager.findStudentById(studentId);
                if (student != null) {
                    System.out.println();
                    manager.displayStudentSummary(student);
                } else {
                    System.out.println("Student ID '" + studentId + "' not found.");
                }
            }
        }
    }

    private static void handleUpdateOrDeleteMenu() {
        boolean inSubMenu = true;
        while (inSubMenu) {
            System.out.println("--- UPDATE OR DELETE RECORDS ---");
            System.out.println("[1] Update Student Information");
            System.out.println("[2] Update Attendance Record Status");
            System.out.println("[3] Delete Student");
            System.out.println("[4] Delete Attendance Record");
            System.out.println("[0] Back to Main Menu");

            int subChoice = readInt("Select an option: ");
            System.out.println();

            switch (subChoice) {
                case 1:
                    handleUpdateStudent();
                    break;
                case 2:
                    handleUpdateAttendanceRecord();
                    break;
                case 3:
                    handleDeleteStudent();
                    break;
                case 4:
                    handleDeleteAttendanceRecord();
                    break;
                case 0:
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select between 0 and 4.");
            }
            System.out.println();
        }
    }

    private static void handleUpdateStudent() {
        if (manager.getAllStudents().isEmpty()) {
            System.out.println("No students available to update.");
            return;
        }
        displayQuickStudentList();
        Student student = promptExistingStudent();
        if (student == null) {
            return;
        }

        String newName = readOptionalString("New Full Name (Enter to keep \"" + student.getFullName() + "\"): ");
        if (newName.isEmpty()) {
            newName = student.getFullName();
        }

        int newAge = readUpdatedAge("New Age (enter 0 to keep " + student.getAge() + "): ", student.getAge());

        String newSection = readOptionalString("New Class/Section (Enter to keep \"" + student.getCourseSection() + "\"): ");
        if (newSection.isEmpty()) {
            newSection = student.getCourseSection();
        }

        try {
            if (manager.updateStudent(student.getId(), newName, newAge, newSection)) {
                System.out.println("Student updated successfully!");
                student.displayProfile();
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    private static void handleUpdateAttendanceRecord() {
        ArrayList<AttendanceRecord> recordList = manager.getAllRecords();
        if (recordList.isEmpty()) {
            System.out.println("No attendance records available to update.");
            return;
        }

        manager.displayRecords();
        int recordNumber = readInt("Enter Record # to update (0 to cancel): ");
        if (recordNumber == 0) {
            return;
        }

        int index = recordNumber - 1;
        if (index < 0 || index >= recordList.size()) {
            System.out.println("Invalid record number.");
            return;
        }

        String newStatus = readValidStatus("Enter New Status [PRESENT (P) / ABSENT (A) / LATE (L)]: ");
        String newRemarks = readOptionalString("Enter New Remarks (press Enter for None): ");

        try {
            if (manager.updateAttendanceRecord(index, newStatus, newRemarks)) {
                System.out.println("Attendance record updated!");
                recordList.get(index).displayRecord();
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    private static void handleDeleteStudent() {
        if (manager.getAllStudents().isEmpty()) {
            System.out.println("No students available to delete.");
            return;
        }

        displayQuickStudentList();
        Student student = promptExistingStudent();
        if (student == null) {
            return;
        }

        String confirmation = readNonEmptyString("Are you sure you want to delete "
                + student.getFullName() + " and all their records? (Y/N): ");
        if (confirmation.equalsIgnoreCase("Y") || confirmation.equalsIgnoreCase("YES")) {
            if (manager.deleteStudent(student.getId())) {
                System.out.println("Student and associated attendance records deleted.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void handleDeleteAttendanceRecord() {
        ArrayList<AttendanceRecord> recordList = manager.getAllRecords();
        if (recordList.isEmpty()) {
            System.out.println("No attendance records available to delete.");
            return;
        }

        manager.displayRecords();
        int recordNumber = readInt("Enter Record # to delete (0 to cancel): ");
        if (recordNumber == 0) {
            return;
        }

        if (manager.deleteAttendanceRecord(recordNumber - 1)) {
            System.out.println("Attendance record deleted successfully.");
        } else {
            System.out.println("Invalid record number.");
        }
    }

    private static void displayQuickStudentList() {
        System.out.println("Registered Students:");
        for (Student student : manager.getAllStudents()) {
            System.out.println("  - " + student.getId() + " : " + student.getFullName()
                    + " (" + student.getCourseSection() + ")");
        }
    }

    private static Student promptExistingStudent() {
        while (true) {
            String studentId = readNonEmptyString("Enter Student ID (or 0 to cancel): ");
            if (studentId.equals("0")) {
                return null;
            }
            Student student = manager.findStudentById(studentId);
            if (student != null) {
                return student;
            }
            System.out.println("Student ID '" + studentId + "' not found. Please try again.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    private static int readValidAge(String prompt) {
        while (true) {
            int age = readInt(prompt);
            if (age > 0 && age <= 120) {
                return age;
            }
            System.out.println("Invalid age! Age must be between 1 and 120.");
        }
    }

    private static int readUpdatedAge(String prompt, int currentAge) {
        while (true) {
            int age = readInt(prompt);
            if (age == 0) {
                return currentAge;
            }
            if (age > 0 && age <= 120) {
                return age;
            }
            System.out.println("Invalid age! Enter 0 to keep current age, or a value from 1 to 120.");
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private static String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static String readValidStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return AttendanceRecord.normalizeStatus(input);
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
