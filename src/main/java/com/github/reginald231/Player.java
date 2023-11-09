package com.github.reginald231;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class Player {

    private User user;
    private Role teamRole;
    private String username;
    public boolean isCaptain;

    public String getUsername() {return this.username;}

    public User getUser(){return this.user;}

    public Role getTeamRole(){return this.teamRole;}

    public boolean isCaptain() {return isCaptain;}
}
