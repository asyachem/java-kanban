package history;

import tasks.Task;
import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> map = new HashMap<>();

    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (task != null) {
            removeNode(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(int id) {
        Node node = map.get(id);
        if (node != null) {
            if (node.prev == null && node.next == null) {
                this.tail = null;
                this.head = null;
            } else if (node.prev != null && node.next != null) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            } else if (node.prev == null) {
                this.head = node.next;
                this.head.prev = null;
            } else if (node.next == null) {
                this.tail = node.prev;
                this.tail.next = null;
            }
            map.remove(id);
        }
    }

    private void linkLast(Task task) {
        Node node = new Node(task, null, tail);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        map.put(task.getId(), node);
    }

    private List<Task> getTasks() {
        List<Task> newList = new ArrayList<>();

        Node current = this.head;
        while (current != null) {
            newList.add(current.data);
            current = current.next;
        }

        return newList;
    }

    private static class Node {
        Task data;
        Node next;
        Node prev;

        public Node(Task data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

    }

}

