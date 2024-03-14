package aimeter.arion.service;

import aimeter.arion.client.TelegramApiClient;
import aimeter.arion.domain.entity.AIMeterData;
import aimeter.arion.domain.entity.AIMeterDataTransaction;
import aimeter.arion.domain.entity.AIMeterDevice;
import aimeter.arion.domain.entity.AIMeterSubscription;
import aimeter.arion.domain.repository.AIMeterBotChatRepository;
import aimeter.arion.domain.repository.AIMeterDataTransactionRepository;
import aimeter.arion.dto.IntegrationContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import static aimeter.arion.domain.AIMeterTransactionStatus.COMPLETED;
import static aimeter.arion.util.DateTimeFormatConstants.FORMATTER_YYYY_MM_DD_HH_MM;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramIntegrationHandler implements IntegrationHandler {

    public static final String TELEGRAM_CAPTION_TEMPLATE = "telegram_caption.ftl";
    
    final Configuration configuration;
    final TelegramApiClient telegramApiClient;
    final ResourceBundleMessageSource messageSource;
    final AIMeterBotChatRepository aiMeterBotChatRepository;

    @Override
    public String integrationType() {
        return "TELEGRAM";
    }

    @Override
    @SneakyThrows
    public void runIntegration(IntegrationContext context) {
        SendPhoto sendPhotoWithCaption = new SendPhoto();
        sendPhotoWithCaption.setChatId(context.subscription().getTelegramChatId());
        sendPhotoWithCaption.setParseMode("html");
        
        InputStream inputStream = new ByteArrayInputStream(context.data().getImageData());
        InputFile photoFile = new InputFile(inputStream, context.data().getImageName());
        sendPhotoWithCaption.setPhoto(photoFile);
        
        Template template = configuration.getTemplate(TELEGRAM_CAPTION_TEMPLATE);
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> parameterMap = prepareTemplateParams(context);
        template.process(parameterMap, stringWriter);
        sendPhotoWithCaption.setCaption(stringWriter.toString());
        telegramApiClient.sendPhoto(sendPhotoWithCaption);
    }
    
    private Map<String, Object> prepareTemplateParams(IntegrationContext context) {
        Locale userLocale = getTelegramUserLocale(context.subscription().getTelegramChatId());
        return Map.of(
                "i18n", new MessageHandler(userLocale, messageSource),
                "meterName", context.device().getDeviceName(),
                "battery", context.device().getBatteryLevel(),
                "date", FORMATTER_YYYY_MM_DD_HH_MM.format(context.data().getImageDate())
        );
    }

    public Locale getTelegramUserLocale(long chatId) {
        return aiMeterBotChatRepository.findTelegramUserSelectedLanguage(chatId)
                .map(Locale::of)
                .orElse(Locale.ENGLISH);
    }
}
