import enums.UserAction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int ID;
    public String FirstName;
    public String LastName;
    public String UserName;
    private String Password;
    protected Account account;
    private List<Account> accounts;


    public User(int ID, String firstName, String lastName, String userName, String password) {
        this.ID = ID;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.UserName = userName;
        this.Password = password;
        this.accounts = new ArrayList<>();
    }


    public void Edit_Pers_info(UserAction type) {
        if (type == UserAction.EDIT_PERSONAL_INFO) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println("Select the information to update:");
                System.out.println("1. First Name");
                System.out.println("2. Last Name");
                System.out.println("3. Username");
                System.out.println("4. Password");

                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        System.out.print("Enter new first name: ");
                        setFirstName(reader.readLine());
                        break;
                    case 2:
                        System.out.print("Enter new last name: ");
                        setLastName(reader.readLine());
                        break;
                    case 3:
                        System.out.print("Enter new username: ");
                        setUserName(reader.readLine());
                        break;
                    case 4:
                        System.out.print("Enter new password: ");
                        setPassword(reader.readLine());
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        return;
                }

                // Save updated personal information to the file
                updatePersonalInfoInFile();
                System.out.println("Personal information updated successfully.");
            } catch (IOException | NumberFormatException e) {
                System.err.println("Error reading from input: " + e.getMessage());
            }
        }
    }

    public void Show_Trans(UserAction type) {
        if (type == UserAction.SHOW_TRANSACTIONS) {
            // Read transactions from the file
            List<String> transactions = readTransactionsFromFile();

            // Display transactions for the user's account
            if (!transactions.isEmpty()) {
                String userUserName = getUserName(); // Assuming getUserName() returns String
                System.out.println("Transactions for User: " + userUserName);

                for (String transaction : transactions) {
                    // Check if the transaction contains "Owner" key
                    if (transaction.contains("Owner")) {
                        // Parse CSV data
                        String[] keyValuePairs = transaction.split(", ");
                        String transactionOwner = null;

                        // Extract the owner information
                        for (String pair : keyValuePairs) {
                            String[] entry = pair.split(": ");
                            if (entry.length == 2 && entry[0].trim().equals("Owner")) {
                                transactionOwner = entry[1].trim();
                                break;
                            }
                        }

                        // Check if the transaction owner's username matches the user's username
                        if (transactionOwner != null && userUserName.equals(transactionOwner)) {
                            // Display the matching transaction
                            System.out.println(transaction);
                        }
                    } else {
                        System.out.println("Error: Transaction data does not contain 'Owner' information: " + transaction);
                    }
                }
            } else {
                System.out.println("No transactions found.");
            }
        }
    }

    public void Show_TransAdmin(UserAction type) {
        if (type == UserAction.SHOW_TRANSACTIONS) {
            // Read transactions from the file
            List<String> transactions = readTransactionsFromFile();

            // Display transactions
            if (!transactions.isEmpty()) {
                System.out.println("All Transactions:");
                for (String transaction : transactions) {
                    // Display each transaction
                    System.out.println(transaction);
                }
            } else {
                System.out.println("No transactions found.");
            }
        }
    }

    public void Display_Pers_info(UserAction type) {
        // Implementation for displaying personal information
        if (type == UserAction.DISPLAY_PERSONAL_INFO) {
            try {
                // Read data from the file
                BufferedReader fileReader = new BufferedReader(new FileReader("client.txt"));
                String line;
                while ((line = fileReader.readLine()) != null) {

                    String[] userData = line.split(",");

                    System.out.println("ID: " + userData[0]);
                    System.out.println("First name: " + userData[1]);
                    System.out.println("Last name: " + userData[2]);
                    System.out.println("Username: " + userData[3]);
                    System.out.println("Password: " + userData[4]);
                    System.out.println("Account Number: " + userData[5]);
                    System.out.println("Balance: " + userData[6]);
                    System.out.println("CVV: " + userData[7]);
                    System.out.println("Expiration Date: " + userData[8]);
                    System.out.println("Account State: " + userData[9]);
                    System.out.println("Telephone: " + userData[10]);
                    System.out.println("Address: " + userData[11]);

                    // Add more fields as needed
                    System.out.println();
                }
                fileReader.close();
            } catch (IOException e) {
                System.err.println("Error reading from the file: " + e.getMessage());
            }
        }
    }

    public void Sign_ln(UserAction type) {
        if (type == UserAction.SIGN_IN) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.print("Enter username: ");
                String enteredUsername = reader.readLine();
                System.out.print("Enter password: ");
                String enteredPassword = reader.readLine();

                if (enteredUsername.equals("admin") && enteredPassword.equals("admin")) {
                    System.out.println("Sign-in successful as admin.");
                }

                // Check if entered username and password match in client data
                if (checkCredentialsInFile("client.txt", enteredUsername, enteredPassword)) {
                    System.out.println("Sign-in successful as client.");
                }
                // Check if entered username and password match in employee data
                else if (checkCredentialsInFile("employee.txt", enteredUsername, enteredPassword)) {
                    System.out.println("Sign-in successful as employee.");
                } else {
                    System.out.println("Invalid username or password. Sign-in failed.");
                }
            } catch (IOException e) {
                System.err.println("Error reading from input: " + e.getMessage());
            }
        }
    }


    private boolean checkCredentialsInFile(String fileName, String enteredUsername, String enteredPassword) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.split(",");
                String usernameFromFile = parts[3];
                String passwordFromFile = parts[4];
                if (enteredUsername.equals(usernameFromFile) && enteredPassword.equals(passwordFromFile)) {
                    return true; // Credentials match
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return false;
    }

    private List<String> readTransactionsFromFile() {
        List<String> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("transaction.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from transaction file: " + e.getMessage());
        }
        return transactions;
    }

    private void updatePersonalInfoInFile() {
        // Read existing content
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("client.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading client file: " + e.getMessage());
            return;
        }

        // Find and update the line corresponding to the client
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            int clientId = Integer.parseInt(parts[0].trim());

            if (clientId == getID()) {
                // Update the necessary fields (e.g., first name, last name, username, password)
                parts[1] = getFirstName();
                parts[2] = getLastName();
                parts[3] = getUserName();
                parts[4] = getPassword();

                // Join the parts back into a line
                lines.set(i, String.join(",", parts));

                // No need to continue searching
                break;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("client.txt"))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to client file: " + e.getMessage());
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

}