package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    protected abstract void doWrite(Resume r, File file) throws IOException;
    protected abstract Resume doRead(File file);

    protected File dir;

    protected AbstractFileStorage(File dir) {
        Objects.requireNonNull(dir, "Directory must be not null");
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not directory");
        }
        if (!dir.canRead()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not readable");
        }
        if (!dir.canWrite()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not writable");
        }
        this.dir = dir;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(dir, uuid);
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> list = new ArrayList<>();
        for (File f : dir.listFiles()) {
            list.add(doGet(f));
        }
        return null;
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        file.delete();
    }

    @Override
    protected Resume doGet(File file) {
        return doRead(file);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    public void clear() {
        for (File f : dir.listFiles()) {
            doDelete(f);
        }
    }

    @Override
    public int size() {
        return Objects.requireNonNull(dir.list()).length;
    }
}
