package manager;

import tasks.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Task task;
        Node last;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        removeNode(task.getId());   //в приватный метод removeNode()
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(id);  //в приватный метод removeNode()
    }

    @Override
    public List<Task> getHistory() {
        List<Task> currentHistory = new ArrayList<>();
        Node current = head;
        while (current != null) {
            currentHistory.add(current.task);
            current = current.next;
        }
        return currentHistory;
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.last = tail;
        }
        tail = newNode;
        historyMap.put(task.getId(), newNode);
    }

    private void removeNode(int id) {
        Node node = historyMap.remove(id);
        if (node != null) {
            if (node.last != null) {
                node.last.next = node.next;
            } else {
                head = node.next;
            }

            if (node.next != null) {
                node.next.last = node.last;
            } else {
                tail = node.last;
            }
        }
    }

}
