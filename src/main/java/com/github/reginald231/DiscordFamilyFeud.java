package com.github.reginald231;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.*;

import java.awt.*;
import java.io.*;
import java.util.*;


public class DiscordFamilyFeud {

    private static final Random rand = new Random(); //For random number generation.
    private static String token; //Discord bot token.
    private static DiscordApi api; //Discord api instance (created via bot token).
    private static FeudManager fm; //com.github.reginald231.FeudManager instance to handle in-depth game functionality.

    private static DBManager dm;
    private static String invite; //Bot invite link

    /**
     * Initializes setup of the program.
     */
    public static void main(String[] args){
        startUp();
    }

    /**
     * Grabs the bot token from a given text file.
     * @return Token from the bot_token.txt file.
     * @throws FileNotFoundException For missing bot token file.
     * @throws IOException For issues with reading file.
     */
    public static String getToken() throws FileNotFoundException, IOException {
        String token = "";
        String line;
        File f = new File("/Users/tech/IdeaProjects/discordFamilyFeud/src/main/java/com/github/reginald231/" +
                "bot_token.txt");


        try(BufferedReader br = new BufferedReader(new FileReader(f))) {
            while ((line = br.readLine()) != null)
                token = line;
            br.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return token;

    }

    /**
     * Prepares the bot for online use (i.e: Logging into discord, setting up commands, etc.).*
     */
    public static void startUp() {
        try {
            token = getToken();
        } catch (FileNotFoundException FNF) {
            FNF.printStackTrace();
            System.err.println("File could not be found at the given path.");
        } catch (IOException IOE) {
            IOE.printStackTrace();
            System.err.println("Token could not be read properly");
        }

        System.out.println("Logging in to Discord...");

        api = new DiscordApiBuilder().setToken(token)
                .addIntents(Intent.MESSAGE_CONTENT, Intent.GUILD_MEMBERS)
                .login().join();

        System.out.println("Bot is online.");
        invite = api.createBotInvite();
        fm = new FeudManager(token, api);
        dm = new DBManager(fm);

        api.updateActivity("Getting dressed by Reg.");

        buildCommands();

    }

    /**
     * Sets up commands for the bot after bot is online.*
     */
   public static void buildCommands() {

       Set <SlashCommandBuilder> builders = new HashSet<>();
       builders.add(new SlashCommandBuilder().setName("play").setDescription("Starts a new game."));
       builders.add(new SlashCommandBuilder().setName("invite").setDescription("Posts a link to share the " +
               "bot with others."));
       builders.add(new SlashCommandBuilder().setName("end").setDescription("Ends the current game."));
       builders.add(new SlashCommandBuilder().setName("tipping").setDescription("Posts a link where you can support " +
               "the creator."));

        builders.add(new SlashCommandBuilder().setName("team_scores")
                .setDescription("Gets the scores for a given team name.")
                .setOptions(Arrays.asList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING,
                                "Team_Name", "Name of the team", true),
                        SlashCommandOption.create(SlashCommandOptionType.DECIMAL,
                                "Number", "Number of maximum scores to return, sorted by most recent.",
                                true)

                )));

        builders.add(new SlashCommandBuilder().setName("set_team_names")
                .setDescription("Initializes two teams using the given roles.")
                .setOptions(Arrays.asList(
                        SlashCommandOption.create(SlashCommandOptionType.ROLE,
                                "Team_A_Name", "Name for team A", true),
                        SlashCommandOption.create(SlashCommandOptionType.ROLE,
                                "Team_B_Name", "Name for Team B", true)
                )));


        builders.add(new SlashCommandBuilder().setName("add_teammate")
                .setDescription("Adds a teammate to the given role.")
                .setOptions(Arrays.asList(
                        SlashCommandOption.create(SlashCommandOptionType.USER,
                                "User", "Name of the user you want to add."
                                , true),
                        SlashCommandOption.create(SlashCommandOptionType.ROLE,
                                "Team_Role", "The role of the team to place this user on."
                                , true),
                        SlashCommandOption.create(SlashCommandOptionType.BOOLEAN,
                                "Captain", "Is this teammate a captain?"
                                ,true)
                )));

        builders.add(new SlashCommandBuilder().setName("test_modal")
                .setDescription("Presents a modal to the user for testing purposes."));

        api.bulkOverwriteGlobalApplicationCommands(builders).join();


