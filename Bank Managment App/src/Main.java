import java.util.*;
import java.io.*;

//-------- Hafiz Abdullah -----------
class Bank {
    private String name;
    private ArrayList<Client> clList;
    private ArrayList<Account> acList;
    private File_Handling acc_file = new File_Handling("accs_data.csv","ACC No, Amount, Holder Name");
    private File_Handling cln_file = new File_Handling("cln_data.csv","ID, Name, CNIC, Phone No");
    private File_Handling bank_file = new File_Handling("bank_info.txt","Bank,Total Accounts,Total Clients,Total Balance");

    public Bank(String name) throws IOException {
        this.name = name;
        clList = new ArrayList<>();
        acList = new ArrayList<>();
    }
    public Client addClient(Person p) throws IOException {
        Client c = new Client(p);
        clList.add(c);
        cln_file.write(c.toString(),true);
        return c;
    }
    public Account addAccount(String id, float amount, Client c) throws IOException {
        Account a = new Account(amount, c);
        c.addAccount(a);
        acc_file.write(a.toString(),true);
        acList.add(a);
        return a;
    }
    public Account searchAccount(String id) {
        for (Account a : acList) {
            if (a.getNumber().equals(id)) {
                return a;
            }
        }
        return null;
    }
    public boolean removeClient(String id) {
        Client target = null;
        for (Client c : clList) {
            if (c.getId().equals(id)) {
                target = c;
                break;
            }
        }
        if (target != null) {
            acList.removeAll(target.getAcList());
            clList.remove(target);
            return true;
        }
        return false;
    }
    public float totalAmount() {
        float total = 0;
        for (Account a : acList) {
            total += a.getAmount();
        }
        return total;
    }
    public Client searchCustomerDetail(String cnic) {
        for (Client c : clList) {
            if (c.getPersonDetails().getCnic().equals(cnic)) {
                return c;
            }
        }
        return null;
    }
    public String showInfo() throws IOException {
        String data = "Bank Name:"+name+",\nTotal Accounts: "+acList.size()+",\nTotal Clients: "+clList.size()+",\nTotal Balance: "+totalAmount();
        bank_file.write(data,false);
        return bank_file.read();
    }
    @Override
    public String toString() {

        String data = "Bank Name: " + name + "\n";

        for (Client c : clList) {
            data += c.toString() + "\n";
        }

        return data;
    }
}
class Account {

    private String number;
    private float amount;
    private Client acHolder;
    public static int count = 1000;

    public Account(float amount, Client acHolder) throws IOException {
        this.number = "ACC" + count++;
        this.amount = amount;
        this.acHolder = acHolder;
    }

    public String getNumber() {
        return number;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Client getAcHolder() {
        return acHolder;
    }

    public void setAcHolder(Client acHolder) {
        this.acHolder = acHolder;
    }

    public float deposit(float amount) {
        this.amount += amount;
        return this.amount;
    }

    public float withdraw(float amount) {

        if (amount > this.amount) {
            System.out.println("Insufficient Balance");
            return this.amount;
        }

        this.amount -= amount;
        return this.amount;
    }

    @Override
    public String toString() {
        return number+","+amount+","+acHolder.getPersonDetails().getName();
    }
}
class Client {

    private String id;
    private Person personDetails;
    private ArrayList<Account> acList;

    public static int count = 1;

    public Client(Person personDetails) {
        this.id = "CL" + count++;
        this.personDetails = personDetails;
        acList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Person getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(Person personDetails) {
        this.personDetails = personDetails;
    }

    public ArrayList<Account> getAcList() {
        return acList;
    }

    public void addAccount(Account a) {
        acList.add(a);
    }

    public float totalAmount() {

        float total = 0;

        for (Account a : acList) {
            total += a.getAmount();
        }

        return total;
    }

    public void deposit(float amount, String accNo) {

        for (Account a : acList) {

            if (a.getNumber().equals(accNo)) {
                System.out.println("Current Balance: " + a.deposit(amount));
                return;
            }
        }

        System.out.println("Account Not Found");
    }

    public void withdraw(float amount, String accNo) {

        for (Account a : acList) {

            if (a.getNumber().equals(accNo)) {
                System.out.println("Remaining Balance: " + a.withdraw(amount));
                return;
            }
        }

        System.out.println("Account Not Found");
    }
    @Override
    public String toString() {

        String data = id+","+personDetails.toString();

        return data;
    }
}
class Person {
    private String name;
    private String cnic;
    private String phoneNo;
    public Person(String name, String cnic, String phoneNo) {
        this.name = name;
        this.cnic = cnic;
        this.phoneNo = phoneNo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCnic() {
        return cnic;
    }
    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    @Override
    public String toString() {
        return name+","+cnic+","+phoneNo;
    }
}
class Main {

    public static void main(String[] args) throws IOException {

//        File_Handling fileHandling = new File_Handling();
//        int id = 25;
//        fileHandling.write(id+",Husnain,20");
//        System.out.println(fileHandling.read());

        Bank bank = new Bank("HBL");

        Person p1 = new Person("Ali", "35202-1234567-1", "03001234567");
        Person p2 = new Person("Ahmed", "35202-9876543-1", "03111234567");
//
        Client c1 = bank.addClient(p1);
        Client c2 = bank.addClient(p2);
//
        Account a1 = bank.addAccount(c1.getId(), 5000, c1);
        Account a2 = bank.addAccount(c1.getId(), 10000, c1);

        Account a3 = bank.addAccount(c2.getId(), 7000, c2);

        c1.deposit(2000, a1.getNumber());

        c1.withdraw(1000, a2.getNumber());

        System.out.println(bank.showInfo());
//
//        System.out.println("\n----- CLIENT DETAIL -----");
//        System.out.println(c1);
//
//        System.out.println("\nTotal Amount in Client Accounts: "
//                + c1.totalAmount());
//
//        System.out.println("\nTotal Amount in Bank: "
//                + bank.totalAmount());
//
//        Client search = bank.searchCustomerDetail("35202-1234567-1");
//
//        if (search != null) {
//            System.out.println("\nCustomer Found");
//            System.out.println(search);
//        }
//
//        boolean removed = bank.removeClient(c2.getId());
//
//        if (removed) {
//            System.out.println("\nClient Removed Successfully");
//        }
//
//        System.out.println("\n----- BANK DETAIL -----");
//        System.out.println(bank);
    }
}
//-----------------------------------



//----------- Husnain ---------------
class File_Handling {
    String name;
    String header;
    File file;
    File_Handling(String name,String header) throws IOException {
        this.name = name;
        this.header = header;
        create_file(name);
        createHeader(header);
    }
    private void create_file(String name) throws IOException {
        file = new File(this.name);

        if (file.createNewFile()) {
            System.out.println("File Created " + file.getName());
        } else {
            System.out.println("File Already Created!");
        }
    }
    private void createHeader(String header) throws IOException {
        if (file.length() == 0) {
            FileWriter writer = new FileWriter(file);
            writer.write(header+"\n");
            writer.close();
        }
    }
    public void write(String data,Boolean b) throws IOException {
        FileWriter writer = new FileWriter(file, b);
        writer.write(data + "\n");
        writer.close();
    }
    public String read() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder data_out = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            for (String value : data) {
                data_out.append(value).append("\t");
            }
            data_out.append("\n");
        }
        reader.close();
        return data_out.toString();
    }
}
//-----------------------------------



//----------- Ali -------------------
// Ali Jutt Start from Here!
//-----------------------------------