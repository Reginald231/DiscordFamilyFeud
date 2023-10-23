import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.github.reginald231.DiscordFamilyFeud;
import org.junit.jupiter.api.Test;
public class TestDiscordFamilyFeud{

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

    @Test
    void testStartUp(){
        DiscordFamilyFeud.main(new String[0]);
    }

//    @Test
//    void testCreateInviteEmbed(){
//        String invite = com.github.reginald231.DiscordFamilyFeud.getInvite();
//        assertNotNull(com.github.reginald231.DiscordFamilyFeud.createInviteEmbed(invite));
//
//    }

}