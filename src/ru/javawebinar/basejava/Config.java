package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.SqlStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private final File storageDir;
    private final SqlStorage sqlStorage;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        File PROPS = new File(getHomeDir(), "config\\resumes.properties");
        try (InputStream fis = new FileInputStream(PROPS)) {

            Properties prop = new Properties();
            prop.load(fis);
            storageDir = new File(prop.getProperty("storage.dir"));
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");
            sqlStorage = new SqlStorage(url, user, password);
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file" + PROPS.getAbsolutePath());
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public SqlStorage getSqlStorage() {
        return sqlStorage;
    }

    private static File getHomeDir() {
        String homeDir = System.getProperty("homeDir");
        File file = new File(homeDir == null ? "." : homeDir);
        if (!file.isDirectory()) {
            throw new IllegalStateException(file + " is not directory.");
        }
        return file;
    }
}
