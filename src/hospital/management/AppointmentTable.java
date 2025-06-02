package hospital.management;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppointmentTable extends JPanel {
    
    // Healthcare Green Gradient Color scheme
    private final Color headerColor = new Color(34, 139, 34);        // Forest Green for header
    private final Color headerTextColor = Color.WHITE;
    private final Color rowEvenColor = new Color(200, 240, 220);     // Light green tint
    private final Color rowOddColor = new Color(245, 255, 250);      // Very light green
    private final Color selectedColor = new Color(86, 171, 47);      // Your gradient start color
    private final Color borderColor = new Color(144, 206, 161);      // Medium green for borders
    private final Color textColor = new Color(34, 89, 34);           // Dark green for text
    
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    
    public AppointmentTable() {
        initializeTable();
        setupLayout();
    }
    
    private void initializeTable() {
        // Create table model with appointment columns
        String[] columnNames = {
            "ID", "User", "Type", "Date", "Time"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        table = new JTable(tableModel);
        
        // Configure table appearance
        setupTableAppearance();
    }
    
    private void setupTableAppearance() {
        // Table settings
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(borderColor);
        table.setSelectionBackground(selectedColor);
        table.setSelectionForeground(Color.WHITE);
        table.setOpaque(false);
        table.setForeground(textColor);
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(headerColor);
        header.setForeground(headerTextColor);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, borderColor));
        
        // Custom cell renderer for alternating row colors
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
        
        // Column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);  // ID
        columnModel.getColumn(1).setPreferredWidth(150); // User
        columnModel.getColumn(2).setPreferredWidth(120); // Type
        columnModel.getColumn(3).setPreferredWidth(100); // Date
        columnModel.getColumn(4).setPreferredWidth(80);  // Time
    }
    
    private void setupTableBehavior() {
        // Basic selection settings only (no interaction handlers)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setOpaque(false); // Allow gradient background to show through
        
        // Create scroll pane
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        scrollPane.getViewport().setOpaque(false);
        
        // Style scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setBackground(new Color(200, 240, 220));
        verticalScrollBar.setUI(new ModernScrollBarUI());
        
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setBackground(new Color(200, 240, 220));
        horizontalScrollBar.setUI(new ModernScrollBarUI());
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    
    // Custom renderer for alternating row colors with transparency
    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                // Make rows semi-transparent to show gradient background
                Color evenColor = new Color(rowEvenColor.getRed(), rowEvenColor.getGreen(), 
                                          rowEvenColor.getBlue(), 150); // 150/255 transparency
                Color oddColor = new Color(rowOddColor.getRed(), rowOddColor.getGreen(), 
                                         rowOddColor.getBlue(), 100);  // 100/255 transparency
                
                if (row % 2 == 0) {
                    c.setBackground(evenColor);
                } else {
                    c.setBackground(oddColor);
                }
                c.setForeground(textColor);
            } else {
                c.setBackground(selectedColor);
                c.setForeground(Color.WHITE);
            }
            
            // No special column styling needed for appointment table
            
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            return c;
        }
    }
    
    // Modern scrollbar UI
    private class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(144, 206, 161); // Medium green
            this.trackColor = new Color(200, 240, 220); // Light green
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    }
    
    // Public methods for external use
    public void addAppointment(Object[] appointmentData) {
        tableModel.addRow(appointmentData);
    }
    
    public void removeAppointment(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            tableModel.removeRow(row);
        }
    }
    
    public void updateAppointment(int row, Object[] appointmentData) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            for (int col = 0; col < appointmentData.length && col < tableModel.getColumnCount(); col++) {
                tableModel.setValueAt(appointmentData[col], row, col);
            }
        }
    }
    
    public void clearTable() {
        tableModel.setRowCount(0);
    }
    
    public void loadDataFromDatabase(Object[][] data) {
        clearTable();
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    public int getSelectedRow() {
        return table.getSelectedRow();
    }
    
    public Object getValueAt(int row, int column) {
        return table.getValueAt(row, column);
    }
    
    public Object[] getRowData(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            Object[] rowData = new Object[tableModel.getColumnCount()];
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                rowData[col] = tableModel.getValueAt(row, col);
            }
            return rowData;
        }
        return null;
    }
    
    public int getRowCount() {
        return tableModel.getRowCount();
    }
    
    public JTable getTable() {
        return table;
    }
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}