import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;
class GameManager {
    private final String[] words = {"apple", "ocean", "dolphin", "planet", "rocket", "castle", "dragon", "forest", "jungle", "bridge", "pencil", "window", "guitar", "camera", "school", "garden", "bottle", "monkey", "turtle", "rabbit", "banana", "pirate", "wizard", "island", "thunder", "diamond", "village", "library", "computer", "keyboard"};
    private int warnings;
    private int guesses;
    private String alp = "abcdefghijklmnopqrstuvwxyz";

    public String guess() {
        Random rand = new Random();
        return words[rand.nextInt(0, words.length)];
    }
    public void restartGame(JFrame frame){
        frame.dispose();
        Main.main(null);
    }

    public void Play(JFrame frame) {

        String word = guess();
        String message = "";

        guesses = (int) (1.5 * word.length());
        warnings = 4;

        char[] org_word = word.toCharArray();
        char[] guessedW = "_".repeat(word.length()).toCharArray();

        JPanel panel1 = new JPanel(null);
        JPanel panel2 = new JPanel();

        panel1.setBackground(Color.darkGray);
        panel2.setBackground(Color.darkGray);
        panel2.setBounds(0, 55, 400, 50);

        // Bottom Buttons
        ImageIcon guess_icon = new ImageIcon("src/assets/btn_Guess.png");
        ImageIcon guess_icon_h = new ImageIcon("src/assets/btn_Guess_h.png");
        ImageIcon back_icon = new ImageIcon("src/assets/btn_back.png");
        ImageIcon back_icon_h= new ImageIcon("src/assets/btn_back_h.png");
        JButton guess_btn = new JButton(guess_icon); //Guess ka btn
        JButton back_btn = new JButton(back_icon); // Back ka btn

        guess_btn.setBorderPainted(false);
        guess_btn.setContentAreaFilled(false);
        guess_btn.setFocusPainted(false);
        guess_btn.setOpaque(false);
        guess_btn.setBounds(140, 200, guess_icon.getIconWidth(), guess_icon.getIconHeight());
        panel1.add(guess_btn);

        back_btn.setBorderPainted(false);
        back_btn.setContentAreaFilled(false);
        back_btn.setFocusPainted(false);
        back_btn.setOpaque(false);
        back_btn.setBounds(140, 200, guess_icon.getIconWidth(), guess_icon.getIconHeight());
        panel1.add(back_btn);
        back_btn.setVisible(false);

        // Top Left
        JLabel guess_l = new JLabel("Guess : " + guesses);
        guess_l.setForeground(Color.WHITE);
        guess_l.setBounds(25, 25, 50, 15);
        guess_l.setFont(new Font("Freeman", Font.PLAIN, 12));
        panel1.add(guess_l);

        // Top Right
        JLabel warning_l = new JLabel("Warnings : " + warnings);
        warning_l.setForeground(Color.WHITE);
        warning_l.setBounds(300, 25, 70, 15);
        warning_l.setFont(new Font("Freeman", Font.PLAIN, 12));
        panel1.add(warning_l);

        // Top Center
        JLabel l = new JLabel("Guess a " + word.length() + " Letter Word");
        l.setForeground(Color.WHITE);
        l.setBounds(136, 33, 400, 20);
        l.setFont(new Font("Freeman", Font.PLAIN, 15));
        panel1.add(l);

        // Output Blocks CENTERED
        ImageIcon icon = new ImageIcon("src/assets/fld_guess.png");
        JLabel[] g_word = new JLabel[word.length()];
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        int x = 106;
        for (int i = 0; i < word.length(); i++) {
            JLabel wt = new JLabel(String.valueOf(guessedW[i]).toUpperCase(), icon, JLabel.CENTER);
            wt.setFont(new Font("Freeman", Font.BOLD, 14));
            wt.setBounds(x, 77, 25, 30);
            wt.setForeground(Color.WHITE);
            wt.setHorizontalTextPosition(JLabel.CENTER);
            wt.setVerticalTextPosition(JLabel.CENTER);
            g_word[i]=wt;
            wt.setIconTextGap(-10);
            panel2.add(wt);
            x += 33;
        }
        panel1.add(panel2);

        // Borderline CENTERED
        ImageIcon icon1 = new ImageIcon("src/assets/line_bdr.png");
        JLabel line = new JLabel(icon1);
        line.setBounds(20, 134, icon1.getIconWidth(), icon1.getIconHeight());
        panel1.add(line);

        // Output Message Bottom
        JLabel msg = new JLabel(message, JLabel.CENTER);
        msg.setForeground(Color.WHITE);
        msg.setBounds(0, 140, 400, 20);
        msg.setFont(new Font("Freeman", Font.PLAIN, 14));
        panel1.add(msg);

        // Alphabets Label
        JLabel alp_left = new JLabel("Letters Left: ", JLabel.CENTER);
        JLabel alp_l = new JLabel(alp.toUpperCase(), JLabel.CENTER);
        alp_left.setForeground(Color.WHITE);
        alp_l.setForeground(Color.WHITE);
        alp_left.setBounds(0, 100, 400, 20);
        alp_l.setBounds(0, 115, 400, 20);
        alp_left.setFont(new Font("Freeman", Font.PLAIN, 12));
        alp_l.setFont(new Font("Freeman", Font.PLAIN, 11));
        panel1.add(alp_left);
        panel1.add(alp_l);

        // Input Box
        ImageIcon icon2 = new ImageIcon("src/assets/fld_Input.png");
        JTextField input = new JTextField(JTextField.CENTER);
        JLabel input_l = new JLabel(icon2, JLabel.CENTER);
        input_l.setBounds(175, 170, icon2.getIconWidth(), icon2.getIconHeight());
        input.setBounds(180, 175, icon2.getIconWidth() - 10, icon2.getIconHeight() - 10);
        input.setHorizontalAlignment(JTextField.CENTER);
        input.setBackground(new Color(33, 33, 33));
        input.setForeground(Color.white);
        input.setFont(new Font("Freeman", Font.PLAIN, 15));
        input.setBorder(null);
        panel1.add(input_l);
        panel1.add(input);

        HashSet<Character> guessed = new HashSet<>();
        JLabel l1 = new JLabel("YOU WIN",JLabel.CENTER);
        l1.setForeground(Color.WHITE);
        l1.setBounds(0, 165, 400, 40);
        l1.setFont(new Font("Freeman", Font.PLAIN, 16));
        l1.setVisible(false);
        panel1.add(l1);

        // Input Btn action Listener
        input.addActionListener(e -> guess_btn.doClick());
        // Guess Btn Action Listener
        guess_btn.addActionListener(e -> {

            String text = input.getText().toLowerCase();
            if (text.isEmpty() || !text.matches("[a-zA-Z]+")) {
                msg.setText("Enter a letter!");
                warnings--;
                warning_l.setText("Warnings : " + warnings);
                return;
            }
            char txt = text.charAt(0);
            alp=alp.replace(txt,' ');
            alp_l.setText(alp.toUpperCase());
            input.setText("");
            if (guessed.contains(txt)) {
                warnings--;
                msg.setText("Already guessed!");
                warning_l.setText("Warnings : " + warnings);

                if (warnings == 0) {
                    guesses--;
                    guess_l.setText("Guess : "+guesses);
                    warnings = 4;
                    msg.setText("No warnings left! Guess lost.");
                }
                return;
            }
            guessed.add(txt);
            boolean found = false;
            for (int y = 0; y < org_word.length; y++) {
                if (org_word[y] == txt && guessedW[y]=='_') {
                    guessedW[y] = txt;
                    g_word[y].setText(String.valueOf(txt).toUpperCase());
                    found = true;
                }
            }
            if (!found) {
                guesses--;
                guess_l.setText("Guess : " + (guesses));
                warning_l.setText("Warnings : " + warnings);
                msg.setText("Wrong Guess!");
            } else {
                msg.setText("Correct!");
            }
            if (String.valueOf(guessedW).equals(word)) {
                input.setVisible(false);
                input_l.setVisible(false);
                guess_btn.setVisible(false);
                l1.setVisible(true);
                l1.setText("YOU WIN!");
                back_btn.setVisible(true);
            }
            if (guesses <= 0) {
                msg.setText("Word: " + word);
                input.setVisible(false);
                input_l.setVisible(false);
                guess_btn.setVisible(false);
                back_btn.setVisible(true);
                l1.setVisible(true);
                l1.setText("YOU LOSE");
                back_btn.setVisible(true);
            }

            panel1.revalidate();
            panel1.repaint();
        });
        guess_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                guess_btn.setIcon(guess_icon_h);
            }
            public void mouseExited(MouseEvent e) {
                guess_btn.setIcon(guess_icon);
            }
        });
        // Back Button Action Listener
        back_btn.addActionListener(e -> restartGame(frame));
        back_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                back_btn.setIcon(back_icon_h);
            }
            public void mouseExited(MouseEvent e) {
                back_btn.setIcon(back_icon);
            }
        });

        frame.add(panel1);
        frame.repaint();
    }
}
public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("HangMan");

        JLabel text = new JLabel("HANGMAN",JLabel.CENTER);
        text.setForeground(Color.white);
        text.setFont(new Font("Bungee",Font.PLAIN,48));
        text.setBounds(0,2,400,150);

        ImageIcon start = new ImageIcon("src/assets/btn_start.png");
        ImageIcon start_h = new ImageIcon("src/assets/btn_start_h.png");
        JButton btn_st = new JButton(start);
        btn_st.setBorderPainted(false);
        btn_st.setContentAreaFilled(false);
        btn_st.setFocusPainted(false);
        btn_st.setOpaque(false);
        btn_st.setBounds(140,145, start.getIconWidth(), start.getIconHeight());

        ImageIcon exit = new ImageIcon("src/assets/btn_quit.png");
        ImageIcon exit_h = new ImageIcon("src/assets/btn_quit_h.png");
        JButton btn_quit = new JButton(exit);
        btn_quit.setBorderPainted(false);
        btn_quit.setContentAreaFilled(false);
        btn_quit.setFocusPainted(false);
        btn_quit.setOpaque(false);
        btn_quit.setBounds(140,200, exit.getIconWidth(), exit.getIconHeight());

        JPanel panel0 = new JPanel();
        panel0.setLayout(null);
        panel0.setBackground(Color.DARK_GRAY);

        panel0.add(btn_st);

        btn_st.addActionListener(e -> {
            panel0.setVisible(false);
            System.out.println("Game Started!");
            GameManager gameManager = new GameManager();
            gameManager.Play(frame);});
        btn_st.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn_st.setIcon(start_h);
            }
            public void mouseExited(MouseEvent e) {
                btn_st.setIcon(start);
            }
        });
        btn_quit.addActionListener(e -> System.exit(0));
        btn_quit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn_quit.setIcon(exit_h);
            }
            public void mouseExited(MouseEvent e) {
                btn_quit.setIcon(exit);
            }
        });

        panel0.add(btn_quit);
        panel0.add(text);

        frame.add(panel0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}