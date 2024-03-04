package restaurant.infrastructure.connfactories;

public class SqlTransactionStatements {
    public static String sqlCreateTransaction = "INSERT INTO transactions (id, earnings, client_email) VALUES (?,0,?);";
    public static String sqlCloseTransaction = "UPDATE transactions SET status = 'FINISHED', earnings = ? WHERE id = ? and status = 'IN_PROGRESS';";
    public static String sqlFindTransaction = "SELECT * FROM transactions WHERE id = ?";
}
