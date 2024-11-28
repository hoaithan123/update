    package database;
    import javax.swing.*;
    import javax.swing.table.DefaultTableModel;
    import javax.swing.border.*;
    import java.awt.*;
    import java.util.List;
    public class DashboardGUI extends JFrame {
        private String username;
        private String role;
        private JPanel contentPanel;
        private CardLayout cardLayout;
        private StudentDAO studentDAO = new StudentDAO();
        private AttendanceDAO attendanceDAO = new AttendanceDAO();
        private ClassDAO classDAO = new ClassDAO();
        private JTable studentTable;
        private DefaultTableModel studentTableModel;

        public DashboardGUI(String username, String role) {
            this.username = username;
            this.role = role;

            setTitle("Hệ thống quản lý điểm danh");
            setSize(800, 600);
            setMinimumSize(new Dimension(600, 400)); // Thiết lập kích thước tối thiểu
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            // Lấy tên sinh viên từ mã số sinh viên
            String studentName = studentDAO.getStudentNameByCode(username);

            // Nếu tên sinh viên không tìm thấy, hiển thị mã số sinh viên thay vì tên
            if (studentName == null) {
                studentName = username; // Dùng mã số sinh viên làm tên nếu không tìm thấy trong CSDL
            }

            // Header
            JLabel headerLabel = new JLabel("Chào mừng, " + studentName + " (" + role + ")", JLabel.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
            headerLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
            add(headerLabel, BorderLayout.NORTH);

            // Menu bên trái
            JPanel menuPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // Sử dụng GridLayout với hàng động
            menuPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            // Tạo các nút chức năng
            JButton viewStudentsButton = createStyledButton("Xem danh sách sinh viên");
            JButton viewClassesButton = createStyledButton("Xem Lớp Học");
            JButton viewSubjectsButton = createStyledButton("Xem Môn Học");
            JButton addStudentButton = createStyledButton("Thêm sinh viên");
            JButton deleteStudentButton = createStyledButton("Xóa sinh viên");
            JButton searchStudentButton = createStyledButton("Tìm kiếm sinh viên");
            JButton attendanceButton = createStyledButton("Điểm danh");
            JButton updateStudentButton = createStyledButton("Cập nhật thông tin");
            JButton changePasswordButton = createStyledButton("Đổi mật khẩu");
            JButton logoutButton = createStyledButton("Đăng xuất");

            // Thêm các nút vào menuPanel dựa trên vai trò
            if ("teacher".equalsIgnoreCase(role)) {
                menuPanel.add(viewStudentsButton);
                menuPanel.add(viewClassesButton);
                menuPanel.add(viewSubjectsButton);
                menuPanel.add(addStudentButton);
                menuPanel.add(deleteStudentButton);
                menuPanel.add(searchStudentButton);
                menuPanel.add(attendanceButton);
                menuPanel.add(updateStudentButton);
                menuPanel.add(changePasswordButton);
                menuPanel.add(logoutButton);
            } else if ("student".equalsIgnoreCase(role)) {
                menuPanel.add(viewStudentsButton);
                menuPanel.add(searchStudentButton);
                menuPanel.add(changePasswordButton);
                menuPanel.add(logoutButton);
            }

            add(menuPanel, BorderLayout.WEST);

            // Nội dung trung tâm với CardLayout
            cardLayout = new CardLayout();
            contentPanel = new JPanel(cardLayout);

            contentPanel.add(createViewStudentsPanel(), "viewStudents");
            contentPanel.add(createViewClassesPanel(), "viewClasses");
            contentPanel.add(createViewSubjectsPanel(), "viewSubjects");
            contentPanel.add(createAddStudentPanel(), "addStudent");
            contentPanel.add(createDeleteStudentPanel(), "deleteStudent");
            contentPanel.add(createSearchStudentPanel(), "searchStudent");
            contentPanel.add(createAttendancePanel(), "attendance");
            contentPanel.add(createUpdateStudentPanel(), "updateStudent");
            contentPanel.add(createChangePasswordPanel(), "changePassword");

            add(contentPanel, BorderLayout.CENTER);

            // Sự kiện cho các nút
            viewStudentsButton.addActionListener(e -> cardLayout.show(contentPanel, "viewStudents"));
            viewClassesButton.addActionListener(e -> cardLayout.show(contentPanel, "viewClasses"));
            viewSubjectsButton.addActionListener(e -> cardLayout.show(contentPanel, "viewSubjects"));
            addStudentButton.addActionListener(e -> cardLayout.show(contentPanel, "addStudent"));
            deleteStudentButton.addActionListener(e -> cardLayout.show(contentPanel, "deleteStudent"));
            searchStudentButton.addActionListener(e -> cardLayout.show(contentPanel, "searchStudent"));
            attendanceButton.addActionListener(e -> cardLayout.show(contentPanel, "attendance"));
            updateStudentButton.addActionListener(e -> cardLayout.show(contentPanel, "updateStudent"));
            changePasswordButton.addActionListener(e -> cardLayout.show(contentPanel, "changePassword"));
            logoutButton.addActionListener(e -> {
                new LoginGUI();
                dispose();
            });

            setVisible(true);
        }

        private JButton createStyledButton(String text) {
            JButton button = new JButton(text);
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            button.setBackground(new Color(0x007BFF));
            button.setForeground(Color.WHITE);
            button.setPreferredSize(new Dimension(200, 40));
            return button;
        }

        // Tạo bảng "Xem danh sách sinh viên"
        private JPanel createViewStudentsPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("Danh sách sinh viên", JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setBorder(new EmptyBorder(0, 0, 20, 0));
            panel.add(label, BorderLayout.NORTH);

            // Thêm cột "Điểm danh" vào bảng
            String[] columns = {"Mã sinh viên", "Tên", "Email", "SĐT", "Địa chỉ", "Điểm danh"};
            studentTableModel = new DefaultTableModel(columns, 0);
            studentTable = new JTable(studentTableModel);
            studentTable.setFont(new Font("Arial", Font.PLAIN, 14));
            studentTable.setRowHeight(25);
            studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Hàm cập nhật bảng
            updateStudentTable();

            // Thay 'table' bằng 'studentTable'
            JScrollPane scrollPane = new JScrollPane(studentTable);
            scrollPane.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Danh sách sinh viên",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)
            ));
            panel.add(scrollPane, BorderLayout.CENTER);
            return panel;
        }

        private void updateStudentTable() {
            studentTableModel.setRowCount(0); // Xóa các hàng cũ

            List<Student> students = studentDAO.getAllStudents();
            for (Student student : students) {
                int attendanceCount = student.getAttendanceCount();
                studentTableModel.addRow(new Object[]{
                        student.getStudentCode(),
                        student.getStudentName(),
                        student.getStudentEmail(),
                        student.getPhoneNumber(),
                        student.getAddress(),
                        attendanceCount
                });
            }
        }
        private JPanel createAddStudentPanel() {
            // Chỉ giáo viên mới được phép truy cập chức năng này
            if (!"teacher".equalsIgnoreCase(role)) {
                JPanel panel = new JPanel(new BorderLayout());
                JLabel label = new JLabel("Bạn không có quyền truy cập chức năng này.", JLabel.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 18));
                label.setForeground(Color.RED);
                panel.add(label, BorderLayout.CENTER);
                return panel;
            }

            // Tạo panel chính với BorderLayout
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel headerLabel = new JLabel("Thêm Sinh Viên", JLabel.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
            headerLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
            mainPanel.add(headerLabel, BorderLayout.NORTH);

            // Panel cho các trường nhập liệu
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;

            // Tạo các nhãn và trường nhập liệu
            JLabel codeLabel = new JLabel("Mã sinh viên:");
            JTextField codeField = new JTextField(20);

            JLabel nameLabel = new JLabel("Tên sinh viên:");
            JTextField nameField = new JTextField(20);

            JLabel emailLabel = new JLabel("Email:");
            JTextField emailField = new JTextField(20);

            JLabel phoneLabel = new JLabel("Số điện thoại:");
            JTextField phoneField = new JTextField(20);

            JLabel addressLabel = new JLabel("Địa chỉ:");
            JTextField addressField = new JTextField(20);

            // Tạo nút thêm sinh viên
            JButton addButton = new JButton("Thêm sinh viên");
            addButton.setFont(new Font("Arial", Font.BOLD, 18));
            addButton.setBackground(new Color(0x28A745));
            addButton.setForeground(Color.WHITE);
            addButton.setPreferredSize(new Dimension(200, 40));
            addButton.setFocusPainted(false);

            // Đặt các thành phần vào formPanel
            addFormRow(formPanel, gbc, codeLabel, codeField, 0);
            addFormRow(formPanel, gbc, nameLabel, nameField, 1);
            addFormRow(formPanel, gbc, emailLabel, emailField, 2);
            addFormRow(formPanel, gbc, phoneLabel, phoneField, 3);
            addFormRow(formPanel, gbc, addressLabel, addressField, 4);

            // Thêm nút vào panel
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            formPanel.add(addButton, gbc);

            mainPanel.add(formPanel, BorderLayout.CENTER);

            // Xử lý sự kiện khi nhấn nút "Thêm sinh viên"
            addButton.addActionListener(e -> {
                String code = codeField.getText().trim();
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();

                if (code.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Đăng ký tài khoản cho sinh viên
                // Tạo đối tượng UserDAO
                UserDAO userDAO = new UserDAO();

// Gọi phương thức registerUser thông qua đối tượng userDAO
                if (userDAO.registerUser(code, phone, "student")) {
                    // Tiến hành thêm sinh viên vào cơ sở dữ liệu sau khi đăng ký tài khoản thành công
                    Student student = new Student(0, code, name, email, 0, address, phone);
                    if (studentDAO.addStudent(student)) {
                        JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
                        clearAddStudentFields(codeField, nameField, emailField, phoneField, addressField);
                        updateStudentTable(); // Cập nhật lại bảng sau khi thêm
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm sinh viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Đăng ký tài khoản sinh viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

            });


            return mainPanel;
        }

        // Phương thức để xóa dữ liệu sau khi thêm sinh viên
        private void clearAddStudentFields(JTextField codeField, JTextField nameField, JTextField emailField,
                                           JTextField phoneField, JTextField addressField) {
            codeField.setText("");
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            addressField.setText("");
        }

        // Phương thức để thêm hàng vào form
        private void addFormRow(JPanel panel, GridBagConstraints gbc, JLabel label, JTextField textField, int row) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.anchor = GridBagConstraints.EAST;
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(textField, gbc);
        }
        // Tạo bảng "Xóa sinh viên"
        private JPanel createDeleteStudentPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel studentCodeLabel = new JLabel("Mã số sinh viên:");
            studentCodeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField studentCodeField = new JTextField(20);
            studentCodeField.setFont(new Font("Arial", Font.PLAIN, 16));

            JButton deleteButton = new JButton("Xóa sinh viên");
            deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
            deleteButton.setBackground(new Color(0xdc3545));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(studentCodeLabel, gbc);
            gbc.gridx = 1;
            panel.add(studentCodeField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(deleteButton, gbc);

            deleteButton.addActionListener(e -> {
                String studentCode = studentCodeField.getText().trim();

                if (studentCode.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập mã số sinh viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sinh viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Xóa sinh viên
                    if (studentDAO.deleteStudent(studentCode)) {
                        JOptionPane.showMessageDialog(this, "Xóa sinh viên thành công!");
                        studentCodeField.setText("");

                        // Cập nhật lại bảng sau khi xóa
                        updateStudentTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa sinh viên thất bại! Vui lòng kiểm tra lại mã số sinh viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            return panel;
        }
        private JPanel createViewClassesPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("Danh sách Lớp Học", JLabel.CENTER);
            panel.add(label, BorderLayout.NORTH);

            String[] columns = {"Mã Lớp", "Tên Lớp", "Giáo Viên"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable classTable = new JTable(model);

            List<ClassDAO.ClassModel> classes = classDAO.getAllClasses();
            for (ClassDAO.ClassModel cls : classes) {
                model.addRow(new Object[]{cls.getClassId(), cls.getClassName(), cls.getTeacherId()});
            }

            panel.add(new JScrollPane(classTable), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createViewSubjectsPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("Danh sách Môn Học", JLabel.CENTER);
            panel.add(label, BorderLayout.NORTH);

            String[] columns = {"Mã Môn", "Tên Môn", "Lớp"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable subjectTable = new JTable(model);
            // Trước khi gọi phương thức, tạo một đối tượng SubjectDAO
            SubjectDAO subjectDAO = new SubjectDAO();

            // Gọi phương thức getSubjectsByClass() từ đối tượng subjectDAO
            List<SubjectDAO.SubjectModel> subjects = subjectDAO.getSubjectsByClass(1);

            for (SubjectDAO.SubjectModel subject : subjects) {
                model.addRow(new Object[]{subject.getSubjectId(), subject.getSubjectName(), subject.getClassId()});
            }

            panel.add(new JScrollPane(subjectTable), BorderLayout.CENTER);
            return panel;
        }

        // Tạo bảng "Tìm kiếm sinh viên" đã chỉnh sửa
        private JPanel createSearchStudentPanel() {
            // Tạo panel chính với BorderLayout và thêm padding
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));

            // Tiêu đề của panel
            JLabel headerLabel = new JLabel("Tìm kiếm sinh viên", JLabel.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
            headerLabel.setBorder(new EmptyBorder(0, 0, 20, 0)); // Khoảng cách dưới tiêu đề
            panel.add(headerLabel, BorderLayout.NORTH);

            // Tạo một panel phụ để chứa các thành phần tìm kiếm với GridBagLayout
            JPanel searchPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các thành phần
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Nhãn cho ô tìm kiếm
            JLabel searchLabel = new JLabel("Tên sinh viên:");
            searchLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0;
            searchPanel.add(searchLabel, gbc);

            // Ô nhập liệu tìm kiếm
            JTextField searchField = new JTextField(20);
            searchField.setFont(new Font("Arial", Font.PLAIN, 16));
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1;
            searchPanel.add(searchField, gbc);

            // Nút tìm kiếm
            JButton searchButton = new JButton("Tìm kiếm");
            searchButton.setFont(new Font("Arial", Font.BOLD, 16));
            searchButton.setBackground(new Color(0x28a745));
            searchButton.setForeground(Color.WHITE);
            searchButton.setFocusPainted(false);
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.weightx = 0;
            searchPanel.add(searchButton, gbc);

            panel.add(searchPanel, BorderLayout.NORTH);

            // Tạo bảng để hiển thị kết quả tìm kiếm
            String[] columns = {"Mã sinh viên", "Tên sinh viên", "Email", "SĐT", "Địa chỉ"};
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
                // Ngăn chặn việc chỉnh sửa trực tiếp trong bảng
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable resultTable = new JTable(tableModel);
            resultTable.setFont(new Font("Arial", Font.PLAIN, 14));
            resultTable.setRowHeight(25);
            resultTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Thêm bảng vào JScrollPane
            JScrollPane scrollPane = new JScrollPane(resultTable);
            scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kết quả tìm kiếm",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)));
            panel.add(scrollPane, BorderLayout.CENTER);

            // Xử lý sự kiện khi bấm nút tìm kiếm
            searchButton.addActionListener(e -> {
                String query = searchField.getText().trim();
                if (query.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sinh viên để tìm kiếm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                List<Student> results = studentDAO.searchStudentByName(query);
                tableModel.setRowCount(0); // Xóa kết quả cũ

                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (Student student : results) {
                        tableModel.addRow(new Object[]{
                                student.getStudentCode(),
                                student.getStudentName(),
                                student.getStudentEmail(),
                                student.getPhoneNumber(),
                                student.getAddress()
                        });
                    }
                }
            });

            // Thêm tính năng tìm kiếm khi nhấn Enter trong ô nhập liệu
            searchField.addActionListener(e -> searchButton.doClick());

            return panel;
        }

        // Tạo bảng "Điểm danh"
        private JPanel createAttendancePanel() {
            // Chỉ giáo viên mới được phép truy cập chức năng này
            if (!"teacher".equalsIgnoreCase(role)) {
                JPanel panel = new JPanel(new BorderLayout());
                JLabel label = new JLabel("Bạn không có quyền truy cập chức năng này.", JLabel.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 18));
                label.setForeground(Color.RED);
                panel.add(label, BorderLayout.CENTER);
                return panel;
            }

            JPanel panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("Điểm danh sinh viên", JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setBorder(new EmptyBorder(0, 0, 20, 0));
            panel.add(label, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel studentCodeLabel = new JLabel("Mã sinh viên:");
            studentCodeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField studentCodeField = new JTextField(15);
            studentCodeField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel classIdLabel = new JLabel("Mã lớp:");
            classIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField classIdField = new JTextField(10);
            classIdField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel subjectIdLabel = new JLabel("Mã môn học:");
            subjectIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField subjectIdField = new JTextField(10);
            subjectIdField.setFont(new Font("Arial", Font.PLAIN, 16));

            JButton markButton = new JButton("Điểm danh");
            markButton.setFont(new Font("Arial", Font.BOLD, 16));
            markButton.setBackground(new Color(0x17a2b8));
            markButton.setForeground(Color.WHITE);
            markButton.setFocusPainted(false);

            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(studentCodeLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(studentCodeField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanel.add(classIdLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(classIdField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            formPanel.add(subjectIdLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(subjectIdField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            formPanel.add(markButton, gbc);

            markButton.addActionListener(e -> {
                String studentCode = studentCodeField.getText().trim();
                String classIdText = classIdField.getText().trim();
                String subjectIdText = subjectIdField.getText().trim();

                if (studentCode.isEmpty() || classIdText.isEmpty() || subjectIdText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin điểm danh.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int classId;
                int subjectId;
                try {
                    classId = Integer.parseInt(classIdText);
                    subjectId = Integer.parseInt(subjectIdText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Mã lớp và mã môn học phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = attendanceDAO.markAttendance(studentCode, classId, subjectId, "Present");
                if (success) {
                    JOptionPane.showMessageDialog(this, "Điểm danh thành công!");
                    studentCodeField.setText("");
                    classIdField.setText("");
                    subjectIdField.setText("");

                    // Gọi phương thức để cập nhật bảng sinh viên ngay sau khi điểm danh
                    updateStudentTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Điểm danh thất bại! Vui lòng kiểm tra lại thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            panel.add(formPanel, BorderLayout.CENTER);
            return panel;
        }

        // Tạo bảng "Cập nhật thông tin sinh viên"
        private JPanel createUpdateStudentPanel() {
            // Chỉ giáo viên mới được phép truy cập chức năng này
            if (!"teacher".equalsIgnoreCase(role)) {
                JPanel panel = new JPanel(new BorderLayout());
                JLabel label = new JLabel("Bạn không có quyền truy cập chức năng này.", JLabel.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 18));
                label.setForeground(Color.RED);
                panel.add(label, BorderLayout.CENTER);
                return panel;
            }

            JPanel panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("Cập nhật thông tin sinh viên", JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setBorder(new EmptyBorder(0, 0, 20, 0));
            panel.add(label, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel studentCodeLabel = new JLabel("Mã sinh viên:");
            studentCodeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField studentCodeField = new JTextField(15);
            studentCodeField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel nameLabel = new JLabel("Tên sinh viên:");
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField nameField = new JTextField(15);
            nameField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField emailField = new JTextField(15);
            emailField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel addressLabel = new JLabel("Địa chỉ:");
            addressLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField addressField = new JTextField(15);
            addressField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel phoneLabel = new JLabel("Số điện thoại:");
            phoneLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField phoneField = new JTextField(15);
            phoneField.setFont(new Font("Arial", Font.PLAIN, 16));

            JButton updateButton = createStyledButton("Cập nhật");
            updateButton.setBackground(new Color(0xffc107)); // Tùy chỉnh màu nếu cần

            // Sắp xếp các thành phần trong formPanel
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(studentCodeLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(studentCodeField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanel.add(nameLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            formPanel.add(emailLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(emailField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            formPanel.add(addressLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(addressField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            formPanel.add(phoneLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(phoneField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            formPanel.add(updateButton, gbc);

            // Xử lý sự kiện khi nhấn nút "Cập nhật"
            updateButton.addActionListener(e -> {
                String studentCode = studentCodeField.getText().trim();
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String address = addressField.getText().trim();
                String phone = phoneField.getText().trim();

                if (studentCode.isEmpty() || name.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Student student = new Student(0, studentCode, name, email, 0, address, phone);
                if (studentDAO.updateStudentInfo(student)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");

                    // Xóa dữ liệu trong các trường nhập sau khi cập nhật thành công
                    studentCodeField.setText("");
                    nameField.setText("");
                    emailField.setText("");
                    addressField.setText("");
                    phoneField.setText("");

                    // Làm mới bảng danh sách sinh viên ngay lập tức
                    updateStudentTable();

                    // Tùy chọn: Chuyển sang panel "Xem danh sách sinh viên" để hiển thị cập nhật ngay lập tức
                    cardLayout.show(contentPanel, "viewStudents");
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            panel.add(formPanel, BorderLayout.CENTER);
            return panel;
        }


        // Tạo bảng "Đổi mật khẩu"
        private JPanel createChangePasswordPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel oldPassLabel = new JLabel("Mật khẩu cũ:");
            oldPassLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JPasswordField oldPassField = new JPasswordField(20);
            oldPassField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel newPassLabel = new JLabel("Mật khẩu mới:");
            newPassLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JPasswordField newPassField = new JPasswordField(20);
            newPassField.setFont(new Font("Arial", Font.PLAIN, 16));

            JButton changeButton = new JButton("Đổi mật khẩu");
            changeButton.setFont(new Font("Arial", Font.BOLD, 16));
            changeButton.setBackground(new Color(0xdc3545));
            changeButton.setForeground(Color.WHITE);
            changeButton.setFocusPainted(false);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(oldPassLabel, gbc);
            gbc.gridx = 1;
            panel.add(oldPassField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(newPassLabel, gbc);
            gbc.gridx = 1;
            panel.add(newPassField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(changeButton, gbc);

            changeButton.addActionListener(e -> {
                String oldPassword = new String(oldPassField.getPassword()).trim();
                String newPassword = new String(newPassField.getPassword()).trim();

                if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ mật khẩu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                UserDAO userDAO = new UserDAO();
                if (userDAO.changePassword(username, oldPassword, newPassword)) {
                    JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
                    oldPassField.setText("");
                    newPassField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại! Vui lòng kiểm tra lại thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            return panel;
        }

        public static void main(String[] args) {
            // Khởi tạo JFrame
            SwingUtilities.invokeLater(() -> new DashboardGUI("Admin", "Quản trị viên"));
        }
    }
