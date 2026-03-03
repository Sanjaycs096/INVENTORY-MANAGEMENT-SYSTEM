import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class PlaceOrderPage {
    private JFrame orderFrame;
    private JTextField orderProductField, orderQuantityField;
    private HashMap<String, Integer> stockLevels;

    public PlaceOrderPage(HashMap<String, Integer> stockLevels) {
        this.stockLevels = stockLevels;

        // Create the place order frame
        orderFrame = new JFrame("Place Order");
        orderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        orderFrame.setSize(400, 300);
        orderFrame.setLayout(new GridLayout(4, 2, 10, 10));

        // Add labels and text fields for product selection and order quantity
        orderFrame.add(new JLabel("Product Name:"));
        orderProductField = new JTextField();
        orderFrame.add(orderProductField);

        orderFrame.add(new JLabel("Order Quantity:"));
        orderQuantityField = new JTextField();
        orderFrame.add(orderQuantityField);

        // Add buttons for placing an order and clearing the fields
        JButton orderButton = new JButton("Place Order");
        orderFrame.add(orderButton);
        JButton clearOrderButton = new JButton("Clear Fields");
        orderFrame.add(clearOrderButton);

        // Action listeners for buttons
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });

        clearOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearOrderFields();
            }
        });

        orderFrame.setVisible(true);
    }

    private void placeOrder() {
        String product = orderProductField.getText();
        String orderQuantityText = orderQuantityField.getText();

        if (product.isEmpty() || orderQuantityText.isEmpty()) {
            JOptionPane.showMessageDialog(orderFrame, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int orderQuantity = Integer.parseInt(orderQuantityText);

            if (!stockLevels.containsKey(product)) {
                JOptionPane.showMessageDialog(orderFrame, "Product not available in stock!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int availableStock = stockLevels.get(product);
            if (orderQuantity > availableStock) {
                JOptionPane.showMessageDialog(orderFrame, "Insufficient stock available!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            stockLevels.put(product, availableStock - orderQuantity);
            JOptionPane.showMessageDialog(orderFrame, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearOrderFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(orderFrame, "Order Quantity must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearOrderFields() {
        orderProductField.setText("");
        orderQuantityField.setText("");
    }
}
