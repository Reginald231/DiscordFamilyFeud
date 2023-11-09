package com.github.reginald231;

import com.github.reginald231.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;


public class FeudManager{

    private final Dotenv config;
    private final ShardManager shardManager;

    /**
     * Initializes login process for the bot.
     * @throws LoginException occurs in the case of invalid bot token.
     */
    public FeudManager() throws LoginException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        DefaultShardManagerBuilder dsmBuilder = DefaultShardManagerBuilder.createDefault(token);
        dsmBuilder.setStatus(OnlineStatus.ONLINE);
        dsmBuilder.setActivity(Activity.playing("Getting dressed by Reg."));
        shardManager = dsmBuilder.build();
        EventListener el = new EventListener();
        el.setFeudManager(this);

        //register listeners
        shardManager.addEventListener(el);
    }



    /**
     * Getter method for Dotenv
     * @return the Dotenv instance for the environment variables.
     */
    public Dotenv getConfig() {
        return this.config;
    }

    /**
     * Getter method for ShardManager.
     * @return the ShardManager instance for the bot.
     */
    public ShardManager getShardManager(){
        return this.shardManager;
    }

}