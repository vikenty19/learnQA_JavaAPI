package tests;

import org.junit.jupiter.api.Test;



public class TasksFoLoop {
    @Test
    public void Main() {

        for (int i = 1; i < 99; i++) {
            while (i < 11) {
                System.out.print(i + "  ");
                i++;
            }
            System.out.println();
            while (10 < i && i < 21) {
                System.out.print(i + " ");
                i++;
            }
            System.out.println();
            while (20 < i && i < 31) {
                System.out.print(i + " ");
                i++;
        }

        }
    }
}
