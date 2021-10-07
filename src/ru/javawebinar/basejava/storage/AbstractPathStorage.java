package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    protected abstract void doWrite(Resume r, OutputStream os) throws IOException;
    protected abstract Resume doRead(InputStream is) throws IOException;

    protected Path dir;

    protected AbstractPathStorage(String path) {
        dir = Paths.get(path);
        Objects.requireNonNull(dir, "Directory must be not null");
        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException(path + " is not directory");
        }
        if (!Files.isReadable(dir)) {
            throw new IllegalArgumentException(path + " is not readable");
        }
        if (!Files.isWritable(dir)) {
            throw new IllegalArgumentException(path + " is not writable");
        }
    }

//    @Override
//    protected Path getSearchKey(String uuid) {
//        return new Path(dir, uuid);
//    }
//
//    @Override
//    protected List<Resume> doCopyAll() {
//        Path[] Paths = dir.listPaths();
//        if (Paths == null) {
//            throw new StorageException("Directory read error", null);
//        }
//        List<Resume> list = new ArrayList<>();
//        for (Path f : Paths) {
//            list.add(doGet(f));
//        }
//        return list;
//    }
//
//    @Override
//    protected void doUpdate(Resume r, Path Path) {
//        try {
//            doWrite(r, new BufferedOutputStream(new PathOutputStream(Path)));
//        } catch (IOException e) {
//            throw new StorageException("Path write error", r.getUuid(), e);
//        }
//    }
//
//    @Override
//    protected void doSave(Resume r, Path Path) {
//        try {
//            Path.createNewPath();
//        } catch (IOException e) {
//            throw new StorageException("Can't create Path" + Path.getAbsolutePath(), Path.getName(), e);
//        }
//        doUpdate(r, Path);
//    }
//
//    @Override
//    protected void doDelete(Path Path) {
//        if (!Path.delete()) {
//            throw new StorageException("Path delete error", Path.getName());
//        }
//    }
//
//    @Override
//    protected Resume doGet(Path Path) {
//        try {
//            return doRead(new BufferedInputStream(new PathInputStream(Path)));
//        } catch (IOException e) {
//            throw new StorageException("Path read error", Path.getName(), e);
//        }
//    }
//
//    @Override
//    protected boolean isExist(Path Path) {
//        return Path.exists();
//    }
//
//    @Override
//    public void clear() {
//        Path[] Paths = dir.listPaths();
//        if (Paths != null) {
//            for (Path f : Paths) {
//                doDelete(f);
//            }
//        }
//    }
//
//    @Override
//    public int size() {
//        String[] list = dir.list();
//        if (list == null) {
//            throw new StorageException("Directory read error", null);
//        }
//        return list.length;
//    }
}
