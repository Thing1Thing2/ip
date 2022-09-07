package utility;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import command.AddDeadlineCommand;
import command.AddEventCommand;
import command.AddTaskCommand;
import command.CheckIsTodayCommand;
import command.Command;
import command.DeleteTaskCommand;
import command.ExitCommand;
import command.FindCommand;
import command.GetLongDescriptionCommand;
import command.HelpCommand;
import command.ListCommand;
import command.MarkCommand;
import command.UnmarkCommand;
import exceptions.DukeException;
import task.Deadline;
import task.Event;
import task.Task;


/**
 * Handles all conversions required in the program.
 */
public class Parser {
    private static HashMap<String, Pattern> commandAliasesHashMap = new HashMap<>();

    /**
     * Initiliase hashmap which stores Pattern
     * required to determine corresponding
     * String command.
     *
     */
    public static void initialiseCommandAliases() {
        if (commandAliasesHashMap.isEmpty()) {
            HashMap<String, Pattern> commandAliases = new HashMap<>();
            Pattern todoRegex = Pattern.compile("t|todo",
                    Pattern.CASE_INSENSITIVE);
            Pattern listRegex = Pattern.compile("l|list",
                    Pattern.CASE_INSENSITIVE);
            Pattern deadlineRegex = Pattern.compile("deadline|d",
                    Pattern.CASE_INSENSITIVE);
            Pattern markRegex = Pattern.compile("mark|m",
                    Pattern.CASE_INSENSITIVE);
            Pattern unmarkRegex = Pattern.compile("unmark|um",
                    Pattern.CASE_INSENSITIVE);
            Pattern byeRegex = Pattern.compile("bye|b|quit|q|exit",
                    Pattern.CASE_INSENSITIVE);
            Pattern findRegex = Pattern.compile("find|f",
                    Pattern.CASE_INSENSITIVE);
            Pattern longdescRegex = Pattern.compile("longdesc",
                    Pattern.CASE_INSENSITIVE);
            Pattern istodayRegex = Pattern.compile("istoday",
                    Pattern.CASE_INSENSITIVE);
            Pattern helpRegex = Pattern.compile("help|h",
                    Pattern.CASE_INSENSITIVE);
            commandAliases.put("todo", todoRegex);
            commandAliases.put("list", listRegex);
            commandAliases.put("deadline", deadlineRegex);
            commandAliases.put("mark", markRegex);
            commandAliases.put("unmark", unmarkRegex);
            commandAliases.put("bye", byeRegex);
            commandAliases.put("find", findRegex);
            commandAliases.put("longdesc", longdescRegex);
            commandAliases.put("istoday", istodayRegex);
            commandAliases.put("help", helpRegex);
            commandAliasesHashMap = commandAliases;
        }
    }

    /**
     * Returns Command object corresponding to
     * command extracted from user input.
     * Looks for first whitespace to distinguish
     * command used.
     *
     * @param userInput User input string to parse into Command.
     * @return Command type object.
     * @throws DukeException When command given is invalid.
     */
    public static Command parse(String userInput) throws DukeException {
        String[] inputSections = userInput.split(" ");
        String stringCommand = extractCommand(inputSections[0]);
        assert !stringCommand.equals(" ");
        switch (stringCommand) {
        case "todo":
            return new AddTaskCommand(inputSections);
        case "event":
            return new AddEventCommand(inputSections);
        case "deadline":
            return new AddDeadlineCommand(inputSections);
        case "delete":
            return new DeleteTaskCommand(inputSections);
        case "mark":
            return new MarkCommand(inputSections);
        case "unmark":
            return new UnmarkCommand(inputSections);
        case "istoday":
            return new CheckIsTodayCommand(inputSections);
        case "longdesc":
            return new GetLongDescriptionCommand(inputSections);
        case "list":
            return new ListCommand(inputSections);
        case "bye":
            return new ExitCommand(inputSections);
        case "help":
            return new HelpCommand(inputSections);
        case "find":
            return new FindCommand(inputSections);
        default:
            String message = "Command invalid. Type help for more information."
                    + stringCommand;
            throw new DukeException(message);
        }
    }


    /**
     * Extracts shortcut or formal command used and returns formal command/
     * If input does not follow valid format, return empty string.
     *
     * @param command
     * @return
     */
    private static String extractCommand(String command) {
        // Make sure commandAliases are not empty
        initialiseCommandAliases();
        Matcher matcher;
        for (Map.Entry<String, Pattern> patternAndString : commandAliasesHashMap.entrySet()) {
            matcher = patternAndString.getValue().matcher(command);
            if (matcher.matches()) {
                return patternAndString.getKey();
            }
        }
        return "";
    }

    /**
     * Converts string command for adding
     * task to corresponding Task object.
     *
     * @param description User input command for creating Task.
     * @return Task object with required description.
     * @throws DukeException When no valid description is found.
     */
    public static Task stringToTask(String description) throws DukeException {
        return new Task(description);
    }

    /**
     * return event
     *
     * @param description description
     * @param date date
     * @return event
     * @throws DukeException error
     */
    public static Event stringToEvent(String description, String date) throws DukeException {
        LocalDate localDate = getDate(date);
        return new Event(description, localDate);
    }

    /**
     * Return deadline
     *
     * @param description description.
     * @param date date
     * @return deadline
     * @throws DukeException error.
     */
    public static Deadline stringToDeadline(String description, String date) throws DukeException {
        LocalDate localDate = getDate(date);
        return new Deadline(description, localDate);
    }


    /**
     * Get date
     *
     * @param date date
     * @return date
     * @throws DukeException date error.
     */
    private static LocalDate getDate(String date) throws DukeException {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException dtpe) {
            throw new DukeException("Date is not valid, require format YYYY-MM-DD");
        }
    }


    /**
     * Extracts task number from user input string.
     *
     * @param s User input string to get number from.
     * @param listSize TaskList size to check if number is valid.
     * @return Index of the task in the list plus one.
     * @throws DukeException when conditions not met.
     */
    public static int getTaskNumber(String s, int listSize) throws DukeException {
        // credit: https://stackoverflow.com/questions/14974033/extract-digits-from-string-stringutils-java
        String numberOnly = s.replaceAll("[^0-9]", "");
        if (numberOnly.length() <= 0) {
            throw new DukeException("no number given");
        }
        int n = Integer.parseInt(numberOnly);
        if (n <= listSize) {
            return n;
        } else {
            throw new DukeException("Task does not exist in list");
        }

    }

    /**
     * Extracts keyword to user is looking up from user input.
     *
     * @param userInput User input to extract keyword from.
     * @return Keyword required to perform Find operation.
     */
    public static String stringToFind(String userInput) {
        return userInput.substring(userInput.indexOf("find") + 5);
    }
}
