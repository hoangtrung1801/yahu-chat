package dto;

import java.io.Serializable;

public class VideoCallData implements Serializable {

    private ConversationDto conversation;
    private byte[] image;

    public VideoCallData() {}

    public VideoCallData(ConversationDto conversation, byte[] image) {
        this.conversation = conversation;
        this.image = image;
    }

    public ConversationDto getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDto conversation) {
        this.conversation = conversation;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
