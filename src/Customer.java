/******************************************************************************
 File: Customer.java
 Date: Sunday 20th April 2025
 Author: Kimi Tang
 Description: store information about a customer’s account and processing actions such as adding funds and
 charging accounts , throws InvalidCustomerException InsufficientBalanceException AgeLimitException

 v1.0.0 20/4/2025 Creation of file and basic fields , variables and constructors, main method
 v1.0.1 20/4/2025 Implemented calculatePricetoPlay()
 v1.0.2 23/4/2025 Implemented toString()
 v1.1.0 28/4/2025 Implemented validateBalance() ,validateCustomerID(), processTransaction()
 v1.1.1 29/4/2025 Fixed logic of validateBalance(): Checks if transaction to commit violates requirements, removed
 code causing double count
 v1.1.2 6/5/2025 Seperate method calculatecharge()  to handle logic returning int
 References ******************************************************************************/

public class Customer {
    private String accountID; //6 digit
    private String name;
    private int age;
    public enum Level {NONE, STAFF, STUDENT}
    private Level level;
    private int balance;


    //constructor
    public Customer(String accountID, String name, int age, Level level, int balance) throws InvalidCustomerException, InsufficientBalanceException {
        this.accountID = accountID;
        this.name = name;
        this.age = age;
        this.level = level;
        this.balance = balance;
        validateCustomerID(accountID, level, balance);
    }

    //getters accessors
    public String getAccountID() {
        return accountID;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Level getLevel() {
        return level;
    }

    public int getBalance() {
        return balance;
    }
    //validation methods for customer balance and ID
    public void validateBalance(Level level, int balance, String accountID, ArcadeGame game , int amount ) throws InsufficientBalanceException {
        balance = balance - amount;
        if(balance < 0 && level != Level.STUDENT) {
            throw new InsufficientBalanceException("Balance cannot be negative, Game attempted " + game + this.toString());
        } else if(balance < -500) {
            throw new InsufficientBalanceException("Student overdraft limit exceeded, Game attempted  "+ game + this.toString());
        }
    }
    public void validateCustomerID(String accountID, Level level, int balance) throws InvalidCustomerException {
        if (accountID == null) {
            throw new InvalidCustomerException("Account ID cannot be null");
        } else if (accountID.length() != 6) {
            throw new InvalidCustomerException("Invalid account ID: format error");
        }
    }

    public void addFunds(int amount ){
        if(amount > 0){
            balance += amount;
        }else{
            System.out.println("Invalid funds" + " (" + amount + ")");
        }
    }
    // Discount on Customer identity
    public int applyDiscount (int PriceToPlay, Level level){
        int priceToCharge = PriceToPlay;
        if(level == Level.STUDENT){
            priceToCharge = (int) ( priceToCharge*0.95);
            return priceToCharge;
        }else if(level == Level.STAFF){
            priceToCharge = (int) ( priceToCharge*0.9);
            return priceToCharge;
        }else{
            return priceToCharge;
        }
    }
    //final calculation of price and catching Age/Balance exception Before deduction
    public int calculatecharge(ArcadeGame Game, boolean peak) throws InsufficientBalanceException, AgeLimitException {
        int price = 0;
        price = Game.calculatePriceToPlay(peak);
        price = applyDiscount(price, level);
        if (Game instanceof ActiveGame) {//detects if game is active game
            int minAge = ((ActiveGame) Game).getMinAge();//get MinAge from Active game
            if(minAge > getAge()) {
                throw new AgeLimitException(
                        "\n !!! Age limit not met, Customer: " + name + " |account ID: " + accountID + " |Game attempted: "
                                + Game.getName());
            }
        }
        validateBalance(level, balance, accountID,Game,price);
        return price;
    }
    //Actual method deduction for deduction from Customer Balance
    public int chargeAccount(ArcadeGame Game, boolean peak) throws InsufficientBalanceException, AgeLimitException {
        int price = calculatecharge(Game, peak);
        balance -= price;
        return price;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Customer info [ accountID : ").append(accountID)
                .append(", name : ").append(name)
                .append(", age : ").append(age)
                .append(",\n level : ").append(level)
                .append(", balance : ").append(balance)
                .append("]\n");
                sb.append("----------------------");
        return sb.toString();
    }


    public static void main (String [] args) throws InvalidCustomerException  , InsufficientBalanceException, InvalidGameIdException, AgeLimitException{
        //Marginal case testing
        CabinetGame TC1 = new CabinetGame("TC1", "C000000001",200,true);
        CabinetGame TC2 = new CabinetGame("TC2", "C000000002",181,true);//expect 130, output 129
        CabinetGame TC3 = new CabinetGame("TC3", "C000000003",200,false);
        ActiveGame TC4 = new ActiveGame("TC4","A000000001",300,15);
        ActiveGame TC5 = new ActiveGame("TC5","A000000002",400,15);
        VirtualRealityGame TC6 = new VirtualRealityGame("TC6","AV00000001",300,15, VirtualRealityGame.VRType.headsetOnly);
        VirtualRealityGame TC7 = new VirtualRealityGame("TC7","AV00000002",300,15, VirtualRealityGame.VRType.headsetAndController);
        VirtualRealityGame TC8 = new VirtualRealityGame("TC8", "AV00000003",300,18, VirtualRealityGame.VRType.fullBodyTracking);
        VirtualRealityGame TC9 = new VirtualRealityGame("TC8", "AV00000004",400,18, VirtualRealityGame.VRType.fullBodyTracking);
        CabinetGame TC10 = new CabinetGame("TC10", "C000000004",0,true);

        Customer STU18 = new Customer("STD_18", "Student-18",18,Level.STUDENT,1500);
        Customer CMP25 = new Customer("CMP_25", "Staff-25",25,Level.STAFF,1000);
        Customer NRM16 = new Customer("NRM_16", "Normal-16",16,Level.NONE,500);
        Customer YNG11 = new Customer("YNG_11", "Student-11",11,Level.STUDENT,200);
        Customer BROKE = new Customer("BROKE!", "Student-25",30,Level.STUDENT,0);

        System.out.println("---------------STU18-------------------\n");
        STU18.chargeAccount(TC3,false);
        STU18.chargeAccount(TC4,false);
        STU18.chargeAccount(TC6,false);
        STU18.chargeAccount(TC7,false);
        STU18.chargeAccount(TC8,false);
        STU18.chargeAccount(TC4,true);
        STU18.chargeAccount(TC9,true);
        System.out.println("---------------CMP25-------------------\n" );

        CMP25.chargeAccount(TC4,false);
        CMP25.chargeAccount(TC1,false);
        CMP25.chargeAccount(TC6,false);
        CMP25.chargeAccount(TC2,false);

        System.out.println("---------------NRM16-------------------\n" );

        NRM16.chargeAccount(TC1,true);
        NRM16.chargeAccount(TC1,true);
//        NRM16.chargeAccount(TC1,true);
//        NRM16.chargeAccount(TC8,true);

        System.out.println("---------------YNG11-------------------\n" );
        YNG11.chargeAccount(TC1,true);
//        YNG11.chargeAccount(TC9,true);

        System.out.println("---------------BROKE-------------------\n" );
        BROKE.chargeAccount(TC5,false);//fix negative price
        BROKE.chargeAccount(TC10,false);
        BROKE.chargeAccount(TC9,true);
//        BROKE.chargeAccount(TC9,true);

    }
}
