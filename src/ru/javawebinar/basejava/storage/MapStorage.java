package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Map;
import java.util.TreeMap;

public class MapStorage extends AbstractStorage{
    private Map<String, Resume> map = new TreeMap<>();

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doUpdate(Resume r, Object key) {
        map.put((String) key, r);
    }

    @Override
    protected void doSave(Resume r, Object key) {
        map.put((String) key, r);
    }

    @Override
    protected void doDelete(Object key) {
        map.remove((String) key);
    }

    @Override
    protected Resume doGet(Object key) {
        return map.get((String) key);
    }

    @Override
    protected boolean isExist(Object key) {
        return map.containsKey((String) key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Resume[] getAll() {
        return map.values().toArray(new Resume[map.size()]);
    }

    @Override
    public int size() {
        return map.size();
    }
    //    @Override
//    public void clear() {
//        map.clear();
//    }
//
//    @Override
//    public void update(Resume r) {
//        if (!map.containsKey(r.getUuid())) {
//            throw new NotExistStorageException("Not Exist " + r.getUuid());
//        } else {
//            map.put(r.getUuid(), r);
//        }
//    }
//
//    @Override
//    public void save(Resume r) {
//        if (map.containsKey(r.getUuid())) {
//            throw new ExistStorageException("Already exist " + r.getUuid());
//        } else {
//            throw new StorageException("Storage overflow", r.getUuid());
//        }
//    }
//
//    @Override
//    public Resume get(String uuid) {
//        if (map.containsKey(uuid)) {
//            return map.get(uuid);
//        }
//        throw new NotExistStorageException("Not Exist " + uuid);
//    }
//
//    @Override
//    public void delete(String uuid) {
//        if (map.containsKey(uuid)) {
//            map.remove(uuid);
//        } else {
//            throw new NotExistStorageException("Not Exist " + uuid);
//        }
//    }
//
//    @Override
//    public Resume[] getAll() {
//        Resume[] r = new Resume[map.size()];
//        return map.values().toArray(r);
//    }
//
//    @Override
//    public int size() {
//        return map.size();
//    }
}
