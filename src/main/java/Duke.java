import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

public class Duke {
    private static ArrayList<Task> userInputHistory = new ArrayList<>();
    private static void greetUser() {
        String logo = "_______     _\n" +
                "|  ___|    | |\n" +
                "|  |_  ____| |_____ ____  _  __\n" +
                "|   _|/ _  \\ | ___|/  _ \\| |/  \\\n"+
                "|  | | |_| | | |___| |_| |  / \\ |\n" +
                "|__|  \\__|_|_|____|\\____/|_|  |_|\n";
        System.out.println("Hello from" );
        System.out.println(logo);
        // prompt user
        System.out.println("Where would you like to go next?");
        System.out.print(">> ");
    }

    /**
     * Method to add Task to history
     * @param s String description to add to userInputHistory
     */
    private static void addTaskToHistory(String s) {
        Task newTask = new Task(s);
        userInputHistory.add(newTask);
        //echo request
        System.out.printf("Noted down: %s\n There are %d items on your list now. \n", s, userInputHistory.size());
        System.out.print(">>");
    }

    /**
     * Method to add Event to userInputHistory
     * @param description
     * @param at
     */
    private static void addEventToHistory(String description, String at) {
        Event newEvent = new Event(description, at);
        userInputHistory.add(newEvent);
        //echo request
        System.out.printf("Noted down: %s\n There are %d items on your list now. \n", description, userInputHistory.size());
        System.out.print(">>");
    }

    /**
     * Method to add Event to userInputHistory
     * @param description
     * @param by
     */
    private static void addDeadlineToHistory(String description, String by) {
        Deadline newDeadline = new Deadline(description, by);
        userInputHistory.add(newDeadline);
        //echo request
        System.out.printf("Noted down: %s\n There are %d items on your list now. \n", description, userInputHistory.size());
        System.out.print(">>");
    }

    /**
     * Method to show history
     */
    private static void showHistory() {
        int count = 1;
        System.out.print("______\n");
        System.out.println("Tasks in your list are: ");
        for (Task t: userInputHistory) {
            System.out.printf("%d. %s\n",count, t);
            count ++;
        }
        userInputHistory.forEach(input -> {

        });
        System.out.printf("Total: %d\n", userInputHistory.size());
        System.out.print("______\n");
        System.out.print(">>");
    }

    /**
     * Mark Task at index n in list.
     * No checks performed to check if task is already marked.
     * @param n task to mark as done (n - 1) index in actual list
     */
    public static void markTask(int n){
        Task taskToModify = userInputHistory.get(n - 1);
        taskToModify.markAsDone();
        System.out.printf("Marked task %d \n%s\n", n, taskToModify);
        System.out.print(">>");
    }

    /**
     * Un-mark task at index n in list.
     * No checks performed to check if task is already unmarked.
     * @param n task to mark as not done (n - 1) index in actual list
     */
    public static void unmarkTask(int n) {
        Task taskToModify = userInputHistory.get(n - 1);
        taskToModify.markAsNotDone();
        System.out.printf("Unmarked task %d \n%s\n", n, taskToModify);
        System.out.print(">>");
    }

    /**
     * Extract task number from string input
     * @param s extracts task number from user input
     * @return index of the task in the list plus one
     */
    private static int getTaskNumber(String s) {
        // credit: https://stackoverflow.com/questions/14974033/extract-digits-from-string-stringutils-java
        String numberOnly= s.replaceAll("[^0-9]", "");
        return Integer.parseInt(numberOnly);
    }

    /**
     * Filter userInput and call addEventToHistory
     * @param event
     */
    private static void handleEvent(String event) {
        String description, at;
        description = event.substring(event.indexOf("event ") + 6, event.indexOf("/at"));
        at = event.substring(event.indexOf("/at") + 3);
        addEventToHistory(description, at);
    }

    /**
     * Filter userInput and call addDeadlineToHistory
     * @param deadline
     */
    private static void handleDeadline(String deadline) throws DukeException{
        String description, by, removeDeadline;
        int byLine;
        removeDeadline = deadline.substring(deadline.indexOf("deadline") + 8).trim();
        if (removeDeadline.equals("")) {
            throw new DukeException("deadline", "Blank fields not allowed");
        } else {
             byLine = removeDeadline.indexOf("/by");
            if (byLine < 0) {
                throw new DukeException("deadline", "Missing by line");
            } else {
                description = removeDeadline.substring(0, byLine + 3).trim();
                if (description.equals("")) {
                    throw new DukeException("deadline", "Blank description not allowed");
                } else {
                    by = removeDeadline.substring(byLine + 3).trim();
                    if (by.equals("")) {
                        throw new DukeException("deadline", "Blank /by date not allowed");
                    } else {
                        addDeadlineToHistory(description, by);
                    }
                    }
                }
            }
        }

    /**
     * Filter userInput and call addDeadlineToHistory
     * @param task
     */
    private static void handleTask(String task) throws DukeException{
        String description;
        description = task.substring(task.indexOf("todo") + 4).trim();
        if (description.equals("")) {
            throw new DukeException("todo", "Blank note not allowed");
        } else {
            addTaskToHistory(description);
        }
    }

    /**
     * Menu handler
     * @param userInput
     */
    private static void handleInput(String userInput) throws DukeException{
        if (userInput.equals("bye") || userInput.equals("exit") || userInput.equals("quit")) {
            //exit
            System.out.println("Thank you for swinging by :)");
            System.exit(0);
        } else if (userInput.equals("list")) {
            showHistory();
        } else if (userInput.startsWith("mark")) {
            markTask(getTaskNumber(userInput));
        } else if (userInput.startsWith("unmark")) {
            unmarkTask(getTaskNumber(userInput));
        } else if(userInput.startsWith("todo")) {
            handleTask(userInput);
        } else if (userInput.startsWith("event")) {
            handleEvent(userInput);
        } else if (userInput.startsWith("deadline")) {
            handleDeadline(userInput);
        }
        else {
            System.out.println("Enter a command (todo, event, deadline, list, mark, unmark, bye)");
            System.out.print(">>\n");
        }
    }

    /**
     * Grabs description from string which is expected to have format:
     * <command> <description>...
     * @param command
     * @param commandUsed
     * @return
     */
    private String getDescription(String command, String commandUsed) throws DukeException{
        String description;
        int startDescriptionIndex = command.indexOf(commandUsed);
        int endDescriptionIndex = command.indexOf("/");
        if (startDescriptionIndex < 0 || endDescriptionIndex < 0) {
            throw new DukeException("Command does not follow pattern <command> <description>...");
        } else {
            description = command.substring(startDescriptionIndex, endDescriptionIndex).trim();
        }
        return description;
    }

    /**
     * Grabs date from string which is expected to have format:
     * ... /<at/by> <date in HH:MM DD:MM:YYYY>
     * @param command - string to extract date from
     * @return <date> component of command
     * @throws DukeException
     */
    private String getDate(String command) throws DukeException{
        String date = "";
        int startDateIndex = command.indexOf("/") + 2;
        if (startDateIndex < 0) {
            throw new DukeException("Command does not follow pattern ... /<at/by> <date in HH:MM DD:MM:YYYY>");
        } else {
            date = command.substring(startDateIndex).trim();
        }
        return date;
    }

    public static void main(String[] args) {
        greetUser();
        Scanner in = new Scanner(System.in);
        String s;
        while(true) {
            try {
                s = in.nextLine();
                handleInput(s);
            } catch (InputMismatchException ime) {
                System.out.println(ime);
                System.exit(0);
            } catch (DukeException de) {
                System.out.println(de);
            }
        }
    }
}
