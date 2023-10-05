import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.io.*;
import java.util.Set;
import java.util.Random;


public class DiscordFamilyFeud {

    public static void main(String[] args){

        Random rand = new Random();

        String token = "";

        try {
            token = getToken();
        }catch (FileNotFoundException FNF){
            FNF.printStackTrace();
            System.err.println("File could not be found at the given path.");
        }catch(IOException IOE){
            IOE.printStackTrace();
            System.err.println("Token could not be read properly");
        }

        DiscordApi api = new DiscordApiBuilder().setToken(token).addIntents(Intent.MESSAGE_CONTENT).login().join();
        String invite = api.createBotInvite();
        System.out.println(invite);
        FeudManager fm = new FeudManager(token, api);


        SlashCommand play = SlashCommand.with("play", "Starts a new game.").createGlobal(api).join();
        SlashCommand inv = SlashCommand.with("invite", "Posts a link to share the bot with others.").
                createGlobal(api).join();

        //remove later
        SlashCommand test = SlashCommand.with("test", "Testing, but for lols").createGlobal(api).
                join();
        Set<SlashCommand> commands = api.getGlobalSlashCommands().join();

//        regs-extra-bot-stuff
        Set<Channel> testchannels = api.getChannelsByName("general");
        //https://tenor.com/view/steve-harvey-scared-nope-no-way-shake-head-gif-4834817
        //new MessageBuilder().append("Testing").send((TextChannel) testchannels.toArray()[0]);
//        new MessageBuilder().append("https://tenor.com/view/steve-harvey-scared-nope-no-way-shake-head-gif-4834817").
//                send((TextChannel) testchannels.toArray()[0]);


        api.addSlashCommandCreateListener(event ->{
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if(interaction.getFullCommandName().equals("test"))
                event.getInteraction().createImmediateResponder()
                        .setContent("https://tenor.com/view/steve-harvey-scared-nope-no-way-shake-head-gif-4834817")
                        .respond();

        });

        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if(interaction.getFullCommandName().equals("play")){
                fm.startGame();
                event.getInteraction().createImmediateResponder()
                        .setContent("Play command received.").respond();
//                int x = rand.nextInt(10);
//                if(10 - x >= 2)
//                    event.getInteraction().createImmediateResponder().setContent("I can't do anything right now. " +
//                    "Reg is too lazy to give me more functionality.").respond();
//                else
//                    event.getInteraction().createImmediateResponder().setContent("No.").respond();

            }

            if(interaction.getFullCommandName().equals("invite")){
                event.getInteraction().createImmediateResponder().setContent(invite).respond();
            }
        });
    }

    /**
     * Grabs the bot token from a given text file.
     * @return Token from the bot_token.txt file.
     * @throws FileNotFoundException For missing bot token file.
     * @throws IOException
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
}
