import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Account {
    private int accountNumber;
    private int balance;
    private User owner;
    private int CVV;
    private LocalDate expDate;
    private boolean accountState;
    private Transaction transaction;
    private List<Transaction> transactions; // Use a list for multiple transactions

    private List<Account> accountList = new ArrayList<>();

    public Account(int accountNumber, int balance, User owner, int cvv, LocalDate expDate, boolean accountState) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.owner = owner;
        this.CVV = cvv;
        this.expDate = expDate;
        this.accountState = accountState;
        this.transactions = new ArrayList<>();

    }
    public Account(int balance, User owner) {
        // Assuming generateRandomCVV and LocalDate.now().plusYears(1) are appropriate default values
        this.accountNumber = getAccountNumber();
        this.balance = balance;
        this.owner = owner;
        this.CVV = generateRandomCVV();
        this.expDate = LocalDate.now().plusYears(1);
        this.accountState = true;
    }

    public Account(int accountNumber,int balance, User owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.owner = owner;
    }

    public Account(int accountNumber, int balance, User owner, int cvv, LocalDate expDate) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.owner = owner;
        this.CVV = cvv;
        this.expDate = expDate;
        this.accountState = true;
    }

    private int generateRandomCVV() {
        // Generate a random 3-digit CVV
        Random random = new Random();
        return this.CVV = 100 + random.nextInt(900);
    }

    public void generateAccountNumber() {
        Random random = new Random();
        this.accountNumber = random.nextInt(900_000_000);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public LocalDate getExp_Date() {
        return expDate;
    }

    public void setExp_Date(LocalDate expDate) {
        this.expDate = expDate;
    }

    public int getAccount_Number() {
        return accountNumber;
    }

    public void setAccount_Number(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public boolean isAccountState() {
        return accountState;
    }

    public void setAccountState(boolean accountState) {
        this.accountState = accountState;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public boolean isAccount_State() {
        return accountState;
    }

    public void setAccount_State(boolean accountState) {
        this.accountState = accountState;
    }

    public int getCVV() {
        return CVV;
    }

    public void setCVV(int CVV) {
        this.CVV = CVV;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
