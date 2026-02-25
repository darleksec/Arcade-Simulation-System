/******************************************************************************
 File: CabinetGame.java
 Date: 19th April 2025
 Author: Kimi Tang
 Description: subclass extending from ArcadeGames to handle cabinet-style arcade games.
adds a payout attribute to indicate if the game dispenses prizes or tickets.
calculatePriceToPlay () adjusts the price based on peak hours and payout status.

 v1.0.0 19/4/2025 Creation of file and basic fields and variables, main method and calculatePricetoPlay()
 v1.0.1 21/4/2025 Implemented toString()
 v1.0.2 27/4/2025 fixed toString()
 References ******************************************************************************/


public class CabinetGame extends ArcadeGame {
    //fields
    private boolean payout;


    //constructor
    public CabinetGame(String Name, String GameId, int PriceToPlay, boolean payout) throws InvalidGameIdException {
        super(Name, GameId, PriceToPlay);
        //check ID for exception
        if (!GameId.startsWith("C")) {
            throw new InvalidGameIdException("\nInvalid Game ID: format error\nAll Cabinet Game ID starts with 'C'");
        }else if (GameId.length() != 10) {
            throw new InvalidGameIdException("Invalid Game ID: format error");
        }
        this.payout = payout;
    }
    //redundant ?
     public boolean isPayout(){
        return payout;
     }
    //Price calculation
    @Override
    public int calculatePriceToPlay(boolean peak) {
        int priceToCharge = PriceToPlay; // Use a local variable for calculation

        if (peak) { // no discount
            return priceToCharge;
        } else if (!peak && !payout) { // non-peak no payout = 50%
            priceToCharge = (int)(priceToCharge * (1 - 0.5));
        } else if (!peak && payout) { // non-peak with payout = 80%
            priceToCharge = (int)(priceToCharge * (1 - 0.2));
        }

        return priceToCharge; // Return the calculated price
    }

    //toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(Name).append("\n")
                .append("ID: ").append(GameId).append("\n")
                .append("Price: ").append(PriceToPlay).append("\n")
                .append("Payout: ").append(payout).append("\n");

        return sb.toString();
    }

    //test harness
    public static void main(String[] args) throws InvalidGameIdException {
        CabinetGame game = new CabinetGame("Pinball", "C123456789", 200, true);
        validateGameId(game.getGameId());
        game.calculatePriceToPlay(false);
        System.out.println(game);
    }
}