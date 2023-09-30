package storage;

import tasks.Task;
import tasks.Todo;
import tasks.Deadline;
import tasks.Event;
import tasks.DukeException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Storage {

    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws DukeException {
        File file = new File(filePath);
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String data = scanner.nextLine();
                String[] parts = data.split(" \\| ");
                
                switch (parts[0]) {
                    case "T":
                        Todo todo = new Todo(parts[2]);
                        if (parts[1].equals("1")) {
                            todo.markAsDone(true);
                        }
                        tasks.add(todo);
                        break;
                    case "D":
                        Deadline deadline = new Deadline(parts[2], parts[3]);
                        if (parts[1].equals("1")) {
                            deadline.markAsDone(true);
                        }
                        tasks.add(deadline);
                        break;
                    case "E":
                        if (parts.length < 5) {
                            throw new DukeException("Stored Event task format is incorrect");
                        }
                        Event event = new Event(parts[2], parts[3], parts[4]);
                        if (parts[1].equals("1")) {
                            event.markAsDone(true);
                        }
                        tasks.add(event);
                        break;
                    default:
                        System.out.println("Invalid task type");
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error loading file");
        }

        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        for (Task task : tasks) {
            fw.write(task.toData() + "\n");
        }
        fw.close();
    }
}
