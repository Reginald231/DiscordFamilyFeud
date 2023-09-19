import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
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


        SlashCommand play = SlashCommand.with("play", "Starts a new game.").createGlobal(api).join();
        SlashCommand inv = SlashCommand.with("invite", "Posts a link to share the bot with others.").
                createGlobal(api).join();

        Set<SlashCommand> commands = api.getGlobalSlashCommands().join();


        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if(interaction.getFullCommandName().equals("play")){
                int x = rand.nextInt(10);
                if(10 - x >= 2)
                    event.getInteraction().createImmediateResponder().setContent("I can't do anything right now. " +
                    "Reg is too lazy to give me more functionality.").respond();
                else
                    event.getInteraction().createImmediateResponder().setContent("No.").respond();
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
