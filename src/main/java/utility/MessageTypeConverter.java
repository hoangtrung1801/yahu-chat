package utility;

import model.MessageType;

import javax.persistence.AttributeConverter;
import java.util.stream.Stream;

public class MessageTypeConverter implements AttributeConverter<MessageType, String> {
    @Override
    public String convertToDatabaseColumn(MessageType messageType) {
        if(messageType == null)  return null;
        return messageType.getType();
    }

    @Override
    public MessageType convertToEntityAttribute(String s) {
        if(s == null) return null;

        return Stream.of(MessageType.values())
                .filter(type -> type.getType().equals(s))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
