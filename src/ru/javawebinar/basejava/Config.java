package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private static File PROPS;
    private static String url;
    private static String user;
    private static String password;

    private Properties prop = new Properties();
    private File storageDir;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        PROPS = new File("config\\resumes.properties");
        try (InputStream fis = new FileInputStream(PROPS)) {

            prop.load(fis);
            storageDir = new File(prop.getProperty("storage.dir"));
            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop. getProperty("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file" + PROPS.getAbsolutePath());
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }
}
