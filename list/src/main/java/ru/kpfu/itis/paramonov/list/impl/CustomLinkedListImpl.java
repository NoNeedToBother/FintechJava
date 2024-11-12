package ru.kpfu.itis.paramonov.list.impl;

import ru.kpfu.itis.paramonov.iterator.CustomIterator;
import ru.kpfu.itis.paramonov.list.CustomLinkedList;

import java.util.List;
import java.util.function.Consumer;

public class CustomLinkedListImpl<E> implements CustomLinkedList<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;

        public Node(E value) {
            this.value = value;
        }
    }

    private class Iterator implements CustomIterator<E> {

        private int cursor;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            E elem = get(cursor);
            cursor++;
            return elem;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            while (hasNext()) {
                action.accept(next());
            }
        }
    }

    private Node<E> head;

    private Node<E> tail;

    private int size = 0;

    @Override
    public void add(E elem) {
        Node<E> node = new Node<>(elem);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
        size++;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return head.value;
        }
        if (index == size - 1) {
            return tail.value;
        }
        return findNode(index).value;
    }

    private Node<E> findNode(int index) {
        Node<E> current = head;
        int counter = 0;
        while(counter < index) {
            current = current.next;
            counter++;
        }
        return current;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return removeFirst();
        }
        if (index == size - 1) {
            return removeLast();
        }
        Node<E> node = findNode(index);
        E elem = node.value;

        node.next.prev = node.prev;
        node.prev.next = node.next;

        dereferenceNode(node);
        size--;
        return elem;
    }

    private E removeFirst() {
        E elem = head.value;
        Node<E> node = head;
        if (node.next != null) {
            node.next.prev = null;
        }
        head = node.next;

        dereferenceNode(node);
        size--;
        return elem;
    }

    private E removeLast() {
        E elem = tail.value;
        Node<E> node = tail;
        if (node.prev != null) {
            node.prev.next = null;
        }
        tail = node.prev;

        dereferenceNode(node);
        size--;
        return elem;
    }

    @Override
    public boolean contains(E elem) {
        Node<E> current = head;
        while (current != null) {
            if (current.value.equals(elem)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void addAll(List<? extends E> from) {
        for (E elem: from) {
            add(elem);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public CustomIterator<E> iterator() {
        return new Iterator();
    }

    private void dereferenceNode(Node<E> node) {
        node.value = null;
        node.next = null;
        node.prev = null;
        node = null;
    }
}
