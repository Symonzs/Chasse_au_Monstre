package model;

import java.io.File;
import java.io.FileNotFoundException;

public class FileFinder {

    private FileFinder() {
        super();
    }

    public static File find(String path, String fileName) throws FileNotFoundException {
        File folder = new File(path);
        File file = null;

        if (!folder.exists() || !folder.isDirectory()) {
            throw new FileNotFoundException("Le dossier " + path + " n'existe pas");
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile() && f.getName().equals(fileName)) {
                    return f;
                }
                if (f.isDirectory()) {
                    file = find(f.getAbsolutePath(), fileName);
                    if (file != null) {
                        return file;
                    }
                }
            }
        }
        throw new FileNotFoundException("Le fichier " + fileName + " n'existe pas");
    }

    public static File find(String fileName) throws FileNotFoundException {
        String path = System.getProperty("user.dir") +
                File.separator + "resources" +
                File.separator + "map"
                + File.separator;
        return find(path, fileName);
    }
}
