import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import java.awt.*;
import java.io.*;
import java.util.Locale;
import java.util.Set;
import java.util.Random;



public class DiscordFamilyFeud {

    private static final Random rand = new Random(); //For random number generation.
    private static String token; //Discord bot token.
    private static DiscordApi api; //Discord api instance (created via bot token).
    private static FeudManager fm; //FeudManager instance to handle in-depth game functionality.
    private static String invite; //Bot invite link
    private static Set<SlashCommand> commands;

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
    static String getToken() throws FileNotFoundException, IOException {
        String token = "";
        String line;
        File f = new File("/Users/tech/IdeaProjects/discordFamilyFeud/src/main/java/bot_token.txt");

        BufferedReader br = new BufferedReader(new FileReader(f));
         while ((line = br.readLine()) != null)
                token = line;
        return token;

    }

    /**
     * Prepares the bot for online use (i.e: Logging into discord, setting up commands, etc.).*
     */
    static void startUp() {
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

        api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        System.out.println("Bot is online.");
        invite = api.createBotInvite();
        fm = new FeudManager(token, api);


        buildCommands();

    }

    /**
     * Sets up commands for the bot after bot is online.*
     */
    static void buildCommands(){
        //Slash command setup
        SlashCommand play = SlashCommand.with("play", "Starts a new game.").createGlobal(api).join();
        SlashCommand inv = SlashCommand.with("invite", "Posts a link to share the bot with others.").
                createGlobal(api).join();
        SlashCommand startGame = SlashCommand.with("start", "Starts a new game.").createGlobal(api)
                .join();
        SlashCommand endGame = SlashCommand.with("end", "Ends the current game.").createGlobal(api)
                .join();

        commands = api.getGlobalSlashCommands().join();


        //Command handlers
        api.addSlashCommandCreateListener(event ->{
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            String commandName = interaction.getFullCommandName().toLowerCase(Locale.ROOT);

            switch(commandName){
                case "test":
                    event.getInteraction().createImmediateResponder()
                            .setContent("https://tenor.com/view/steve-harvey-scared-nope-no-way-shake-head-gif-4834817")
                            .respond();
                    break;

                case "play":
                    fm.startGame();
                    event.getInteraction().createImmediateResponder()
                            .setContent("Play command received.").respond();
                    break;

                case "start":
                    if(fm.startGame() == 0)
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
                    if(fm.endGame() == 1)
                        event.getInteraction().createImmediateResponder()
                                .setContent("There is no active game to end.");
                    else
                        event.getInteraction().createImmediateResponder()
                                .setContent("Current game has been ended.");
                    break;

                case "invite":
                    EmbedBuilder eb = createInviteEmbed(invite);
                    event.getInteraction().createImmediateResponder()
                            .addEmbed(eb)
                            .respond();
                    break;
            }
        });
    }

    /**
     * Creates a discord invite link embed.*
     * @param invite Bot invite link.
     * @return EmbedBuilder created from given bot invite link.
     */
    static EmbedBuilder createInviteEmbed(String invite){
        return new EmbedBuilder()
                .setTitle("FeudBot")
                .setDescription("A bot that lets you and your friends play a discord version of Family Feud " +
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
