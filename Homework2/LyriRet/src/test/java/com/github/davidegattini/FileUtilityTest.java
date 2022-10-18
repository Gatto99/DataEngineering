package com.github.davidegattini;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtilityTest {

    @Test
    public void testReadStringFromTxt() throws Exception{
        Path filePath = Path.of("../lyrics/Coldplay/42");
        String content = Files.readString(filePath);
        content = content.replace("\n", " ");
        Assert.assertTrue(!content.contains("\n"));
    }

    @Test
    public void testAllFilesDirectory(){
        FileUtility futils = new FileUtility();
        List<File> files = new ArrayList<>();
        files = futils.listAllFileOfDirectory("../lyrics/");
        System.out.println(files.get(0).toString());
    }
}
