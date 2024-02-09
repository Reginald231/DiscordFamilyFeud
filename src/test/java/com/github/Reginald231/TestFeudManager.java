package com.github.Reginald231;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.github.reginald231.FeudManager;
import com.github.reginald231.Player;
import com.mysql.cj.jdbc.JdbcPropertySetImpl;

import io.github.cdimascio.dotenv.Dotenv;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.MemberImpl;
import net.dv8tion.jda.internal.entities.RoleImpl;
import net.dv8tion.jda.internal.entities.UserImpl;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class TestFeudManager {

  FeudManager fm;
  private static final int TEAM_1_ROLE_ID = 0;
  private static final int TEAM_2_ROLE_ID = 1;
  private static final int HOST_ROLE_ID = 2;

  private static final int USER_1_ID = 3;
  private static final int USER_2_ID = 4;
  private static final int USER_3_ID = 5;

  private static final String USERNAME_1 = "TEST USER 1";
  private static final String USERNAME_2 = "TEST USER 2";
  private static final String USERNAME_3 = "TEST USER 3";

  private static final int TEAM_SIZE = 5;

  private static final JDA TEST_JDA = new JDAImpl(null);
  private static final Guild TEST_GUILD = new GuildImpl((JDAImpl) TEST_JDA, 0);

  private static final Member MEMBER_1 = new MemberImpl(
    (GuildImpl) TEST_GUILD,
    new UserImpl(USER_1_ID, (JDAImpl) TEST_JDA)
  );
  private static final Member MEMBER_2 = new MemberImpl(
    (GuildImpl) TEST_GUILD,
    new UserImpl(USER_2_ID, (JDAImpl) TEST_JDA)
  );
  private static final Member MEMBER_3 = new MemberImpl(
    (GuildImpl) TEST_GUILD,
    new UserImpl(HOST_ROLE_ID, (JDAImpl) TEST_JDA)
  );

  private static final Role TEAM_ROLE_1 = new RoleImpl(
    TEAM_1_ROLE_ID,
    TEST_GUILD
  );
  private static final Role TEAM_ROLE_2 = new RoleImpl(
    TEAM_2_ROLE_ID,
    TEST_GUILD
  );
  private static final Role HOST_ROLE = new RoleImpl(HOST_ROLE_ID, TEST_GUILD);

  private static final Player TEST_PLAYER_1 = new Player(
    MEMBER_1,
    TEAM_ROLE_1,
    false
  );
  private static final Player TEST_PLAYER_2 = new Player(
    MEMBER_2,
    TEAM_ROLE_2,
    false
  );

  @BeforeAll
  void prepareFeudManager() {
    try {
      fm = new FeudManager();
    } catch (LoginException e) {
      fail();
    }
  }

  @Test
  void testAddPlayer() {
    Player p = new Player(MEMBER_1, TEAM_ROLE_1, false);
    p.setUsername(USERNAME_1);

    fm.addPlayer(p, p.getTeamRole());
    try {
      fm.findPlayer(p, p.getTeamRole());
    } catch (InvalidTokenException e) {
      fail();
    }
  }

  @Test
  void testFindPlayer() {
    Player p = new Player(MEMBER_2, TEAM_ROLE_2, false);
    p.setUsername(USERNAME_2);
    fm.addPlayer(p, p.getTeamRole());
    try {
      fm.findPlayer(p, p.getTeamRole());
    } catch (InvalidTokenException e) {
      fail();
    }
  }

  @Test
  void testGetConfig() {
    Dotenv config = fm.getConfig();
    assertNotNull(config);
  }

  @Test
  void testGetHost() {
    Member testHost = fm.getHost();
    assertNotNull(testHost);
  }

  @Test
  void testGetHostRole() {
    Role testHostRole = fm.getHostRole();
    assertNotNull(testHostRole);
  }

  @Test
  void testGetShardManager() {
     ShardManager sm = fm.getShardManager();
     assertNotNull(sm);
  }

  @Test
  void testGetTeam1Captain() {
    fm.setTeamCaptain(TEAM_ROLE_1, MEMBER_1);
    Member t1captain = fm.getTeam1Captain();
    assertNotNull(t1captain);

    assertTrue(t1captain == MEMBER_1);
    assertTrue(TEAM_ROLE_1 == fm.getTeam1Role());
  }

  @Test
  void testGetTeam1List() {
    List<Player> team1List = fm.getTeam1List();
    assertNotNull(team1List);
  }

  @Test
  void testGetTeam1Role() {
    Role team1role = fm.getTeam1Role();
    assertNotNull(team1role);
  }

  @Test
  void testGetTeam2Captain() {
    fm.setTeam2Role(TEAM_ROLE_2);
    fm.setTeamCaptain(TEAM_ROLE_2, MEMBER_1);

    Member team2Captain = fm.getTeam2Captain();
    assertNotNull(team2Captain);
  }

  @Test
  void testGetTeam2List() {
    List<Player> team2List = fm.getTeam2List();
    assertNotNull(team2List);
    }

  @Test
  void testGetTeam2Role() {
    Role team2Role = fm.getTeam2Role();
    assertNotNull(team2Role);
  }

  @Test
  void testGetTeamSize() {
    fm.setTeamSize(6);

    int teamSize = fm.getTeamSize();
    assertTrue(teamSize >= 0);
  }

  @Test
  void testRemoveHost() {
    fm.setHost(MEMBER_1, HOST_ROLE);
    assertNotNull(fm.getHost());

    assertEquals(0, fm.removeHost());
  }

  @Test
  void testRemovePlayer() {
    TEST_PLAYER_1.setUsername("Test User 1");
    fm.addPlayer(TEST_PLAYER_1, TEAM_ROLE_1);
    assertNotNull(fm.getTeam1List());

    assertEquals(0, fm.removePlayer(TEST_PLAYER_1, TEAM_ROLE_1));
  } 

  @Test
  @BeforeEach
  void testSetHost() {
    fm.setHost(MEMBER_3, HOST_ROLE);
    assertEquals(MEMBER_3, fm.getHost());
  }

  @Test
  @BeforeEach
  void testSetHostRole() {
    fm.setHostRole(HOST_ROLE);
    assertEquals(HOST_ROLE_ID, Integer.parseInt(fm.getHostRole().getId()));
  }

  @Test
  @BeforeEach
  void testSetTeam1Role() {
    fm.setTeam1Role(TEAM_ROLE_1);
    assertEquals(TEAM_1_ROLE_ID, Integer.parseInt(fm.getTeam1Role().getId()));
  }

  @Test
  @BeforeEach
  void testSetTeam2Role() {
    fm.setTeam2Role(TEAM_ROLE_2);
    assertEquals(TEAM_2_ROLE_ID, Integer.parseInt(fm.getTeam2Role().getId()));
  }

  @Test
  @BeforeEach
  void testSetTeamSize() {
    fm.setTeamSize(TEAM_SIZE);
    assertEquals(TEAM_SIZE, fm.getTeamSize());
  }
}
