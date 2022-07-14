package client.stickerpicker;

import java.util.Arrays;

public class Sticker {
    private String stickerName;
    private byte[] stickerImage;

    public Sticker(String stickerName, byte[] stickerImage) {
        this.stickerName = stickerName;
        this.stickerImage = stickerImage;
    }

    public Sticker() {}

    public String getStickerName() {
        return stickerName;
    }

    public void setStickerName(String stickerName) {
        this.stickerName = stickerName;
    }

    public byte[] getStickerImage() {
        return stickerImage;
    }

    public void setStickerImage(byte[] stickerImage) {
        this.stickerImage = stickerImage;
    }

    @Override
    public String toString() {
        return "Sticker{" +
                "stickerName='" + stickerName + '\'' +
                ", stickerImage=" + Arrays.toString(stickerImage) +
                '}';
    }
}
