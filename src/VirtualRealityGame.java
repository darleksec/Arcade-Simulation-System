/******************************************************************************
 File: VirtualRealityGame.java
 Date: Monday 23th April 2025
 Author: Kimi Tang
 Description: subclass extending from ActiveGame to handle VR games.
 introduces VRtype  and adjusts game price  based on the VR setup and whether it's peak hours.
 enforces age checks before gameplay (subclass of ActiveGame).

 v1.0.0 23/4/2025 Creation of file and basic fields and variables, main method
 v1.0.1 24/4/2025 Implemented enums VRType and price calculation logic
 v1.1.0 24/4/2025 Implemented toString()

 References ******************************************************************************/

public class VirtualRealityGame extends ActiveGame{
    //fields
    public enum VRType{headsetOnly,
        headsetAndController,
        fullBodyTracking}
    public  VRType vrType;

    public VRType getVrType() {
        return vrType;
    }

    //constructor
    public VirtualRealityGame(String Name, String GameId, int PriceToPlay,int minAge,VRType vrType) throws InvalidGameIdException {
        super(Name, GameId, PriceToPlay, minAge);
        //check ID for exception
        if(!GameId.startsWith("AV")){
            throw new InvalidGameIdException("\nInvalid Game ID: format error\nAll VR Game ID starts with 'AV'");
        }
        else if (GameId.length() != 10) {
            throw new InvalidGameIdException("Invalid Game ID: format error");
        } else {
            System.out.println();
        }

        this.vrType = vrType;
    }

    //price calculation
    @Override
    public int calculatePriceToPlay(boolean peak) {
        int priceToCharge = PriceToPlay; // Use a local variable for calculation

        if (peak) { // no discount during peak hours
            return priceToCharge;
        } else if (vrType == vrType.headsetOnly) { // non-peak with headsetOnly VR type = 10% discount
            priceToCharge = (int)(priceToCharge * (1 - 0.1));
        } else if (vrType == vrType.headsetAndController) { // non-peak with headsetAndController VR type = 5% discount
            priceToCharge = (int)(priceToCharge * (1 - 0.05));
        }

        return priceToCharge; // Return the calculated price
    }

    //toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(Name).append("\n")
                .append("ID: ").append(GameId).append("\n")
                .append("Price: ").append(PriceToPlay).append("\n")
                .append("Minimum Age to play: ").append(getMinAge()).append("\n")
                .append("VR type: ").append(vrType).append("\n");

        return sb.toString();
    }

    //test harness
    public static void main(String[] args) throws InvalidGameIdException {
        VirtualRealityGame VR1 = new VirtualRealityGame("Miko","AV12345678",
                1500,15, VRType.headsetOnly);
        System.out.println(VR1);
        validateGameId(VR1.getGameId());
        VR1.calculatePriceToPlay(false);
        System.out.println(VR1.calculatePriceToPlay(false));

    }
}
