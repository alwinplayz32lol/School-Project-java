// Subclass of AttendanceRecord, formatting and displaying school event attendance with venue details.
public class EventAttendance extends AttendanceRecord {
    private String eventName;
    private String venue;

    public EventAttendance(Student student, String attendanceDate, String status,
                           String eventName, String venue, String remarks) {
        super(student, attendanceDate, status, remarks);
        setEventName(eventName);
        setVenue(venue);
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        if (eventName == null || eventName.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty.");
        }
        this.eventName = eventName.trim();
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        if (venue == null || venue.trim().isEmpty()) {
            throw new IllegalArgumentException("Event venue cannot be empty.");
        }
        this.venue = venue.trim();
    }

    @Override
    public String getRecordType() {
        return "School Event Attendance";
    }

    @Override
    public void displayRecord() {
        System.out.println("===== EVENT ATTENDANCE RECORD =====");
        System.out.println("Type    : " + getRecordType());
        System.out.println("Event   : " + eventName);
        System.out.println("Venue   : " + venue);
        displayCommonInfo();
        System.out.println("===================================");
    }
}
