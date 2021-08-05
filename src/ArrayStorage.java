/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(r.uuid)) {
                return;
            }
        }
        storage[size] = r;
        size++;
    }

    Resume get(String uuid) {
        if (uuid == null) {
            return null;
        }
        for (int i = 0; i < size; i++) {
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
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].uuid)) {
                while (i++ < size) {
                    storage[i - 1] = storage[i];
                }
                storage[i] = null;
                size--;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumeList = new Resume[size];
        System.arraycopy(storage, 0, resumeList, 0, resumeList.length);
        return resumeList;
    }

    int size() {
        return size;
    }

}