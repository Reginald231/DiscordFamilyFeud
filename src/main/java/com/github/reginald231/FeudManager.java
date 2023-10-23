package com.github.reginald231;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.message.MessageBuilder;

import java.util.*;


public class FeudManager {
    private final String token;
    public ArrayList<String> TeamAMembers;
    private static int teamAScore;
    private static int teamBScore;
    public ArrayList<String> TeamBMembers;
    private final DiscordApi api;
    private boolean gameStarted;

    public FeudManager(String token, DiscordApi api) {
        this.token = token;
        this.api = api;
        this.TeamAMembers = new ArrayList<String>();
        this.TeamBMembers = new ArrayList<String>();
    }

    public int addTeamMember(String member, String team) throws InputMismatchException {
        switch (team.toLowerCase(Locale.ROOT)) {
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

    public ArrayList<String> getTeamMembers(String team) {
        switch (team) {
            case "a":
                return TeamAMembers;
            case "b":
                return TeamBMembers;
            default:
                throw new InputMismatchException("Invalid team label. Must only be 'A' or 'B'.");
        }
    }

    public boolean isGameStarted() {
        return gameStarted;
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
            return 0;
        }

        return 1;
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
