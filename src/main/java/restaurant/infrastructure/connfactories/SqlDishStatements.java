package restaurant.infrastructure.connfactories;

public class SqlDishStatements {

    public static String getAllDishes = "SELECT * FROM dishes";
    public static String dishFindByTitle = "SELECT * FROM dishes WHERE title = ?";
    public static String dishFindById = "SELECT * FROM dishes WHERE id = ?";
    public static String addDish = "INSERT INTO dishes (title, price, amount, duration_seconds) VALUES (?, ?, ?, ?)";
    public static String removeDish = "DELETE FROM dishes where id = ?";
    public static String updateDish = "UPDATE dishes SET price = ?, amount = ?, duration_seconds = ? WHERE id = ?";

}
