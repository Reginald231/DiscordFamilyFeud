package com.github.reginald231.commands;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private final List<CommandData> commandDataList = new ArrayList<>();

    public CommandManager(){
        buildCommands();
    }

    /**
     * Builds all bot commands, storing them in a List of CommandData instances.
     */
    public void buildCommands(){

        System.out.println("Building commands...");
        this.commandDataList.add(Commands.slash("test", "Test command for development purposes."));
        this.commandDataList.add(Commands.slash("invite", "Provides a bot invite link."));
        this.commandDataList.add(Commands.slash("support", "Post an embed for users to support the creator"));
        this.commandDataList.add(Commands.slash("play", "Test command for playing the game."));
        this.commandDataList.add(Commands.slash("setup", "Begin setting up a new game.")
                .addOption(OptionType.INTEGER, "team_size", "The size of each team", true)
                .addOption(OptionType.STRING, "team_a_name", "The name of Team A", true)
                .addOption(OptionType.STRING, "team_b_name", "The name of Team B", true)
                .addOption(OptionType.ROLE, "team_a_role", "Role to use for Team A", true)
                .addOption(OptionType.ROLE, "team_b_role", "Role to use for Team B", true)
                .addOption(OptionType.MENTIONABLE, "team_a_captain", "The captain of Team A", true)
                .addOption(OptionType.MENTIONABLE, "team_b_captain", "The captain of Team B", true)
                .addOption(OptionType.MENTIONABLE, "host", "The host for this session", true)
                .addOption(OptionType.ROLE, "host_role", "The role for the host of this session", true));

        System.out.println("All commands built.");
    }

    /**
     * Getter method for ComamndManager's List of CommandData.
     * @return the list of commands built by CommandManager.
     */
    public List<CommandData> getCommandDataList(){
        return this.commandDataList;
    }
}
