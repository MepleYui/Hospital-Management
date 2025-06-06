
package hospital.management;

import java.sql.*;
import javax.swing.*;

public class SignUpDialog extends javax.swing.JDialog {

    /**
     * Creates new form SignUpDialog
     */
    
    Connection con;
    public SignUpDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gradient1 = new hospital.management.Gradient();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        hospitalSuccessButton1 = new hospital.management.HospitalSuccessButton();
        hospitalSecondaryButton1 = new hospital.management.HospitalSecondaryButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(300, 380));
        setMinimumSize(new java.awt.Dimension(300, 380));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradient1.setMaximumSize(new java.awt.Dimension(300, 380));
        gradient1.setMinimumSize(new java.awt.Dimension(300, 380));
        gradient1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(44, 62, 80));
        jLabel1.setText("Register An Account");
        gradient1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 40, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(44, 62, 80));
        jLabel2.setText("Username:");
        gradient1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 80, 30));

        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        gradient1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 170, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(44, 62, 80));
        jLabel3.setText("Password:");
        gradient1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 80, 30));

        jTextField2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        gradient1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 170, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(44, 62, 80));
        jLabel4.setText("Re-type Pass:");
        gradient1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 100, 30));

        jTextField3.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        gradient1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 170, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(44, 62, 80));
        jLabel5.setText("Sign up as:");
        gradient1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 80, 30));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Patient", "Employee", "Admin" }));
        gradient1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 100, 30));

        hospitalSuccessButton1.setText("Register");
        hospitalSuccessButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalSuccessButton1ActionPerformed(evt);
            }
        });
        gradient1.add(hospitalSuccessButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, -1, -1));

        hospitalSecondaryButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hospitalSecondaryButton1ActionPerformed(evt);
            }
        });
        gradient1.add(hospitalSecondaryButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, -1, -1));

        getContentPane().add(gradient1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 380));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void hospitalSecondaryButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalSecondaryButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_hospitalSecondaryButton1ActionPerformed

    private void hospitalSuccessButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hospitalSuccessButton1ActionPerformed
        // TODO add your handling code here
        
        String user = jTextField1.getText();
    String password = jTextField2.getText();
    String retype = jTextField3.getText();
    String type = jComboBox1.getSelectedItem().toString();
    String approved = "false";

    if (retype.equals(password)) {
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
                JOptionPane.showMessageDialog(null, "Username '" + user + "' is already taken. Please choose a different username.", "Username Taken", JOptionPane.WARNING_MESSAGE);
                return; // or continue to allow user to try again
            }

            rs.close();
            checkPst.close();

            // If username is available, proceed with registration
            String sql = "INSERT INTO accounts (user, password, type, approved) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, password);
            pst.setString(3, type);
            pst.setString(4, approved);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Registered Complete!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to Register your account.");
            }
            pst.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error adding account to database: " + ex.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(null, "Your password does not match!");
    }
    }//GEN-LAST:event_hospitalSuccessButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(SignUpDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignUpDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignUpDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignUpDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SignUpDialog dialog = new SignUpDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private hospital.management.Gradient gradient1;
    private hospital.management.HospitalSecondaryButton hospitalSecondaryButton1;
    private hospital.management.HospitalSuccessButton hospitalSuccessButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
