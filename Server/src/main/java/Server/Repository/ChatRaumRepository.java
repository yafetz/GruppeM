package Server.Repository;

import Server.Modell.ChatRaum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRaumRepository extends JpaRepository<ChatRaum, Long> {
    ChatRaum findChatRaumById(long chat_id);
}
