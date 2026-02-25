
/******************************************************************************
 File: Simulation.java
 Date: Saturday 26th April 2025
 Author: Kimi Tang
 Description: simulate the creation and use of an arcade by reading the txt files
 To do History: Modify Test files so it runs to the end.
                must clarify exceptions , if its caught before it finishes running or after

 v1.0.0 26/4/2025 Creation of file and basic fields and variables, main method
 v1.0.1 27/4/2025 Implemented BufferedReader and methods from Arcade.java
 v1.1.0 27/4/2025 Implemented logic for reading files and populating arcade obj with Customers and ArcadeGames
 v1.1.2 28/4/2025 Fixed behaviour of caught errors
 v1.1.3 28/4/2025 Fixed display currency format and cosmetics println() in most methods
 v1.2.0 29/4/2025 Fixed logic for validating balance in Customer, returned expected results, ready for fine-tuning

 References ******************************************************************************/
import java.io.*;


public class Simulation {

    public static void main(String[] args) {
        try {
            File customerFile = new File("customers.txt");
            File gamesFile = new File("games.txt");
            File transactionsFile = new File("transactions.txt");
            Arcade arcade = initialiseArcade("Test", gamesFile, customerFile);
            simulateFun(arcade, transactionsFile);
            arcade.getMedianGamePrice();
            arcade.findRichestCustomer();
            System.out.println(arcade);
            System.out.println(arcade.getRevenue());
        } catch (Exception e ){
            e.printStackTrace();
        }
    }

    public static Arcade initialiseArcade(String arcadeName, File gamesFile, File customerFile) throws IOException, InvalidGameIdException, InsufficientBalanceException, InvalidCustomerException {
        Arcade arcade = new Arcade(arcadeName,0);

        //Reading the games into the arcade
        BufferedReader gameReader = new BufferedReader(new FileReader(gamesFile));
            String line_Game;
            while ((line_Game = gameReader.readLine()) != null) {
                // Assuming format: gameId,name,type,price
                String[] parts = line_Game.trim().split("@");
                String id = parts[0];
                String name = parts[1];
                ArcadeGame.gameType = ArcadeGame.gameType.valueOf(parts[2]);
                int price = Integer.parseInt(parts[3]);
                boolean payout;
                int minAge;
                VirtualRealityGame.VRType vrType ;
                switch (ArcadeGame.gameType){
                    case cabinet:
                        String Payout = parts[4];
                        payout = Payout.equals("yes");;
                        arcade.addArcadeGame(new CabinetGame(name,id,price,payout));
                        break;
                    case active:
                        minAge = Integer.parseInt(parts[4]);
                        arcade.addArcadeGame(new ActiveGame(name,id,price,minAge));
                        break;
                    case virtualReality:
                        minAge = Integer.parseInt(parts[4]);
                        vrType = VirtualRealityGame.VRType.valueOf(parts[5]);
                        arcade.addArcadeGame(new VirtualRealityGame(name,id,price,minAge,vrType));
                        break;
                }
            }
        //Reading the customers into the arcade
        BufferedReader customerReader = new BufferedReader(new FileReader(customerFile));
            String line_Customer;
            while ((line_Customer = customerReader.readLine()) != null) {
                // Assuming format: customerId,name,balance
                String[] parts = line_Customer.trim().split("#");
                String id = parts[0];
                String name = parts[1];
                int balance = Integer.parseInt(parts[2]);
                int age = Integer.parseInt(parts[3]);
                Customer.Level level = Customer.Level.NONE;
                if (parts.length > 4) {
                    level = Customer.Level.valueOf(parts[4]);
                }
                Customer customer = new Customer(id,name,age,level,balance);  //  Constructor expected
                arcade.addCustomer(customer);  // =Method expected in Arcade
            }

        return arcade;
    }

    public static void simulateFun(Arcade arcade, File transactionsFile) throws IOException, AgeLimitException, InsufficientBalanceException, InvalidCustomerException, InvalidGameIdException {
        BufferedReader customerReader = new BufferedReader(new FileReader(transactionsFile));
        String line;
        while ((line = customerReader.readLine()) != null) {
            try {
                String[] parts = line.trim().split(",");
                char firstChar = parts[0].charAt(0);
                //PLAY case = transaction processing
                if (firstChar == 'P') {
                    String customerID = parts[1];
                    Customer customer = arcade.getCustomer(customerID);
                    String GameID = parts[2];
                    ArcadeGame game = arcade.getArcadeGame(GameID);
                    boolean peak;
                    if (parts[3].equals("PEAK")) {
                        peak = true;
                    } else {
                        peak = false;
                    }
                    arcade.processTransaction(customerID, GameID, peak);
                    // Build transaction summary using StringBuilder
                    StringBuilder sb = new StringBuilder();
                    String formattedPrice = String.format("£%d.%02d",customer.calculatecharge(game, peak) / 100, Math.abs(customer.calculatecharge(game, peak) % 100));
                    String formattedBalance = String.format("£%d.%02d", customer.getBalance() / 100, Math.abs(customer.getBalance() % 100));
                    sb.append("\nTransaction Summary:\n");
                    sb.append("----------------------\n");
                    sb.append("Customer: ").append(customer.getName())
                            .append(" (").append(customer.getAccountID()).append(")\n");
                    sb.append("Game: ").append(game.getName())
                            .append(" (").append(game.getGameId()).append(")\n");
                    sb.append("Amount Paid: ").append(formattedPrice).append("\n");
                    sb.append("Remaining Balance: ").append(formattedBalance).append("\n");
                    sb.append("----------------------");
                    System.out.println(sb);
                }
                //New_CUSTOMER case , add customer
                if (firstChar == 'N') {
                    String id = parts[1];
                    String name = parts[2];
                    int balance = 0;
                    int age = 0;
                    Customer.Level level = Customer.Level.NONE;
                    switch (parts.length) {
                        case 5:
                            balance = Integer.parseInt(parts[3]);
                            age = Integer.parseInt(parts[4]);
                            break;
                        case 6:
                            level = Customer.Level.valueOf(parts[3]);
                            balance = Integer.parseInt(parts[4]);
                            age = Integer.parseInt(parts[5]);
                    }
                    Customer customer = new Customer(id, name, age, level, balance);  //  Constructor expected
                    arcade.addCustomer(customer);  //  Method expected in Arcade
                    System.out.println("\n ** Successfully added customer, details : \n " + customer + "\n");
                }

                //ADD_FUNDS case, calls addFund()
                if (firstChar == 'A') {
                    String id = parts[1];
                    int amount = Integer.parseInt(parts[2]);
                    Customer c = arcade.getCustomer(id);
                    c.addFunds(amount);
                    System.out.println("\n++ Successfully added funds to :" + c);
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}