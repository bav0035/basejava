package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final int MAX_SIZE = 10000;
    Resume[] storage = new Resume[MAX_SIZE];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (isExist(r.getUuid())) {
            System.out.println("Резюме уже существует, невозможно сохранить.");
            return;
        }
        if (size != MAX_SIZE) {
            storage[size] = r;
            size++;
        } else {
            System.out.println("Превышен размер хранилища, резюме не сохранено!");
        }
    }

    public void update(Resume r) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(r.getUuid())) {
                storage[i] = r;
                return;
            }
        }
        System.out.println("Такого резюме не существует, невозможно обновить.");
    }

    public Resume get(String uuid) {
        if (uuid == null) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return storage[i];
            }
        }
        System.out.println("Резюме с запрошенным uuid не существует.");
        return null;
    }

    public void delete(String uuid) {
        if (uuid == null) {
            return;
        }
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                while (i++ < size) {
                    storage[i - 1] = storage[i];
                }
                storage[i] = null;
                size--;
                return;
            }
        }
        System.out.println("Резюме с указанным uuid не существует.");
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private boolean isExist(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return true;
            }
        }
        return false;
    }
}