package manager;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final int maxHistorySize = 10;
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == maxHistorySize) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

}
