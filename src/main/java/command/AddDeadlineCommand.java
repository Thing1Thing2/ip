package command;

import exceptions.DukeException;
import storage.Storage;
import task.Deadline;
import tasklist.TaskList;
import ui.UI;
import utility.Parser;

/**
 * Command that handles adding Deadline to TaskList and Storage.
 */
public class AddDeadlineCommand extends Command {
    private String[] slicedUserCommands;

    public AddDeadlineCommand(String[] slicedUserInput) {
        this.slicedUserCommands = slicedUserInput;
    }
     /**
     * Runs when deadline is to be added.
     *
     * @param taskList TaskList to append Deadline to.
     * @param ui ui provides user command.
     * @param storage Storage space to append Deadline to.
     * @throws DukeException When parsing user command fails.
     */
    @Override
    public void execute(TaskList taskList, UI ui, Storage storage) throws DukeException {
        Deadline deadlineToAdd = Parser.stringToDeadline(slicedUserCommands[1], slicedUserCommands[2]);
        String storableLine = deadlineToAdd + "\n";
        if (storage.isLineAppended(storableLine)) {
            taskList.addDeadline(deadlineToAdd);
            ui.showMessage("added deadline");
        }
    }
}
