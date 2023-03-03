package pro.sky.lesson3_7skypro.services;

import java.io.File;

public interface FileService {
    boolean saveToFile(String json);

    String readFromFile();

    boolean cleanDataFile();

    File getDataFileTxt();
}
