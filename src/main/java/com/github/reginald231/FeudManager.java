package com.github.reginald231;

import com.github.reginald231.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class FeudManager {

  private final Dotenv config;
  private final ShardManager shardManager;

  private Member host;
  private Role hostRole;
  private Role team1Role;
  private Member team1Captain;
  private Member team2Captain;
  private Role team2Role;

  private int teamSize;

  private ArrayList<Player> team1List = new ArrayList<>();
  private ArrayList<Player> team2List = new ArrayList<>();

  /**
   * Initializes login process for the bot.
   * @throws LoginException occurs in the case of invalid bot token.
   */
  public FeudManager() throws LoginException {
    config = Dotenv.configure().load();
    String token = config.get("TOKEN");

    DefaultShardManagerBuilder dsmBuilder = DefaultShardManagerBuilder.createDefault(
      token
    );
    dsmBuilder.setStatus(OnlineStatus.ONLINE);
    dsmBuilder.setActivity(Activity.playing("Getting dressed by Reg."));
    shardManager = dsmBuilder.build();
    EventListener el = new EventListener();
    el.setFeudManager(this);

    //register listeners
    shardManager.addEventListener(el);
  }

  /**
   * Adds a new player to a team using the provided team role (provided that the role is one of the registered
   * team roles for the game.)
   * @param p Player to add to the respective team list.
   * @param teamrole Role of the player to be added to the respective team list.
   */
  public void addPlayer(Player p, Role teamrole) {
    if (teamrole == this.team1Role) this.team1List.add(p); else if (
      teamrole == team2Role
    ) {
      this.team2List.add(p);
    } else throw new InvalidParameterException(
      "Provided team role does not match the currently registered roles."
    );
  }

  /**
   * Removes the given player from the specified team.
   * @param p The player to remove
   * @param teamrole The team to remove the player from.
   * @return 0 if the player was removed successfully, -1 if the player could not be found and removed.
   */
  public int removePlayer(Player p, Role teamrole) {
    HashMap<Player, Role> result;

    try {
      result = (HashMap<Player, Role>) findPlayer(p, teamrole);
    } catch (InvalidParameterException e) {
      return -1;
    }
    if (!result.isEmpty()) {
      if (teamrole == this.team1Role) team1List.remove(p);
      if (teamrole == this.team2Role) team2List.remove(p);
    }

    return 0;
  }

  /**
   * Searches for the provided player in the provided team.
   * @param p Player to be searched.
   * @param teamRole Team role of the player to be searched.
   * @return A hashmap of the found player and role.
   * @throws InvalidParameterException if player cannot be found.
   */
  public Map<Player, Role> findPlayer(Player p, Role teamRole) {
    HashMap<Player, Role> result = new HashMap<>();

    if (teamRole == this.team1Role) {
      for (Player teammate : team1List) {
        if (teammate.getUsername().equals(p.getUsername())) result.put(
          p,
          teamRole
        );
      }
    } else if (teamRole == this.team2Role) {
      for (Player teammate : team2List) {
        if (teammate.getUsername().equals(p.getUsername())) result.put(
          p,
          teamRole
        );
      }
    } else throw new InvalidParameterException(
      "The provided role is not currently registered in this game session."
    );
    return result;
  }

  /**
   * Removes the current host clears current host role in the game
   * @return 0 if host and host role was cleared successfully, -1 if there was no host and role to remove.
   */
  public int removeHost() {
    if (host != null && hostRole != null) {
      this.host = null;
      this.hostRole = null;
      return 0;
    } else return -1;
  }

  /**
   * Getter method for Dotenv
   * @return the Dotenv instance for the environment variables.
   */
  public Dotenv getConfig() {
    return this.config;
  }

  /**
   *
   * @return the ShardManager instance for the bot.
   */
  public ShardManager getShardManager() {
    return this.shardManager;
  }

  /**
   *
   * @return first team role value.
   */
  public Role getTeam1Role() {
    return this.team1Role;
  }

  public Member getTeam1Captain() {
    return this.team1Captain;
  }

  public List<Player> getTeam1List() {
    return this.team1List;
  }

  /**
   *
   * @return second team role value.
   */
  public Role getTeam2Role() {
    return this.team2Role;
  }

  public Member getTeam2Captain() {
    return this.team2Captain;
  }

  public List<Player> getTeam2List() {
    return this.team2List;
  }

  public Member getHost() {
    return this.host;
  }

  public Role getHostRole() {
    return this.hostRole;
  }

  public void setTeamCaptain(Role teamRole, Member p){
    if(teamRole == this.team1Role){
        this.team1Captain = p;
    }

    if(teamRole == this.team2Role){
        this.team2Captain = p;
  }

}

  public void setTeam1Role(Role team1Role) {
    this.team1Role = team1Role;
  }

  public void setTeam2Role(Role team2Role) {
    this.team2Role = team2Role;
  }

  /**
   * Adds a host to the game.
   * @param host User to be designated as the host of the game.
   * @param hostRole Role for the designated host of the game.
   */
  public void setHost(Member host, Role hostRole) {
    this.host = host;
    this.hostRole = hostRole;
  }

  
  public void setHostRole(Role hostRole) {
    this.hostRole = hostRole;
  }

  public int getTeamSize() {
    return this.teamSize;
  }

  public void setTeamSize(int teamSize) {
    this.teamSize = teamSize;
  }
}
