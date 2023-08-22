import org.apache.logging.log4j.core.util.JsonUtils;
import org.junit.jupiter.api.Test;

public class Polindrom {
    @Test
    public void getWordAsChar() {
        String string = "abc";
        char[] chars = string.toCharArray();
        for(char char1: chars){
        System.out.print(char1 + " ");}
        System.out.println(chars.length);
        for(int i =chars.length-1;i >= 0;i --){
            System.out.print(chars[i] + " ");
        }

    }
}