import java.util.*;

public class poker {
    // cards value order of rank
    private static final String CARD_VALUES = "23456789TJQKA";

    //Hand contains rank and kickers
    private static class Hand {
        //rank is hand pattern (e.g pair, three of a kind and etc.)
        int rank;
        //kickers are the cards that do not form a rank
        int[] kickers;
        
        Hand(int rank, int[] kickers) {
            this.rank = rank;
            this.kickers = kickers;
        }
    }
    
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

    private static Hand evaluateHand(String[] cards) {
        // Extract values and suits
        int[] values = new int[5];
        int[] suits = new int[5];
        
        for (int i = 0; i < 5; i++) {
            String card = cards[i];
            values[i] = CARD_VALUES.indexOf(card.charAt(0));
            suits[i] = card.charAt(1);
        }
        
        // Sort values in descending order for easier evaluation
        Arrays.sort(values);
        for (int i = 0; i < 2; i++) {
            int temp = values[i];
            values[i] = values[4 - i];
            values[4 - i] = temp;
        }
        
        boolean flush = isFlush(suits);
        boolean straight = isStraight(values);

        //for avoiding implement err
        return new Hand(1,values);
    }

    //check flush
    private static boolean isFlush(int[] suits) {
        int firstSuit = suits[0];
        for (int suit : suits) {
            if (suit != firstSuit) return false;
        }
        return true;
    }
    
    //check straight
    private static boolean isStraight(int[] values) {
        // Check if values are consecutive (already sorted in descending order)
        for (int i = 0; i < 4; i++) {
            if (values[i] - 1 != values[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private static int[] getValueCounts(int[] values) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int value : values) {
            countMap.put(value, countMap.getOrDefault(value, 0) + 1);
        }
        
        // Get counts in descending order
        List<Integer> counts = new ArrayList<>(countMap.values());
        counts.sort(Collections.reverseOrder());
        
        int[] result = new int[counts.size()];
        for (int i = 0; i < counts.size(); i++) {
            result[i] = counts.get(i);
        }
        return result;
    }

}
