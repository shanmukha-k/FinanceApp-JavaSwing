import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;

// import com.formdev.flatlaf.FlatDarculaLaf;
// import com.formdev.flatlaf.FlatDarkLaf;
// import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;

public class MainApp extends JFrame {
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JTabbedPane tabbedPane;
    private int currentUserId;

    public MainApp() {
        try {
            // UIManager.setLookAndFeel(new FlatDarkLaf());            
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            // UIManager.setLookAndFeel(new FlatDarculaLaf());
            // UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("Financial Data Application");
        setSize(800,600); // Make the window cover the entire screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        // Initialize tabbed pane
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Add Login Tab
        JPanel loginPanel = createLoginPanel();
        tabbedPane.addTab("", loginPanel);

        // Display the main window
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(245, 245, 245));
        // loginPanel.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameLabel, gbc);

        loginUsernameField = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(loginUsernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(passwordLabel, gbc);

        loginPasswordField = new JPasswordField(12);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(loginPasswordField, gbc);

        GridBagConstraints gbcl = new GridBagConstraints();
        gbcl.insets = new Insets(10,0,0,0);

        JButton loginButton = new JButton("login");
        gbcl.gridx = 0; gbcl.gridy = 2; gbcl.gridwidth = 1; gbcl.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbcl);

        loginButton.addActionListener(e -> handleLogin());

        // Add a "Register" button to switch to the Registration Tab
        JButton registerButton = new JButton("Register");
        gbcl.gridx = 1; gbcl.gridy = 2; gbcl.gridwidth = 1; gbcl.anchor = GridBagConstraints.CENTER;
        loginPanel.add(registerButton, gbcl);

        registerButton.addActionListener(e -> openRegistrationTab());

        return loginPanel;
    }

