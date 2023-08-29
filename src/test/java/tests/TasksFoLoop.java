package tests;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TasksFoLoop {
    @Test
    public void Main() {
        List<Integer> num = new ArrayList<>();
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
