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



        @Test
        public void checkWord(){
            System.out.println(isPalindrome("Aaaa1"));
        }

        public boolean isPalindrome(String myString) {
            String myStringReverse = "";
            for (int i = myString.length() - 1; i >= 0; i--) {
                myStringReverse = myStringReverse + myString.charAt(i);
            }
            if (myString.equalsIgnoreCase(myStringReverse)) {
                return true;
            } else {
                return false;
            }

        }

    }

