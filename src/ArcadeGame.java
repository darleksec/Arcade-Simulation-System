/******************************************************************************
 File: ArcadeGame.java
 Date: 6th April 2025
 Author: Kimi Tang
 Description: abstract base class that defines common attributes and behaviors for all arcade game types
 defines an abstract method calculatePriceToPlay(boolean peak) that must be implemented by all subclasses.
 The class categorizes games into types using the gameType enum and includes basic validation for game IDs.

 v1.0.0 6/4/2025 Creation of file and basic fields and variables, main method
 v1.0.1 19/4/2025 Getters, main args and calculatePricetoPlay()
 v1.1.0 21/4/2025 Implemented getMinAge()
 v1.2.0 28/4/2025 fixed toString()
 References ******************************************************************************/

abstract class ArcadeGame {
    //fields -- protected -> abstract class
    protected String Name;
    protected String GameId;
    protected int PriceToPlay;

    public enum gameType {virtualReality,cabinet,active}
    protected static gameType gameType;

    //accessor
    public String getName() {
        return Name;
    }

    public String getGameId() {
        return GameId;
    }

    public int getPriceToPlay() {
        return PriceToPlay;
    }

    public gameType getGameType() {
        return gameType;
    }

    //constructor
    public ArcadeGame(String Name, String GameId, int PriceToPlay) throws InvalidGameIdException {
        this.Name = Name;
        this.GameId = GameId;
        this.PriceToPlay = PriceToPlay;
    }

    //Generic Game ID check
    public static void validateGameId(String GameId) throws InvalidGameIdException {
        //check ID for exception
        if (GameId == null) {
            throw new InvalidGameIdException("Invalid Game ID: null");
        } else if (GameId.length() != 10) {
            throw new InvalidGameIdException("Invalid Game ID: format error");
        } else {
            System.out.println();
        }
    }

    //abstract method
    public abstract int calculatePriceToPlay(boolean peak);

//    public static ArcadeGame game{
//        ActiveGame activeGame1 = new ActiveGame("active1","A123456789" , 2000, 15);
//    };

    public static void main (String[] args) throws InvalidGameIdException {

        CabinetGame game1 = new CabinetGame("LOL","C123456789",200,true);
        validateGameId(game1.getGameId());
        game1.calculatePriceToPlay(false);
        System.out.println(game1);

    }
}