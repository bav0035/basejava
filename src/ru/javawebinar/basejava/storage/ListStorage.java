package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListStorage extends AbstractStorage {
    private List<Resume> storageList = new ArrayList<>();

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < storageList.size(); i++) {
            if (storageList.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void doUpdate(Resume r, Object searchKey) {
        storageList.set((int) searchKey, r);
    }

    @Override
    protected void doSave(Resume r, Object searchKey) {
        storageList.add(r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storageList.remove((int) searchKey);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storageList.get((int) searchKey);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }

    @Override
    public void clear() {
        storageList.clear();
    }

    @Override
    public Resume[] getAll() {
        Resume[] r = new Resume[storageList.size()];
        return storageList.toArray(r);
    }

    @Override
    public int size() {
        return storageList.size();
    }
}
