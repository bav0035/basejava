package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.StreamSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    protected File dir;
    private StreamSerializer streamSerializer;

    protected FileStorage(File dir, StreamSerializer streamSerializer) {
        Objects.requireNonNull(dir, "Directory must be not null");
        this.dir = dir;
        this.streamSerializer = streamSerializer;
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not directory");
        }
        if (!dir.canRead()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not readable");
        }
        if (!dir.canWrite()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not writable");
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(dir, uuid);
    }

    @Override
    protected List<Resume> doCopyAll() {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new StorageException("Directory read error");
        }
        List<Resume> list = new ArrayList<>();
        for (File f : files) {
            list.add(doGet(f));
        }
        return list;
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            streamSerializer.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File write error", r.getUuid(), e);
        }
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Can't create file" + file.getAbsolutePath(), file.getName(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File read error", file.getName(), e);
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    public void clear() {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                doDelete(f);
            }
        }
    }

    @Override
    public int size() {
        String[] list = dir.list();
        if (list == null) {
            throw new StorageException("Directory read error");
        }
        return list.length;
    }
}
