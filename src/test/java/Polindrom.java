//import org.apache.logging.log4j.core.util.JsonUtils;
//import org.junit.Assert;
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
        String s = "Hello world";
        String s1= "aaaaaaaaaa";
 //       System.out.println(isPalindrome(s1));
String s3 ="My example is example is is";
        List<Integer> number1 = new ArrayList<>();
        number1.add(3);
        number1.add(1);
        number1.add(3);
        deleteThree(number1);
//        System.out.println(40 +50+ "  ");
//        System.out.println( "  "+(40 +50));
        int a = s.indexOf("o");
        System.out.println(s.substring(a));
        System.out.println(a);
        System.out.println(s3.replaceAll("is","on"));

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

@Test

    public void deleteThree(List<Integer> numbers) {


       // for (Integer num : numbers) {
        for(int i = 0; i < numbers.size(); i++){
            if (numbers.get(i)== 3) {
                numbers.remove(numbers.get(i));
            }

       }
        System.out.println(numbers);
        System.out.println("qq "+40+50);
    }


}