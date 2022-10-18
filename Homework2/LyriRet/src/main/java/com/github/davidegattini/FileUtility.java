package com.github.davidegattini;

import org.apache.lucene.store.Directory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtility {

    public List<File> listAllFileOfDirectory(String directoryName) {
        List<File> files = new ArrayList<>();
        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if(!file.isHidden()){
                    if (file.isFile()) {
                        files.add(file);
                    } else if (file.isDirectory()) {
                        files.addAll(listAllFileOfDirectory(file.getAbsolutePath()));
                    }
                }
            }
        return files;
    }

    public static String readFromTxt(String pathFile) throws Exception{
        System.out.println(pathFile);
        Path filePath = Path.of(pathFile);
        String content = Files.readString(filePath);
        content = content.replace("\n", " ");
        return content;
    }
}
