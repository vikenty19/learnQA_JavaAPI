import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Homework3 {
    @ParameterizedTest
    @ValueSource(strings = {"1","12345","12345678910123456","vasyavasyavasya","vasyavasyavasya1"} )
    public void shortPhraseTest(String givenString){
        int length = givenString.length();
        if(length<=15){ givenString = "Name doesn't exist";}
        String answer = (length > 15) ? givenString : "Name doesn't exist";
        System.out.println(answer);
          assertEquals(givenString,answer,"Name doesn't exist");

    }
}
