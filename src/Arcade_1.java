/******************************************************************************
 File: Arcade_1.java
 Date: Monday 28th April 2025
 Author: Kimi Tang
 Description: experimental version of Arcade.java testing other data structures beyond using
 ArrayList

 v1.0.0 28/4/2025 Creation of file and basic fields and variables, main method
 v1.0.1 28/4/2025 Replacing Arraylist structures to Hashmap, class is functional as intended
 References ******************************************************************************/

import java.util.*;

public class Arcade_1 {
    private final String name;
    private int revenue;
    private Map<String, ArcadeGame> arcadeGames = new HashMap<>();
    private Map<String, Customer> customers = new HashMap<>();

    public Arcade_1(String name) {
        this.name = name;
        this.revenue = 0;
        this.arcadeGames = new HashMap<>();
        this.customers = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getRevenue() {
        return revenue;
    }

    public void addCustomer(Customer c) {
        customers.put(c.getAccountID(),c);
    }

    public Customer getCustomer(String customerID) throws InvalidCustomerException {
        Customer customer = customers.get(customerID);
        if (customer == null) {
            throw new InvalidCustomerException("Customer not found: " + customerID);
        }
        return customer;
    }

    public void addArcadeGame(ArcadeGame game) throws InvalidGameIdException {
        if (arcadeGames.containsKey(game.getGameId())) {
            throw new InvalidGameIdException("Duplicate game ID: " + game.getGameId());
        }
        arcadeGames.put(game.getGameId(), game);
    }
    public ArcadeGame getArcadeGame(String gameID) throws InvalidGameIdException {
        ArcadeGame game = arcadeGames.get(gameID);
        if (game == null) {
            throw new InvalidGameIdException("Game not found: " + gameID);
        }
        return game;
    }

    public void listAllGames() {
        System.out.println("Listing All Games in Arcade: " + this.getName());
        for (ArcadeGame game : arcadeGames.values()) {
            System.out.println(game); // calls toString()
        }
    }

    public void listAllCustomers() {
        for (Customer c : customers.values()) {
            System.out.println(c);
        }
    }


    public boolean processTransaction(String customerID, String gameID, boolean peak) throws Exception {
        Customer customer = getCustomer(customerID);
        ArcadeGame game = getArcadeGame(gameID);

        int chargedAmount = customer.chargeAccount(game, peak);
        revenue += chargedAmount;
        return true;
    }

    public Customer findRichestCustomer() throws InvalidCustomerException {
        if (customers.isEmpty()) {
            throw new InvalidCustomerException("No customers in arcade.");
        }

        Customer richest = null;
        for (Customer c : customers.values()) {
            if (richest == null || c.getBalance() > richest.getBalance()) {
                richest = c;
            }
        }
        return richest;
    }

    public int getMedianGamePrice() {
        if (arcadeGames.isEmpty()) return 0;

        List<Integer> prices = new ArrayList<>();
        for (ArcadeGame game : arcadeGames.values()) {
            prices.add(game.getPriceToPlay());
        }

        Collections.sort(prices);

        int mid = prices.size() / 2;
        if (prices.size() % 2 == 0) {
            return (prices.get(mid - 1) + prices.get(mid)) / 2;
        } else {
            return prices.get(mid);
        }
    }

    public int[] countArcadeGames() {
        int[] counts = new int[3]; // [cabinet, active, VR]

        for (ArcadeGame game : arcadeGames.values()) {
            if (game instanceof VirtualRealityGame) {
                counts[2]++;
            } else if (game instanceof ActiveGame) {
                counts[1]++;
            } else if (game instanceof CabinetGame) {
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
        return "Arcade: " + name + ", Revenue: " + revenue + ", Games: " + arcadeGames.size() + ", Customers: " + customers.size();
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
        arcade.processTransaction("000001","C000000001",true);
        arcade.processTransaction("STD_18","C000000001",false);
        arcade.processTransaction("NRM_16","A000000001",false);
        arcade.processTransaction("CMP_25","AV00000003",false);
        arcade.processTransaction("YNG_11","AV00000001",false);

        int[] gameCounts = arcade.countArcadeGames();
        System.out.println("Cabinet Games: " + gameCounts[0]);
        System.out.println("Active Games: " + gameCounts[1]);
        System.out.println("Virtual Reality Games: " + gameCounts[2]);





        printCorporateJargon();
    }
}
