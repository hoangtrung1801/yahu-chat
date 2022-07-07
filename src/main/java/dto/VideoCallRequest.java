package dto;

import java.io.Serializable;

public class VideoCallRequest implements Serializable {
    private ConversationDto conversation;

    public VideoCallRequest(ConversationDto conversation) {
        this.conversation = conversation;
    }

    public VideoCallRequest() {}

    public ConversationDto getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDto conversation) {
        this.conversation = conversation;
    }
}
