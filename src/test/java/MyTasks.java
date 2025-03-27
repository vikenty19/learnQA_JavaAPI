import org.junit.jupiter.api.Test;

public class MyTasks {
    @Test
      public void checkWord(){
        System.out.println(isPalindrome("Aaaa1"));
      }

    public boolean isPalindrome(String myString) {
        String myStringReverse = "";
        for (int i = myString.length() - 1; i >= 0; i--) {
            myStringReverse = myStringReverse + myString.charAt(i);
        }
        return myString.equalsIgnoreCase(myStringReverse);

    }

}
