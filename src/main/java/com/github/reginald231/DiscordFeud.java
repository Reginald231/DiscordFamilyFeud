package com.github.reginald231;

import javax.security.auth.login.LoginException;


public class DiscordFeud {

    public static void main(String[] args){

        try {
            //com.github.reginald231.FeudManager instance to handle in-depth game functionality.
            new FeudManager();
        }catch(LoginException e){
            System.out.println("Error. Invalid bot token.");
            e.printStackTrace();
        }
    }
}
