import org.javacord.api.DiscordApi;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;


public class FeudManager {
    private String token;
    private ArrayList<String> TeamAMembers;
    private ArrayList<String> TeamBMembers;
    private DiscordApi api;
    public boolean gameStarted;

    public FeudManager(String token, DiscordApi api){
        this.token = token;
        this.api = api;
    }

    public int addTeamAMember(String member, String team){
        switch(team.toLowerCase(Locale.ROOT)) {
            case "a":
                TeamAMembers.add(member);
                break;
            case "b":
                TeamBMembers.add(member);
                break;
            default:
                return 1;
        }
        return 0;
    }
    public void removeTeamMember(String member, String team) throws InputMismatchException {
        switch(team.toLowerCase(Locale.ROOT)) {
            case "a":
                TeamAMembers.remove(member);
                break;
            case "b":
                TeamBMembers.remove(member);
                break;
            default:
                throw new InputMismatchException("Member not found.");
        }

    }
    public void startGame(){
        gameStarted = true;
    }
}
