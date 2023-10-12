import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class BatchInsertComparison {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/your_database"; // Use database URL here
        String username = "your_username";
        String password = "your_password";
        int recordsCount = 1000;

        try {
            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Create the Temp table if it doesn't exist
            createTempTable(connection);

            // Perform individual insert and measure time
            long start = System.currentTimeMillis();
            insertIndividualRecords(connection, recordsCount);
            long end = System.currentTimeMillis();
            System.out.println("Time taken for individual inserts: " + (end - start) + " ms");

            // Perform batch insert and measure time
            start = System.currentTimeMillis();
            insertBatchRecords(connection, recordsCount);
            end = System.currentTimeMillis();
            System.out.println("Time taken for batch inserts: " + (end - start) + " ms");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTempTable(Connection connection) throws SQLException {
        // SQL statement to create the Temp table if it doesn't exist
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Temp (num1 DOUBLE, num2 DOUBLE, num3 DOUBLE)";
        PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL);
        preparedStatement.execute();
    }

    private static void insertIndividualRecords(Connection connection, int recordsCount) throws SQLException {
        // SQL statement for individual record insert
        String insertSQL = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

        Random random = new Random();
        for (int i = 0; i < recordsCount; i++) {
            // Set random values for each column
            preparedStatement.setDouble(1, random.nextDouble());
            preparedStatement.setDouble(2, random.nextDouble());
            preparedStatement.setDouble(3, random.nextDouble());

            // Execute the insert
            preparedStatement.executeUpdate();
        }
    }

    private static void insertBatchRecords(Connection connection, int recordsCount) throws SQLException {
        // SQL statement for batch record insert
        String insertSQL = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

        Random random = new Random();
        for (int i = 0; i < recordsCount; i++) {
            // Set random values for each column
            preparedStatement.setDouble(1, random.nextDouble());
            preparedStatement.setDouble(2, random.nextDouble());
            preparedStatement.setDouble(3, random.nextDouble());

            // Add the batch
            preparedStatement.addBatch();
        }

        // Execute the batch insert
        preparedStatement.executeBatch();
    }
}
