package cr.una.bolsaempleo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;

    public static MessageResponse of(String message) {
        return new MessageResponse(message);
    }
}
