import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class TestDiscordFamilyFeud{

    // @Test
    // void testBotStartup(){
    //     void 
    // }

    @Test
    void testGetTokenNotNull(){

        try{
        assertNotNull(DiscordFamilyFeud.getToken());
        }catch(FileNotFoundException fne){
            fne.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        
    }

}