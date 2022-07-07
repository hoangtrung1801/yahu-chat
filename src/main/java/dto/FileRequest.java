package dto;

import java.io.Serializable;

public class FileRequest implements Serializable  {

    private UserDto user;
    private String filename;
    private String filenameOut;
    public FileRequest() {}

    public FileRequest(UserDto user, String filename, String filenameOut) {
        this.user = user;
        this.filename = filename;
        this.filenameOut = filenameOut;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilenameOut() {
        return filenameOut;
    }

    public void setFilenameOut(String filenameOut) {
        this.filenameOut = filenameOut;
    }
}
