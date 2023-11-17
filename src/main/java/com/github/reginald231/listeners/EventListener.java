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
                this.setup(event);
                break;

            case "create_roles":
                this.createRoles(event);
                break;

            case "invite":
                this.invite(event);
                break;

            case "support":
                this.support(event);
                break;

            default:
                event.reply("Unacknowledged command received. Update switch statement.").queue();
                break;

        }
    }


    /**
     * Provides a bot invite link within an embed for users to add the bot to their server.
     * @param event SlashCommandInteractionEvent received from the user interaction
     */
    private void invite(@NotNull SlashCommandInteractionEvent event){
        String inviteLink = event.getJDA().getInviteUrl();
        event.deferReply().queue();
        event.getHook().sendMessageEmbeds(getInviteEmbed(inviteLink).build()).queue();
    }

    /**
     * Creates roles usable for Discord Feud.
     * @param event SlashCommandInteractionEvent received from the user interaction
     */
    private void createRoles(@NotNull SlashCommandInteractionEvent event){
        event.deferReply().queue();
        String teamRole1 = event.getOption("team_role_1").getAsString();
        String teamRole2 = event.getOption("team_role_2").getAsString();
        String hostRole = event.getOption("host_role").getAsString();

        event.getGuild().createRole().setName(teamRole1).setHoisted(true).setColor(Color.RED).queue();
        event.getGuild().createRole().setName(teamRole2).setHoisted(true).setColor(Color.CYAN).queue();
        event.getGuild().createRole().setName(hostRole).setHoisted(true).setColor(Color.GREEN).queue();

        String roleList = """
                The following roles have been created:
                %s
                %s
                %s""".formatted(teamRole1, teamRole2, hostRole);

        event.getHook().sendMessage(roleList).setEphemeral(true).queue();
    }

    /**
     * Sets up key game information from the setup command
     * @param event SlashCommandInteractionEvent received from the user interaction
     */
    private void setup(@NotNull SlashCommandInteractionEvent event){
        event.deferReply().queue();

        int teamSize = event.getOption("team_size").getAsInt();
        String team1Name = event.getOption("team_a_name").getAsString();
        String team2Name = event.getOption("team_b_name").getAsString();
        Role team1Role = event.getOption("team_a_role").getAsRole();
        Role team2Role = event.getOption("team_b_role").getAsRole();
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
                       Host Role:\t%s""".formatted(teamSize, team1Name, team2Name, team1Role, team2Role, teamACaptain,
                teamBCaptain, host, hostRole);

        this.fm.setTeam1Role(team1Role);
        this.fm.setTeam2Role(team2Role);
        this.fm.setHost(host, hostRole);
        this.fm.setTeamSize(teamSize);

        event.getHook().sendMessage(setupResults).setEphemeral(true).queue();

    }

    /**
     * Provides an embed to users, allowing them to support the creator if they wish.
     * @param event SlashCommandInteractionEvent received from the user interaction
     */
    private void support(@NotNull SlashCommandInteractionEvent event){
        event.deferReply().queue();
        event.getHook().sendMessageEmbeds(getSupportEmbed().build()).queue();
    }

    /**
     * Gets the FeudManager used by EventListener.
     * @return The respective FeudManager instance assigned to this EventListener.
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
