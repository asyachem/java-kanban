package history;

import tasks.Task;
import java.util.*;

// реализовать метод remove


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> map = new HashMap<>();

    private Node<Task> head = null;
    private Node<Task> tail = null;

    @Override
    public void add(Task task) {
        if (map.isEmpty()) {
            Node<Task> node = new Node(task, null,null);
            head = node;
            tail = node;
            map.put(task.getId(), node);
        } else {
            // ищем дубликат, если есть - удаляем
            if (map.containsKey(task.getId())) {
                remove(task.getId());
            }
            Node<Task> node = new Node(task, null, tail);
            tail.setNext(node);
            tail = node;
            map.put(task.getId(), node);
        }

    }

    @Override
    public void remove(int id) {
        if (map.containsKey(id)) {
            Node<Task> node = map.get(id);
            if (node.getPrev() != null) {
                node.getPrev().setNext(node.getNext());
            }
            if (node.getNext() != null) {
                node.getNext().setPrev(node.getPrev());
            }
            map.remove(id);
        }
    }

    public void removeNode(Node<Task> node) {
        remove(node.getData().getId());
    }

    public void clearAll() {
        this.map.clear();
        this.head = null;
        this.tail = null;
    }

    public void linkLast(Task task) {
        add(task);
    }

    public List<Task> getTasks() {
        List<Task> newList = new ArrayList<>();
        for (Node<Task> node : map.values()) {
            newList.add(node.getData());
        }
        return newList;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}

class Node <T> {
    private T data;
    private Node<T> next;
    private Node<T> prev;

    public Node(T data, Node<T> next, Node<T> prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }
}