package model;

import java.io.File;

public class FileFinder {

    public static final String PATH = System.getProperty("user.dir");

    private FileFinder() {
        super();
    }

    public static File find(String path, String fileName) {
        path += File.separator;
        File folder = new File(path);
        System.out.println(folder.getAbsolutePath());
        File file = null;

        if (!folder.exists())
            return null;
        if (folder.isDirectory()) {
            for (File f : folder.listFiles()) {
                if (f.isFile() && f.getName().equals(fileName))
                    file = f;
                if (f.isDirectory())
                    file = find(path + f.getName(), fileName);
                if (file != null)
                    return file;
            }
        }

        return file;
    }

    public static File find(String fileName) {
        return find(FileFinder.PATH, fileName);
    }
}
