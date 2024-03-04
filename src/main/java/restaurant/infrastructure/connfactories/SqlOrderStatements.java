package restaurant.infrastructure.connfactories;

public class SqlOrderStatements {
    public static String sqlAddOrder = "INSERT INTO orders (transaction_id, finished_at) VALUES (?, ?) RETURNING id;";
    public static String sqlRemoveOrder = "DELETE FROM orders where id = ?";
    public static String sqlFindOrdersOwnToClient = "SELECT * FROM orders INNER JOIN dish_order ON orders.id = dish_order.order_id INNER JOIN dishes ON dishes.id = dish_order.dish_id WHERE transaction_id = ?";
    public static String sqlUpdateOrder = "UPDATE orders SET finished_at = ? WHERE id = ? and ? > finished_at;";
}
