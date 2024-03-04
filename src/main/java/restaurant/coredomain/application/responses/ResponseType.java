package restaurant.coredomain.application.responses;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseType {

    public List<String> messages;
    public boolean isSuccess = false;
    public ResponseType(List<String> messages) {
        this.messages = messages;
    }

    public ResponseType(String message, boolean isSuccess) {
        this.messages = new ArrayList<>();
        messages.add(message);
        this.isSuccess = isSuccess;
    }

    public static String OrderDoesNotExist(String email) {
        return "Order does not exist in current email: " + email;
    }
    public static String DishNotFoundWithName(String name) {
        return "Dish not found with name" + name;
    }
    public static String AmountError(String dishName) {
        return "The restaurant does not have enough amount of this dishname:" + dishName;
    }

    // have the same value with AmountError (database multithreading handling)
    public static String ErrorWhileUpdatingAmount(String dishName) {
        return "The restaurant does not have enough amount of this dishname: " + dishName;
    }


    public static String OrderCreated = "OK! Order was created!";
    public static String OrderAppended = "OK! Dish was appended!";
    public static String UnAuthenticated = "User not found";
    public static String ErrorCreatingOrder = "Cant create order";
    public static String ErrorAppendOrder = "Cant append dish to order because it is completed order";
    public static String ErrorWithTransaction = "Can't create order session now";
    public static String EmailNotFound = "EmailNotFound";
    public static String OrderCantBePaid = "Order cant be paid";
    public static String OrderWasSuccessfullyPaid = "Order was paid! Good day";
}
