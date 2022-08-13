import java.util.Scanner;
import java.util.ArrayList;

public class Duke {

    public class Task {
        protected String description;
        protected boolean isDone;

        public Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        public String getStatus() {
            String status;
            if(this.isDone) {
                status = String.format("[√] %s", this.description);
            } else {
                status = String.format("[X] %s", this.description);
            }
            return status;
        }

        public void markAsDone() {
            
        }

        public void markAsNotDone() {

        }
    }

    private static ArrayList<String> userInputHistory = new ArrayList();
    private static void greetUser() {
        String logo = "_______     _\n" +
                "|  ___|    | |\n" +
                "|  |_  ____| |_____ ____  _  __\n" +
                "|   _|/ _  \\ | ___|/  _ \\| |/  \\\n"+
                "|  | | |_| | | |___| |_| |  / \\ |\n" +
                "|__|  \\__|_|_|____|\\____/|_|  |_|\n";
        System.out.println("Hello from\n" + logo);
        // prompt user
        System.out.println("Where would you like to go next?");
    }

    /**
     * Method to add user input to history
     * @param s
     */
    private static void addToHistory(String s) {
        userInputHistory.add(s);
        //echo request
        System.out.printf("Noted down: %s\n", s);
    }

    /**
     * Method to show history
     */
    private static void showHistory() {
        System.out.print("______\n");
        userInputHistory.forEach(input -> {
            System.out.printf("• %s\n", input);
        });
        System.out.print("______\n");
    }

    private static void handleInput(String userInput) {
        if (userInput.equals("bye")) {
            //exit
            System.out.println("Thank you for swinging by :)");
            System.exit(0);
        } else if (userInput.equals("list")) {
            showHistory();
        } else {
            addToHistory(userInput);
        }
    }

    public static void main(String[] args) {
        greetUser();
        Scanner in = new Scanner(System.in);
        String s;
        while(true) {
            s = in.nextLine();
            handleInput(s);
        }
    }
}
