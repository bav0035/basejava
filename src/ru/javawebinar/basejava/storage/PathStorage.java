package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.StreamSerializer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    protected Path dir;
    private StreamSerializer streamSerializer;

    protected PathStorage(String path, StreamSerializer streamSerializer) {
        Objects.requireNonNull(path, "Directory must be not null");
        dir = Paths.get(path);
        this.streamSerializer = streamSerializer;
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

    @Override
    protected Path getSearchKey(String uuid) {
        return dir.resolve(uuid);
    }

    @Override
    protected List<Resume> doCopyAll() {
        return getFilesList().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            streamSerializer.doWrite(r, new BufferedOutputStream(new FileOutputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Can't create Path" + path, path.getFileName().toString(), e);
        }
        doUpdate(r, path);
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.isRegularFile(path);
    }

    @Override
    public void clear() {
        getFilesList().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getFilesList().count();
    }

    private Stream<Path> getFilesList() {
        try {
            return Files.list(dir);
        } catch (IOException e) {
            throw new StorageException("Dir read error", e);
        }
    }
}
