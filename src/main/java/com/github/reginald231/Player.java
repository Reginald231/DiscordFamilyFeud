package com.github.reginald231;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;


public class Player {

    private final Member user;
    private final Role teamRole;
    private final String username;
    private final boolean isCaptain;

    public Player(@NotNull Member user, Role teamRole, boolean isCaptain){
        this.user = user;
        this.username = user.getEffectiveName();
        this.teamRole = teamRole;
        this.isCaptain = isCaptain;
    }

    public String getUsername() {return this.username;}

    public Member getUser(){return this.user;}

    public Role getTeamRole(){return this.teamRole;}

    public boolean isCaptain() {return isCaptain;}
}
