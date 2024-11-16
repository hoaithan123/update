package database;

public class Student {
    private int studentId;
    private String studentCode;
    private String studentName;
    private String studentEmail;
    private int attendanceCount;
    private String address;
    private String phoneNumber;

    public Student(int studentId, String studentCode, String studentName, String studentEmail,
                   int attendanceCount, String address, String phoneNumber) {
        this.studentId = studentId;
        this.studentCode = studentCode;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.attendanceCount = attendanceCount;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters và Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public int getAttendanceCount() { return attendanceCount; }
    public void setAttendanceCount(int attendanceCount) { this.attendanceCount = attendanceCount; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return "MSSV: " + studentCode + ", Tên: " + studentName + ", Email: " + studentEmail +
                ", Số lần có mặt: " + attendanceCount + ", Địa chỉ: " + address +
                ", Số điện thoại: " + phoneNumber;
    }
}
