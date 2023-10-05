import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;

import java.util.*;


public class FeudManager {
    private final String token;
    public ArrayList<String> TeamAMembers;
    public ArrayList<String> TeamBMembers;
    private final DiscordApi api;
    public boolean gameStarted;

    public FeudManager(String token, DiscordApi api){
        this.token = token;
        this.api = api;
        this.TeamAMembers = new ArrayList<String>();
        this.TeamBMembers = new ArrayList<String>();
    }

    public int addTeamMember(String member, String team) throws InputMismatchException{
        switch(team.toLowerCase(Locale.ROOT)) {
            case "a":
                TeamAMembers.add(member);
                break;
            case "b":
                TeamBMembers.add(member);
                break;
            default:
                throw new InputMismatchException("Invalid team label. Must only be 'A' or 'B'.");
        }
        return 0;
    }

    public void removeTeamMember(String member, String team) throws InputMismatchException, NullPointerException {
        switch(team.toLowerCase(Locale.ROOT)) {
            case "a":
                TeamAMembers.remove(member);
                break;
            case "b":
                TeamBMembers.remove(member);
                break;
            default:
                throw new InputMismatchException("Invalid team label. Must only be 'A' or 'B'.");
        }

    }

    public ArrayList<String> getTeamMembers(String team){
         switch(team){
             case "a":
                 return TeamAMembers;
             case "b":
                 return TeamBMembers;
             default:
                 throw new InputMismatchException("Invalid team label. Must only be 'A' or 'B'.");
         }
    }

    public void startGame(){
        gameStarted = true;
        Set<ServerChannel> servers = api.getServerChannels();
        System.out.println("List of server channels:\n" + Arrays.toString(servers.toArray()));
    }
}
