package util.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DataStream {
    public static InputStream read(File fileName) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("image.png");
        } catch (FileNotFoundException e) {
            System.err.println("Error occured in the File read.");
        }

        return inputStream;
    }
}