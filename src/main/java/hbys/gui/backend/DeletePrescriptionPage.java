package hbys.gui.backend;

import hbys.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class DeletePrescriptionPage extends javax.swing.JFrame {

    private static int prescriptionID;

    public DeletePrescriptionPage(int prescriptionID) {
        this.prescriptionID = prescriptionID;
        System.out.println("Initializing DeletePrescriptionPage...");
        System.out.println("Prescription ID: " + prescriptionID);
        initComponents();
    }

    private void deletePrescription() {
        System.out.println("Deleting prescription...");
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "DELETE FROM Prescriptions WHERE PrescriptionID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, prescriptionID);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Prescription deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Pencereyi kapat
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete prescription.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting prescription: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "An error occurred while deleting the prescription.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel_Message = new javax.swing.JLabel();
        jButton_Delete = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Delete Prescription");

        jLabel_Message.setText("Are you sure you want to delete this prescription?");

        jButton_Delete.setText("Delete");
        jButton_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePrescription();
            }
        });

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {
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
                                        .addComponent(jLabel_Message, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jButton_Cancel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_Delete)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel_Message)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_Delete)
                                        .addComponent(jButton_Cancel))
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Delete;
    private javax.swing.JLabel jLabel_Message;
}
