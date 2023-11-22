package com.github.Reginald231;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.reginald231.FeudManager;
import com.github.reginald231.Player;
import com.mysql.cj.jdbc.JdbcPropertySetImpl;
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
  void testGetConfig() {}

  @Test
  void testGetHost() {}

  @Test
  void testGetHostRole() {}

  @Test
  void testGetShardManager() {}

  @Test
  void testGetTeam1Captain() {}

  @Test
  void testGetTeam1List() {}

  @Test
  void testGetTeam1Role() {}

  @Test
  void testGetTeam2Captain() {}

  @Test
  void testGetTeam2List() {}

  @Test
  void testGetTeam2Role() {}

  @Test
  void testGetTeamSize() {}

  @Test
  void testRemoveHost() {}

  @Test
  void testRemovePlayer() {}

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
