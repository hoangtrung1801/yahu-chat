package dto;

import java.io.Serializable;

public class FileRespone implements Serializable {

    private UserDto user;
    private String filenameOut;
    private byte[] fileBuffer;

    public FileRespone() {}

    public FileRespone(UserDto user, String filenameOut, byte[] fileBuffer) {
        this.user = user;
        this.filenameOut = filenameOut;
        this.fileBuffer = fileBuffer;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getFilenameOut() {
        return filenameOut;
    }

    public void setFilenameOut(String filenameOut) {
        this.filenameOut = filenameOut;
    }
    public byte[] getFileBuffer() {
        return fileBuffer;
    }

    public void setFileBuffer(byte[] fileBuffer) {
        this.fileBuffer = fileBuffer;
    }

}
