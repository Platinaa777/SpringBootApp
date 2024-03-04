package restaurant.auth.repositories;

public class SqlAuthStatements {
    public static String sqlCreateUser = "INSERT INTO clients (email, password, role) VALUES (?, ?, ?);";
    public static String sqlUserExist = "SELECT * from clients WHERE email = ?";
    public static String sqlUserSelect = "SELECT * FROM clients WHERE email = ?";
}
