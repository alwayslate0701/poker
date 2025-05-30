import java.util.*;

public class poker {
    // cards value order of rank
    private static final String CARD_VALUES = "23456789TJQKA";
    
    public static void main(String[] args) {
        // System.out.println("Hello World!");
        // win count
        int player1Wins = 0;
        int player2Wins = 0;
        //read stdin           
        try (Scanner scanner = new Scanner(System.in)) {
            //read line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                //remove space
                String[] cards = line.split("\\s+");
                if (cards.length != 10) {
                    System.err.println("Invalid input: " + line);
                    continue;
                }
                
                String[] player1Cards = Arrays.copyOfRange(cards, 0, 5);
                String[] player2Cards = Arrays.copyOfRange(cards, 5, 10);
                // for test
                System.out.println("Player 1: " + Arrays.toString(player1Cards));
                System.out.println("Player 2: " + Arrays.toString(player2Cards));
            }
        }
        
        //output
        System.out.println("Player 1: " + player1Wins + " hands");
        System.out.println("Player 2: " + player2Wins + " hands");
    }
}