    private JPanel createRegistrationPanel() {
        JPanel registrationPanel = new JPanel(new GridBagLayout());
        registrationPanel.setBackground(new Color(245, 245, 245));
        registrationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
    
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        registrationPanel.add(usernameLabel, gbc);
    
        registerUsernameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        registrationPanel.add(registerUsernameField, gbc);
    
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        registrationPanel.add(passwordLabel, gbc);
    
        registerPasswordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        registrationPanel.add(registerPasswordField, gbc);
    
        JButton registerButton = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        registrationPanel.add(registerButton, gbc);
    
        registerButton.addActionListener(e -> handleRegister());
    
        // Add "Login" button to switch back to Login panel
        JButton loginButton = new JButton("Back to Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        registrationPanel.add(loginButton, gbc);
    
        loginButton.addActionListener(e -> openLoginTab());
    
        return registrationPanel;
    }
    
    private void openLoginTab() {
        // Remove the registration tab
        int registerTabIndex = tabbedPane.indexOfTab("Register");
        if (registerTabIndex != -1) {
            tabbedPane.removeTabAt(registerTabIndex);
        }
    
        // Create and add the login panel
        JPanel loginPanel = createLoginPanel();
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.setSelectedComponent(loginPanel); // Automatically switch to the login tab
    }
    

    private void openRegistrationTab() {
        // Remove the login tab
        int loginTabIndex = tabbedPane.indexOfTab("Login");
        if (loginTabIndex != -1) {
            tabbedPane.removeTabAt(loginTabIndex);
        }
    
        // Create and add the registration panel
        JPanel registrationPanel = createRegistrationPanel();
        tabbedPane.addTab("Register", registrationPanel);
        tabbedPane.setSelectedComponent(registrationPanel); // Automatically switch to the registration tab
    }
    

    private void openFinanceOperations(int userId) {
        // Remove all tabs and add financial operations tab
        tabbedPane.removeAll();
    
        // Create the finance panel
        JPanel financePanel = new JPanel(new GridBagLayout());
        financePanel.setBackground(new Color(245, 245, 245));
        financePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
    
        // Display balance
        JLabel balanceLabel = new JLabel("Current Balance: ");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        financePanel.add(balanceLabel, gbc);
    
        JLabel balanceValueLabel = new JLabel();
        balanceValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        financePanel.add(balanceValueLabel, gbc);
    
        // Add Money Section
        addTransactionSection("Add Money:", "ADD", gbc, financePanel, balanceValueLabel);
    
        // Remove Money Section
        addTransactionSection("Remove Money:", "REMOVE", gbc, financePanel, balanceValueLabel);
    
        // Sign Out Button
        JButton signOutButton = new JButton("Sign Out");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER;
        financePanel.add(signOutButton, gbc);
    
        signOutButton.addActionListener(e -> {
            tabbedPane.removeAll();
            JPanel loginPanel = createLoginPanel();
            tabbedPane.addTab("Login", loginPanel);
            tabbedPane.setSelectedComponent(loginPanel);
        });
    
        // Add the finance panel to the tabbed pane
        tabbedPane.addTab("Finance Operations", financePanel);
    
        // Update balance on startup
        updateBalance(userId, balanceValueLabel);
    }
    
    private void addTransactionSection(String labelText, String transactionType, GridBagConstraints gbc, JPanel financePanel, JLabel balanceValueLabel) {
        JLabel transactionLabel = new JLabel(labelText);
        transactionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = (transactionType.equals("ADD")) ? 1 : 2; gbc.anchor = GridBagConstraints.WEST;
        financePanel.add(transactionLabel, gbc);
    
        JTextField amountField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = (transactionType.equals("ADD")) ? 1 : 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        financePanel.add(amountField, gbc);
    
        JButton button = new JButton(transactionType.equals("ADD") ? "Add" : "Remove");
        gbc.gridx = 2; gbc.gridy = (transactionType.equals("ADD")) ? 1 : 2; gbc.fill = GridBagConstraints.NONE;
        financePanel.add(button, gbc);
    
        button.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                FinanceDAO financeDAO = new FinanceDAO();
                financeDAO.addTransaction(currentUserId, transactionType, amount);
                updateBalance(currentUserId, balanceValueLabel);
                JOptionPane.showMessageDialog(this, "Transaction successful!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void updateBalance(int userId, JLabel balanceValueLabel) {
        FinanceDAO financeDAO = new FinanceDAO();
        double balance = financeDAO.getBalance(userId);
        balanceValueLabel.setText(String.format("\u20B9%.2f", balance));  // \u20B9 is the Unicode for ₹
    }
    

    private void handleLogin() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());
    
        // Check for admin credentials
        if ("admin".equals(username) && "admin123".equals(password)) {
            openAdminFinanceOperations();
        } else {
            UserDAO userDAO = new UserDAO();
            int userId = userDAO.authenticateUser(username, password);
    
            if (userId != -1) {
                currentUserId = userId;
                openFinanceOperations(userId);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    private void handleRegister() {
        String username = registerUsernameField.getText();
        String password = new String(registerPasswordField.getPassword());

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.registerUser(username, password);

        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please log in.");
            tabbedPane.removeAll();
            JPanel loginPanel = createLoginPanel();
            tabbedPane.addTab("Login", loginPanel);
            tabbedPane.setSelectedComponent(loginPanel);
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAdminFinanceOperations() {
    // Remove all tabs and add financial operations tab
    tabbedPane.removeAll();

    // Create the finance panel
    JPanel financePanel = new JPanel(new BorderLayout());
    financePanel.setBackground(new Color(245, 245, 245));
    financePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Create the table to show finance_info
    String[] columnNames = {"Finance ID", "User ID", "Transaction Type", "Amount", "Balance", "Date-Time"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    JTable table = new JTable(model);

    // Fetch all finance info and populate the table
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/finance", "shannu", "Shannu02*");
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM finance_info");
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            int financeId= rs.getInt("finance_id");
            int userId = rs.getInt("user_id");
            String transactionType = rs.getString("transaction_type");
            double amount = rs.getDouble("amount");
            double balance = rs.getDouble("balance");
            String time=rs.getString("transaction_date");
            model.addRow(new Object[]{financeId,userId, transactionType, amount, balance,time});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    JScrollPane scrollPane = new JScrollPane(table);
    financePanel.add(scrollPane, BorderLayout.CENTER);

    // Sign Out Button
    JButton signOutButton = new JButton("Sign Out");
    financePanel.add(signOutButton, BorderLayout.SOUTH);
    signOutButton.addActionListener(e -> {
        tabbedPane.removeAll();
        JPanel loginPanel = createLoginPanel();
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.setSelectedComponent(loginPanel);
    });

    // Add the finance panel to the tabbed pane
    tabbedPane.addTab("Admin Finance Operations", financePanel);
    tabbedPane.setSelectedComponent(financePanel);
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp());
    }
}
