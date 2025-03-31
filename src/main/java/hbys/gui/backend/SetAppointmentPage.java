package hbys.gui.backend;

import hbys.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import javax.swing.JOptionPane;

public class SetAppointmentPage extends javax.swing.JFrame {

    private static int patientID;
    private static int doctorID;

    public SetAppointmentPage(int patientID, int doctorID) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        System.out.println("Initializing SetAppointmentPage...");
        System.out.println("Patient ID: " + patientID);
        System.out.println("Doctor ID: " + doctorID);
        initComponents();
        loadPatientDetails(); // Hasta bilgilerini yükle
    }

    private void loadPatientDetails() {
        System.out.println("Loading patient details...");
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT CONCAT(u.FirstName, ' ', u.LastName) AS PatientName, u.Email, p.ContactNumber, p.Address "
                    + "FROM Users u "
                    + "JOIN Patients p ON u.UserID = p.UserID "
                    + "WHERE p.PatientID = ?";
            System.out.println("SQL Query: " + query);

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, patientID);
            System.out.println("Patient ID for Query: " + patientID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String patientName = rs.getString("PatientName");
                String email = rs.getString("Email");
                String contactNumber = rs.getString("ContactNumber");
                String address = rs.getString("Address");

                jLabel_PatientName.setText("Name: " + patientName);
                jLabel_Email.setText("Email: " + email);
                jLabel_ContactNumber.setText("Contact Number: " + contactNumber);
                jLabel_Address.setText("Address: " + address);
                jLabel_PatientID.setText("Patient ID: " + patientID);

                System.out.println("Patient Details Loaded Successfully.");
                System.out.println("Name: " + patientName);
                System.out.println("Email: " + email);
                System.out.println("Contact Number: " + contactNumber);
                System.out.println("Address: " + address);
            } else {
                System.out.println("No details found for Patient ID: " + patientID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading patient details: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading patient details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createAppointment() {
        System.out.println("Creating appointment...");
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Doktorun belirlediği ücret
            double appointmentFee = 0;
            try {
                appointmentFee = Double.parseDouble(jTextField_Fee.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid fee. Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "INSERT INTO Appointments (PatientID, DoctorID, AppointmentDate, Status, Notes) VALUES (?, ?, ?, ?, ?)";
            System.out.println("SQL Query: " + query);

            PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, patientID);
            ps.setInt(2, doctorID);
            ps.setTimestamp(3, new Timestamp(jDateChooser_AppointmentDate.getDate().getTime()));
            ps.setString(4, "Scheduled");
            ps.setString(5, jTextArea_Notes.getText());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                int appointmentID = -1;
                if (rs.next()) {
                    appointmentID = rs.getInt(1);
                }

                // Ücret bilgisi "Billing" tablosuna yalnızca "Completed" durumu ile eklenir
                String insertBillingQuery = "INSERT INTO Billing (AppointmentID, TotalAmount, PaymentStatus, IssueDate) VALUES (?, ?, 'Unpaid', GETDATE())";
                PreparedStatement psBilling = conn.prepareStatement(insertBillingQuery);
                psBilling.setInt(1, appointmentID);
                psBilling.setDouble(2, appointmentFee);

                // Ücret bilgisi yalnızca durum "Completed" olduğunda eklenir
                if (appointmentID != -1) {
                    System.out.println("Appointment ID: " + appointmentID + " created. Fee: " + appointmentFee);
                }

                JOptionPane.showMessageDialog(this, "Appointment created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Pencereyi kapat
            } else {
                System.out.println("Failed to create appointment.");
                JOptionPane.showMessageDialog(this, "Failed to create appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating appointment: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "An error occurred while creating the appointment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAppointment() {
        // Kaydetme işlemini burada gerçekleştir
        System.out.println("Saving appointment details...");
        JOptionPane.showMessageDialog(this, "Appointment details saved successfully!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel_PatientName = new javax.swing.JLabel();
        jLabel_Email = new javax.swing.JLabel();
        jLabel_ContactNumber = new javax.swing.JLabel();
        jLabel_Address = new javax.swing.JLabel();
        jLabel_PatientID = new javax.swing.JLabel();
        jLabel_Date = new javax.swing.JLabel();
        jDateChooser_AppointmentDate = new com.toedter.calendar.JDateChooser();
        jLabel_Fee = new javax.swing.JLabel(); // Ücret etiketi
        jTextField_Fee = new javax.swing.JTextField(); // Ücret alanı
        jLabel_Notes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Notes = new javax.swing.JTextArea();
        jButton_CreateAppointment = new javax.swing.JButton();
        jButton_Save = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Set Appointment");

        jLabel_Date.setText("Appointment Date:");
        jLabel_Fee.setText("Appointment Fee (Optional):"); // Ücret etiketi
        jLabel_Notes.setText("Notes:");

        jTextArea_Notes.setColumns(20);
        jTextArea_Notes.setRows(5);
        jScrollPane1.setViewportView(jTextArea_Notes);

        jButton_CreateAppointment.setText("Create Appointment");
        jButton_CreateAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAppointment();
            }
        });

        jButton_Save.setText("Okay");
        jButton_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAppointment();
            }
        });

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        // Layout düzenlemesi
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel_Date)
                                                .addGap(18, 18, 18)
                                                .addComponent(jDateChooser_AppointmentDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel_Fee)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextField_Fee)) // Ücret alanını ekledik
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 242, Short.MAX_VALUE)
                                                .addComponent(jButton_Cancel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_Save)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_CreateAppointment))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel_PatientName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel_ContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel_Address, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel_PatientID, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel_Notes))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel_PatientName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel_Email)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel_ContactNumber)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel_Address)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel_PatientID)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel_Date)
                                        .addComponent(jDateChooser_AppointmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_Fee)
                                        .addComponent(jTextField_Fee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)) // Ücret alanını ekledik
                                .addGap(18, 18, 18)
                                .addComponent(jLabel_Notes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_CreateAppointment)
                                        .addComponent(jButton_Save)
                                        .addComponent(jButton_Cancel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_CreateAppointment;
    private javax.swing.JButton jButton_Save;
    private javax.swing.JLabel jLabel_Address;
    private javax.swing.JLabel jLabel_ContactNumber;
    private javax.swing.JLabel jLabel_Date;
    private javax.swing.JLabel jLabel_Email;
    private javax.swing.JLabel jLabel_Notes;
    private javax.swing.JLabel jLabel_PatientID;
    private javax.swing.JLabel jLabel_PatientName;
    private com.toedter.calendar.JDateChooser jDateChooser_AppointmentDate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_Notes;
    private javax.swing.JLabel jLabel_Fee;
    private javax.swing.JTextField jTextField_Fee;
}
