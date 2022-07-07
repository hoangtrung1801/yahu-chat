package dto;

import model.MessageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Instant;
import java.util.Objects;

public class ImageMessageDto extends MessageDto implements Serializable {
    transient private BufferedImage image;

    public ImageMessageDto() {
    }

    public ImageMessageDto(MessageType messageType, String messageText, BufferedImage image, Instant sentDatetime, ConversationDto conversation, UserDto user) {
        super(messageType, messageText, sentDatetime, conversation, user);
        this.image = image;
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = ImageIO.read(in);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(image, "png", out);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ImageMessageDto that = (ImageMessageDto) o;

        return image != null ? image.equals(that.image) : that.image == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ImageMessageDto{" +
                "image=" + image +
                "} " + super.toString();
    }
}
