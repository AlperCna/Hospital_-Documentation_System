package hbys.gui.backend;

import hbys.database.DatabaseConnection;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class WritePrescriptionPage extends javax.swing.JFrame {

    private static int patientID;
    private static int doctorID;

    public WritePrescriptionPage(int patientID, int doctorID) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        System.out.println("Initializing WritePrescriptionPage...");
        System.out.println("Patient ID: " + patientID);
        System.out.println("Doctor ID: " + doctorID);
        initComponents();
        loadPharmacyInventory(); // İlaç listesini yükle
    }

    private void loadPharmacyInventory() {
        System.out.println("Loading pharmacy inventory...");
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT ItemID, ItemName, Quantity, PricePerUnit, ExpiryDate, SupplierName FROM PharmacyInventory";
            System.out.println("SQL Query: " + query);

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            // JTable için model oluştur
            String[] columnNames = {"Item ID", "Item Name", "Quantity", "Price Per Unit", "Expiry Date", "Supplier Name"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            // Verileri JTable'a ekle
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ItemID"),
                    rs.getString("ItemName"),
                    rs.getInt("Quantity"),
                    rs.getDouble("PricePerUnit"),
                    rs.getDate("ExpiryDate"),
                    rs.getString("SupplierName")
                };
                model.addRow(row);
            }

            jTable_PharmacyInventory.setModel(model);
            System.out.println("Pharmacy inventory loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading pharmacy inventory: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading pharmacy inventory.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void savePrescription() {
        int selectedRow = jTable_PharmacyInventory.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medicine.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null; // Connection değişkenini burada tanımlayın
        try {
            conn = DatabaseConnection.getConnection(); // Veritabanı bağlantısını alın
            if (conn == null) {
                System.out.println("Database connection failed.");
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            conn.setAutoCommit(false); // Transaction başlat

            // Seçilen ilacı ve miktarı al
            int itemID = (int) jTable_PharmacyInventory.getValueAt(selectedRow, 0); // İlacın ID'si
            int quantity = Integer.parseInt(jTextField_Quantity.getText().trim());
            String notes = jTextArea_Notes.getText().trim();

            // **1. Adım: Prescriptions tablosuna ekle**
            String insertPrescription = "INSERT INTO Prescriptions (PatientID, DoctorID, DateIssued, Notes) "
                    + "VALUES (?, ?, GETDATE(), ?)";
            PreparedStatement psPrescription = conn.prepareStatement(insertPrescription, PreparedStatement.RETURN_GENERATED_KEYS);
            psPrescription.setInt(1, patientID);
            psPrescription.setInt(2, doctorID);
            psPrescription.setString(3, notes);
            psPrescription.executeUpdate();

            // Oluşturulan PrescriptionID'yi al
            ResultSet rsPrescription = psPrescription.getGeneratedKeys();
            int prescriptionID = -1;
            if (rsPrescription.next()) {
                prescriptionID = rsPrescription.getInt(1);
            }

            // **2. Adım: Documents tablosuna ekle**
            String insertDocument = "INSERT INTO Documents (DocumentType, RelatedID, CreatedBy, CreatedFor, CreationDate, Status, Description) "
                    + "VALUES ('Prescription', ?, ?, ?, GETDATE(), 'Completed', ?)";
            PreparedStatement psDocument = conn.prepareStatement(insertDocument);
            psDocument.setInt(1, prescriptionID); // RelatedID olarak PrescriptionID'yi ayarlayın
            psDocument.setInt(2, doctorID);
            psDocument.setInt(3, patientID);
            psDocument.setString(4, notes);
            psDocument.executeUpdate();

            // **3. Adım: PrescriptionItems tablosuna ekle**
            String insertPrescriptionItem = "INSERT INTO PrescriptionItems (PrescriptionID, ItemID, Quantity) VALUES (?, ?, ?)";
            PreparedStatement psItem = conn.prepareStatement(insertPrescriptionItem);
            psItem.setInt(1, prescriptionID);
            psItem.setInt(2, itemID);
            psItem.setInt(3, quantity);
            psItem.executeUpdate();

            // **4. Adım: PrescriptionDetails tablosuna ekle**
            String insertPrescriptionDetails = "INSERT INTO PrescriptionDetails (DocumentID, ItemID, Quantity) "
                    + "VALUES ((SELECT DocumentID FROM Documents WHERE RelatedID = ? AND DocumentType = 'Prescription'), ?, ?)";
            PreparedStatement psDetails = conn.prepareStatement(insertPrescriptionDetails);
            psDetails.setInt(1, prescriptionID); // RelatedID ile eşleştir
            psDetails.setInt(2, itemID);
            psDetails.setInt(3, quantity);
            psDetails.executeUpdate();

            conn.commit(); // Transaction'ı tamamla
            JOptionPane.showMessageDialog(this, "Prescription saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Pencereyi kapat

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving prescription: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "An error occurred while saving the prescription.", "Error", JOptionPane.ERROR_MESSAGE);
            if (conn != null) {
                try {
                    conn.rollback(); // Hata durumunda transaction'ı geri al
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close(); // Bağlantıyı kapat
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_PharmacyInventory = new javax.swing.JTable();
        jLabel_Quantity = new javax.swing.JLabel();
        jTextField_Quantity = new javax.swing.JTextField();
        jLabel_Notes = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_Notes = new javax.swing.JTextArea();
        jButton_SavePrescription = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Write Prescription");

        jTable_PharmacyInventory.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"Item ID", "Item Name", "Quantity", "Price Per Unit", "Expiry Date", "Supplier Name"}
        ));
        jScrollPane1.setViewportView(jTable_PharmacyInventory);

        jLabel_Quantity.setText("Quantity:");

        jLabel_Notes.setText("Notes:");

        jTextArea_Notes.setColumns(20);
        jTextArea_Notes.setRows(5);
        jScrollPane2.setViewportView(jTextArea_Notes);

        jButton_SavePrescription.setText("Save Prescription");
        jButton_SavePrescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePrescription();
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
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel_Quantity)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField_Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel_Notes)
                                        .addComponent(jScrollPane2)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jButton_Cancel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_SavePrescription)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_Quantity)
                                        .addComponent(jTextField_Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel_Notes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_SavePrescription)
                                        .addComponent(jButton_Cancel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_SavePrescription;
    private javax.swing.JLabel jLabel_Notes;
    private javax.swing.JLabel jLabel_Quantity;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_PharmacyInventory;
    private javax.swing.JTextArea jTextArea_Notes;
    private javax.swing.JTextField jTextField_Quantity;
}
