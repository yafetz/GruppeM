package Server.Repository;

import Server.Modell.ChatNachrichten;
import Server.Modell.ChatRaum;
import Server.Modell.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ChatNachrichtenRepository extends JpaRepository<ChatNachrichten, Long> {
    List<ChatNachrichten> findChatNachrichtenByChat(ChatRaum chat);

    ArrayList<ChatNachrichten> findChatNachrichtenByChatAndNutzer(ChatRaum chatRaum, Nutzer nutzerid);
    @Query("SELECT MAX (chatnachrichten) FROM ChatNachrichten chatnachrichten WHERE chatnachrichten.chat LIKE ?1")
    ChatNachrichten findLastChatNachricht(ChatRaum chatRaum);
}
