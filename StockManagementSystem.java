import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class StockManagementSystem {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField productField, quantityField, priceField;
    private HashMap<String, Integer> stockLevels;

    public StockManagementSystem() {
        stockLevels = new HashMap<>();
        frame = new JFrame("Stock Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new String[] { "Product", "Quantity", "Price" }, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        frame.add(tableScrollPane, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add / Update Product"));
        inputPanel.add(new JLabel("Product Name:"));
        productField = new JTextField();
        inputPanel.add(productField);
        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);
        JButton addButton = new JButton("Add / Update Product");
        inputPanel.add(addButton);
        JButton clearButton = new JButton("Clear Fields");
        inputPanel.add(clearButton);
        frame.add(inputPanel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton deleteButton = new JButton("Delete Selected");
        JButton refreshButton = new JButton("Refresh Stock");
        JButton placeOrderButton = new JButton("Place Order");
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(placeOrderButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        addButton.addActionListener(e -> addOrUpdateProduct());
        clearButton.addActionListener(e -> clearFields());
        deleteButton.addActionListener(e -> deleteSelectedProduct());
        refreshButton.addActionListener(e -> refreshStock());
        placeOrderButton.addActionListener(e -> openPlaceOrderPage());
        frame.setVisible(true);
    }

    private void addOrUpdateProduct() {
        String product = productField.getText();
        String quantityText = quantityField.getText();
        String priceText = priceField.getText();
        if (product.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);
            stockLevels.put(product, stockLevels.getOrDefault(product, 0) + quantity);
            boolean found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(product)) {
                    tableModel.setValueAt(stockLevels.get(product), i, 1);
                    tableModel.setValueAt(price, i, 2);
                    found = true;
                    break;
                }
            }
            if (!found) {
                tableModel.addRow(new Object[] { product, quantity, price });
            }
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Quantity and Price must be valid numbers!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a product to delete!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String product = (String) tableModel.getValueAt(selectedRow, 0);
        stockLevels.remove(product);
        tableModel.removeRow(selectedRow);
    }

    private void refreshStock() {
        tableModel.setRowCount(0);
        for (String product : stockLevels.keySet()) {
            tableModel.addRow(new Object[] { product, stockLevels.get(product), "N/A" });
        }
    }

    private void openPlaceOrderPage() {
        new PlaceOrderPage(stockLevels);
    }

    private void clearFields() {
        productField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StockManagementSystem::new);
    }
}