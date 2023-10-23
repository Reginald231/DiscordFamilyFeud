package com.github.reginald231;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.*;

import java.awt.*;
import java.io.*;
import java.util.*;


public class DiscordFamilyFeud {

    private static final Random rand = new Random(); //For random number generation.
    private static String token; //Discord bot token.
    private static DiscordApi api; //Discord api instance (created via bot token).
    private static FeudManager fm; //com.github.reginald231.FeudManager instance to handle in-depth game functionality.
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

        builders.add(new SlashCommandBuilder().setName("get_scores")
                .setDescription("Gets the scores for a given team name.")
                .setOptions(Arrays.asList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING,
                                "All", "Returns all scores for the team.", true),
                        SlashCommandOption.create(SlashCommandOptionType.STRING,
                                "Top_Ten", "Returns the top ten scores for the team.", true)
                )));

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

               case "play":
                   fm.startGame();
                   event.getInteraction().createImmediateResponder()
                           .setContent("Play command received.")
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

               case "invite":
                   EmbedBuilder eb = createInviteEmbed(invite);
                   event.getInteraction().createImmediateResponder()
                           .addEmbed(eb)
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

    /**
     * @return Bot invite link. Primarily for testing purposes.
     */
    public static String getInvite(){
        if (invite == null)
            startUp();
        return invite;
    }
}
