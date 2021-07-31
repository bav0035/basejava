/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        for (int i = 0; i < size(); i++) {
            storage[i] = null;
        }
    }

    void save(Resume r) {
        for (int i = 0; i < size(); i++) {
            if (storage[i].uuid.equals(r.uuid)) {
                storage[i] = r;
                return;
            }
        }
        storage[size()] = r;
    }

    Resume get(String uuid) {
        if (uuid == null) {
            return null;
        }
        for (int i = 0; i < size(); i++) {
            if (uuid.equals(storage[i].uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        if (uuid == null) {
            return;
        }
        for (int i = 0; i < size(); i++) {
            if (uuid.equals(storage[i].uuid)) {
                while (i++ < size()) {
                    storage[i - 1] = storage[i];
                }
                storage[i] = null;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumeList = new Resume[size()];
        System.arraycopy(storage, 0, resumeList, 0, resumeList.length);
        return resumeList;
    }

    int size() {
        int size = 0;
        for (Resume r : storage) {
            if (r == null) {
                return size;
            }
            size++;
        }
        return size;
    }

}

