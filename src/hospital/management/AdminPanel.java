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
        loadApprovalsFromDatabase();
        loadPendingTransactionFromDatabase();
        loadTransactionFromDatabase();
        updateRegisteredAccountsCount();
        updateApprovalsCount();
        
        String username = userData.get("username");
        namelabel.setText(username);
    }
    
    public AdminPanel() {
        initComponents();
    }
    
    //Counts all registered accounts
    public void updateRegisteredAccountsCount() {
        if (con == null) {
            labelregister.setText("Database Error");
            return;
        }

        try {
            String query = "SELECT COUNT(*) FROM accounts WHERE approved = 'true'";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int registeredCount = rs.getInt(1);
                labelregister.setText(String.valueOf(registeredCount));
            } else {
                labelregister.setText("0");
            }

            rs.close();
            pst.close();

        } catch (SQLException ex) {
            System.out.println("Error counting registered accounts: " + ex.getMessage());
            labelregister.setText("Error counting accounts");
        }
    }
    
    //Counts all unapproved accounts
    public void updateApprovalsCount() {
        if (con == null) {
            labelregister.setText("Database Error");
            return;
        }

        try {
            String query = "SELECT COUNT(*) FROM accounts WHERE approved = 'false'";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int registeredCount = rs.getInt(1);
                labelapproval.setText(String.valueOf(registeredCount));
            } else {
                labelapproval.setText("0");
            }

            rs.close();
            pst.close();

        } catch (SQLException ex) {
            System.out.println("Error counting registered accounts: " + ex.getMessage());
            labelregister.setText("Error counting accounts");
        }
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
    
    //load all accounts from database into the table (approvals)
    public void loadApprovalsFromDatabase() {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String query = "SELECT user, password, type, approved FROM accounts WHERE approved = 'false'";
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
            approvaltable.loadDataFromDatabase(data);
            
            rs.close();
            pst.close();
            
        } catch (SQLException ex) {
            System.out.println("Error loading accounts: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading accounts: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Load all pending transactions from database into the BillTable
    public void loadPendingTransactionFromDatabase() {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Updated query for transactions table with the correct columns
            String query = "SELECT id, user, amount, employee, approved FROM transaction WHERE approved = 'pending'";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            // Create list to store data
            List<Object[]> dataList = new ArrayList<>();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("id"),
                    rs.getString("user"),
                    rs.getDouble("amount"),  // Using getDouble for amount column
                    rs.getString("employee"),
                    rs.getString("approved")
                };
                dataList.add(row);
            }

            // Convert list to array and load into BillTable
            Object[][] data = dataList.toArray(new Object[0][]);
            billTable.loadDataFromDatabase(data);  // Changed from approvaltable to billTable

            rs.close();
            pst.close();

        } catch (SQLException ex) {
            System.out.println("Error loading pending transactions: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading pending transactions: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Load all transactions from database into the BillTable1
    public void loadTransactionFromDatabase() {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Updated query for transactions table with the correct columns
            String query = "SELECT id, user, amount, employee, approved FROM transaction";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            // Create list to store data
            List<Object[]> dataList = new ArrayList<>();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("id"),
                    rs.getString("user"),
                    rs.getDouble("amount"),  // Using getDouble for amount column
                    rs.getString("employee"),
                    rs.getString("approved")
                };
                dataList.add(row);
            }

            // Convert list to array and load into BillTable
            Object[][] data = dataList.toArray(new Object[0][]);
            billTable1.loadDataFromDatabase(data);  // Changed from approvaltable to billTable

            rs.close();
            pst.close();

        } catch (SQLException ex) {
            System.out.println("Error loading pending transactions: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading pending transactions: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
    
    //Edit approved status in Database for transactions
    public boolean updateTransactionApprovalInDatabase(String transactionId, String approved) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            String query = "UPDATE transaction SET approved=? WHERE id=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, approved.toUpperCase());
            pst.setString(2, transactionId);

            int result = pst.executeUpdate();
            pst.close();

            if (result > 0) {
                // Refresh table data
                loadTransactionFromDatabase(); // Assuming you have this method
                JOptionPane.showMessageDialog(this, "Transaction approval status updated successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Transaction not found or no changes made!", 
                                            "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            System.out.println("Error updating transaction approval: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error updating transaction approval: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    //Delete stuff from database
    public boolean deleteAccountFromDatabase(String user) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(this, "Account deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            
        } catch (SQLException ex) {
            System.out.println("Error deleting account: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error deleting account: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    //Delete transactions from database
    public boolean deleteTransactionFromDatabase(String user) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete the transaction id: " + user + "?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM transaction WHERE ID=?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, user);
                
                int result = pst.executeUpdate();
                pst.close();
                
                if (result > 0) {
                    // Remove from table display
                    int selectedRow = billTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        billTable.removeUser(selectedRow);
                    }
                    JOptionPane.showMessageDialog(this, "Transaction deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            
        } catch (SQLException ex) {
            System.out.println("Error deleting account: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error deleting account: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    //Deny accounts
    public boolean denyAccountFromDatabase(String user) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to reject/delete the account: " + user + "?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM accounts WHERE user=?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, user);
                
                int result = pst.executeUpdate();
                pst.close();
                
                if (result > 0) {
                    // Remove from table display
                    int selectedRow = approvaltable.getSelectedRow();
                    if (selectedRow >= 0) {
                        approvaltable.removeUser(selectedRow);
                    }
                    JOptionPane.showMessageDialog(this, "Account deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            
        } catch (SQLException ex) {
            System.out.println("Error deleting account: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error deleting account: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    //Refresh Table
    public void refreshTable() {
        loadAccountsFromDatabase();
    }
    
    public Object[] getSelectedAccountData() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow >= 0) {
            return adminTable.getRowData(selectedRow);
        }
        return null;
    }
    
    public Object[] getSelectedApprovalData() {
        int selectedRow = approvaltable.getSelectedRow();
        if (selectedRow >= 0) {
            return approvaltable.getRowData(selectedRow);
        }
        return null;
    }
    
    public Object[] getSelectedApprovalBillData() {
        int selectedRow = billTable.getSelectedRow();
        if (selectedRow >= 0) {
            return billTable.getRowData(selectedRow);
        }
        return null;
    }
    
    private Object[] getSelectedTransactionData() {
        int selectedRow = billTable1.getSelectedRow();
        if (selectedRow != -1) {
            return billTable1.getRowData(selectedRow);
        }
        return null;
}
    
    private boolean isValidInput(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, fieldName + " cannot be empty.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    //approve accounts
    public boolean approveAccountFromDatabase(String user) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            // Confirm approval
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to approve account: " + user + "?", 
                "Confirm Approval", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String query = "UPDATE accounts SET approved = 'true' WHERE user = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, user);

                int result = pst.executeUpdate();
                pst.close();

                if (result > 0) {
                    // Remove from table display (since it's no longer pending approval)
                    int selectedRow = approvaltable.getSelectedRow();
                    if (selectedRow >= 0) {
                        approvaltable.removeUser(selectedRow);
                    }
                    JOptionPane.showMessageDialog(this, "Account approved successfully!", 
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found!", 
                                                "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error approving account: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error approving account: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
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
    
    //approve bills
    public boolean approveBillsFromDatabase(String user) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            // Confirm approval
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to Approve the transaction id: " + user + "?", 
                "Confirm Approval", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String query = "UPDATE transaction SET approved = 'APPROVED' WHERE ID = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, user);

                int result = pst.executeUpdate();
                pst.close();

                if (result > 0) {
                    // Remove from table display (since it's no longer pending approval)
                    int selectedRow = billTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        billTable.removeUser(selectedRow);
                    }
                    JOptionPane.showMessageDialog(this, "Bill approved successfully!", 
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found!", 
                                                "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error approving bill for the account: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error approving account: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    //deny bills
    public boolean denyBillsFromDatabase(String user) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to Reject the transaction id: " + user + "?", 
                "Confirm Approval", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                    String query = "UPDATE transaction SET approved = 'REJECTED' WHERE ID = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, user);

                int result = pst.executeUpdate();
                pst.close();

                if (result > 0) {
                    // Remove from table display (since it's no longer pending approval)
                    int selectedRow = billTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        billTable.removeUser(selectedRow);
                    }
                    JOptionPane.showMessageDialog(this, "Bill has been Rejected successfully!", 
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found!", 
                                                "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error dening bill for the account: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error approving account: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    
    //-------SEARCH FUNCTIONS----------
    
    private void performSearch() {
        String searchTerm = searchTextField.getText().trim();
        String searchColumn = (String) searchColumnComboBox.getSelectedItem();
        
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term!", "Empty Search", JOptionPane.WARNING_MESSAGE);
            loadAccountsFromDatabase();
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

    //-------TRANSACTION SEARCH FUNCTIONS----------

    private void performTransactionSearch() {
        String searchTerm = transactionSearchTextField.getText().trim();
        String searchColumn = (String) transactionSearchColumnComboBox.getSelectedItem();

        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term!", "Empty Search", JOptionPane.WARNING_MESSAGE);
            loadTransactionFromDatabase();
            return;
        }

        searchTransaction(searchTerm, searchColumn);
    }

    private void clearTransactionSearch() {
        transactionSearchTextField.setText("");
        loadTransactionFromDatabase(); // Assuming you have this method to load all transactions
    }

    public void searchTransaction(String searchTerm, String searchColumn) {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection!", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (billTable == null) {
            JOptionPane.showMessageDialog(this, "Transaction table not initialized!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Valid columns for transaction search
            String[] validColumns = {"id", "user", "amount", "employee", "approved"};
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

            String query = "SELECT id, user, amount, employee, approved FROM transaction WHERE " + searchColumn + " LIKE ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, searchTerm + "%");  // Prefix search
            ResultSet rs = pst.executeQuery();

            List<Object[]> dataList = new ArrayList<>();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("id"),
                    rs.getString("user"),
                    rs.getDouble("amount"),  // Using getDouble for amount
                    rs.getString("employee"),
                    rs.getString("approved")
                };
                dataList.add(row);
            }

            // Convert list to array and load into BillTable
            Object[][] data = dataList.toArray(new Object[0][]);
            billTable1.loadDataFromDatabase(data);

            if (dataList.size() == 0) {
                JOptionPane.showMessageDialog(this, "No transactions found matching your search criteria.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            }

            rs.close();
            pst.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching transactions: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
        jSeparator2 = new javax.swing.JSeparator();
        accountsside = new javax.swing.JPanel();
        gradient25 = new hospital.management.Gradient2();
        jLabel2 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        approvalsside = new javax.swing.JPanel();
        gradient26 = new hospital.management.Gradient2();
        jLabel3 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        billsside = new javax.swing.JPanel();
        gradient27 = new hospital.management.Gradient2();
        jLabel4 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        transactionsside = new javax.swing.JPanel();
        gradient21 = new hospital.management.Gradient2();
        jLabel5 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
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
        gradient28 = new hospital.management.Gradient2();
        approvaltable = new hospital.management.AdminTable();
        hospitalSuccessButton4 = new hospital.management.HospitalSuccessButton();
        hospitalEmergencyButton3 = new hospital.management.HospitalEmergencyButton();
        billspanel = new javax.swing.JPanel();
        gradient30 = new hospital.management.Gradient2();
        billTable = new hospital.management.BillTable();
        hospitalSuccessButton5 = new hospital.management.HospitalSuccessButton();
        hospitalEmergencyButton4 = new hospital.management.HospitalEmergencyButton();
        hospitalEmergencyButton5 = new hospital.management.HospitalEmergencyButton();
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
        gradient31 = new hospital.management.Gradient2();
        billTable1 = new hospital.management.BillTable();
        transactionSearchTextField = new javax.swing.JTextField();
        transactionSearchColumnComboBox = new javax.swing.JComboBox<>();
        hospitalSuccessButton6 = new hospital.management.HospitalSuccessButton();
        hospitalEmergencyButton6 = new hospital.management.HospitalEmergencyButton();
        hospitalSuccessButton7 = new hospital.management.HospitalSuccessButton();
        hospitalPrimaryButton2 = new hospital.management.HospitalPrimaryButton();
        jLabel10 = new javax.swing.JLabel();
        namelabel = new javax.swing.JLabel();
        labelapproval = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        labelbill = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        labelregister = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        labeltransactions = new javax.swing.JLabel();

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
        gradient29.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 220, -1));

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
        gradient25.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 220, -1));

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
        gradient26.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 220, -1));

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
        gradient27.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 220, -1));

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
        gradient21.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 220, -1));

        transactionsside.add(gradient21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -270, 220, 480));

        gradient24.add(transactionsside, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 220, 40));
        gradient24.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, -1, -1));
        gradient24.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, -1, -1));
        gradient24.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 220, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pictures/admincar.png"))); // NOI18N
        gradient24.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 200, 140));

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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyTyped(evt);
            }
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
        gradient23.add(hospitalSuccessButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 95, 80, -1));

        hospitalEmergencyButton2.setText("Clear");
        hospitalEmergencyButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalEmergencyButton2ActionPerformed(evt);
            }
        });
        gradient23.add(hospitalEmergencyButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 95, 80, -1));

        hospitalPrimaryButton1.setText("Refresh");
        hospitalPrimaryButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalPrimaryButton1ActionPerformed(evt);
            }
        });
        gradient23.add(hospitalPrimaryButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 320, 90, -1));

        accountspanel.add(gradient23, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        gradient22.add(accountspanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 640, 480));

        approvalspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradient28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        gradient28.add(approvaltable, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 410, 480));

        hospitalSuccessButton4.setText("Approve");
        hospitalSuccessButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalSuccessButton4ActionPerformed(evt);
            }
        });
        gradient28.add(hospitalSuccessButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 180, -1, -1));

        hospitalEmergencyButton3.setText("Reject");
        hospitalEmergencyButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalEmergencyButton3ActionPerformed(evt);
            }
        });
        gradient28.add(hospitalEmergencyButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 240, -1, -1));

        approvalspanel.add(gradient28, new org.netbeans.lib.awtextra.AbsoluteConstraints(-220, 0, 860, 480));

        gradient22.add(approvalspanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 640, 480));

        billspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradient30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        gradient30.add(billTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, -1, 480));

        hospitalSuccessButton5.setText("Approve");
        hospitalSuccessButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalSuccessButton5ActionPerformed(evt);
            }
        });
        gradient30.add(hospitalSuccessButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(705, 170, -1, -1));

        hospitalEmergencyButton4.setText("Reject");
        hospitalEmergencyButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalEmergencyButton4ActionPerformed(evt);
            }
        });
        gradient30.add(hospitalEmergencyButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(705, 220, -1, -1));

        hospitalEmergencyButton5.setText("Delete");
        hospitalEmergencyButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalEmergencyButton5ActionPerformed(evt);
            }
        });
        gradient30.add(hospitalEmergencyButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(705, 270, -1, -1));

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

        gradient31.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        gradient31.add(billTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, -1, 480));

        transactionSearchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transactionSearchTextFieldActionPerformed(evt);
            }
        });
        transactionSearchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                transactionSearchTextFieldKeyPressed(evt);
            }
        });
        gradient31.add(transactionSearchTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, 170, 30));

        transactionSearchColumnComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "id", "user", "amount", "employee", "approved" }));
        gradient31.add(transactionSearchColumnComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 60, 170, -1));

        hospitalSuccessButton6.setText("Search");
        hospitalSuccessButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalSuccessButton6ActionPerformed(evt);
            }
        });
        gradient31.add(hospitalSuccessButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 100, 80, -1));

        hospitalEmergencyButton6.setText("Clear");
        hospitalEmergencyButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalEmergencyButton6ActionPerformed(evt);
            }
        });
        gradient31.add(hospitalEmergencyButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 100, 82, -1));

        hospitalSuccessButton7.setText("Edit Approvals");
        hospitalSuccessButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalSuccessButton7ActionPerformed(evt);
            }
        });
        gradient31.add(hospitalSuccessButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 150, 170, -1));

        hospitalPrimaryButton2.setText("Refresh");
        hospitalPrimaryButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalPrimaryButton2ActionPerformed(evt);
            }
        });
        gradient31.add(hospitalPrimaryButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 200, 170, -1));

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

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(44, 62, 80));
        jLabel18.setText("Total Registered Accoutns:");
        gradient22.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, -1, -1));

        labelregister.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelregister.setForeground(new java.awt.Color(44, 62, 80));
        labelregister.setText("0");
        gradient22.add(labelregister, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 260, 40, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(44, 62, 80));
        jLabel19.setText("Total Transactions:");
        gradient22.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 300, -1, -1));

        labeltransactions.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labeltransactions.setForeground(new java.awt.Color(44, 62, 80));
        labeltransactions.setText("0");
        gradient22.add(labeltransactions, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 300, 40, -1));

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
        updateRegisteredAccountsCount();
        updateApprovalsCount();
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
        loadApprovalsFromDatabase();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        accountspanel.setVisible(false);
        approvalspanel.setVisible(false);
        transactionpanel.setVisible(false);
        billspanel.setVisible(true);
        loadPendingTransactionFromDatabase();
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        accountspanel.setVisible(false);
        approvalspanel.setVisible(false);
        billspanel.setVisible(false);
        transactionpanel.setVisible(true);
        loadTransactionFromDatabase();
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
        String user = JOptionPane.showInputDialog(this, "Enter username:");
        if (!isValidInput(user, "Username")) return;

        // Get password
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        if (!isValidInput(password, "Password")) return;

        // Get type with dropdown selection
        String[] typeOptions = {"Admin", "Employee", "Patient"};
        String type = (String) JOptionPane.showInputDialog(
            this,
            "Select account type:",
            "Account Type",
            JOptionPane.QUESTION_MESSAGE,
            null,
            typeOptions,
            typeOptions[0] // Default selection
        );

        // Check if user cancelled or didn't select anything
        if (type == null) {
            JOptionPane.showMessageDialog(this, "Account type selection cancelled.", 
                                        "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Add account to database
        String approved = "true";
        addAccountToDatabase(user.trim(), password.trim(), type.toUpperCase(), approved);
    }//GEN-LAST:event_hospitalSuccessButton1ActionPerformed

    private void hospitalSuccessButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalSuccessButton2ActionPerformed
        Object[] selectedData = getSelectedAccountData();
        if (selectedData != null) {
            String oldUser = (String) selectedData[0];
            String currentUser = oldUser;
            String currentPassword = (String) selectedData[1];
            String currentType = (String) selectedData[2];
            String currentApproved = (String) selectedData[3];

            boolean continueEditing = true;
            StringBuilder editLog = new StringBuilder("Edit Results:\n");
            boolean hasChanges = false;

            while (continueEditing) {
                // Show current values and ask which column to edit
                String[] columns = {"Username", "Password", "Type", "Approved", "Finish Editing"};
                String currentValues = String.format(
                    "Current Values:\nUsername: %s\nPassword: %s\nType: %s\nApproved: %s\n\nWhich column would you like to edit?",
                    currentUser, currentPassword, currentType, currentApproved
                );

                String selectedColumn = (String) JOptionPane.showInputDialog(
                    this,
                    currentValues,
                    "Select Column to Edit",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    columns,
                    columns[0]
                );

                if (selectedColumn == null || selectedColumn.equals("Finish Editing")) {
                    continueEditing = false;
                } else {
                    String oldValue = "";
                    String newValue = "";

                    switch (selectedColumn) {
                        case "Username":
                            oldValue = currentUser;
                            newValue = JOptionPane.showInputDialog(this, "Enter new Username:", currentUser);
                            if (newValue != null && !newValue.equals(oldValue)) {
                                currentUser = newValue;
                                editLog.append(String.format("Username: '%s' → '%s'\n", oldValue, newValue));
                                hasChanges = true;
                            }
                            break;

                        case "Password":
                            oldValue = currentPassword;
                            newValue = JOptionPane.showInputDialog(this, "Enter new Password:", currentPassword);
                            if (newValue != null && !newValue.equals(oldValue)) {
                                currentPassword = newValue;
                                editLog.append(String.format("Password: '%s' → '%s'\n", oldValue, newValue));
                                hasChanges = true;
                            }
                            break;

                        case "Type":
                            oldValue = currentType;
                            String[] typeOptions = {"ADMIN", "EMPLOYEE", "PATIENT"};
                            newValue = (String) JOptionPane.showInputDialog(
                                this,
                                "Select new Type:",
                                "Edit Type",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                typeOptions,
                                currentType
                            );
                            if (newValue != null && !newValue.equals(oldValue)) {
                                currentType = newValue;
                                editLog.append(String.format("Type: '%s' → '%s'\n", oldValue, newValue));
                                hasChanges = true;
                            }
                            break;

                        case "Approved":
                            oldValue = currentApproved;
                            String[] approvedOptions = {"true", "false"};
                            newValue = (String) JOptionPane.showInputDialog(
                                this,
                                "Select new Approved status:",
                                "Edit Approved Status",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                approvedOptions,
                                currentApproved
                            );
                            if (newValue != null && !newValue.equals(oldValue)) {
                                currentApproved = newValue;
                                editLog.append(String.format("Approved: '%s' → '%s'\n", oldValue, newValue));
                                hasChanges = true;
                            }
                            break;
                    }

                    // If user cancelled the input, ask if they want to continue
                    if (newValue == null) {
                        int choice = JOptionPane.showConfirmDialog(
                            this,
                            "Edit cancelled. Do you want to continue editing other columns?",
                            "Continue Editing?",
                            JOptionPane.YES_NO_OPTION
                        );
                        continueEditing = (choice == JOptionPane.YES_OPTION);
                    }
                }
            }

            // If changes were made, update database and show results
            if (hasChanges) {
                try {
                    updateAccountInDatabase(oldUser, currentUser, currentPassword, currentType, currentApproved);
                    editLog.append("\n✓ Changes saved successfully to database!");
                    JOptionPane.showMessageDialog(
                        this,
                        editLog.toString(),
                        "Edit Complete",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error saving changes: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No changes were made.",
                    "Edit Complete",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } else {
            JOptionPane.showMessageDialog(
                this, 
                "Please select an account to edit!", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE
            );
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

    private void searchTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyTyped
        //NOTHING TO USE
    }//GEN-LAST:event_searchTextFieldKeyTyped

    private void hospitalSuccessButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalSuccessButton4ActionPerformed
        Object[] selectedData = getSelectedApprovalData();
        if (selectedData != null) {
            String user = (String) selectedData[0];
            approveAccountFromDatabase(user);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an account to approve!", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_hospitalSuccessButton4ActionPerformed

    private void hospitalEmergencyButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalEmergencyButton3ActionPerformed
        Object[] selectedData = getSelectedApprovalData();
        if (selectedData != null) {
            String user = (String) selectedData[0];
            denyAccountFromDatabase(user);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an account to deny!", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_hospitalEmergencyButton3ActionPerformed

    private void hospitalSuccessButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalSuccessButton5ActionPerformed
        Object[] selectedData = getSelectedApprovalBillData();
        if (selectedData != null) {
            String user = (String) selectedData[0];
            approveBillsFromDatabase(user);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a bill to approve!", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_hospitalSuccessButton5ActionPerformed

    private void hospitalEmergencyButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalEmergencyButton4ActionPerformed
        Object[] selectedData = getSelectedApprovalBillData();
        if (selectedData != null) {
            String user = (String) selectedData[0];
            denyBillsFromDatabase(user);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a bill to deny!", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_hospitalEmergencyButton4ActionPerformed

    private void hospitalEmergencyButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalEmergencyButton5ActionPerformed
        Object[] selectedData = getSelectedApprovalBillData();
        if (selectedData != null) {
            String user = (String) selectedData[0];
            deleteAccountFromDatabase(user);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a bill to delete!", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_hospitalEmergencyButton5ActionPerformed

    private void transactionSearchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionSearchTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_transactionSearchTextFieldActionPerformed

    private void hospitalSuccessButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalSuccessButton6ActionPerformed
        performTransactionSearch();
    }//GEN-LAST:event_hospitalSuccessButton6ActionPerformed

    private void hospitalEmergencyButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalEmergencyButton6ActionPerformed
        clearTransactionSearch();
    }//GEN-LAST:event_hospitalEmergencyButton6ActionPerformed

    private void transactionSearchTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_transactionSearchTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            performTransactionSearch();
        }
    }//GEN-LAST:event_transactionSearchTextFieldKeyPressed

    private void hospitalPrimaryButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalPrimaryButton2ActionPerformed
        loadTransactionFromDatabase();
    }//GEN-LAST:event_hospitalPrimaryButton2ActionPerformed

    private void hospitalSuccessButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalSuccessButton7ActionPerformed
        // TODO add your handling code here:
        Object[] selectedData = getSelectedTransactionData();
        if (selectedData != null) {
            String transactionId = (String) selectedData[0]; // ID is in column 0
            String currentApproval = (String) selectedData[4]; // Approved is in column 4

            // Show options for approval status
            String[] options = {"approved", "pending", "rejected"};
            String newApproval = (String) JOptionPane.showInputDialog(
                this,
                "Current Status: " + currentApproval + "\nSelect new approval status:",
                "Update Transaction Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                currentApproval
            );

            if (newApproval != null && !newApproval.equals(currentApproval)) {
                updateTransactionApprovalInDatabase(transactionId, newApproval);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a transaction to edit!", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_hospitalSuccessButton7ActionPerformed

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
    private hospital.management.AdminTable approvaltable;
    private hospital.management.BillTable billTable;
    private hospital.management.BillTable billTable1;
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
    private hospital.management.HospitalEmergencyButton hospitalEmergencyButton3;
    private hospital.management.HospitalEmergencyButton hospitalEmergencyButton4;
    private hospital.management.HospitalEmergencyButton hospitalEmergencyButton5;
    private hospital.management.HospitalEmergencyButton hospitalEmergencyButton6;
    private hospital.management.HospitalPrimaryButton hospitalPrimaryButton1;
    private hospital.management.HospitalPrimaryButton hospitalPrimaryButton2;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton1;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton2;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton3;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton4;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton5;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton6;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel labelapproval;
    private javax.swing.JLabel labelbill;
    private javax.swing.JLabel labelregister;
    private javax.swing.JLabel labeltransactions;
    private javax.swing.JLabel namelabel;
    private javax.swing.JComboBox<String> searchColumnComboBox;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JPanel sidepanel;
    private javax.swing.JComboBox<String> transactionSearchColumnComboBox;
    private javax.swing.JTextField transactionSearchTextField;
    private javax.swing.JPanel transactionpanel;
    private javax.swing.JPanel transactionsside;
    // End of variables declaration//GEN-END:variables
}
