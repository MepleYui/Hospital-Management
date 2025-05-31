package hospital.management;

import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.HashMap;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AdminPanel extends javax.swing.JFrame {

    /**
     * Creates new form AdminPanel
     */
    Connection con;
    
    private AdminTable accountTable;
    
    public AdminPanel(HashMap<String, String> userData) {
        initComponents();
        String db = "jdbc:mysql://localhost/hospital";
        String user = "root";
        String pass = "";
        
        try {
            con = DriverManager.getConnection(db, user, pass);
        }
        catch(Exception ex) {
            System.out.println("Error : " + ex.getMessage());
        }
        
        accountspanel.setVisible(false);
        approvalspanel.setVisible(false);
        billspanel.setVisible(false);
        transactionpanel.setVisible(false);
        loadAccountsFromDatabase();
        
        String username = userData.get("username");
        namelabel.setText(username);
    }
    
    public AdminPanel() {
        initComponents();
    }
    
    //load all accounts from database into the table
    public void loadAccountsFromDatabase() {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String query = "SELECT user, password, type, approved FROM accounts";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            // Create list to store data
            List<Object[]> dataList = new ArrayList<>();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("user"),
                    rs.getString("password"),
                    rs.getString("type"),
                    rs.getString("approved")
                };
                dataList.add(row);
            }
            
            // Convert list to array and load into table
            Object[][] data = dataList.toArray(new Object[0][]);
            adminTable.loadDataFromDatabase(data);
            
            rs.close();
            pst.close();
            
        } catch (SQLException ex) {
            System.out.println("Error loading accounts: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading accounts: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Add accounts to database
    public boolean addAccountToDatabase(String user, String password, String type, String approved) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            // First check if username already exists
            String checkQuery = "SELECT COUNT(*) FROM accounts WHERE user = ?";
            PreparedStatement checkPst = con.prepareStatement(checkQuery);
            checkPst.setString(1, user);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Username already exists
                rs.close();
                checkPst.close();
                JOptionPane.showMessageDialog(this, "Username '" + user + "' is already taken. Please choose a different username.", "Username Taken", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            rs.close();
            checkPst.close();

            // If username is available, proceed with insertion
            String query = "INSERT INTO accounts (user, password, type, approved) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, user);
            pst.setString(2, password);
            pst.setString(3, type);
            pst.setString(4, approved);

            int result = pst.executeUpdate();
            pst.close();

            if (result > 0) {
                // Add to table display
                Object[] newRow = {user, password, type, approved};
                adminTable.addUser(newRow);
                JOptionPane.showMessageDialog(this, "Account added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }

        } catch (SQLException ex) {
            System.out.println("Error adding account: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error adding account: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    //Edit stuff on Database
    public boolean updateAccountInDatabase(String oldUser, String newUser, String password, String type, String approved) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            String query = "UPDATE accounts SET user=?, password=?, type=?, approved=? WHERE user=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, newUser);
            pst.setString(2, password);
            pst.setString(3, type);
            pst.setString(4, approved);
            pst.setString(5, oldUser);
            
            int result = pst.executeUpdate();
            pst.close();
            
            if (result > 0) {
                // Refresh table data
                loadAccountsFromDatabase();
                JOptionPane.showMessageDialog(this, "Account updated successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Account not found or no changes made!", 
                                            "Warning", JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error updating account: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error updating account: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    //Delete stuff from database
    public boolean deleteAccountFromDatabase(String user) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete account: " + user + "?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM accounts WHERE user=?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, user);
                
                int result = pst.executeUpdate();
                pst.close();
                
                if (result > 0) {
                    // Remove from table display
                    int selectedRow = adminTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        adminTable.removeUser(selectedRow);
                    }
                    JOptionPane.showMessageDialog(this, "Account deleted successfully!", 
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found!", 
                                                "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            
        } catch (SQLException ex) {
            System.out.println("Error deleting account: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error deleting account: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    //Refresh Table
    public void refreshTable() {
        loadAccountsFromDatabase();
    }
    
    // Method to get selected account data for editing
    public Object[] getSelectedAccountData() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow >= 0) {
            return adminTable.getRowData(selectedRow);
        }
        return null;
    }
    
    public void closeDatabaseConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException ex) {
            System.out.println("Error closing database connection: " + ex.getMessage());
        }
    }
    
    
    //-------SEARCH FUNCTIONS----------
    
    private void performSearch() {
        String searchTerm = searchTextField.getText().trim();
        String searchColumn = (String) searchColumnComboBox.getSelectedItem();
        
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term!", "Empty Search", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        searchAccount(searchTerm, searchColumn);
    }
    
    private void clearSearch() {
        searchTextField.setText("");
        loadAccountsFromDatabase();
    }
    
    public void searchAccount(String searchTerm, String searchColumn) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (adminTable == null) {
            JOptionPane.showMessageDialog(this, "Table not initialized!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String[] validColumns = {"user", "password", "type", "approved"};
            boolean validColumn = false;
            for (String col : validColumns) {
                if (col.equals(searchColumn)) {
                    validColumn = true;
                    break;
                }
            }

            if (!validColumn) {
                JOptionPane.showMessageDialog(this, "Invalid search column!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT user, password, type, approved FROM accounts WHERE " + searchColumn + " LIKE ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, searchTerm + "%");  // Changed from "%" + searchTerm + "%" to searchTerm + "%"
            ResultSet rs = pst.executeQuery();

            List<Object[]> dataList = new ArrayList<>();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("user"),
                    rs.getString("password"),
                    rs.getString("type"),
                    rs.getString("approved")
                };
                dataList.add(row);
            }

            Object[][] data = dataList.toArray(new Object[0][]);
            adminTable.loadDataFromDatabase(data);

            if (dataList.size() == 0) {
                JOptionPane.showMessageDialog(this, "No accounts found matching your search criteria.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            }

            rs.close();
            pst.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching accounts: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        gradient22 = new hospital.management.Gradient2();
        sidepanel = new javax.swing.JPanel();
        gradient24 = new hospital.management.Gradient2();
        homeside = new javax.swing.JPanel();
        gradient29 = new hospital.management.Gradient2();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        accountsside = new javax.swing.JPanel();
        gradient25 = new hospital.management.Gradient2();
        jLabel2 = new javax.swing.JLabel();
        approvalsside = new javax.swing.JPanel();
        gradient26 = new hospital.management.Gradient2();
        jLabel3 = new javax.swing.JLabel();
        billsside = new javax.swing.JPanel();
        gradient27 = new hospital.management.Gradient2();
        jLabel4 = new javax.swing.JLabel();
        transactionsside = new javax.swing.JPanel();
        gradient21 = new hospital.management.Gradient2();
        jLabel5 = new javax.swing.JLabel();
        accountspanel = new javax.swing.JPanel();
        gradient23 = new hospital.management.Gradient2();
        adminTable = new hospital.management.AdminTable();
        hospitalSuccessButton1 = new hospital.management.HospitalSuccessButton();
        hospitalSuccessButton2 = new hospital.management.HospitalSuccessButton();
        hospitalEmergencyButton1 = new hospital.management.HospitalEmergencyButton();
        searchTextField = new javax.swing.JTextField();
        searchColumnComboBox = new javax.swing.JComboBox<>();
        hospitalSuccessButton3 = new hospital.management.HospitalSuccessButton();
        hospitalEmergencyButton2 = new hospital.management.HospitalEmergencyButton();
        hospitalPrimaryButton1 = new hospital.management.HospitalPrimaryButton();
        approvalspanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        gradient28 = new hospital.management.Gradient2();
        billspanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        gradient30 = new hospital.management.Gradient2();
        accountspanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        gradient32 = new hospital.management.Gradient2();
        approvalspanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        gradient33 = new hospital.management.Gradient2();
        billspanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        gradient34 = new hospital.management.Gradient2();
        accountspanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        gradient35 = new hospital.management.Gradient2();
        approvalspanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        gradient36 = new hospital.management.Gradient2();
        transactionpanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        gradient31 = new hospital.management.Gradient2();
        jLabel10 = new javax.swing.JLabel();
        namelabel = new javax.swing.JLabel();
        labelapproval = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        labelbill = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        gradient22.setMaximumSize(new java.awt.Dimension(854, 480));
        gradient22.setMinimumSize(new java.awt.Dimension(854, 480));
        gradient22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradient24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        homeside.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        homeside.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homesideMouseClicked(evt);
            }
        });
        homeside.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradient29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(44, 62, 80));
        jLabel1.setText("HOME");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        gradient29.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, -1, -1));
        gradient29.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, -1, -1));

        homeside.add(gradient29, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -110, 220, 480));

        gradient24.add(homeside, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 220, 40));

        accountsside.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        accountsside.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accountssideMouseClicked(evt);
            }
        });
        accountsside.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradient25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(44, 62, 80));
        jLabel2.setText("ACCOUNTS");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        gradient25.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 160, -1, -1));

        accountsside.add(gradient25, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -150, 220, 480));

        gradient24.add(accountsside, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 220, 40));

        approvalsside.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradient26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(44, 62, 80));
        jLabel3.setText("APPROVALS");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        gradient26.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, -1, -1));

        approvalsside.add(gradient26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -190, 220, 480));

        gradient24.add(approvalsside, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 220, 40));

        billsside.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradient27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(44, 62, 80));
        jLabel4.setText("BILLS");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        gradient27.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 240, -1, -1));

        billsside.add(gradient27, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -230, 220, 480));

        gradient24.add(billsside, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 220, 40));

        transactionsside.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradient21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(44, 62, 80));
        jLabel5.setText("TRANSACTIONS");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        gradient21.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 280, -1, -1));

        transactionsside.add(gradient21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -270, 220, 480));

        gradient24.add(transactionsside, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 220, 40));

        javax.swing.GroupLayout sidepanelLayout = new javax.swing.GroupLayout(sidepanel);
        sidepanel.setLayout(sidepanelLayout);
        sidepanelLayout.setHorizontalGroup(
            sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gradient24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sidepanelLayout.setVerticalGroup(
            sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gradient24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        gradient22.add(sidepanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 480));

        accountspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradient23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        gradient23.add(adminTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 410, 480));

        hospitalSuccessButton1.setText("Add");
        hospitalSuccessButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalSuccessButton1ActionPerformed(evt);
            }
        });
        gradient23.add(hospitalSuccessButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 370, 90, -1));

        hospitalSuccessButton2.setText("Edit");
        hospitalSuccessButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalSuccessButton2ActionPerformed(evt);
            }
        });
        gradient23.add(hospitalSuccessButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 370, 90, -1));

        hospitalEmergencyButton1.setText("Delete");
        hospitalEmergencyButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalEmergencyButton1ActionPerformed(evt);
            }
        });
        gradient23.add(hospitalEmergencyButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 420, 190, -1));

        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyPressed(evt);
            }
        });
        gradient23.add(searchTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 20, 190, 30));

        searchColumnComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "user", "password", "type", "approved" }));
        gradient23.add(searchColumnComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 60, 190, -1));

        hospitalSuccessButton3.setText("Search");
        hospitalSuccessButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalSuccessButton3ActionPerformed(evt);
            }
        });
        gradient23.add(hospitalSuccessButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, 80, -1));

        hospitalEmergencyButton2.setText("Clear");
        hospitalEmergencyButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalEmergencyButton2ActionPerformed(evt);
            }
        });
        gradient23.add(hospitalEmergencyButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 90, 80, -1));

        hospitalPrimaryButton1.setText("Refresh");
        hospitalPrimaryButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalPrimaryButton1ActionPerformed(evt);
            }
        });
        gradient23.add(hospitalPrimaryButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 310, 90, -1));

        accountspanel.add(gradient23, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        gradient22.add(accountspanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 640, 480));

        approvalspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setText("jLabel7");
        approvalspanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 96, -1, -1));

        javax.swing.GroupLayout gradient28Layout = new javax.swing.GroupLayout(gradient28);
        gradient28.setLayout(gradient28Layout);
        gradient28Layout.setHorizontalGroup(
            gradient28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        gradient28Layout.setVerticalGroup(
            gradient28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        approvalspanel.add(gradient28, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        gradient22.add(approvalspanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 640, 480));

        billspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setText("jLabel8");
        billspanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 128, -1, -1));

        javax.swing.GroupLayout gradient30Layout = new javax.swing.GroupLayout(gradient30);
        gradient30.setLayout(gradient30Layout);
        gradient30Layout.setHorizontalGroup(
            gradient30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        gradient30Layout.setVerticalGroup(
            gradient30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        billspanel.add(gradient30, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        accountspanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setText("Accounts");
        accountspanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 100, -1, -1));

        javax.swing.GroupLayout gradient32Layout = new javax.swing.GroupLayout(gradient32);
        gradient32.setLayout(gradient32Layout);
        gradient32Layout.setHorizontalGroup(
            gradient32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        gradient32Layout.setVerticalGroup(
            gradient32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        accountspanel1.add(gradient32, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        billspanel.add(accountspanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 410, 640, 480));

        approvalspanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setText("jLabel7");
        approvalspanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 96, -1, -1));

        javax.swing.GroupLayout gradient33Layout = new javax.swing.GroupLayout(gradient33);
        gradient33.setLayout(gradient33Layout);
        gradient33Layout.setHorizontalGroup(
            gradient33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        gradient33Layout.setVerticalGroup(
            gradient33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        approvalspanel1.add(gradient33, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        billspanel.add(approvalspanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 410, 640, 480));

        billspanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setText("jLabel8");
        billspanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 128, -1, -1));

        javax.swing.GroupLayout gradient34Layout = new javax.swing.GroupLayout(gradient34);
        gradient34.setLayout(gradient34Layout);
        gradient34Layout.setHorizontalGroup(
            gradient34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        gradient34Layout.setVerticalGroup(
            gradient34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        billspanel1.add(gradient34, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        accountspanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setText("Accounts");
        accountspanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 100, -1, -1));

        javax.swing.GroupLayout gradient35Layout = new javax.swing.GroupLayout(gradient35);
        gradient35.setLayout(gradient35Layout);
        gradient35Layout.setHorizontalGroup(
            gradient35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        gradient35Layout.setVerticalGroup(
            gradient35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        accountspanel2.add(gradient35, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        billspanel1.add(accountspanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 410, 640, 480));

        approvalspanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setText("jLabel7");
        approvalspanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 96, -1, -1));

        javax.swing.GroupLayout gradient36Layout = new javax.swing.GroupLayout(gradient36);
        gradient36.setLayout(gradient36Layout);
        gradient36Layout.setHorizontalGroup(
            gradient36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        gradient36Layout.setVerticalGroup(
            gradient36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        approvalspanel2.add(gradient36, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        billspanel1.add(approvalspanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 410, 640, 480));

        billspanel.add(billspanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 410, 640, 480));

        gradient22.add(billspanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 640, 480));

        transactionpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setText("jLabel9");
        transactionpanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 113, -1, -1));

        javax.swing.GroupLayout gradient31Layout = new javax.swing.GroupLayout(gradient31);
        gradient31.setLayout(gradient31Layout);
        gradient31Layout.setHorizontalGroup(
            gradient31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        gradient31Layout.setVerticalGroup(
            gradient31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        transactionpanel.add(gradient31, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        gradient22.add(transactionpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 640, 480));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(44, 62, 80));
        jLabel10.setText("Welcome Back");
        gradient22.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, -1, -1));

        namelabel.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        namelabel.setForeground(new java.awt.Color(44, 62, 80));
        namelabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        namelabel.setText("...");
        gradient22.add(namelabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, 430, -1));

        labelapproval.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelapproval.setForeground(new java.awt.Color(44, 62, 80));
        labelapproval.setText("0");
        gradient22.add(labelapproval, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 180, 40, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(44, 62, 80));
        jLabel12.setText("Awaiting for Account Approvals:");
        gradient22.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, -1, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(44, 62, 80));
        jLabel13.setText("Awaiting for Bill Approvals:");
        gradient22.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 220, -1, -1));

        labelbill.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelbill.setForeground(new java.awt.Color(44, 62, 80));
        labelbill.setText("0");
        gradient22.add(labelbill, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 220, 40, -1));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(gradient22, gridBagConstraints);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void homesideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homesideMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_homesideMouseClicked

    private void accountssideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountssideMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_accountssideMouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        accountspanel.setVisible(false);
        approvalspanel.setVisible(false);
        billspanel.setVisible(false);
        transactionpanel.setVisible(false);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        approvalspanel.setVisible(false);
        billspanel.setVisible(false);
        transactionpanel.setVisible(false);
        accountspanel.setVisible(true);
        loadAccountsFromDatabase();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        accountspanel.setVisible(false);
        billspanel.setVisible(false);
        transactionpanel.setVisible(false);
        approvalspanel.setVisible(true);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        accountspanel.setVisible(false);
        approvalspanel.setVisible(false);
        transactionpanel.setVisible(false);
        billspanel.setVisible(true);
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        accountspanel.setVisible(false);
        approvalspanel.setVisible(false);
        billspanel.setVisible(false);
        transactionpanel.setVisible(true);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void hospitalSuccessButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalSuccessButton3ActionPerformed
        performSearch();
    }//GEN-LAST:event_hospitalSuccessButton3ActionPerformed

    private void hospitalEmergencyButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalEmergencyButton2ActionPerformed
        clearSearch();
    }//GEN-LAST:event_hospitalEmergencyButton2ActionPerformed

    private void searchTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            performSearch();
        }
    }//GEN-LAST:event_searchTextFieldKeyPressed

    private void hospitalSuccessButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalSuccessButton1ActionPerformed
        String user = JOptionPane.showInputDialog(null, "Enter username:");
        if (user != null && !user.trim().isEmpty()) {
            String password = JOptionPane.showInputDialog(null, "Enter password:");
            if (password != null && !password.trim().isEmpty()) {
                String type = JOptionPane.showInputDialog(null, "Enter type (Admin/Employee/Patient):");
                if (type != null && !type.trim().isEmpty()) {
                    String finaltype = type.toUpperCase();
                    String approved = "true";

                    if (finaltype.equals("ADMIN") || finaltype.equals("EMPLOYEE") || finaltype.equals("PATIENT")) {
                        addAccountToDatabase(user, password, finaltype, approved);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid type entered. Must be Admin, Employee, or Patient.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Type cannot be empty.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Password cannot be empty.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Username cannot be empty.");
        }
    }//GEN-LAST:event_hospitalSuccessButton1ActionPerformed

    private void hospitalSuccessButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalSuccessButton2ActionPerformed
        Object[] selectedData = getSelectedAccountData();
        if (selectedData != null) {
            String oldUser = (String) selectedData[0];
            
            String newUser = JOptionPane.showInputDialog(this, "Username:", selectedData[0]);
            if (newUser != null) {
                String password = JOptionPane.showInputDialog(this, "Password:", selectedData[1]);
                String type = JOptionPane.showInputDialog(this, "Type:", selectedData[2]);
                String approved = JOptionPane.showInputDialog(this, "Approved:", selectedData[3]);
                
                updateAccountInDatabase(oldUser, newUser, password, type, approved);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an account to edit!", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_hospitalSuccessButton2ActionPerformed

    private void hospitalEmergencyButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalEmergencyButton1ActionPerformed
        Object[] selectedData = getSelectedAccountData();
        if (selectedData != null) {
            String user = (String) selectedData[0];
            deleteAccountFromDatabase(user);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an account to delete!", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_hospitalEmergencyButton1ActionPerformed

    private void hospitalPrimaryButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalPrimaryButton1ActionPerformed
        refreshTable();
    }//GEN-LAST:event_hospitalPrimaryButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel accountspanel;
    private javax.swing.JPanel accountspanel1;
    private javax.swing.JPanel accountspanel2;
    private javax.swing.JPanel accountsside;
    private hospital.management.AdminTable adminTable;
    private javax.swing.JPanel approvalspanel;
    private javax.swing.JPanel approvalspanel1;
    private javax.swing.JPanel approvalspanel2;
    private javax.swing.JPanel approvalsside;
    private javax.swing.JPanel billspanel;
    private javax.swing.JPanel billspanel1;
    private javax.swing.JPanel billsside;
    private hospital.management.Gradient2 gradient21;
    private hospital.management.Gradient2 gradient22;
    private hospital.management.Gradient2 gradient23;
    private hospital.management.Gradient2 gradient24;
    private hospital.management.Gradient2 gradient25;
    private hospital.management.Gradient2 gradient26;
    private hospital.management.Gradient2 gradient27;
    private hospital.management.Gradient2 gradient28;
    private hospital.management.Gradient2 gradient29;
    private hospital.management.Gradient2 gradient30;
    private hospital.management.Gradient2 gradient31;
    private hospital.management.Gradient2 gradient32;
    private hospital.management.Gradient2 gradient33;
    private hospital.management.Gradient2 gradient34;
    private hospital.management.Gradient2 gradient35;
    private hospital.management.Gradient2 gradient36;
    private javax.swing.JPanel homeside;
    private hospital.management.HospitalEmergencyButton hospitalEmergencyButton1;
    private hospital.management.HospitalEmergencyButton hospitalEmergencyButton2;
    private hospital.management.HospitalPrimaryButton hospitalPrimaryButton1;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton1;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton2;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelapproval;
    private javax.swing.JLabel labelbill;
    private javax.swing.JLabel namelabel;
    private javax.swing.JComboBox<String> searchColumnComboBox;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JPanel sidepanel;
    private javax.swing.JPanel transactionpanel;
    private javax.swing.JPanel transactionsside;
    // End of variables declaration//GEN-END:variables
}
