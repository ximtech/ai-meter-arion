package aimeter.arion.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class AIMeterBotChat {

    public static final String TELEGRAM_CHAT_TYPE_NAME = "TELEGRAM";

    @Id
    long id;
    Long chatId;
    String userLanguage;
}
