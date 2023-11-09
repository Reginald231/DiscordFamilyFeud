package com.github.reginald231.listeners;

import com.github.reginald231.commands.CommandManager;
import com.github.reginald231.FeudManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EventListener extends ListenerAdapter {

    private FeudManager fm;
    private final CommandManager commandManager;

    public EventListener(){
        this.commandManager = new CommandManager();
    }

    public EventListener(FeudManager fm){
        this.fm = fm;
        this.commandManager = new CommandManager();
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        //event.getGuild().updateCommands().queue(); //Deletes all commands for the respective guild onReady.
        event.getGuild().updateCommands().addCommands(commandManager.getCommandDataList()).queue();

    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        event.getJDA().updateCommands().addCommands(commandManager.getCommandDataList()).queue();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        //event.getJDA().updateCommands().queue(); //Deletes all global commands.
    }


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        System.out.println("Caught a command: " + event.getName());
        String commandName = event.getName();

        switch(commandName.toLowerCase()){
            case "test":
                System.out.println("test comm");
                event.reply("Received test command.").setEphemeral(true).queue();
                break;

            case "play":
                event.reply("Received play command.").setEphemeral(true).queue();
                break;

            case "setup":

                int teamSize = event.getOption("team_size").getAsInt();
                String teamAName = event.getOption("team_a_name").getAsString();
                String teamBName = event.getOption("team_b_name").getAsString();
                Role teamARole = event.getOption("team_a_role").getAsRole();
                Role teamBRole = event.getOption("team_b_role").getAsRole();
                Member teamACaptain = event.getOption("team_a_captain").getAsMember();
                Member teamBCaptain = event.getOption("team_b_captain").getAsMember();
                Member host = event.getOption("host").getAsMember();
                Role hostRole = event.getOption("host_role").getAsRole();

                String setupResults = """
                       Team Size:\t%d
                       Team A Name:\t%s
                       Team B Name:\t%s
                       Team A Role:\t%s
                       Team B Role:\t%s
                       Team A Captain:\t%s
                       Team B Captain:\t%s
                       Host:\t%s
                       Host Role:\t%s""".formatted(teamSize, teamAName, teamBName, teamARole, teamBRole, teamACaptain,
                        teamBCaptain, host, hostRole);
                event.deferReply().queue();

                event.getHook().sendMessage(setupResults).setEphemeral(true).queue();

                break;

            case "invite":
                String inviteLink = event.getJDA().getInviteUrl();
                event.deferReply().queue();
                event.getHook().sendMessageEmbeds(getInviteEmbed(inviteLink).build()).queue();
                break;

            case "support":
                event.deferReply().queue();
                event.getHook().sendMessageEmbeds(getSupportEmbed().build()).queue();
                break;

            default:
                event.reply("Unknown command received.");
                break;

        }
    }

    /**
     * Gets the FeudManager used by EventListener.
     * @return
     */
    public FeudManager getFeudManager(){return this.fm;}

    /**
     * Sets the FeudManager to be used by EventListener.
     * @param fm the FeudManager instance to assign to EventListener.
     */
    public void setFeudManager(FeudManager fm){this.fm = fm;}

    /**
     * Generates an embed to present to users who wish to support the creator.
     * @return EmbedBuilder to respond to slash command event.
     */
    public EmbedBuilder getSupportEmbed(){
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Thank you for using the program. ♥\uFE0F", null);
        eb.setColor(new Color(0xBFDAF7));
        String tipLink = "https://www.buymeacoffee.com/v2ftrsbdkv7";
        String supportMessage = """
                Hi, and thanks for checking out FeudBot.
                If you would like to support me and my projects, consider [leaving a tip here. ☕\uFE0F](%s). Of course, these tips are not necessary, but they are always deeply appreciated.
                """.formatted(tipLink);
        String supportEmbedThumbnail = "https://cdn.buymeacoffee.com/uploads/profile_pictures/2023/10/CpfzXbqDUKjnP7pg." +
                "jpg@300w_0e.webp";

        eb.addField("A message from Reg.", supportMessage, false);
        eb.setThumbnail(supportEmbedThumbnail);

        return eb;
    }

    /**
     * Builds an embed containing a bot invite link.
     * @param inviteLink Bot invite link
     * @return an EmbedBuilder containing the invite link.
     */
    public EmbedBuilder getInviteEmbed(String inviteLink){
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("FeudBot", null);
        eb.setColor(Color.BLUE);
        eb.addField("Invite the bot to your server!",
                "Use [this link](%s) to invite FeudBot to your server.".formatted(inviteLink), false);

        return eb;
    }

}
