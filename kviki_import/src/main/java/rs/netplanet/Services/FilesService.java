package rs.netplanet.Services;

import java.io.File;
import java.io.FileNotFoundException;

public class FilesService {

    public File getTheLastestFile() throws FileNotFoundException {
        File processedDir = new File("processed");
        File[] files = processedDir.listFiles();
        if (processedDir.exists() && files.length > 0) {
            File lastModifiedFile = files[0];
            long lastModifiedTime = 0;
            for (File file : files) {
                long fileLastModifiedTime = file.lastModified();
                if (fileLastModifiedTime > lastModifiedTime) {
                    lastModifiedFile = file;
                    lastModifiedTime = fileLastModifiedTime;
                }
            }
            return lastModifiedFile;
        } else {
            processedDir.mkdir();
            throw new FileNotFoundException();
        }
    }
}
