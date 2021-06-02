import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(final String[] args) {
        final Integer numberOfPlayers = Integer.parseInt(args[0]);
        final Integer pointsOfaccumilate = Integer.parseInt(args[1]);
        final Map<Integer, PlayerInfo> playerOrder = new LinkedHashMap<Integer, PlayerInfo>();
        final Map<String, Integer> penalty = new HashMap<>();

        /* randomly create the order*/
        final Random rng = new Random();
        final Set<Integer> generated = new LinkedHashSet<Integer>();
        while (generated.size() < numberOfPlayers) {
            final Integer next = rng.nextInt(numberOfPlayers) + 1;
            generated.add(next);
        }
        int playerCount = 1;
        for (final Integer order : generated) {
            final PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.setName("Player-" + playerCount);
            playerInfo.setPointsOfaccumilate(pointsOfaccumilate);
            playerOrder.put(order, playerInfo);
            playerCount++;
        }

        final List<Integer> sortedList = new ArrayList<>(generated);
        Collections.sort(sortedList);

        int index = 0;
        boolean showRank =true;
        while (!sortedList.isEmpty()) {
            if (index == 0 && showRank) {
                rankTable(playerOrder);
            }
            showRank = true;
            boolean penaltyFlag= false;
            if(penalty.get(playerOrder.get(sortedList.get(index)).getName()) != null){
                if(penalty.get(playerOrder.get(sortedList.get(index)).getName()).equals(2)){
                   penaltyFlag = true; 
                }
            }
            if (!penaltyFlag) {
                System.out.println(playerOrder.get(sortedList.get(index)).getName() + "  your turn");
                final Integer diceNumber = rng.nextInt(6) + 1;
                System.out.println(playerOrder.get(sortedList.get(index)).getName() + "  you got" + diceNumber);
                final Integer points = playerOrder.get(sortedList.get(index)).getPointsOfaccumilate() - diceNumber;
                playerOrder.get(sortedList.get(index)).setPointsOfaccumilate(points);
                if (points < 1) {
                    sortedList.remove(index);
                }
                if (diceNumber == 1) {
                    if (penalty.putIfAbsent(playerOrder.get(sortedList.get(index)).getName(), 1) != null) {
                        penalty.put(playerOrder.get(sortedList.get(index)).getName(), 2);
                    }
                }
                if (points > 0 && diceNumber == 6) {
                    System.out.println(
                            playerOrder.get(sortedList.get(index)).getName() + " will get the one more chance");
                            showRank = false;
                }
                else if (!sortedList.isEmpty()) {
                    index = ((index + 1) % sortedList.size());
                }
            }
            else {
                if (!sortedList.isEmpty()) {
                    index = ((index + 1) % sortedList.size());
                    penalty.remove(playerOrder.get(sortedList.get(index)).getName());
                }

            }
        }


        System.out.println(playerOrder);
    }

    private static void rankTable(final Map<Integer, PlayerInfo> playerOrder) {
        /* issue in Rank post points become 0 can be fixed with hashset*/
        
        final List<PlayerInfo> list = new ArrayList<PlayerInfo>(playerOrder.values());
        final List<PlayerInfo> sortedList =
                list.stream().sorted(Comparator.comparingInt(PlayerInfo::getPointsOfaccumilate))
                        .collect(Collectors.toList());
        System.out.println("Points table");
        for (final PlayerInfo pi : sortedList) {
            System.out.println(pi.getName() + "----------------points =" + pi.getPointsOfaccumilate());
        }
    }

    public static class PlayerInfo {
        String name;

        Integer pointsOfaccumilate;

        public String getName() {
            return name;
        }

        public Integer getPointsOfaccumilate() {
            return pointsOfaccumilate;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public void setPointsOfaccumilate(final Integer pointsOfaccumilate) {
            this.pointsOfaccumilate = pointsOfaccumilate;
        }
    }
}

