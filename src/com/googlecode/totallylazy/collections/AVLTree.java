package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface AVLTree<K, V> extends TreeMap<K, V> {
    int height();

    int balance();

    @Override
    AVLTree<K, V> empty();

    @Override
    AVLTree<K, V> cons(Pair<K, V> head);

    @Override
    AVLTree<K, V> tail() throws NoSuchElementException;

    @Override
    AVLTree<K, V> insert(K key, V value);

    @Override
    AVLTree<K, V> delete(K key);

    @Override
    Pair<AVLTree<K, V>, Pair<K, V>> removeFirst();

    @Override
    Pair<AVLTree<K, V>, Pair<K, V>> removeLast();

    @Override
    AVLTree<K, V> left();

    @Override
    AVLTree<K, V> left(TreeMap<K, V> newLeft);

    @Override
    AVLTree<K, V> right();

    @Override
    AVLTree<K, V> right(TreeMap<K, V> newRight);

    @Override
    AVLTree<K, V> rotateLeft();

    @Override
    AVLTree<K, V> rotateRight();

    enum constructors implements TreeFactory {
        factory;

        @Override
        public <K, V> AVLTree<K, V> create(Comparator<K> comparator) {
            return new Empty<K, V>(comparator);
        }

        @Override
        public <K, V> AVLTree<K, V> create(Comparator<K> comparator, K key, V value) {
            return create(comparator, key, value, this.<K, V>create(comparator), this.<K, V>create(comparator));
        }

        @Override
        public <K, V> AVLTree<K, V> create(Comparator<K> comparator, K key, V value, TreeMap<K, V> left, TreeMap<K, V> right) {
            return methods.balance(new Node<K, V>(comparator, key, value, Unchecked.<AVLTree<K, V>>cast(left), Unchecked.<AVLTree<K, V>>cast(right)));
        }

        public static <K extends Comparable<? super K>, V> AVLTree<K, V> avlTree(K key, V value) {
            return factory.create(Comparators.<K>ascending(), key, value);
        }

        public static <K,V> TreeMapFactory<K,V, AVLTree<K,V>> factory(Comparator<K> comparator) {
            return TreeMapFactory.treeMapFactory(factory, comparator);
        }

        public static <K extends Comparable<? super K>,V> TreeMapFactory<K,V, AVLTree<K,V>> factory() {
            return TreeMapFactory.<K,V, AVLTree<K,V>>treeMapFactory(factory);
        }
    }

    class methods {
        // http://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/AVL_Tree_Rebalancing.svg/350px-AVL_Tree_Rebalancing.svg.png
        static <K, V> AVLTree<K, V> balance(AVLTree<K, V> node) {
            int balance = node.balance();
            if (balance == -2) return balanceRight(node);
            if (balance == 2) return balanceLeft(node);
            return node;
        }

        static <K, V> AVLTree<K, V> balanceLeft(AVLTree<K, V> node) {
            int balance = node.left().balance();
            if (balance == -1) return balanceLeftRight(node);
            if (balance == 1) return balanceLeftLeft(node);
            return node;
        }

        static <K, V> AVLTree<K, V> balanceRight(AVLTree<K, V> node) {
            int balance = node.right().balance();
            if (balance == 1) return balanceRightLeft(node);
            if (balance == -1) return balanceRightRight(node);
            return node;
        }

        static <K, V> AVLTree<K, V> balanceLeftLeft(AVLTree<K, V> parent) {
            return parent.rotateRight();
        }

        static <K, V> AVLTree<K, V> balanceLeftRight(AVLTree<K, V> parent) {
            AVLTree<K, V> four = parent.left().rotateLeft();
            return balanceLeftLeft(parent.left(four));
        }

        static <K, V> AVLTree<K, V> balanceRightRight(AVLTree<K, V> parent) {
            return parent.rotateLeft();
        }

        static <K, V> AVLTree<K, V> balanceRightLeft(AVLTree<K, V> parent) {
            AVLTree<K, V> four = parent.right().rotateRight();
            return balanceRightRight(parent.right(four));
        }
    }

    final class Empty<K, V> extends AbstractEmptyTreeMap<K, V, AVLTree<K, V>> implements AVLTree<K, V> {
        public Empty(Comparator<K> comparator) {
            super(comparator, AVLTree.constructors.factory);
        }

        @Override
        public int height() {
            return 0;
        }

        @Override
        public int balance() {
            return 0;
        }
    }

    final class Node<K, V> extends AbstractTreeMap<K, V, AVLTree<K, V>> implements AVLTree<K, V> {
        private final int height;
        private final int balance;

        private Node(Comparator<K> comparator, K key, V value, AVLTree<K, V> left, AVLTree<K, V> right) {
            super(comparator, key, value, left, right, AVLTree.constructors.factory);
            height = Math.max(left.height(), right.height()) + 1;
            balance = left.height() - right.height();
        }

        @Override
        public int height() {
            return height;
        }

        @Override
        public int balance() {
            return balance;
        }
    }
}
