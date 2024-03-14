package aimeter.arion.domain.repository;

import aimeter.arion.domain.entity.AIMeterBotChat;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AIMeterBotChatRepository extends CrudRepository<AIMeterBotChat, Long> {

    @Query("""
        SELECT botChat.user_language FROM ai_meter_bot_chat AS botChat
        WHERE botChat.chat_id = :chatId
        AND botChat.chat_type='""" + AIMeterBotChat.TELEGRAM_CHAT_TYPE_NAME + "'")
    Optional<String> findTelegramUserSelectedLanguage(@Param("chatId") long chatId);
    
}
