package hospital.management;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminTable extends JPanel {
    
    // Color scheme matching your gradient
    private final Color headerColor = new Color(52, 73, 94);        // #34495e - Dark Gray
    private final Color headerTextColor = Color.WHITE;
    private final Color rowEvenColor = new Color(236, 240, 241);    // #ecf0f1 - Very Light Gray
    private final Color rowOddColor = new Color(255, 255, 255);     // #ffffff - White
    private final Color selectedColor = new Color(41, 128, 185);    // #2980b9 - Professional Blue
    private final Color borderColor = new Color(189, 195, 199);     // #bdc3c7 - Light Gray
    private final Color textColor = new Color(44, 62, 80);          // #2c3e50 - Charcoal
    
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    
    public AdminTable() {
        initializeTable();
        setupLayout();
    }
    
    private void initializeTable() {
        // Create table model with database columns
        String[] columnNames = {
            "User", "Password", "Type", "Approved"
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
        columnModel.getColumn(0).setPreferredWidth(150); // User
        columnModel.getColumn(1).setPreferredWidth(200); // Password
        columnModel.getColumn(2).setPreferredWidth(120); // Type
        columnModel.getColumn(3).setPreferredWidth(100); // Approved
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
        verticalScrollBar.setBackground(new Color(236, 240, 241));
        verticalScrollBar.setUI(new ModernScrollBarUI());
        
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setBackground(new Color(236, 240, 241));
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
            
            // Special styling for approved column
            if (column == 3 && !isSelected) { // Approved column
                String approved = (String) value;
                if ("Yes".equals(approved) || "true".equalsIgnoreCase(approved) || "1".equals(approved)) {
                    c.setForeground(new Color(39, 174, 96)); // Green
                } else if ("No".equals(approved) || "false".equalsIgnoreCase(approved) || "0".equals(approved)) {
                    c.setForeground(new Color(231, 76, 60)); // Red
                }
            }
            
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            return c;
        }
    }
    
    // Modern scrollbar UI
    private class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(149, 165, 166);
            this.trackColor = new Color(236, 240, 241);
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
    public void addUser(Object[] userData) {
        tableModel.addRow(userData);
    }
    
    public void removeUser(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            tableModel.removeRow(row);
        }
    }
    
    public void updateUser(int row, Object[] userData) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            for (int col = 0; col < userData.length && col < tableModel.getColumnCount(); col++) {
                tableModel.setValueAt(userData[col], row, col);
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