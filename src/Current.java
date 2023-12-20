import enums.UserAction;

import java.time.LocalDate;

class Current extends Account {
    private static final double FEES_RATE = 0.03;  // 3%

    private int fees;

    public Current(int accountNumber, int balance, User owner, int fees) {
        super(accountNumber, balance, owner);
        this.fees = fees;
    }
    public Current(int accountNumber, int balance, User owner, int cvv, LocalDate expDate, int feePercentage) {
        super(accountNumber, balance, owner, cvv, expDate);
        this.fees = feePercentage;
    }


    public void Fees_Display(UserAction actionType) {
        if (actionType == UserAction.DISPLAY_DETAILS) {
            System.out.println("Fees: " + fees);
        }
    }

    public void Fees_Apply(UserAction actionType) {
        if (actionType == UserAction.TRANSACTION_PROCESS) {
            // Specific logic for applying fees
            int currentBalance = getBalance();

            if (currentBalance < 3000) {
                int feeAmount = (int) (currentBalance * FEES_RATE); // Calculate fee
                setBalance(currentBalance - feeAmount); // Deduct fees from the balance
                System.out.println("Fees Applied: " + feeAmount);
            } else {
                System.out.println("No fees applied. Balance is above 3000.");
            }
        }
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }
}
