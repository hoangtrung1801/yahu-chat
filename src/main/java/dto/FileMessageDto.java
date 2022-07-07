package dto;

import model.MessageType;

import java.time.Instant;
import java.util.Arrays;

public class FileMessageDto extends MessageDto  {

    private byte[] buffer;
    private String filename;

    public FileMessageDto() {}

    public FileMessageDto(MessageType messageType, String messageText, String filename, byte[] buffer, Instant sentDatetime, ConversationDto conversation, UserDto user) {
        super(messageType, messageText, sentDatetime, conversation, user);
        this.filename = filename;
        this.buffer = buffer;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "FileMessageDto{" +
                "buffer=" + Arrays.toString(buffer) +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FileMessageDto that = (FileMessageDto) o;

        return Arrays.equals(buffer, that.buffer);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(buffer);
        return result;
    }
}
