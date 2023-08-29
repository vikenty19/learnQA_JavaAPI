import org.apache.logging.log4j.core.util.JsonUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Polindrom {

    @Test
    public void getWordAsChar() {
        String string = "abc";
        char[] chars = string.toCharArray();
        for (char char1 : chars) {
            System.out.print(char1 + " ");
        }
        System.out.println(chars.length);
        for (int i = chars.length - 1; i >= 0; i--) {
            System.out.print(chars[i] + " ");
        }

    }


    @Test
    public void checkWord() {
        System.out.println(isPalindrome("Aaaaa"));

        List<Integer> number1 = new ArrayList<>();
        number1.add(3);
        number1.add(1);
        number1.add(3);
        deleteThree(number1);

    }

    String myStringReverse = "";

    public boolean isPalindrome(String myString) {

        for (int i = myString.length() - 1; i >= 0; i--) {
            myStringReverse = myStringReverse + myString.charAt(i);

        }


        Assert.assertTrue(myString.equalsIgnoreCase(myStringReverse));
        System.out.println(myStringReverse + "  " + myString);
        if (myString.equalsIgnoreCase(myStringReverse)) {
            return true;
        } else {
            return false;
        }

    }



    public void deleteThree(List<Integer> numbers) {



       // for (Integer num : numbers) {
        for(int i = 0; i < numbers.size(); i++){
            if (numbers.get(i)== 3) {
                numbers.remove(numbers.get(i));
            }

       }
        System.out.println(numbers);
    }


}