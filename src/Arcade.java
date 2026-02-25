/******************************************************************************
 File: Arcade.java
 Date: 23th April 2025
 Author: Kimi Tang
 Description: manages an entire arcade venue by keeping track of the arcade’s name,
 revenue, a list of games, and a list of customers
 allows adding games/customers, finding specific ones, processing transactions and special functions such as
 finding median game price, richest customer, and game type counts.
 v1.0.0 23/4/2025 Creation of file and basic fields and variables, main method
 v1.0.1 24/4/2025 Placeholder processTransaction(), findRichestCustomer(), getMedianGamePrice(),countArcadeGames()
 v1.1.0 25/4/2025 Logic for methods from v1.0.1 plus helper method listAllGames(), listAllCustomers()
 v1.1.1 27/4/2025 Testing logic and debugging
 References ******************************************************************************/

import java.util.ArrayList;

public class Arcade {
    private final String name;
    private int revenue;
    private ArrayList<ArcadeGame> arcadeGames;
    private ArrayList<Customer> customers;

    public Arcade(String name, int revenue) {
        this.name = name;
        this.revenue = revenue;
        this.arcadeGames = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getRevenue() {
        return revenue;
    }

    public void addCustomer(Customer c) {
        customers.add(c);
    }

    public Customer getCustomer(String customerID) throws InvalidCustomerException {
        for (Customer c : customers) {
            if (c.getAccountID().equals(customerID)) {
                return c;
            }
        }
        throw new InvalidCustomerException("Customer not found: " + customerID);
    }

    public void addArcadeGame(ArcadeGame game) throws InvalidGameIdException {
        arcadeGames.add(game);
    }

    public ArcadeGame getArcadeGame(String gameID) throws InvalidGameIdException {
        for (ArcadeGame game : arcadeGames) {
            if (game.getGameId().equals(gameID)) {
                return game;
            }

        }
        throw new InvalidGameIdException("Game not found: " + gameID +"\n");
    }

    public void listAllGames() {
        System.out.println("Listing All Games in Arcade: " + this.getName());
        for (ArcadeGame game : arcadeGames) {
            System.out.println(game); // calls toString()
        }
    }

    public void listAllCustomers() {
        for (Customer c : customers) {
            System.out.println(c);
        }
    }


    public boolean processTransaction(String customerID, String gameID, boolean peak) throws InvalidCustomerException,AgeLimitException, InsufficientBalanceException, InvalidGameIdException {
        Customer c = getCustomer(customerID);
        int price = c.chargeAccount(getArcadeGame(gameID),peak);
        revenue = revenue + price;
//        System.out.println("Revenue = " + revenue + " Increase = " + price); For local testing harness
        return true;
    }

    public Customer findRichestCustomer() throws InvalidCustomerException {
        if (customers.isEmpty()) {
            throw new InvalidCustomerException("Customer not found");
        }
        Customer Rich = customers.getFirst();
        for (Customer c : customers) {
            if(c.getBalance()>Rich.getBalance()) {
                Rich = c;
            }
        }
        System.out.println("Richest customer: " + Rich);
        return Rich;

    }

    public int getMedianGamePrice(){
        String formattedPrice = "";
        ArrayList<Integer> prices = new ArrayList<>();
        for (ArcadeGame game : arcadeGames) {
           prices.add(game.getPriceToPlay());
        }
        prices.sort(null);
        int size = prices.size();
        if (prices.size() % 2 == 1 ) {
            int Median = prices.get(size / 2);
            formattedPrice = String.format("£%d.%02d",Median / 100, Math.abs(Median % 100));
            System.out.println("\nMedian Game Price : " + formattedPrice + "\n");
            return Median;
        }else {
            int Median = (prices.get(size / 2-1) + prices.get(size / 2))/2;
            formattedPrice = String.format("£%d.%02d", Median / 100, Math.abs(Median % 100));
            System.out.println("\nMedian Game Price : " +formattedPrice+"\n");
            return Median;
        }
    }

    public int[] countArcadeGames(){
        int[] counts = new int[3];
        for (ArcadeGame game : arcadeGames) {
            if (game instanceof VirtualRealityGame){
                counts[2]++;
            } else if (game instanceof ActiveGame) {
                counts[1]++;
            }else if (game instanceof CabinetGame){
                counts[0]++;
            }
        }
        return counts;
    }

    public static void printCorporateJargon() {
        System.out.println("\nGamesCo does not take responsibility for any accidents or fits of rage that occur on the premises.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String formattedRevenue = String.format("£%d.%02d",revenue / 100, Math.abs(revenue % 100));

        // Append general arcade information
        sb.append("Arcade: ").append(name)
                .append(", Revenue: ").append(formattedRevenue)
                .append(", Games: ").append(arcadeGames.size())
                .append(", Customers: ").append(customers.size());

        // Get the counts of each type of game from countArcadeGames()
        int[] counts = countArcadeGames();

        // Append game type counts
        sb.append(", Cabinet Games: ").append(counts[0])
                .append(", Active Games: ").append(counts[1])
                .append(", Virtual Reality Games: ").append(counts[2]);

        return sb.toString();
    }

    public static void main(String[] args) throws InvalidGameIdException, InvalidCustomerException, InsufficientBalanceException,AgeLimitException {
        Arcade arcade = new Arcade("Arcadia", 0);

        // Assuming Customer class and enum DiscountType are implemented
        Customer student = new Customer("000001", "Student1", 20, Customer.Level.STUDENT, 0);
        Customer STU18 = new Customer("STD_18", "Student-18",18, Customer.Level.STUDENT,1500);
        Customer CMP25 = new Customer("CMP_25", "Staff-25",25, Customer.Level.STAFF,1000);
        Customer NRM16 = new Customer("NRM_16", "Normal-16",16, Customer.Level.NONE,500);
        Customer YNG11 = new Customer("YNG_11", "Student-11",11, Customer.Level.STUDENT,200);
        Customer BROKE = new Customer("BROKE!", "Student-25",30, Customer.Level.STUDENT,0);

        Customer [] customersArray = {student, STU18, CMP25, NRM16, YNG11, BROKE};
        for(Customer customer: customersArray){
            arcade.addCustomer(customer);
        }

        // Assuming ArcadeGame and CabinetGame exist and are implemented properly
        ArcadeGame TC1 = new CabinetGame("TC1", "C000000001",100,true);
        ArcadeGame TC2 = new CabinetGame("TC2", "C000000002",181,true);//expect 130, output 129
        ArcadeGame TC3 = new CabinetGame("TC3", "C000000003",200,false);
        ArcadeGame TC4 = new ActiveGame("TC4","A000000001",301,15);
        ArcadeGame TC5 = new ActiveGame("TC5","A000000002",303,15);//incorrect price check
        ArcadeGame TC6 = new VirtualRealityGame("TC6","AV00000001",303,15, VirtualRealityGame.VRType.headsetOnly);
        ArcadeGame TC7 = new VirtualRealityGame("TC7","AV00000002",304,15, VirtualRealityGame.VRType.headsetAndController);
        ArcadeGame TC8 = new VirtualRealityGame("TC8", "AV00000003",305,18, VirtualRealityGame.VRType.fullBodyTracking);
        ArcadeGame TC9 = new VirtualRealityGame("TC8", "AV00000004",400,18, VirtualRealityGame.VRType.fullBodyTracking);
        ArcadeGame TC10 = new CabinetGame("TC10", "C000000004",0,true);
        ArcadeGame[] games = {TC1, TC2, TC3, TC4, TC5, TC6, TC7, TC8, TC9, TC10};

        for (ArcadeGame game : games) {
            arcade.addArcadeGame(game);
        }

        System.out.println(arcade.findRichestCustomer());
        System.out.println(arcade.getMedianGamePrice());
        System.out.println("Count of games :" + arcade.countArcadeGames());
        Customer retrieved = arcade.getCustomer("000001");
        ArcadeGame retrievedGame = arcade.getArcadeGame("C000000001");
        System.out.println("Retrieved: " + retrieved );
        System.out.println("Retrieved Game: " + retrievedGame );
        arcade.listAllGames();
        arcade.listAllCustomers();
        //marginal cases
//        arcade.processTransaction("000001","C000000001",true);
//        arcade.processTransaction("STD_18","C000000001",false);
//        arcade.processTransaction("NRM_16","A000000001",false);
//        arcade.processTransaction("CMP_25","AV00000003",false);

        int[] gameCounts = arcade.countArcadeGames();
        System.out.println("Cabinet Games: " + gameCounts[0]);
        System.out.println("Active Games: " + gameCounts[1]);
        System.out.println("Virtual Reality Games: " + gameCounts[2]);
        printCorporateJargon();
    }
}
