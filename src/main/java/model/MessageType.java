package model;

public enum MessageType {
    TEXT("text"),
    FILE("file"),
    IMAGE("image"),
    ICON("icon");

    private String type;

    private MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
