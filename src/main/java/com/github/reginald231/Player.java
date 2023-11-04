package com.github.reginald231;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class Player {
    private String username;
    private final User user;
    private final Role team;

    private Server server;
    public boolean isCaptain;

    public Player (User user, Role team, Server server){
        this.user = user;
        this.username = this.user.getDisplayName(server);
        this.team = team;
    }

    public Player (User user, Role team, boolean isCaptain){
        this.user = user;
        this.team = team;
        this.isCaptain = isCaptain;
    }

    public Role getTeam(){
        return this.team;
    }

    public User getUser(){
        return this.user;
    }

    public Server getServer(){
        return this.server;
    }

    public String getUsername() {
        return username;
    }

    public String getTeamName(){
        return this.team.getName();
    }

    public String getServerNamer(){
        return this.server.getName();
    }
}
