package ru.javawebinar.basejava.storage;

public class ObjectsStreamStorageTest extends AbstractStorageTest {

    public ObjectsStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR));
    }
}