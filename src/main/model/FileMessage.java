package main.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class FileMessage extends Message {

    private File file;

    public FileMessage(User user, String message, Date timestamp, File file) {
        super(user, message, timestamp);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
