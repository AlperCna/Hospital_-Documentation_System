package hbys.gui.backend;

import hbys.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class UpdateAppointmentPage extends javax.swing.JFrame {

    private static int appointmentID;

    public UpdateAppointmentPage(int appointmentID) {
        this.appointmentID = appointmentID;
        System.out.println("Initializing UpdateAppointmentPage...");
        System.out.println("Appointment ID: " + appointmentID);
        initComponents();
    }

    private void updateAppointment() {
        System.out.println("Updating appointment...");
        try ( Connection conn = DatabaseConnection.getConnection()) {

            if (conn == null) {
                System.out.println("Database connection failed.");
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String status = (String) jComboBox_Status.getSelectedItem(); // ComboBox'tan seçilen değer
            String notes = jTextArea_Notes.getText().trim();
            double consultationFee = 0;

            // Muayene ücreti alanı boş değilse değerini al
            try {
                consultationFee = Double.parseDouble(jTextField_ConsultationFee.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid consultation fee. Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (status == null || status.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Status cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            conn.setAutoCommit(false); // Transaction başlat

            // Appointments tablosunu güncelle
            String query = "UPDATE Appointments SET Status = ?, Notes = ? WHERE AppointmentID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ps.setString(2, notes);
            ps.setInt(3, appointmentID);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Appointment updated successfully.");

                // Eğer "Completed" olarak güncellenmişse ücret hastaya yansıtılır
                if (status.equals("Completed")) {
                    updateBillingTable(conn, consultationFee);
                }

                conn.commit(); // Transaction tamamla
                JOptionPane.showMessageDialog(this, "Appointment updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Pencereyi kapat
            } else {
                throw new Exception("Failed to update appointment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating appointment: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "An error occurred while updating the appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            try ( Connection conn = DatabaseConnection.getConnection()) {
                conn.rollback(); // Hata durumunda transaction geri alınır
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    private void updateBillingTable(Connection conn, double consultationFee) throws Exception {
        // Billing tablosunda bu AppointmentID var mı kontrol et
        String checkQuery = "SELECT BillID, PaymentStatus FROM Billing WHERE AppointmentID = ?";
        PreparedStatement checkPs = conn.prepareStatement(checkQuery);
        checkPs.setInt(1, appointmentID);
        ResultSet rs = checkPs.executeQuery();

        if (rs.next()) {
            String currentStatus = rs.getString("PaymentStatus");
            if (!currentStatus.equals("Paid")) {
                // Eğer mevcut status "Paid" değilse, güncelleme yap
                String updateBillingQuery = "UPDATE Billing SET TotalAmount = ?, PaymentStatus = ?, IssueDate = GETDATE() WHERE AppointmentID = ?";
                PreparedStatement updatePs = conn.prepareStatement(updateBillingQuery);
                updatePs.setDouble(1, consultationFee);
                updatePs.setString(2, "Paid"); // Tamamlanan muayeneler için "Paid"
                updatePs.setInt(3, appointmentID);
                updatePs.executeUpdate();
                System.out.println("Billing updated for AppointmentID: " + appointmentID);
            } else {
                System.out.println("Billing already marked as Paid for AppointmentID: " + appointmentID);
            }
        } else {
            // Eğer Billing kaydı yoksa, yeni bir Billing kaydı oluştur
            String insertBillingQuery = "INSERT INTO Billing (AppointmentID, TotalAmount, PaymentStatus, IssueDate) VALUES (?, ?, ?, GETDATE())";
            PreparedStatement insertPs = conn.prepareStatement(insertBillingQuery);
            insertPs.setInt(1, appointmentID);
            insertPs.setDouble(2, consultationFee);
            insertPs.setString(3, "Paid"); // Yeni kayıtlar için "Paid"
            insertPs.executeUpdate();
            System.out.println("New billing record created for AppointmentID: " + appointmentID);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel_Status = new javax.swing.JLabel();
        jComboBox_Status = new javax.swing.JComboBox<>();
        jLabel_Notes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Notes = new javax.swing.JTextArea();
        jLabel_ConsultationFee = new javax.swing.JLabel();
        jTextField_ConsultationFee = new javax.swing.JTextField(); // Muayene ücreti alanı
        jButton_Update = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update Appointment");

        jLabel_Status.setText("Status:");

        jComboBox_Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Scheduled", "Completed", "Cancelled"}));

        jLabel_Notes.setText("Notes:");

        jTextArea_Notes.setColumns(20);
        jTextArea_Notes.setRows(5);
        jScrollPane1.setViewportView(jTextArea_Notes);

        jLabel_ConsultationFee.setText("Consultation Fee:"); // Yeni alan etiketi

        jButton_Update.setText("Update");
        jButton_Update.addActionListener(evt -> updateAppointment());

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.addActionListener(evt -> dispose());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jButton_Cancel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_Update))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel_Status)
                                                        .addComponent(jLabel_Notes)
                                                        .addComponent(jLabel_ConsultationFee)) // Yeni alan etiketi
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jComboBox_Status, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextField_ConsultationFee) // Yeni alan
                                                .addGap(0, 0, Short.MAX_VALUE))) // Yeni alan
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel_Status)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox_Status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel_Notes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel_ConsultationFee) // Yeni alan etiketi
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField_ConsultationFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) // Yeni alan
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_Update)
                                        .addComponent(jButton_Cancel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Update;
    private javax.swing.JComboBox<String> jComboBox_Status; // ComboBox tanımlandı
    private javax.swing.JLabel jLabel_Notes;
    private javax.swing.JLabel jLabel_Status;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_Notes;
    private javax.swing.JTextField jTextField_ConsultationFee;
    private javax.swing.JLabel jLabel_ConsultationFee;
}
