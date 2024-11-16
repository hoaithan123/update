package database;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        StudentDAO studentDAO = new StudentDAO();
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        ClassDAO classDAO = new ClassDAO();
        SubjectDAO subjectDAO = new SubjectDAO();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

        System.out.println("===== HỆ THỐNG QUẢN LÝ ĐIỂM DANH =====");
        System.out.print("Nhập tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine();

        // Kiểm tra đăng nhập
        if (!userDAO.loginUser(username, password)) {
            System.out.println("Tên đăng nhập hoặc mật khẩu không đúng.");
            return;
        }

        // Lấy vai trò người dùng
        String role = userDAO.getUserRole(username);
        System.out.println("Đăng nhập thành công với vai trò: " + role);

        boolean isTeacher = "teacher".equalsIgnoreCase(role);

        while (true) {
            if (isTeacher) {
                System.out.println("\n===== MENU GIÁO VIÊN =====");
                System.out.println("1. Thêm sinh viên");
                System.out.println("2. Xem danh sách sinh viên");
                System.out.println("3. Tìm kiếm sinh viên");
                System.out.println("4. Điểm danh theo lớp và môn học");
                System.out.println("5. Xem lịch sử điểm danh của sinh viên");
                System.out.println("6. Xem lịch sử điểm danh của lớp và môn học");
                System.out.println("7. Cập nhật thông tin sinh viên");
                System.out.println("8. Xóa sinh viên");
                System.out.println("9. Đổi mật khẩu");
                System.out.println("10. Thêm lớp học");
                System.out.println("11. Thêm môn học");
                System.out.println("12. Thoát");

                System.out.print("Chọn tùy chọn: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        // Thêm sinh viên
                        System.out.print("Nhập mã số sinh viên: ");
                        String studentCode = scanner.nextLine();
                        System.out.print("Nhập tên sinh viên: ");
                        String studentName = scanner.nextLine();
                        System.out.print("Nhập email: ");
                        String email = scanner.nextLine();
                        System.out.print("Nhập địa chỉ: ");
                        String address = scanner.nextLine();
                        System.out.print("Nhập số điện thoại: ");
                        String phone = scanner.nextLine();

                        if (userDAO.registerUser(studentCode, phone, "student")) {
                            Student student = new Student(0, studentCode, studentName, email, 0, address, phone);
                            studentDAO.addStudent(student);
                            System.out.println("Thêm sinh viên thành công!");
                        }
                        break;

                    case 2:
                        // Xem danh sách sinh viên
                        List<Student> students = studentDAO.getAllStudents();
                        for (Student student : students) {
                            System.out.println(student);
                        }
                        break;

                    case 3:
                        // Tìm kiếm sinh viên
                        System.out.print("Nhập tên sinh viên cần tìm: ");
                        String searchName = scanner.nextLine();
                        List<Student> foundStudents = studentDAO.searchStudentByName(searchName);
                        for (Student student : foundStudents) {
                            System.out.println(student);
                        }
                        break;

                    case 4:
                        // Điểm danh
                        System.out.print("Nhập mã số sinh viên: ");
                        String attendanceCode = scanner.nextLine();
                        System.out.print("Nhập mã lớp: ");
                        int classId = scanner.nextInt();
                        System.out.print("Nhập mã môn học: ");
                        int subjectId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Trạng thái (Present/Absent): ");
                        String status = scanner.nextLine();
                        attendanceDAO.markAttendance(attendanceCode, classId, subjectId, status);
                        break;

                    case 5:
                        // Xem lịch sử điểm danh của sinh viên
                        System.out.print("Nhập mã số sinh viên: ");
                        String historyCode = scanner.nextLine();
                        List<String> history = attendanceDAO.getAttendanceHistoryByStudent(historyCode);
                        for (String record : history) {
                            System.out.println(record);
                        }
                        break;

                    case 6:
                        // Xem lịch sử điểm danh của lớp và môn học
                        System.out.print("Nhập mã lớp: ");
                        classId = scanner.nextInt();
                        System.out.print("Nhập mã môn học: ");
                        subjectId = scanner.nextInt();
                        List<String> classHistory = attendanceDAO.getAttendanceHistoryByClassAndSubject(classId, subjectId);
                        for (String record : classHistory) {
                            System.out.println(record);
                        }
                        break;

                    case 7:
                        // Cập nhật thông tin sinh viên
                        System.out.print("Nhập mã số sinh viên: ");
                        String updateCode = scanner.nextLine();
                        System.out.print("Nhập tên mới: ");
                        String newName = scanner.nextLine();
                        System.out.print("Nhập email mới: ");
                        String newEmail = scanner.nextLine();
                        System.out.print("Nhập địa chỉ mới: ");
                        String newAddress = scanner.nextLine();
                        System.out.print("Nhập số điện thoại mới: ");
                        String newPhone = scanner.nextLine();
                        studentDAO.updateStudentInfo(new Student(0, updateCode, newName, newEmail, 0, newAddress, newPhone));
                        break;

                    case 8:
                        // Xóa sinh viên
                        System.out.print("Nhập mã số sinh viên cần xóa: ");
                        String deleteCode = scanner.nextLine();
                        studentDAO.deleteStudent(deleteCode);
                        break;

                    case 9:
                        // Đổi mật khẩu
                        System.out.print("Nhập mật khẩu cũ: ");
                        String oldPassword = scanner.nextLine();
                        System.out.print("Nhập mật khẩu mới: ");
                        String newPassword = scanner.nextLine();
                        userDAO.changePassword(username, oldPassword, newPassword);
                        break;

                    case 10:
                        // Thêm lớp học
                        System.out.print("Nhập tên lớp học: ");
                        String className = scanner.nextLine();
                        System.out.print("Nhập mã giáo viên: ");
                        int teacherId = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        if (classDAO.addClass(className, teacherId)) {
                            System.out.println("Thêm lớp học thành công!");
                        } else {
                            System.out.println("Thêm lớp học thất bại!");
                        }
                        break;

                    case 11:
                        // Thêm môn học
                        System.out.print("Nhập tên môn học: ");
                        String subjectName = scanner.nextLine();
                        System.out.print("Nhập mã lớp: ");
                        int classIdForSubject = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        if (subjectDAO.addSubject(subjectName, classIdForSubject)) {
                            System.out.println("Thêm môn học thành công!");
                        } else {
                            System.out.println("Thêm môn học thất bại!");
                        }
                        break;

                    case 12:
                        System.out.println("Thoát chương trình.");
                        return;

                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } else {
                System.out.println("\n===== MENU SINH VIÊN =====");
                System.out.println("1. Xem danh sách lớp học của bạn");
                System.out.println("2. Xem danh sách môn học của bạn");
                System.out.println("3. Xem lịch sử điểm danh theo lớp và môn học");
                System.out.println("4. Đổi mật khẩu");
                System.out.println("5. Thoát");

                System.out.print("Chọn tùy chọn: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        // Xem danh sách lớp học mà sinh viên đã đăng ký
                        List<String> classes = enrollmentDAO.getClassesByStudent(username);
                        if (classes.isEmpty()) {
                            System.out.println("Bạn chưa đăng ký lớp học nào.");
                        } else {
                            System.out.println("===== DANH SÁCH LỚP HỌC CỦA BẠN =====");
                            for (String classInfo : classes) {
                                System.out.println(classInfo);
                            }
                        }
                        break;
                    case 2:
                        // Xem danh sách môn học mà sinh viên đã đăng ký
                        List<String> subjects = enrollmentDAO.getSubjectsByStudent(username);
                        if (subjects.isEmpty()) {
                            System.out.println("Bạn chưa đăng ký môn học nào.");
                        } else {
                            System.out.println("===== DANH SÁCH MÔN HỌC CỦA BẠN =====");
                            for (String subjectInfo : subjects) {
                                System.out.println(subjectInfo);
                            }
                        }
                        break;
                    case 3:
                        // Xem lịch sử điểm danh theo lớp và môn học
                        System.out.print("Nhập mã lớp: ");
                        int classId = scanner.nextInt();
                        System.out.print("Nhập mã môn học: ");
                        int subjectId = scanner.nextInt();
                        scanner.nextLine();

                        List<String> attendanceHistory = attendanceDAO.getAttendanceHistoryByStudentAndSubject(username, classId, subjectId);
                        if (attendanceHistory.isEmpty()) {
                            System.out.println("Bạn chưa có buổi điểm danh nào cho lớp và môn học này.");
                        } else {
                            System.out.println("===== LỊCH SỬ ĐIỂM DANH CỦA BẠN =====");
                            for (String record : attendanceHistory) {
                                System.out.println(record);
                            }
                        }
                        break;
                    case 4:
                        // Đổi mật khẩu
                        System.out.print("Nhập mật khẩu cũ: ");
                        String oldPassword = scanner.nextLine();
                        System.out.print("Nhập mật khẩu mới: ");
                        String newPassword = scanner.nextLine();
                        if (userDAO.changePassword(username, oldPassword, newPassword)) {
                            System.out.println("Đổi mật khẩu thành công!");
                        } else {
                            System.out.println("Đổi mật khẩu thất bại. Vui lòng thử lại.");
                        }
                        break;


                    case 5:
                        // Thoát chương trình
                        System.out.println("Thoát chương trình.");
                        return;

                    default:
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                }
            }
        }
    }
}
