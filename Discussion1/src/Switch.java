import java.util.Random;

public class Switch {
    public static void main(String[] args) {
        int dayOfWeek = new Random().nextInt(4) + 1;

        // Java 11-style switch statement
        System.out.print("Java 11 style switch: ");
        switch (dayOfWeek) {
            case 1:
                System.out.println("This is a Monday");
                break;
            case 2:
                System.out.println("This is a Tuesday");
                break;
            case 3:
                System.out.println("This is a Wednesday");
                break;
            default:
                System.out.println("Invalid day");
        }

        // Java 14+ style switch expression
        System.out.print("Java 17 style switch: ");
        String result = switch (dayOfWeek) {
            case 1 -> "This is a Monday";
            case 2 -> "This is a Tuesday";
            case 3 -> "This is a Wednesday";
            default -> "Invalid day";
        };
        System.out.println(result);
    }
}