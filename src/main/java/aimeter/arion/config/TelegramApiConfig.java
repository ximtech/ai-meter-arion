package aimeter.arion.config;

import aimeter.arion.client.TelegramApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramApiConfig {

    @Value("${telegram.bot.token}")
    String telegramBotToken;
    
    @Bean
    public TelegramApiClient telegramBot() {
        return new TelegramApiClient(telegramBotToken);
    }
}
