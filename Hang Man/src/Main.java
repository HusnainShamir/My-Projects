import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.event.*;
class GameManager {
    private final String[] words = {"apple", "ocean", "dolphin", "robots", "tablets"};
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

        ImageIcon guess_icon = new ImageIcon("src/assets/btn_Guess.png");
        JButton guess_btn = new JButton(guess_icon);
        ImageIcon back_icon = new ImageIcon("src/assets/btn_quit.png");
        JButton back_btn = new JButton(back_icon);

        ImageIcon icon1 = new ImageIcon("src/assets/line_bdr.png");
        ImageIcon icon = new ImageIcon("src/assets/fld_guess.png");
        ImageIcon icon2 = new ImageIcon("src/assets/fld_Input.png");

        JTextField input = new JTextField(JTextField.CENTER);

        JLabel guess_l = new JLabel("Guess : " + guesses);
        JLabel alp_left = new JLabel("Letters Left: ", JLabel.CENTER);
        JLabel alp_l = new JLabel(alp.toUpperCase(), JLabel.CENTER);
        JLabel warning_l = new JLabel("Warnings : " + warnings);
        JLabel l = new JLabel("Guess a " + word.length() + " Letter Word");
        JLabel line = new JLabel(icon1);
        JLabel input_l = new JLabel(icon2, JLabel.CENTER);
        JLabel msg = new JLabel(message, JLabel.CENTER);
        JLabel[] g_word = new JLabel[word.length()];


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


        guess_l.setForeground(Color.WHITE);
        guess_l.setBounds(25, 25, 50, 15);
        guess_l.setFont(new Font("Freeman", Font.PLAIN, 12));
        panel1.add(guess_l);

        warning_l.setForeground(Color.WHITE);
        warning_l.setBounds(300, 25, 70, 15);
        warning_l.setFont(new Font("Freeman", Font.PLAIN, 12));
        panel1.add(warning_l);

        l.setForeground(Color.WHITE);
        l.setBounds(136, 33, 400, 20);
        l.setFont(new Font("Freeman", Font.PLAIN, 15));
        panel1.add(l);

        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        int x = 106;
        int xt = 114;
        for (int i = 0; i < word.length(); i++) {
            //JLabel w = new JLabel(icon);
            JLabel wt = new JLabel(String.valueOf(guessedW[i]).toUpperCase(), icon, JLabel.CENTER);//String.valueOf(guessedW[i]).toUpperCase()
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
        panel2.setBounds(0, 55, 400, 50);
        panel2.setBackground(Color.darkGray);
        panel1.add(panel2);

        alp_left.setForeground(Color.WHITE);
        alp_left.setBounds(0, 100, 400, 20);
        alp_left.setFont(new Font("Freeman", Font.PLAIN, 12));
        panel1.add(alp_left);
        alp_l.setForeground(Color.WHITE);
        alp_l.setBounds(0, 115, 400, 20);
        alp_l.setFont(new Font("Freeman", Font.PLAIN, 11));
        panel1.add(alp_l);

        line.setBounds(20, 134, icon1.getIconWidth(), icon1.getIconHeight());
        panel1.add(line);

        msg.setForeground(Color.WHITE);
        msg.setBounds(0, 140, 400, 20);
        msg.setFont(new Font("Freeman", Font.PLAIN, 14));
        panel1.add(msg);

        input_l.setBounds(175, 170, icon2.getIconWidth(), icon2.getIconHeight());
        input.setBounds(180, 175, icon2.getIconWidth() - 10, icon2.getIconHeight() - 10);
        input.setHorizontalAlignment(JTextField.CENTER);
        input.setBackground(new Color(33, 33, 33));
        input.setForeground(Color.white);
        input.setFont(new Font("Freeman", Font.PLAIN, 15));
        input.setBorder(null);
        panel1.add(input_l);
        panel1.add(input);guess_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
                boolean found = false;
                for (int y = 0; y < org_word.length; y++) {
                    if (org_word[y] == txt) {
                        guessedW[y] = txt;
                        g_word[y].setText(String.valueOf(txt).toUpperCase());
                        found = true;
                    }
                }
                if (!found) {
                    guess_l.setText("Guess : " + (--guesses));
                    warning_l.setText("Warnings : " + warnings);
                    msg.setText("Wrong Guess!");
                } else {
                    msg.setText("Correct!");
                }
                if (String.valueOf(guessedW).equals(word)) {
                    msg.setText("YOU WIN!");
                    input.setVisible(false);
                    guess_btn.setVisible(false);
                    back_btn.setVisible(true);
                }
                if (guesses <= 0 || warnings <= 0) {
                    msg.setText("YOU LOSE! Word: " + word);
                    guess_btn.setVisible(false);
                    back_btn.setVisible(true);
                }

                panel1.revalidate();
                panel1.repaint();
            }
        });
        back_btn.addActionListener(e -> restartGame(frame));

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
        JButton btn_st = new JButton(start);
        btn_st.setBorderPainted(false);
        btn_st.setContentAreaFilled(false);
        btn_st.setFocusPainted(false);
        btn_st.setOpaque(false);
        btn_st.setBounds(140,145, start.getIconWidth(), start.getIconHeight());

        ImageIcon exit = new ImageIcon("src/assets/btn_quit.png");
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

        btn_st.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel0.setVisible(false);
                System.out.println("Game Started!");
                GameManager gameManager = new GameManager();
                gameManager.Play(frame);}
        });
        panel0.add(btn_quit);

        btn_quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel0.add(text);
        frame.add(panel0);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}