package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (LOAD_FACTOR * capacity);
        buckets = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);
        insertNode(newNode, index);

    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> bucket = buckets[index];
        while (bucket != null && !Objects.equals(bucket.key, key)) {
            bucket = bucket.next;
        }
        return bucket != null ? bucket.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize() {
        if (size >= threshold) {
            resize();
        }
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (LOAD_FACTOR * capacity);
        size = 0;
        Node<K, V>[] oldCopy = buckets;
        buckets = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : oldCopy) {
            while (node != null) {
                Node<K, V> next = node.next;
                node.next = null;
                insertNode(node, getIndex(node.key));
                node = next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : (key.hashCode() & Integer.MAX_VALUE) % capacity;
    }

    private void insertNode(Node<K, V> newNode, int index) {
        Node<K, V> bucket = buckets[index];
        if (bucket == null) {
            newNode.next = bucket;
            buckets[index] = newNode;
            size++;
            return;
        } else if (Objects.equals(bucket.key, newNode.key)) {
            bucket.value = newNode.value;
            return;
        } else {
            while (bucket.next != null) {
                if (Objects.equals(bucket.key, newNode.key)) {
                    bucket.value = newNode.value;
                    return;
                }
                bucket = bucket.next;
            }
            bucket.next = newNode;
            size++;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
