package history;

import tasks.Task;
import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> map = new HashMap<>();

    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        Node node;
        if (head == null) {
            node = new Node(task, null,null);
            head = node;
        } else {
            // ищем дубликат, если есть - удаляем
            remove(task.getId());

            node = new Node(task, null, tail);
            tail.next = node;
        }
        tail = node;
        map.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        System.out.println(map);
        System.out.println(map.containsKey(id));

        if (map.containsKey(id)) {

            removeNode(id);
        }
    }

    private void removeNode(int id) {
        Node node = map.get(id);
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

    private void linkLast(Task task) {
        add(task);
    }

    private List<Task> getTasks() {
        List<Task> newList = new ArrayList<>();
        for (Node node : map.values()) {
            newList.add(node.data);
        }
        return newList;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
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

