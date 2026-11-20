import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Validates login credentials (username and password).
 *
 * Username rules:
 *  - 3 to 20 characters
 *  - starts with a letter
 *  - only letters, digits, underscore (_) or dot (.)
 *
 * Password rules:
 *  - 8 to 64 characters
 *  - at least one uppercase letter, one lowercase letter, one digit, one special character
 *  - no whitespace
 *  - must not contain the username
 */
public class LoginValidator {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9_.]{2,19}$");
    private static final Pattern UPPER = Pattern.compile("[A-Z]");
    private static final Pattern LOWER = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL = Pattern.compile("[^A-Za-z0-9\\s]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s");

    public static List<String> validateUsername(String username) {
        List<String> errors = new ArrayList<>();
        if (username == null || username.isBlank()) {
            errors.add("Username is required.");
            return errors;
        }
        if (username.length() < 3 || username.length() > 20) {
            errors.add("Username must be 3-20 characters long.");
        }
        if (!Character.isLetter(username.charAt(0))) {
            errors.add("Username must start with a letter.");
        }
        if (!USERNAME_PATTERN.matcher(username).matches() && errors.isEmpty()) {
            errors.add("Username may only contain letters, digits, '_' or '.'.");
        }
        return errors;
    }

    public static List<String> validatePassword(String password, String username) {
        List<String> errors = new ArrayList<>();
        if (password == null || password.isEmpty()) {
            errors.add("Password is required.");
            return errors;
        }
        if (password.length() < 8 || password.length() > 64) {
            errors.add("Password must be 8-64 characters long.");
        }
        if (!UPPER.matcher(password).find()) {
            errors.add("Password must contain at least one uppercase letter.");
        }
        if (!LOWER.matcher(password).find()) {
            errors.add("Password must contain at least one lowercase letter.");
        }
        if (!DIGIT.matcher(password).find()) {
            errors.add("Password must contain at least one digit.");
        }
        if (!SPECIAL.matcher(password).find()) {
            errors.add("Password must contain at least one special character.");
        }
        if (WHITESPACE.matcher(password).find()) {
            errors.add("Password must not contain spaces.");
        }
        if (username != null && !username.isBlank()
                && password.toLowerCase().contains(username.toLowerCase())) {
            errors.add("Password must not contain the username.");
        }
        return errors;
    }

    /** Returns all validation errors; an empty list means the credentials are valid. */
    public static List<String> validate(String username, String password) {
        List<String> errors = new ArrayList<>(validateUsername(username));
        errors.addAll(validatePassword(password, username));
        return errors;
    }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Username: ");
            String username = in.nextLine().trim();
            System.out.print("Password: ");
            String password = in.nextLine();

            List<String> errors = validate(username, password);
            if (errors.isEmpty()) {
                System.out.println("Login input is valid.");
            } else {
                System.out.println("Invalid login:");
                errors.forEach(e -> System.out.println("  - " + e));
            }
        }
    }
}
