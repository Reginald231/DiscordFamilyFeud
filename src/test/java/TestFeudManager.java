import com.github.reginald231.DiscordFamilyFeud;
import com.github.reginald231.FeudManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;

import static org.junit.jupiter.api.Assertions.*;

public class TestFeudManager {

    @Test
    void testRemoveTeamMemberInputMismatchException() throws InputMismatchException, IOException {
        String token = DiscordFamilyFeud.getToken();
        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        String member = "David";
        String team = "c";
        FeudManager fm = new FeudManager(token, api);
        assertThrows(InputMismatchException.class, () ->
            fm.removeTeamMember(member, team));
    }

    @Test
    void testStartGameWhileGameActive() throws IOException {
        String token = DiscordFamilyFeud.getToken();
        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        FeudManager fm = new FeudManager(token, api);
        fm.setGameStarted(true);
        assertEquals(1, fm.startGame());
    }

    @Test
    void testEndGame() throws IOException{
        String token = DiscordFamilyFeud.getToken();
        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        FeudManager fm = new FeudManager(token, api);
        fm.setGameStarted(true);
        assertEquals(0, fm.endGame());
    }

    @Test
    void testEndGameWithNoGameActive() throws IOException{
        String token = DiscordFamilyFeud.getToken();
        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        FeudManager fm = new FeudManager(token, api);
        assertEquals(1, fm.endGame());
    }
}
