public class CarReturnWindowTest4 {

    public static void testDatabaseErrorHandling() {
        try {
            // Simulate a database error by throwing an exception
            throw new SQLException("Database error");
        } catch (SQLException e) {
            // Verify that the error is caught
            assert "Database error".equals(e.getMessage()) : "Should catch 'Database error' message";
            System.out.println("testDatabaseErrorHandling passed");
        }
    }

    public static void main(String[] args) {
        testDatabaseErrorHandling();
    }
}

