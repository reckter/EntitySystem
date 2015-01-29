package me.reckter.entity.util;

import java.util.*;

/**
 * Created by hannes on 21.01.15.
 */
public class UpdateList<E> implements List<E> {

    List<E> list;
    List<E> toAdd;
    List<Object> toRemove;


    public UpdateList() {
        list = new ArrayList<>();
        toAdd = new ArrayList<>();
        toRemove = new ArrayList<>();
    }


    public void update() {
        list.addAll(toAdd);
        list.removeAll(toRemove);
        toAdd.clear();
        toRemove.clear();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return toAdd.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return toRemove.add(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return toAdd.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new RuntimeException("This function is not supported!");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return toRemove.addAll((Collection<? extends E>) c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        clear();
        return toRemove.removeAll(c);
    }

    @Override
    public void clear() {
        toRemove.addAll(list);
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        throw new RuntimeException("This function is not supported!");
    }

    @Override
    public void add(int index, E element) {
        throw new RuntimeException("This function is not supported!");
    }

    @Override
    public E remove(int index) {
        E ret = get(index);
        remove(ret);
        return ret;
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex,toIndex);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        list.sort(c);
    }
}
