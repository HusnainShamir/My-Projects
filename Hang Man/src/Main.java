import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
class GameManager{
    private final String[] words = {"apple","ocean","dolphin","Robots"};
    private String alp = "abcdefghijklmnopqrstuvwxyz";
    private int guesses;
    private int warnings;

    public String guess(){
        Random rand = new Random();
        return words[rand.nextInt(0,words.length-1)];
    }
    public String getInput(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Take a Guess : ");
        String input = sc.next();
        if(!input.isEmpty()){
            input = String.valueOf(Character.toLowerCase(input.charAt(0)));
        }
        return input;
    }
    public void print(char[] arr){
        for(char x:arr){
            System.out.print(x);
        }
        System.out.println();
    }
    public static boolean check(char[] array,char ch) {
        for (char c : array) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }
    public void Play(JFrame frame) {

        String word = guess();
        var ref = new Object() {
            int guesses = (int) (1.5 * word.length());

            int warnings = 4;
        };

        char[] guessedW = "_".repeat(word.length()).toCharArray();
        StringBuilder alp = new StringBuilder("abcdefghijklmnopqrstuvwxyz");

        JPanel panel1 = new JPanel(null);
        panel1.setBackground(Color.darkGray);

        JLabel infoLabel = new JLabel("I am thinking of a " + word.length() + " letter word");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        panel1.add(infoLabel);

        JPanel wordPanel = new JPanel();
        wordPanel.setBackground(Color.darkGray);

    }
}
public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("HangMan");

        JButton btn_st = new JButton("Start");
        JButton btn_exit = new JButton("Exit");

        JPanel panel0 = new JPanel();
        panel0.setLayout(null);
        panel0.setBackground(Color.DARK_GRAY);

        btn_st.setBounds(100, 80, 200, 40);
        panel0.add(btn_st);

        btn_st.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel0.setVisible(false);
                System.out.println("Game Started!");
                GameManager gameManager = new GameManager();
                gameManager.Play(frame);
            }
        });

        // Exit Button
        btn_exit.setBounds(100, 140, 200, 40);
        panel0.add(btn_exit);

        btn_exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.add(panel0);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}