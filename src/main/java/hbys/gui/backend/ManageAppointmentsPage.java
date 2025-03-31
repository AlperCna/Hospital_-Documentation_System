package hbys.gui.backend;

import hbys.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ManageAppointmentsPage extends javax.swing.JFrame {

    private static int patientID;

    public ManageAppointmentsPage(int patientID) {
        this.patientID = patientID;
        initComponents();
        loadAppointments();
    }

    private void loadAppointments() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT AppointmentID, AppointmentDate, Status, Notes FROM Appointments WHERE PatientID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, patientID);
            ResultSet rs = ps.executeQuery();

            String[] columnNames = {"Appointment ID", "Appointment Date", "Status", "Notes"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("AppointmentID"),
                    rs.getTimestamp("AppointmentDate"),
                    rs.getString("Status"),
                    rs.getString("Notes")
                };
                model.addRow(row);
            }

            jTable_Appointments.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading appointments.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Randevuyu iptal etmek için metot
     */
    private void cancelAppointment() {
        int selectedRow = jTable_Appointments.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int appointmentID = (int) jTable_Appointments.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this appointment?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "UPDATE Appointments SET Status = 'Cancelled' WHERE AppointmentID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, appointmentID);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Appointment cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAppointments(); // Tabloyu yeniden yükle
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel the appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while cancelling the appointment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Appointments = new javax.swing.JTable();
        jButton_CancelAppointment = new javax.swing.JButton();
        jButton_Back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manage Appointments");

        jTable_Appointments.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"Appointment ID", "Appointment Date", "Status", "Notes"}
        ));
        jScrollPane1.setViewportView(jTable_Appointments);

        jButton_CancelAppointment.setText("Cancel Appointment");
        jButton_CancelAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelAppointment();
            }
        });

        jButton_Back.setText("Back");
        jButton_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton_CancelAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton_Back, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_CancelAppointment)
                                        .addComponent(jButton_Back))
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

    private javax.swing.JButton jButton_Back;
    private javax.swing.JButton jButton_CancelAppointment;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Appointments;
}
