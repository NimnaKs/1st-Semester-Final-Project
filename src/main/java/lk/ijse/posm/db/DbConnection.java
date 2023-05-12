package lk.ijse.posm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
        private final static String URL = "jdbc:mysql://localhost:3306/postOffice";
        private final static Properties props = new Properties();

        static {
            props.setProperty("user", "root");
            props.setProperty("password", "ijse@200108");
        }

        private static DbConnection dbConnection;
        private Connection connection;

        private DbConnection() throws SQLException {
            connection = DriverManager.getConnection(URL, props);
        }

        public static DbConnection getInstance() throws SQLException {
            if(dbConnection == null) {
                return dbConnection = new DbConnection();
            } else {
                return dbConnection;
            }
        }
        public Connection getConnection() {
            return connection;
        }
}


