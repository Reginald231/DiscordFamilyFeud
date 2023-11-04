package com.github.reginald231;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.team.Team;
import org.javacord.api.entity.user.User;

import java.util.*;


/**
 * note:  return "<@!" + [userIDStringHere] + ">" is the format for username tags.
 */

public class FeudManager {
    private final String token;

    Role teamARole;
    Role teamBRole;
    public ArrayList<Player> TeamAMembers;
    private int teamAScore;
    private String teamACaptain;
    private  String teamBCaptain;
    private int teamBScore;
    public ArrayList<Player> TeamBMembers;
    private final DiscordApi api;
    public boolean gameStarted;
    public boolean teamsSet;

    public FeudManager(String token, DiscordApi api) {
        this.token = token;
        this.api = api;
        this.TeamAMembers = new ArrayList<Player>();
        this.TeamBMembers = new ArrayList<Player>();
    }


    public void removeTeamMember(String member, String team) throws InputMismatchException, NullPointerException {
        switch (team.toLowerCase(Locale.ROOT)) {
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

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * Starts a new game (if not already active).
     * @return 1 if game failed to start, otherwise 0.
     */
    public int startGame() {
        if(!gameStarted){
            gameStarted = true;

            preGameSetup();
            return 0;
        }

        return 1;
    }

    public void preGameSetup(){

        MessageBuilder msg = new MessageBuilder();

    }


    /**
     * * Ends the current game.
     *
     * @return 1 if the game has not already been started. 0 if the game has already been started (successfully ended).
     */
    public int endGame() {
        if (!gameStarted)
            return 1;

        else {
            gameStarted = false;
            return 0;
        }

    }
}
