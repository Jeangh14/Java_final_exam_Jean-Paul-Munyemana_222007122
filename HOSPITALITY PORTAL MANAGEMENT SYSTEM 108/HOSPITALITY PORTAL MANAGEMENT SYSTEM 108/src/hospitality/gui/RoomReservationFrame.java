package hospitality.gui;
import com.hospitality.RoomDAO;
import hospitality.model.Guest;
import hospitality.model.Room;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RoomReservationFrame extends JFrame{
    private Guest guest;
    private JComboBox<Room> roomComboBox;
    private JTextArea descriptionArea;
    private JLabel priceLabel, categoryLabel;
    
    public RoomReservationFrame(Guest guest) {
        this.guest = guest;
        initializeUI();
        loadRooms();
    }
    
    private void initializeUI() {
        setTitle("Book Room - " + guest.getFullName());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        formPanel.add(new JLabel("Select Room:"));
        roomComboBox = new JComboBox<>();
        roomComboBox.addActionListener(e -> updateRoomDetails());
        formPanel.add(roomComboBox);
        
        formPanel.add(new JLabel("Category:"));
        categoryLabel = new JLabel("-");
        formPanel.add(categoryLabel);
        
        formPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane);
        
        formPanel.add(new JLabel("Price per Night:"));
        priceLabel = new JLabel("$0.00");
        formPanel.add(priceLabel);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton bookButton = new JButton("Book Room");
        JButton payButton = new JButton("Pay Now");
        JButton backButton = new JButton("Back");
        
        buttonPanel.add(bookButton);
        buttonPanel.add(payButton);
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        bookButton.addActionListener(new BookButtonListener());
        payButton.addActionListener(e -> processPayment());
        backButton.addActionListener(e -> dispose());
    }
    
    private void loadRooms() {
        RoomDAO roomDAO = new RoomDAO();
        List<Room> rooms = roomDAO.getAllAvailableRooms();
        
        for (Room room : rooms) {
            roomComboBox.addItem(room);
        }
        
        if (rooms.size() > 0) {
            updateRoomDetails();
        }
    }
    
    private void updateRoomDetails() {
        Room selectedRoom = (Room) roomComboBox.getSelectedItem();
        if (selectedRoom != null) {
            categoryLabel.setText(selectedRoom.getCategory());
            descriptionArea.setText(selectedRoom.getDescription());
            priceLabel.setText("$" + selectedRoom.getPriceOrValue());
        }
    }
    
    private class BookButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Room selectedRoom = (Room) roomComboBox.getSelectedItem();
            if (selectedRoom != null) {
                JOptionPane.showMessageDialog(RoomReservationFrame.this,
                    "Room booked successfully!\n" +
                    "Room: " + selectedRoom.getName() + "\n" +
                    "Category: " + selectedRoom.getCategory() + "\n" +
                    "Price: $" + selectedRoom.getPriceOrValue() + " per night",
                    "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void processPayment() {
        Room selectedRoom = (Room) roomComboBox.getSelectedItem();
        if (selectedRoom != null) {
            int option = JOptionPane.showConfirmDialog(this,
                "Total Amount: $" + selectedRoom.getPriceOrValue() + " per night\n" +
                "Proceed with payment?",
                "Payment Confirmation", JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                    "Payment processed successfully!\n" +
                    "Thank you for your payment.\n" +
                    "Room: " + selectedRoom.getName() + " has been booked.",
                    "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        }
    }
}


