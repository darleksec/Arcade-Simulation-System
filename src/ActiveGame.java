/******************************************************************************
 File: ActiveGame.java
 Date: Monday 28th April 2025
 Author: Kimi Tang
 Description: subclass extending from ArcadeGames to handle active arcade games.
 introduces a minAge field and adjusts game price with a discount during non-peak hours.
 enforces age checks before gameplay.

 v1.0.0 19/4/2025 Creation of file and basic fields and variables, main method
 v1.0.1 21/4/2025 Implemented calculatePricetoPlay()
 References ******************************************************************************/

public class ActiveGame extends ArcadeGame {
    //fields
    private int minAge;
    //getters
    public int getMinAge() {
        return minAge;
    }
    //constructor
    public ActiveGame(String Name, String GameId, int PriceToPlay, int minAge) throws InvalidGameIdException {
        super(Name, GameId, PriceToPlay);
        this.minAge = minAge;
        //check ID for exception
        if(!GameId.startsWith("A")){
            throw new InvalidGameIdException("\nInvalid Game ID: format error\nAll Active Game ID starts with 'A'");
        } else if (GameId.length() != 10) {
            throw new InvalidGameIdException("Invalid Game ID: format error");
        } else {
            System.out.println();

        }

    }

    //price calculation
    @Override
    public int calculatePriceToPlay(boolean peak) {
        int priceToCharge = PriceToPlay;

        if (peak) {

        } else {
            priceToCharge = (int)(priceToCharge * 0.8);  // Apply 20% off for off-peak hours
        }

        return priceToCharge;
    }

    //toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(Name).append("\n")
                .append("ID: ").append(GameId).append("\n")
                .append("Price: ").append(PriceToPlay).append("\n")
                .append("Minimum Age to play: ").append(minAge).append("\n");

        return sb.toString();
    }

    //tset harness
    public static void main(String[] args) throws InvalidGameIdException {
        ActiveGame CSGO = new ActiveGame("CSGO","A123456789",500,15);
        validateGameId(CSGO.GameId);
        CSGO.calculatePriceToPlay(false);
        System.out.println(CSGO);
    }

}
