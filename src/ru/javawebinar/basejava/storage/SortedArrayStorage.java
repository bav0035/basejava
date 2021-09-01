package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void insertElement(Resume r, int index) {
        index = -1 * (index + 1);
        for (int i = size - 1; i >= index; i--) {
            storage[i + 1] = storage[i];
        }
        storage[index] = r;
    }

    @Override
    public void removeElement(int index) {
        for (int i = index; i < size - 1; i++) {
            storage[i] = storage[i + 1];
        }
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}