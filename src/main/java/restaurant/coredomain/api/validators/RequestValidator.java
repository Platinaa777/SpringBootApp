package restaurant.coredomain.api.validators;

public class RequestValidator {

    public static boolean IsLong(String word) {
        try {
            var number = Long.valueOf(word);

            return true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid integer input");
        }

        return false;
    }
}