       //Command handlers
       api.addSlashCommandCreateListener(event -> {
           SlashCommandInteraction interaction = event.getSlashCommandInteraction();
           String commandName = interaction.getFullCommandName().toLowerCase(Locale.ROOT);


           switch (commandName) {
               case "test":
                   event.getInteraction().createImmediateResponder()
                           .setContent("https://tenor.com/view/steve-harvey-scared-nope-no-way-shake-head-gif-4834817")
                           .respond();

                   break;

               case "test_modal":
                   event.getInteraction().respondWithModal("modalID", "Modal Title",
                           ActionRow.of(TextInput.create(TextInputStyle.SHORT, "text_input_ID",
                                   "Test Input."),
                                   SelectMenu.createMentionableMenu("Mentionable menu",
                                           "Add a mentionable")));
                   break;


               case "play":
                   Optional<TextChannel> eventChannel = event.getInteraction().getChannel();
                   event.getInteraction().createImmediateResponder()
                           .setContent("Hi, if you want to play the game, use the setup commands first.\n" +
                                   "(/set_team_names, /add_teammate)")
                           .respond();
                   break;

               case "start":
                   if (fm.startGame() == 0)
                       event.getInteraction().createImmediateResponder()
                               .setContent("New game has been started.")
                               .respond();

                   else
                       event.getInteraction().createImmediateResponder()
                               .setContent("There is a game active. End the current game before starting a " +
                                       "new one.")
                               .respond();

                   break;

               case "end":
                   if (fm.endGame() == 1)
                       event.getInteraction().createImmediateResponder()
                               .setContent("There is no active game to end.")
                               .respond();
                   else
                       event.getInteraction().createImmediateResponder()
                               .setContent("Current game has been ended.")
                               .respond();
                   break;


               case "team_scores":
                  String teamName = event.getSlashCommandInteraction()
                          .getArguments().get(0).getStringValue().get();
                  double number = event.getSlashCommandInteraction()
                                  .getArguments().get(1).getDecimalValue().get();

                  //Note, do not use .get() on optionals unless you can guarantee the optional is not null.
                   //Using .get() defeats the purpose of the Optional class.

                   System.out.println("Team name:\t" + teamName + "\nNumber:\t" + number);
                   HashMap results = dm.getScores(teamName, (int) number);
                   if (results.isEmpty()){
                       event.getInteraction().createImmediateResponder()
                               .setContent("There are no entries for the team, \"" + teamName + "\".")
                               .respond();
                   }
                   else{
                       System.out.println(results);
                   }
                  break;

               case "setup":
                    String teammate = event.getSlashCommandInteraction().getArguments().get(0).getStringValue().get();
                    String team = event.getSlashCommandInteraction().getArguments().get(1).getStringValue().get();

                    System.out.println(teammate + ", " + team);

                   break;

               case "set_team_names":
                   if(!fm.gameStarted) {
                       fm.teamARole = event.getSlashCommandInteraction().getArguments().get(0).getRoleValue().get();
                       fm.teamBRole = event.getSlashCommandInteraction().getArguments().get(1).getRoleValue().get();

                       event.getSlashCommandInteraction().createImmediateResponder()
                               .setContent(fm.teamARole.getMentionTag() + " will be Team A."
                                       + "\n" + fm.teamBRole.getMentionTag() + " will be Team B.")
                               .respond();
                   }
                   else
                       event.getSlashCommandInteraction().createImmediateResponder()
                               .setContent("You can't change the team names right now. A game is already in progress.")
                               .respond();
                   break;

               case "add_teammate":

                   if(fm.teamsSet) {
                       event.getSlashCommandInteraction().createImmediateResponder()
                               .setContent("You can't set add teammates without setting your teams first.");
                       break;
                   }

                   User user = event.getSlashCommandInteraction().getArguments().get(0).getUserValue().get();
                   Role role = event.getSlashCommandInteraction().getArguments().get(1).getRoleValue().get();

                   Player p = new Player(user, role, event.getSlashCommandInteraction().getServer().get());

                   if(role == fm.teamARole)
                       fm.TeamAMembers.add(p);
                   else if (role == fm.teamBRole)
                       fm.TeamBMembers.add(p);
                   else
                       event.getInteraction().createImmediateResponder()
                               .setContent("That role does not match the either of the roles set for this game " +
                                       "session. The role must be either " + fm.teamARole.toString() +
                                       " or " + fm.teamBRole.toString())
                               .respond();


                   if(event.getSlashCommandInteraction().getArguments().get(2).getBooleanValue().get())
                        p.isCaptain = true;

                   String assignmentSuccessful = user.getMentionTag() + " has been added to " + role;

                   if (p.isCaptain)
                       assignmentSuccessful += " as captain.";
                   else
                       assignmentSuccessful += ".";

                   event.getSlashCommandInteraction().createImmediateResponder()
                           .setContent(assignmentSuccessful);
                   break;



               case "invite":
                   EmbedBuilder eb = createInviteEmbed(invite);
                   event.getInteraction().createImmediateResponder()
                           .addEmbed(eb)
                           .respond();
                   break;

               case "tipping":
                   EmbedBuilder tipEB = createTipEmbed();
                   event.getInteraction().createImmediateResponder()
                           .addEmbed(tipEB)
                           .respond();
                   break;


               default:
                   System.out.println("Unknown command.");
                   break;
           }
       });

   }

    /**
     * Creates a discord invite link embed.*
     * @param invite Bot invite link.
     * @return EmbedBuilder created from given bot invite link.
     */
    public static EmbedBuilder createInviteEmbed(String invite){
        return new EmbedBuilder()
                .setTitle("FeudBot")
                .setDescription("A bot that lets you and your friends play a discord spinoff-version of Family Feud " +
                        "in real time.")
                .addField("Bot Invite", "[Add this bot to your server](" + invite + ")")
                .setAuthor("Reg.")
                .setColor(Color.BLUE);
    }

    public static EmbedBuilder createTipEmbed(){
        return new EmbedBuilder()
                .setTitle("Thank you for using the program. ♥\uFE0F")
                .addField("A message from Reg.", "Hi, and thanks for checking out FeudBot. " +
                        "If you'd like to support me and my projects," +
                        " consider [leaving a tip here. ☕️](https://www.buymeacoffee.com/v2ftrsbdkv7) Of course, " +
                        "these tips are not necessary, but they are always deeply appreciated.")
                .setThumbnail("https://cdn.buymeacoffee.com/uploads/profile_pictures/2023/10/CpfzXbqDUKjnP7pg." +
                        "jpg@300w_0e.webp");

    }

    /**
     * @return Bot invite link. Primarily for testing purposes.
     */
    public static String getInvite(){
        if (invite == null)
            startUp();
        return invite;
    }
}
