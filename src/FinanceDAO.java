import java.sql.*;
import java.util.*;

public class FinanceDAO {
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3307/finance", "shannu", "Shannu02*");
    }

    public void addTransaction(int userId, String type, double amount) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
    
            double currentBalance = getCurrentBalance(userId, conn);
            double newBalance = type.equals("ADD") ? currentBalance + amount : currentBalance - amount;
    
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO finance_info (user_id, transaction_type, amount, balance) VALUES (?, ?, ?, ?)")) {
                stmt.setInt(1, userId);
                stmt.setString(2, type);
                stmt.setDouble(3, amount);
                stmt.setDouble(4, newBalance);
                stmt.executeUpdate();
            }
    
            try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE user_info SET balance = ? WHERE user_id = ?")) {
                updateStmt.setDouble(1, newBalance); // Use newBalance here
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
            }
    
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
    

    private double getCurrentBalance(int userId, Connection conn) throws SQLException {
        double balance = 0.0;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM finance_info WHERE user_id = ? ORDER BY transaction_date DESC LIMIT 1")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        }
        return balance;
    }

    public List<Map<String, Object>> getAllTransactions() {
        List<Map<String, Object>> transactions = new ArrayList<>();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            String sql = "SELECT * FROM finance_info";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                rs = stmt.executeQuery();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);
                        row.put(columnName, value);
                    }
                    transactions.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close ResultSet and Connection
            try {
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return transactions;
    }

    public double getBalance(int userId){
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM finance_info WHERE user_id = ? ORDER BY transaction_date DESC LIMIT 1")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
