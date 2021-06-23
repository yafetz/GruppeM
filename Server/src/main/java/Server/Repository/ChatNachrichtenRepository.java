package Server.Repository;

import Server.Modell.ChatNachrichten;
import Server.Modell.ChatRaum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatNachrichtenRepository extends JpaRepository<ChatNachrichten, Long> {
    List<ChatNachrichten> findChatNachrichtenByChat(ChatRaum chat);
}
