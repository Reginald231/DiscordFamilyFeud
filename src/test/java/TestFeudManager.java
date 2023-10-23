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
    void testAddTeamMember() throws IOException {
        String token = DiscordFamilyFeud.getToken();
        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        String member = "Reginald";
        String team = "a"; //Should be 'a' or 'b'
        FeudManager fm = new FeudManager(token, api);
        assertEquals(0, fm.addTeamMember(member, team));
        assertTrue(fm.TeamAMembers.contains(member));
    }

    @Test
    void testAddTeamMemberInputMismatchException() throws InputMismatchException, IOException{
        String token = DiscordFamilyFeud.getToken();
        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        String member = "Reginald";
        String team = "C";
        FeudManager fm = new FeudManager(token, api);

        assertThrows(InputMismatchException.class, () ->
            fm.addTeamMember(member, team));

    }

    @Test
    void testRemoveTeamMember() throws InputMismatchException, IOException {
        String token = DiscordFamilyFeud.getToken();
        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        String member = "Reginald";
        String team = "a";
        FeudManager fm = new FeudManager(token, api);
        fm.addTeamMember(member, team);
        fm.removeTeamMember(member, team);
        assertFalse(fm.TeamBMembers.contains(member));
    }

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
    void testGetTeamMembers() throws IOException {
        String token = DiscordFamilyFeud.getToken();
        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        String[] teamAMembers = {"John", "David", "Mike"};
        String[] teamBMembers = {"Jacob", "Devin", "John"};
        String teamA = "a";
        String teamB = "b";
        FeudManager fm = new FeudManager(token, api);

        for(String member:teamAMembers){
            fm.addTeamMember(member, teamA);
        }
        for(String member:teamBMembers){
            fm.addTeamMember(member, teamB);
        }

        System.out.print("Team A Members:\t" + Arrays.toString(fm.getTeamMembers("a").toArray()) + "\n");
        System.out.print("Team B Members:\t" + Arrays.toString(fm.getTeamMembers("b").toArray()) + "\n");


        //team member cleanup
        for(String member:teamAMembers){
            fm.removeTeamMember(member, teamA);
        }
        for(String member:teamBMembers){
            fm.removeTeamMember(member, teamB);
        }
    }

    @Test
    void testGetTeamMemberInputMismatchException() throws InputMismatchException, IOException {
        String token = DiscordFamilyFeud.getToken();
        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        String team = "d";
        FeudManager fm = new FeudManager(token, api);
        assertThrows(InputMismatchException.class, () ->
                fm.getTeamMembers(team));
    }

    @Test
    void testStartGame() throws IOException {
        String token = DiscordFamilyFeud.getToken();
        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        FeudManager fm = new FeudManager(token, api);
        fm.startGame();
        assertTrue(fm.isGameStarted());
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
