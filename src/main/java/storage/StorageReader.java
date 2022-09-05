package storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import exceptions.DukeException;
import task.Task;
import tasklist.TaskList;
import utility.StorageParser;

/**
 * Encapsulates all read operations on disk file storage.
 */
public class StorageReader {
    private Path path;

    public StorageReader(Path path) {
        this.path = path;
    }

    /**
     * Return contents of file history.
     *
     * @return arraylist containing all the lines in the file.
     */
    private Stream<String> getAllLines() throws IOException{
        return Files.lines(path);
    }

    /**
     * Syncs all changes stored in disk to arrayList maintained by program, by:
     * 1. Emptying  arraylist,
     * 2. Copying all lines on disk to arraylist
     *
     * @throws DukeException when fileLineToTask() fails.
     */
    public TaskList syncArrayList() throws DukeException {
        try {
            TaskList userInputHistory = new TaskList();
            getAllLines().filter(lineInFile -> {
                return !lineInFile.isBlank();
            }).forEach(currTask -> {
                try {
                    userInputHistory.addTask(StorageParser.fileLineToTask(currTask));
                } catch (DukeException de) {
                    System.out.println(de);
                }
            });
            return userInputHistory;
        } catch (IOException e) {
            throw new DukeException("Error reading file");
        }
    }
}
