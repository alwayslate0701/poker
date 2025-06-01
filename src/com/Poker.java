package src.com;
import java.util.*;


public class Poker {
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
                // System.out.println("Player 1: " + Arrays.toString(player1Cards));
                // System.out.println("Player 2: " + Arrays.toString(player2Cards));
                
                //read cards into hand
                Hand p1 = evaluateHand(player1Cards);
                Hand p2 = evaluateHand(player2Cards);

                int comparison = compareHands(p1, p2);
                if (comparison > 0) {
                    player1Wins++;
                }
                if (comparison < 0) {
                    player2Wins++;
                }
                //tie no count change
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

        //test
        // if(flush){
        //     System.out.println("this hand is flush");
        // }
        // if(straight){
        //     System.out.println("this hand is straight");
        // }
        // int[] c = getValueCounts(values);
        // //test getValueCount
        // System.out.println(Arrays.toString(c));
        // Check for royal flush
        if (flush && straight && values[0] == CARD_VALUES.indexOf('A')) {
            return new Hand(10, values);
        }
        
        // Check for straight flush
        if (flush && straight) {
            return new Hand(9, values);
        }
        
        // check for four of a kind
        int[] valueCounts = getValueCounts(values);
        if (valueCounts[0] == 4) {
            return new Hand(8, getKickers(values, 1));
        }
        
        // check for full house
        if (valueCounts[0] == 3 && valueCounts[1] == 2) {
            return new Hand(7, values);
        }
        
        // check for flush
        if (flush) {
            return new Hand(6, values);
        }
        
        // check for straight
        if (straight) {
            return new Hand(5, values);
        }
        
        // check for three of a kind
        if (valueCounts[0] == 3) {
            return new Hand(4, getKickers(values, 2));
        }
        
        // check for two pairs
        if (valueCounts[0] == 2 && valueCounts[1] == 2) {
            return new Hand(3, getKickers(values, 1));
        }
        
        // check for pair
        if (valueCounts[0] == 2) {
            return new Hand(2, getKickers(values, 3));
        }

        // high card
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
        // check if values are consecutive (already sorted in descending order)
        for (int i = 0; i < 4; i++) {
            if (values[i] - 1 != values[i + 1]) {
                return false;
            }
        }
        return true;
    }

    //count the numbers of appearence for values
    private static int[] getValueCounts(int[] values) {
        System.out.println(Arrays.toString(values));
        //using hashmap to count value
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int value : values) {
            countMap.put(value, countMap.getOrDefault(value, 0) + 1);
        }
        
        //get counts in descending order
        List<Integer> counts = new ArrayList<>(countMap.values());
        counts.sort(Collections.reverseOrder());
        
        int[] result = new int[counts.size()];
        for (int i = 0; i < counts.size(); i++) {
            result[i] = counts.get(i);
        }
        return result;
    }

    private static int[] getKickers(int[] values, int numKickers) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int value : values) {
            countMap.put(value, countMap.getOrDefault(value, 0) + 1);
        }
        
        // Separate paired cards and kickers
        List<Integer> paired = new ArrayList<>();
        List<Integer> kickers = new ArrayList<>();
        
        for (int value : values) {
            if (countMap.get(value) > 1) {
                paired.add(value);
            } else {
                kickers.add(value);
            }
        }
        
        // sort paired cards by count then value
        paired.sort(Collections.reverseOrder());
        
        // sort kickers in descending order
        kickers.sort(Collections.reverseOrder());

        // two pairs which would be compaired in order of top pair,bottom pair, kicker
        if (paired.size() == 2) { 
            // return top pair, bottom paire and kicker
            return new int[]{paired.get(0), paired.get(1), kickers.get(0)};
        }
        
        //combine results
        int[] result = new int[1 + numKickers];
        
        //three kind, 1 pair
        if (!paired.isEmpty()) {
            result[0] = paired.get(0); 
        } 
        //high card
        else {
            result[0] = kickers.get(0); 
        }

        // put kickers behind pattern card
        for (int i = 0; i < numKickers && i < kickers.size(); i++) {
            result[i + 1] = kickers.get(i);
        }

        return result;
    }

    private static int compareHands(Hand hand1, Hand hand2) {
        //compare rank
        if (hand1.rank != hand2.rank) {
            return Integer.compare(hand1.rank, hand2.rank);
        }
        
        // compare kickers
        for (int i = 0; i < hand1.kickers.length; i++) {
            if (hand1.kickers[i] != hand2.kickers[i]) {
                return Integer.compare(hand1.kickers[i], hand2.kickers[i]);
            }
        }
        //tie
        return 0; 
    }
}
