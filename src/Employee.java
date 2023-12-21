import enums.AccountType;
import enums.UserAction;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Employee extends User {
    protected String AddressEmp;
    protected String Position;
    protected String Graduation_College;
    protected int Graduation_Year;
    protected String Total_Grade;
    protected int Salary;
    private static List<Client> clientList = new ArrayList<>();
    private static int randomAccountNumber;

    private static final String CLIENT_FILE_PATH = "client.txt";

    public Employee(int ID, String FirstName, String LastName, String UserName, String Password,
                    String addressEmp, String position, String graduation_College,
                    int graduation_Year, String total_Grade, int salary) {
        super(ID, FirstName, LastName, UserName, Password);
        this.AddressEmp = addressEmp;
        this.Position = position;
        this.Graduation_College = graduation_College;
        this.Graduation_Year = graduation_Year;
        this.Total_Grade = total_Grade;
        this.Salary = salary;
    }


    public void Create_Client_Acc(UserAction action) {
        // Implementation for creating a new client account
        if (action == UserAction.CREATE) {
            System.out.println("Creating a new client account...");

            int clientID = generateRandomClientId();

            System.out.print("Enter client first name: ");
            String clientFirstName = new Scanner(System.in).nextLine();

            System.out.print("Enter client last name: ");
            String clientLastName = new Scanner(System.in).nextLine();

            System.out.print("Enter client username: ");
            String clientUsername = new Scanner(System.in).nextLine();

            System.out.print("Enter client telephone number: ");
            int clientTelephoneNumber = new Scanner(System.in).nextInt();

            System.out.print("Enter client address: ");
            String clientAddress = new Scanner(System.in).nextLine();

            System.out.print("Enter the number of accounts to create: ");
            int numAccounts = new Scanner(System.in).nextInt();

            List<Integer> accountNumbers = new ArrayList<>();
            List<AccountType> accountTypes = new ArrayList<>();

            for (int i = 0; i < numAccounts; i++) {
                // Generate a new unique account number for each account
                boolean isUnique;
                do {
                    randomAccountNumber = generateRandomAccountNumber();
                    isUnique = isAccountNumberUnique(randomAccountNumber);
                } while (!isUnique);

                accountNumbers.add(randomAccountNumber);

                // Ask for the account type for each account
                System.out.print("Enter account type for Account " + (i + 1) + " (SAVING/CURRENT): ");
                String accountTypeStr = new Scanner(System.in).nextLine();
                AccountType accountType = AccountType.valueOf(accountTypeStr.toUpperCase());

                accountTypes.add(accountType);
            }

            // Create a new client with multiple accounts
            Client newClient = new Client(
                    clientID,
                    clientFirstName,
                    clientLastName,
                    clientUsername,
                    "12345",
                    accountNumbers.stream().mapToInt(Integer::intValue).toArray(),
                    0,
                    clientTelephoneNumber,
                    clientAddress
            );

            // Instantiate a new User object (owner of the accounts)
            User userObject = new User(clientID, clientFirstName, clientLastName, clientUsername, "12345");

            // Create the accounts based on the specified types
            for (AccountType accountType : accountTypes) {
                if (accountType == AccountType.SAVING) {
                    // Assuming Saving constructor requires a balance and owner
                    newClient.addAccount(new Saving(0, 0, userObject));
                } else {
                    // Assuming Current constructor requires a balance and owner
                    newClient.addAccount(new Current(0, 0, userObject, 3)); // Assuming 3% fee
                }
            }


            getClientList().add(newClient);

            //**********************************************************************************************************************************************//



            // Check for profit gain is saving account and apply fees if Current account after a month


            //**********************************************************************************************************************************************//
            checkProfitAndApplyFees(newClient);

            // Save the updated list of clients to the file
            writeClientsToFile(getClientList());
            System.out.println("Client account created successfully. And password is '12345' ");
        }
    }

    private void checkProfitAndApplyFees(Client client) {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastTransactionDate = client.getLastTransactionDate();

        if (lastTransactionDate == null) {
            // Perform profit gain and apply fees logic here
            // Profit gain for Savings accounts
            for (Account account : client.getAccountList()) {
                if (account instanceof Saving savingAccount) {
                    savingAccount.Profit_gain(UserAction.PROFIT_GAIN);
                }
            }

            // Apply fees for Current accounts
            for (Account account : client.getAccountList()) {
                if (account instanceof Current currentAccount) {
                    currentAccount.Fees_Apply(UserAction.TRANSACTION_PROCESS);
                }
            }

            // Update the last transaction date
            client.setLastTransactionDate(currentDate);
        }
    }

    private boolean isAccountNumberUnique(int accountNumber) {
        // Check if the generated account number is unique
        for (Client client : getClientList()) {
            for (int existingAccountNumber : client.getAccount_Number()) {
                if (existingAccountNumber == accountNumber) {
                    return false; // Not unique
                }
            }
        }
        return true; // Unique
    }

    private int generateRandomClientId() {
        // Generate a random 5-digit client ID
        Random random = new Random();
        return 10000 + random.nextInt(90000);
    }

    private int generateRandomAccountNumber() {
        Random random = new Random();
        int min = 100000000; // Minimum 8-digit number
        int max = 999999999; // Maximum 8-digit number
        return random.nextInt((max - min) + 1) + min;
    }


    public void Edit_Client_Acc(UserAction action) {
        if (action == UserAction.EDIT) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Editing a client account...");

            // Read client ID to edit
            System.out.print("Enter client ID to edit: ");
            int editClientID = new Scanner(System.in).nextInt();
            scanner.nextLine(); // Consume the newline character

            // Find the client in the file based on ID
            Client clientToEdit = searchClientInFile(editClientID);

            if (clientToEdit != null) {
                // Display menu for editing
                System.out.println("Select the information to update:");
                System.out.println("1. Address");
                System.out.println("2. Telephone Number");
                System.out.print("Enter your choice (1 or 2): ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        // Update address
                        System.out.print("Enter new Address: ");
                        String newAddress = scanner.nextLine();
                        clientToEdit.setAddress(newAddress);
                        break;
                    case 2:
                        // Update telephone number
                        System.out.print("Enter new Telephone Number: ");
                        int newTelephoneNumber = scanner.nextInt();
                        clientToEdit.setTelephone_Number(newTelephoneNumber);
                        break;
                    default:
                        System.out.println("Invalid choice. No information updated.");
                        return;
                }

                // Write the updated client list back to the file
                updateClientInFile(clientToEdit);
                System.out.println("Client information updated successfully.");
            } else {
                System.out.println("Client not found.");
            }
        }
    }

    private void updateClientInFile(Client updatedClient) {
        List<Client> updatedClientList = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(new File(CLIENT_FILE_PATH))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                int clientID = Integer.parseInt(parts[0].trim());

                if (clientID != updatedClient.getID()) {
                    // If the client ID doesn't match, add it to the updated list
                    updatedClientList.add(createClientFromParts(clientID, parts));
                } else {
                    // If the client ID matches, add the updated client
                    updatedClientList.add(updatedClient);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading from client file: " + e.getMessage());
        }

        // Write the updated client list back to the file
        writeClientsToFile(updatedClientList);
    }


    public void Search_Client_Acc(UserAction action) {
        if (action == UserAction.SEARCH) {
            try {
                System.out.println("Searching for a client account...");

                // Example: Read client ID to search
                System.out.print("Enter client ID to search: ");
                int searchClientID = new Scanner(System.in).nextInt();

                // Search for the client in the file
                Client clientToSearch = searchClientInFile(searchClientID);

                if (clientToSearch != null) {
                    // Display client details
                    System.out.println("Client Details:");
                    System.out.println("ID: " + clientToSearch.getID());
                    System.out.println("First Name: " + clientToSearch.getFirstName());
                    System.out.println("Last Name: " + clientToSearch.getLastName());
                    System.out.println("Username: " + clientToSearch.getUserName());
                    System.out.println("Telephone: " + clientToSearch.getTelephone_Number());
                    System.out.println("Address: " + clientToSearch.getAddress());
                    System.out.println("Account Numbers: " + arrayToString(clientToSearch.getAccount_Number(), ","));
                    System.out.println("Balance: " + clientToSearch.getBalance());
                } else {
                    System.out.println("Client not found.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: Please enter a valid integer for the client ID.");
            }
        }
    }

    private Client searchClientInFile(int searchClientID) {
        try (Scanner fileScanner = new Scanner(new File(CLIENT_FILE_PATH))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                System.out.println(parts.length);
                // Check if the line has enough parts to create a client
                if (parts.length < 11) {
                    System.err.println("Error: Invalid data format in client file. Skipping line.");
                    continue;
                }
                try {
                    int clientID = Integer.parseInt(parts[0].trim());
                    System.out.println(clientID);
                    if (clientID == searchClientID) {
                        // Create a Client object based on the file data
                        return createClientFromParts(clientID, parts);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error: Invalid client ID format. Skipping line.");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading from client file: " + e.getMessage());
        }

        return null; // Client not found or error occurred
    }


    private Client createClientFromParts(int clientID, String[] parts) {

        String firstName = parts[1].trim();
        String lastName = parts[2].trim();
        String userName = parts[3].trim();
        String password = parts[4].trim();

        // Parse account details
        List<Account> accounts = new ArrayList<>();
        int balanceIndex = 5; // Assuming the balance information starts at index 5

        // Create a User object based on the parsed information
        User user = new User(Integer.parseInt(parts[0].trim()), firstName, lastName, userName, password);

        while (balanceIndex + 4 <= parts.length) {
            String accountTypeStr = parts[balanceIndex++].trim();
            int accountBalance = Integer.parseInt(parts[balanceIndex++].trim());
            int cvv = Integer.parseInt(parts[balanceIndex++].trim());
            String expDateStr = parts[balanceIndex++].trim();
            int accountNumber = Integer.parseInt(parts[balanceIndex++].trim());

            // Parse the expiration date
            LocalDate expDate = LocalDate.parse(expDateStr, DateTimeFormatter.ofPattern("MM/yy"));

            // Create the corresponding account type based on the parsed information
            AccountType accountType = AccountType.valueOf(accountTypeStr.toUpperCase());

            if (accountType == AccountType.SAVING) {
                accounts.add(new Saving(accountNumber, accountBalance, user, cvv, expDate));
            } else {
                // Assuming Current constructor requires a fee percentage, balance, owner
                accounts.add(new Current(accountNumber, accountBalance, user, cvv, expDate, 3)); // Assuming 3% fee
            }
        }

        // Parse the remaining client information
        int telephoneNumber = Integer.parseInt(parts[balanceIndex++].trim());
        String address = parts[balanceIndex].trim();
        // Create the client using the parsed information
        return new Client(clientID, firstName, lastName, userName, password, accounts, telephoneNumber, address);
    }


    public void Delete_Client_Acc(UserAction action) {
        if (action == UserAction.DELETE) {
            System.out.println("Deleting a client account...");

            // Read client ID to identify the client to delete
            System.out.print("Enter client ID to delete: ");
            int deleteClientID = new Scanner(System.in).nextInt();

            // Find the client in the file based on ID
            deleteClientFromFile(deleteClientID);

            System.out.println("Client deleted successfully.");
        }
    }

    private void deleteClientFromFile(int deleteClientID) {
        List<Client> updatedClientList = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(new File(CLIENT_FILE_PATH))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                int clientID = Integer.parseInt(parts[0].trim());

                if (clientID != deleteClientID) {
                    // If the client ID doesn't match, add it to the updated list
                    updatedClientList.add(createClientFromParts(clientID, parts));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading from client file: " + e.getMessage());
        }

        // Write the updated client list back to the file
        writeClientsToFile(updatedClientList);
    }


    private static void writeClientsToFile(List<Client> clientList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CLIENT_FILE_PATH))) {
            for (Client client : clientList) {
                writer.print(client.getID() + "," + client.getFirstName() + "," + client.getLastName()
                        + "," + client.getUserName() + "," + client.getPassword()
                        + "," + randomAccountNumber + ","
                        + client.getTelephone_Number() + "," + client.getAddress());

                // Save account details for each client
                for (Account account : client.getAccountList()) {
                    writer.print(ACCOUNT_DELIMITER + account.getClass().getSimpleName()); // Account type
                    writer.print(ACCOUNT_DELIMITER + account.getBalance());
                    writer.print(ACCOUNT_DELIMITER + generateRandomCVV());
                    writer.print(ACCOUNT_DELIMITER + generateRandomExpirationDate());
                }

                writer.println(); // Move to the next line for the next client
            }
        } catch (IOException e) {
            System.err.println("Error saving clients to file: " + e.getMessage());
        }
    }

    private static String generateRandomExpirationDate() {
        // Generate a random expiration date in the format MM / YY (month and year)
        Random random = new Random();

        // Generate a random month between 1 and 12
        int randomMonth = random.nextInt(12) + 1;

        // Generate a random two-digit year (considering it's a future date)
        int randomYear = Calendar.getInstance().get(Calendar.YEAR) % 100 + random.nextInt(10);

        // Format the expiration date as MM / YY
        return String.format("%02d/%02d", randomMonth, randomYear);
    }

    private static int generateRandomCVV() {
        // Generate a random 3-digit CVV
        Random random = new Random();
        return 100 + random.nextInt(900);
    }

    private Client getClientById(int searchClientID) {
        for (Client client : clientList) {
            if (client.getID() == searchClientID) {
                return client;
            }
        }
        return null; // Client not found
    }

    private static String arrayToString(int[] array, String delimiter) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            result.append(array[i]);
            if (i < array.length - 1) {
                result.append(delimiter);
            }
        }
        return result.toString();
    }

    private static final String ACCOUNT_DELIMITER = ","; // Choose an appropriate delimiter


    public static List<Client> getClientList() {
        return clientList;
    }

    public String getAddressEmp() {
        return AddressEmp;
    }

    public void setAddressEmp(String addressEmp) {
        AddressEmp = addressEmp;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getGraduation_College() {
        return Graduation_College;
    }

    public void setGraduation_College(String graduation_College) {
        Graduation_College = graduation_College;
    }

    public int getGraduation_Year() {
        return Graduation_Year;
    }

    public void setGraduation_Year(int graduation_Year) {
        Graduation_Year = graduation_Year;
    }

    public String getTotal_Grade() {
        return Total_Grade;
    }

    public void setTotal_Grade(String total_Grade) {
        Total_Grade = total_Grade;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int salary) {
        Salary = salary;
    }

    public static void setClientList(List<Client> clientList) {
        Employee.clientList = clientList;
    }

    public void editPersonalInfo(UserAction userAction) {
        // Implementation for editing personal information
        if (userAction == UserAction.EDIT_PERSONAL_INFO) {
            Scanner scanner = new Scanner(System.in);

            // Display menu
            System.out.println("Select the information to update:");
            System.out.println("1. Address");
            System.out.println("2. Position");
            System.out.print("Enter your choice (1 or 2): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Update address
                    System.out.print("Enter new Address: ");
                    String newAddress = scanner.nextLine();
                    setAddressEmp(newAddress);
                    break;
                case 2:
                    // Update position
                    System.out.print("Enter new Position: ");
                    String newPosition = scanner.nextLine();
                    setPosition(newPosition);
                    break;
                default:
                    System.out.println("Invalid choice. No information updated.");
                    return;
            }

            // Save updated personal information to the file
            updatePersonalInfoInFile();
            System.out.println("Personal information updated successfully.");
        }
    }


    private void updatePersonalInfoInFile() {
        // Update personal information in the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("employee.txt", true))) {
            writer.write(getID() + "," + getFirstName() + "," + getLastName() + "," + getUserName() + "," + getPassword()
                    + "," + getAddressEmp() + "," + getPosition() + "," + getGraduation_College() + ","
                    + getGraduation_Year() + "," + getSalary() + "," + getSalary());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to employee file: " + e.getMessage());
        }
    }

}