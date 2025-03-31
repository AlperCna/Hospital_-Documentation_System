package hbys.gui.backend;

import hbys.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class DischargePatientPage extends javax.swing.JFrame {

    private int admissionID;

    public DischargePatientPage(int admissionID) {
        this.admissionID = admissionID;
        initComponents();
    }

    private void dischargePatient() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String notes = jTextArea_Notes.getText().trim();
            java.sql.Date dischargeDate = new java.sql.Date(jDateChooser_DischargeDate.getDate().getTime());

            conn.setAutoCommit(false); // Transaction başlat

            // 1. Admissions tablosunu güncelle
            String updateAdmissionQuery = "UPDATE Admissions SET DischargeDate = ?, Notes = ? WHERE AdmissionID = ?";
            PreparedStatement psAdmission = conn.prepareStatement(updateAdmissionQuery);
            psAdmission.setDate(1, dischargeDate);
            psAdmission.setString(2, notes);
            psAdmission.setInt(3, admissionID);

            int rowsUpdated = psAdmission.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Admission updated successfully.");

                // 2. Giriş ve çıkış tarihlerini al ve gün farkını hesapla
                String selectAdmissionQuery = "SELECT AdmissionDate, RoomID, PatientID, ResponsibleDoctorID FROM Admissions WHERE AdmissionID = ?";
                PreparedStatement psSelectAdmission = conn.prepareStatement(selectAdmissionQuery);
                psSelectAdmission.setInt(1, admissionID);
                ResultSet rsAdmission = psSelectAdmission.executeQuery();

                if (rsAdmission.next()) {
                    java.sql.Date admissionDate = rsAdmission.getDate("AdmissionDate");
                    int roomID = rsAdmission.getInt("RoomID");
                    int patientID = rsAdmission.getInt("PatientID");
                    int doctorID = rsAdmission.getInt("ResponsibleDoctorID");

                    long daysStayed = (dischargeDate.getTime() - admissionDate.getTime()) / (1000 * 60 * 60 * 24);

                    // 3. Room ücretini al
                    String selectRoomQuery = "SELECT RoomPrice FROM Rooms WHERE RoomID = ?";
                    PreparedStatement psSelectRoom = conn.prepareStatement(selectRoomQuery);
                    psSelectRoom.setInt(1, roomID);
                    ResultSet rsRoom = psSelectRoom.executeQuery();

                    if (rsRoom.next()) {
                        double roomPrice = rsRoom.getDouble("RoomPrice");
                        double totalAmount = daysStayed * roomPrice;

                        // 4. Billing tablosuna toplam ücreti kaydet (AppointmentID NULL)
                        String insertBillingQuery = "INSERT INTO Billing (AppointmentID, TotalAmount, PaymentStatus, IssueDate) VALUES (NULL, ?, 'Unpaid', GETDATE())";
                        PreparedStatement psInsertBilling = conn.prepareStatement(insertBillingQuery);
                        psInsertBilling.setDouble(1, totalAmount);
                        psInsertBilling.executeUpdate();
                        System.out.println("Billing record created successfully with NULL AppointmentID.");

                        // 5. Documents tablosuna faturayı ekle
                        String insertDocumentQuery = "INSERT INTO Documents (DocumentType, RelatedID, CreatedBy, CreatedFor, CreationDate, Status, Description) "
                                + "VALUES ('Invoice', ?, ?, ?, GETDATE(), 'Completed', ?)";
                        PreparedStatement psInsertDocument = conn.prepareStatement(insertDocumentQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                        psInsertDocument.setInt(1, admissionID);
                        psInsertDocument.setInt(2, doctorID);
                        psInsertDocument.setInt(3, patientID);
                        psInsertDocument.setString(4, "Discharge invoice for room stay.");
                        psInsertDocument.executeUpdate();

                        ResultSet rsDocument = psInsertDocument.getGeneratedKeys();
                        if (rsDocument.next()) {
                            int documentID = rsDocument.getInt(1);

                            // 6. InvoiceDetails tablosuna detay ekle
                            String insertInvoiceDetailsQuery = "INSERT INTO InvoiceDetails (DocumentID, ItemDescription, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
                            PreparedStatement psInsertInvoiceDetails = conn.prepareStatement(insertInvoiceDetailsQuery);
                            psInsertInvoiceDetails.setInt(1, documentID);
                            psInsertInvoiceDetails.setString(2, "Room stay for " + daysStayed + " days");
                            psInsertInvoiceDetails.setInt(3, (int) daysStayed);
                            psInsertInvoiceDetails.setDouble(4, roomPrice);
                            psInsertInvoiceDetails.executeUpdate();
                        }
                    }
                }

                conn.commit(); // Transaction tamamla
                JOptionPane.showMessageDialog(this, "Patient discharged and invoice created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Pencereyi kapat
            } else {
                throw new Exception("Failed to update admission.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error discharging patient.", "Error", JOptionPane.ERROR_MESSAGE);
            try ( Connection conn = DatabaseConnection.getConnection()) {
                if (conn != null) {
                    conn.rollback(); // Hata durumunda transaction geri alınır
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel_DischargeDate = new javax.swing.JLabel();
        jDateChooser_DischargeDate = new com.toedter.calendar.JDateChooser();
        jLabel_Notes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Notes = new javax.swing.JTextArea();
        jButton_Save = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Discharge Patient");

        jLabel_DischargeDate.setText("Discharge Date:");

        jLabel_Notes.setText("Notes:");

        jTextArea_Notes.setColumns(20);
        jTextArea_Notes.setRows(5);
        jScrollPane1.setViewportView(jTextArea_Notes);

        jButton_Save.setText("Save");
        jButton_Save.addActionListener(evt -> dischargePatient());

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.addActionListener(evt -> dispose());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel_DischargeDate)
                                                        .addComponent(jLabel_Notes))
                                                .addGap(0, 300, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 200, Short.MAX_VALUE)
                                                .addComponent(jButton_Cancel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_Save))
                                        .addComponent(jDateChooser_DischargeDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel_DischargeDate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser_DischargeDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel_Notes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_Save)
                                        .addComponent(jButton_Cancel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Save;
    private com.toedter.calendar.JDateChooser jDateChooser_DischargeDate;
    private javax.swing.JLabel jLabel_DischargeDate;
    private javax.swing.JLabel jLabel_Notes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_Notes;
}
