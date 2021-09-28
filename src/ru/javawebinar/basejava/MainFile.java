package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class MainFile {
    public static void main(String[] args) throws IOException {
        String filePath = ".gitignore";
        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File dir = new File("C:\\projects\\basejava\\src\\ru\\javawebinar\\basejava");

        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("=====================================");
        File startDir = new File(".");
        fileNamesViewerRecurse(startDir);

    }

    public static void fileNamesViewerRecurse(File dir) throws IOException {
        System.out.println("Dir " + dir.getCanonicalPath());

        List<File> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(dir.listFiles())));
        Iterator<File> fIterator = files.iterator();
        while (fIterator.hasNext()) {
            File f = fIterator.next();
            if (f.isFile()) {
                System.out.println(f.getName());
                fIterator.remove();
            }
        }
        System.out.println();
        for(File f : files) {
            fileNamesViewerRecurse(f);
        }
    }
}
