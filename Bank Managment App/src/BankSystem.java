import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Person {
    private String name;
    private String cnic;
    private String phoneNo;

    public Person(String name, String cnic, String phoneNo) {
        this.name = name;
        this.cnic = cnic;
        this.phoneNo = phoneNo;
    }
    public String getName() { return name; }
    public String getCnic() { return cnic; }
    public String getPhoneNo() { return phoneNo; }
    public void setName(String name) { this.name = name; }
    public void setCnic(String cnic) { this.cnic = cnic; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String toString() {
        return name + "," + cnic + "," + phoneNo;
    }
}
class Account {
    private String number;
    private float amount;
    private Client acHolder;
    public static int count = 1000;

    public Account(float amount, Client acHolder) {
        this.number = "ACC" + count++;
        this.amount = amount;
        this.acHolder = acHolder;
    }
    public String getNumber() { return number; }
    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }
    public Client getAcHolder() { return acHolder; }
    public void setAcHolder(Client c) { this.acHolder = c; }

    public float deposit(float amount) {
        this.amount += amount;
        return this.amount;
    }
    public float withdraw(float amount) {
        if (amount > this.amount) {
            return -1;
        }
        this.amount -= amount;
        return this.amount;
    }
    public String toString() {
        return number + "," + amount + "," + acHolder.getPersonDetails().getName();
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
    public String getId() { return id; }
    public Person getPersonDetails() { return personDetails; }
    public void setPersonDetails(Person p) { this.personDetails = p; }
    public ArrayList<Account> getAcList() { return acList; }

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
    public String deposit(float amount, String accNo) {
        for (Account a : acList) {
            if (a.getNumber().equals(accNo)) {
                float bal = a.deposit(amount);
                return "Deposit ho gaya! New balance: " + bal;
            }
        }
        return "Account nahi mila: " + accNo;
    }
    public String withdraw(float amount, String accNo) {
        for (Account a : acList) {
            if (a.getNumber().equals(accNo)) {
                float result = a.withdraw(amount);
                if (result == -1) return "Paisa kam hai!";
                return "Nikal gaya! Bacha hua: " + result;
            }
        }
        return "Account nahi mila: " + accNo;
    }
    public String toString() {
        return id + "," + personDetails.toString();
    }
}
class File_Handling {
    String name;
    String header;
    File file;

    File_Handling(String name, String header) throws IOException {
        this.name = name;
        this.header = header;
        file = new File(name);
        if (file.createNewFile()) {
            System.out.println("File bani: " + name);
        } else {
            System.out.println("File pehle se hai: " + name);
        }
        if (file.length() == 0) {
            FileWriter writer = new FileWriter(file);
            writer.write(header + "\n");
            writer.close();
        }
    }
    public void write(String data, boolean append) throws IOException {
        FileWriter writer = new FileWriter(file, append);
        writer.write(data + "\n");
        writer.close();
    }
    public String read() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
class Bank {
    private String name;
    private ArrayList<Client> clList;
    private ArrayList<Account> acList;
    private File_Handling acc_file;
    private File_Handling cln_file;
    private File_Handling bank_file;

    public Bank(String name) throws IOException {
        this.name = name;
        clList = new ArrayList<>();
        acList = new ArrayList<>();
        acc_file  = new File_Handling("accs_data.csv",  "ACC No,Amount,Holder Name");
        cln_file  = new File_Handling("cln_data.csv",   "ID,Name,CNIC,Phone");
        bank_file = new File_Handling("bank_info.txt",  "Bank,Accounts,Clients,Balance");
    }
    public String getName() { return name; }
    public ArrayList<Client> getClList() { return clList; }
    public ArrayList<Account> getAcList() { return acList; }

    public Client addClient(Person p) throws IOException {
        Client c = new Client(p);
        clList.add(c);
        cln_file.write(c.toString(), true);
        return c;
    }
    public Account addAccount(String id, float amount, Client c) throws IOException {
        Account a = new Account(amount, c);
        c.addAccount(a);
        acc_file.write(a.toString(), true);
        acList.add(a);
        return a;
    }
    public Account searchAccount(String id) {
        for (Account a : acList) {
            if (a.getNumber().equals(id)) return a;
        }
        return null;
    }
    public boolean removeClient(String id) {
        Client target = null;
        for (Client c : clList) {
            if (c.getId().equals(id)) { target = c; break; }
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
        for (Account a : acList) total += a.getAmount();
        return total;
    }
    public Client searchCustomerDetail(String cnic) {
        for (Client c : clList) {
            if (c.getPersonDetails().getCnic().equals(cnic)) return c;
        }
        return null;
    }
    public String showInfo() throws IOException {
        String data = "Name ; "+name + "\nTotal Accs; " + acList.size() + "\nTotal Clients; " + clList.size() + "\nTotal Balance; " + totalAmount();
        bank_file.write(data, false);
        return bank_file.read();
    }
    public String toString() {
        String data = "Bank: " + name + "\n";
        for (Client c : clList) data += c.toString() + "\n";
        return data;
    }
}

class BankSystem extends JFrame {
    Bank bank;
    JTextArea outputArea;
    public BankSystem() {
        try {
            bank = new Bank("UET Bank");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

        setTitle("Bank Management System");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ---- Top label ----
        JLabel title = new JLabel("UET Bank Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ---- Button Panel (left side) ----
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(10, 1, 5, 5));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAddClient    = new JButton("Add Client");
        JButton btnAddAccount   = new JButton("Add Account");
        JButton btnDeposit      = new JButton("Deposit");
        JButton btnWithdraw     = new JButton("Withdraw");
        JButton btnSearchAcc    = new JButton("Search Account");
        JButton btnSearchCnic   = new JButton("Search by CNIC");
        JButton btnRemoveClient = new JButton("Remove Client");
        JButton btnBankInfo     = new JButton("Bank Info");
        JButton btnAllClients   = new JButton("All Clients");
        JButton btnClear        = new JButton("Clear Output");

        btnPanel.add(btnAddClient);
        btnPanel.add(btnAddAccount);
        btnPanel.add(btnDeposit);
        btnPanel.add(btnWithdraw);
        btnPanel.add(btnSearchAcc);
        btnPanel.add(btnSearchCnic);
        btnPanel.add(btnRemoveClient);
        btnPanel.add(btnBankInfo);
        btnPanel.add(btnAllClients);
        btnPanel.add(btnClear);

        add(btnPanel, BorderLayout.WEST);

        // ---- Output Area (right side) ----
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setText("Welcome! Choose any Service.");
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        add(scrollPane, BorderLayout.CENTER);

        // ---- Button Actions ----

        // 1. Add Client
        btnAddClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name  = JOptionPane.showInputDialog("Client's Name:");
                if (name == null || name.trim().isEmpty()) { show("Field can't be empty!"); return; }
                String cnic  = JOptionPane.showInputDialog("CNIC:");
                if (cnic == null || cnic.trim().isEmpty()) { show("Field can't be empty!"); return; }
                String phone = JOptionPane.showInputDialog("Phone Number:");
                if (phone == null || phone.trim().isEmpty()) { show("Field can't be empty!"); return; }

                if (bank.searchCustomerDetail(cnic) != null) {
                    show("Client already exists!"); return;
                }
                try {
                    Person p = new Person(name, cnic, phone);
                    Client c = bank.addClient(p);
                    show("Client added!\nID: " + c.getId() + "\nNaam: " + name + "\nCNIC: " + cnic + "\nPhone: " + phone + "\n(cln_data.csv mein save ho gaya)");
                } catch (IOException ex) {
                    show("File error: " + ex.getMessage());
                }
            }
        });

        // 2. Add Account
        btnAddAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cid = JOptionPane.showInputDialog("Client ID (i.e CLXX) :");
                if (cid == null || cid.trim().isEmpty()) { show("Client ID is necessary!"); return; }

                Client target = null;
                for (Client c : bank.getClList()) {
                    if (c.getId().equals(cid)) { target = c; break; }
                }
                if (target == null) { show("Client not Found!: " + cid); return; }

                String amtStr = JOptionPane.showInputDialog("Starting amount:");
                float amt;
                try { amt = Float.parseFloat(amtStr); }
                catch (Exception ex) { show("Invalid Amount!"); return; }

                try {
                    Account a = bank.addAccount(cid, amt, target);
                    show("Account Created!\nACC No: " + a.getNumber() + "\nAmount: " + amt + "\nClient: " + target.getPersonDetails().getName() + "\n(accs_data.csv mein save ho gaya)");
                } catch (IOException ex) {
                    show("File error: " + ex.getMessage());
                }
            }
        });

        // 3. Deposit
        btnDeposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accNo = JOptionPane.showInputDialog("Account Number (i.e ACCXXXX):");
                if (accNo == null || accNo.trim().isEmpty()) { show("Account Number is necessary!"); return; }

                Account acc = bank.searchAccount(accNo);
                if (acc == null) { show("Account not found: " + accNo); return; }

                String amtStr = JOptionPane.showInputDialog("Amount to deposit: ");
                float amt;
                try { amt = Float.parseFloat(amtStr); }
                catch (Exception ex) { show("Invalid Amount"); return; }

                String result = acc.getAcHolder().deposit(amt, accNo);
                show(result + "\nAccount: " + accNo + "\nHolder: " + acc.getAcHolder().getPersonDetails().getName());
            }
        });

        // 4. Withdraw
        btnWithdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accNo = JOptionPane.showInputDialog("Account Number (i.e ACCXXXX):");
                if (accNo == null || accNo.trim().isEmpty()) { show("Account Number is necessary!"); return; }

                Account acc = bank.searchAccount(accNo);
                if (acc == null) { show("Account not found: " + accNo); return; }

                String amtStr = JOptionPane.showInputDialog("Amount to Withdraw: ");
                float amt;
                try { amt = Float.parseFloat(amtStr); }
                catch (Exception ex) { show("Invalid Amount"); return; }

                String result = acc.getAcHolder().withdraw(amt, accNo);
                show(result + "\nAccount: " + accNo + "\nHolder: " + acc.getAcHolder().getPersonDetails().getName());
            }
        });

        // 5. Search Account
        btnSearchAcc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accNo = JOptionPane.showInputDialog("Account Number: ");
                if (accNo == null) return;
                Account acc = bank.searchAccount(accNo);
                if (acc == null) {
                    show("Account not Found! " + accNo);
                } else {
                    show("Account Fetched!\nACC No: " + acc.getNumber() + "\nBalance: " + acc.getAmount() + "\nHolder: " + acc.getAcHolder().getPersonDetails().getName() + "\nClient ID: " + acc.getAcHolder().getId());
                }
            }
        });

        // 6. Search by CNIC
        btnSearchCnic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cnic = JOptionPane.showInputDialog("CNIC :");
                if (cnic == null) return;
                Client c = bank.searchCustomerDetail(cnic);
                if (c == null) {
                    show("Client not found of CNIC: " + cnic);
                } else {
                    String info = "Client Fetched!\nID: " + c.getId() + "\nName: " + c.getPersonDetails().getName() + "\nCNIC: " + c.getPersonDetails().getCnic() + "\nPhone: " + c.getPersonDetails().getPhoneNo() + "\nTotal Balance: " + c.totalAmount() + "\n\nAccounts:\n";
                    for (Account a : c.getAcList()) {
                        info += "  " + a.getNumber() + " -> Rs." + a.getAmount() + "\n";
                    }
                    show(info);
                }
            }
        });

        // 7. Remove Client
        btnRemoveClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cid = JOptionPane.showInputDialog("Client ID (i.e CLXX):");
                if (cid == null) return;
                int confirm = JOptionPane.showConfirmDialog(null, "Confrim to Delete " + cid + " ?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean done = bank.removeClient(cid);
                    if (done) show("Client Deleted: " + cid);
                    else show("Client not found: " + cid);
                }
            }
        });

        // 8. Bank Info
        btnBankInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String info = null;
                try {
                    info = bank.showInfo();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                show("Bank Info:\n\n" + info);
            }
        });

        // 9. All Clients
        btnAllClients.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (bank.getClList().isEmpty()) {
                    show("No Client");
                    return;
                }
                String info = "All Clients:\n";
                info += "----------------------------\n";
                for (Client c : bank.getClList()) {
                    info += "ID: " + c.getId() + "  Name: " + c.getPersonDetails().getName() + "  Total: Rs." + c.totalAmount() + "\n";
                    for (Account a : c.getAcList()) {
                        info += "   -> " + a.getNumber() + " Rs." + a.getAmount() + "\n";
                    }
                }
                info += "----------------------------\n";
                info += "Bank Total: Rs." + bank.totalAmount();
                show(info);
            }
        });

        // 10. Clear
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
            }
        });

        setVisible(true);
    }

    void show(String msg) {
        outputArea.setText(msg);
    }

    public static void main(String[] args) {
        new BankSystem();
    }
}
