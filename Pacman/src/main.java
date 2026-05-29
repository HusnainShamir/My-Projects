import java.util.Random;
import java.util.Scanner;
class Board // Handles the Game Board
{
    public char[][] board = {
            "#############################################".toCharArray(),
            "#.................##........................#".toCharArray(),
            "#.#####.#########.##.#########.#####.######.#".toCharArray(),
            "#.#...#.#. . . .#.##.#. . . .#.#...#.....@ .#".toCharArray(),
            "#.# # #.#.#####.#.##.# ##### #.# # #.#.####.#".toCharArray(),
            "#...#...#.....#...........#@.#...#...#....#.#".toCharArray(),
            "#####.#######.###########.######..# #####.#.#".toCharArray(),
            "#.....#@....#.#.........# #. . .#.#@....#.#.#".toCharArray(),
            "#.#########.#.#.#######.#.# ### #.#####.#.#.#".toCharArray(),
            "#.# ..... #.#.#.#.....#.# #.#@..#.....#.#.#.#".toCharArray(),
            "#.# ##### #.#.#.# ###.#.# # ### #####.#.#.#.#".toCharArray(),
            "#...#.@.# ...... .###.#.#.#. . . . . .#.#.. #".toCharArray(),
            "###.#.#.######### ###.....#.######### #.###.#".toCharArray(),
            "#.....#........@#@###...#..........@# #.... #".toCharArray(),
            "#############################################".toCharArray()
  };
//    public char[][] board = {
//            "#############################################".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#       @...............................    #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#                                           #".toCharArray(),
//            "#############################################".toCharArray()
//    };
    public int n = board.length-1;
    public int m = board[0].length-1;
    public void drawBoard()// Draws the board on Screen
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                sb.append(board[i][j]);
            }
            sb.append("\n");
        }
        System.out.print(sb);
        System.out.println("\nLives:\n\t"+"O".repeat(Player.lives+1)+"\nScore : "+Player.Score+"\n"+Player.msg);
    }
    public void setPlayer(int y,int x)  //Sets Player Position on Board
    {
        board[y][x]='P';
    }
    public void setGhost(int y,int x) // Sets Ghosts on Board
    {
        board[y][x]='G';
    }
}
class Player // Control Players
{
    private int x;
    private int y;
    int xi;
    int yi;
    public static int Score = 0;
    public static int lives = 2;
    public static String msg = " ";
    static volatile char d = ' ';
    static volatile boolean running=true;

    Player(int y, int x) {
        this.x = x;
        this.y = y;
        xi=x;
        yi=y;
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public static void InputThread() // Handles the Input at Background
    {
        Scanner sc = new Scanner(System.in);

        new Thread(() -> {
            while (running) {
                String input = sc.next();
                if (!input.isEmpty()) {
                    d = Character.toUpperCase(input.charAt(0));
                }
                System.out.println();
            }
        }).start();
    }

    public boolean move(Board Board, Ghost g1, Ghost g2, Ghost g3) {
        int x_ = x;
        int y_ = y;

        if (d == 'W') y_--;
        else if (d == 'S') y_++;
        else if (d == 'A') x_--;
        else if (d == 'D') x_++;

        if ((x_ == g1.getX() && y_ == g1.getY()) ||
                (x_ == g2.getX() && y_ == g2.getY()) ||
                (x_ == g3.getX() && y_ == g3.getY())) {
            return true;
        }
        if (x_ >= 0 && x_ < Board.board[0].length && y_ >= 0 && y_ < Board.board.length
                && Board.board[y_][x_] != '#') {

            Board.board[y][x] = ' ';
            x = x_;
            y = y_;

            if (Board.board[y][x] == '.') Score++;
            else if (Board.board[y][x] == '@') Score += 3;
            Player.msg=" ";
            Board.setPlayer(y, x);
        }
        else if (Board.board[y_][x_] == '#'){
            Player.msg="Hurdle! Can't Move";
        }
//        if (d != ' ') {
//            d = ' ';
//        } Single Movement
        return false;
    }
    public static void reset() {
        running = true;
        Score = 0;
        lives = 2;
        msg = " ";
        d = ' ';
    }
}
class Ghost // Controls Ghosts
{
    Random rand = new Random();
    private final char[] moves ={'W','S','A','D'};
    private int x;
    private int y;
    private char[] _c={' ',' '};
    Ghost(int x,int y){
        this.x=x;
        this.y=y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void move(Board Board){
        while(true){
            int nx = x;
            int ny = y;

            char c = moves[rand.nextInt(4)];
            if (c =='W') ny--;
            else if (c =='S') ny++;
            else if (c =='A') nx--;
            else if (c =='D') nx++;

            if (ny >= 0 && ny < Board.board.length && nx >= 0 && nx < Board.board[0].length && Board.board[ny][nx] != '#' && Board.board[ny][nx] != 'G'){
                _c[1] = Board.board[ny][nx];
                Board.board[y][x]= _c[0];
                _c[0]=_c[1];
                x= nx;
                y= ny;
                Board.setGhost(y,x);
                break;
            }
        }
    }
}
class Game_Manager // Manages the Game Control
{
    Board Board =new Board();
    Player p;
    Ghost g1,g2,g3;
    static boolean win = false;

    Game_Manager() {
        this.p = new Player(13,43);
        this.g1 = new Ghost(1,1);
        this.g2 = new Ghost(1,13);
        this.g3 = new Ghost(43,1);
    }

    public void update() throws InterruptedException {

        if (p.move(Board, g1, g2, g3)) {
            handleDeath();
            return;
        }
        g1.move(Board);
        if (checkGhostHit(g1)) {
            handleDeath();
            return;
        }
        g2.move(Board);
        if (checkGhostHit(g2)) {
            handleDeath();
            return;
        }
        g3.move(Board);
        if (checkGhostHit(g3)) {
            handleDeath();
            return;
        }
        check_win(Board);
    }
    public void renderer() {
        System.out.print("\033\033");
        System.out.flush();
        System.out.println();
        clean();
        Board.drawBoard();
    }
    public void check_win(Board Board) {
        int e_count = 0;
        for (int i = 0; i < Board.board.length; i++) {
            for (int j = 0; j < Board.board[0].length; j++) {
                if (Board.board[i][j] == '.' || Board.board[i][j] == '@') {
                    e_count += 1;
                }
            }
        }
        if (e_count == 0) {
            win = true;
            Player.running = false;
        }
    }
    public void clean() {
        for (int i = 0; i < Board.board.length; i++) {
            for (int j = 0; j < Board.board[0].length; j++) {
                if (Board.board[i][j] == 'P' && (j != p.getX() || i != p.getY())) {
                    Board.board[i][j] = ' ';
                }
            }
        }
    }
    public boolean checkGhostHit(Ghost g) {
        return p.getX() == g.getX() && p.getY() == g.getY();
    }

    public void handleDeath() throws InterruptedException {
        if (Player.lives > 0) {
            Player.lives--;
            Player.msg = "Ghost Captured You!";
            Thread.sleep(500);
            p.setX(p.xi);
            p.setY(p.yi);
            Board.setPlayer(p.getY(), p.getX());
        } else {
            Player.running = false;
        }
    }
}
public class main // Main
{
    static Scanner sc = new Scanner(System.in);
    static boolean inputStarted = false;
    static boolean inGame=false;
    static int v=300;

    public static void main(String[] args) throws InterruptedException {

        while(true) {

            if (!inGame) {
                char c = MainMenu();

                switch(c) {
                    case '1':{
                        inGame = true;
                        GameStart();
                        inGame = false;
                    }
                    case '2':{
                        if(v-50>=150) v-=50;
                        break;
                    }
                    case '3':{
                        if(v+50<=400) v+=50;
                        break;
                    }
                    case '4':{
                        System.out.println("Exiting");
                        return;
                    }
                }
            }
        }
    }
    public static char MainMenu() {
        System.out.println("=".repeat(20)+"\n\t  PACMAN\t\n"+"=".repeat(20));
        System.out.print("1) START\n2) Increase Difficulty\n3) Decrease Difficulty\n4) EXIT\n>");
        char c=' ';
        String input = sc.nextLine();
        if (!input.isEmpty()) {
            c = Character.toUpperCase(input.charAt(0));
        }
        return c;
    }
    public static void GameStart() throws InterruptedException {

        Player.reset();
        Game_Manager gameManager = new Game_Manager();

        if (!inputStarted) {
            Player.InputThread();
            inputStarted = true;
        }

        while(Player.running){
            gameManager.update();
            gameManager.renderer();
            Thread.sleep(v);
        }
        if(Game_Manager.win){
            System.out.println("YOU WIN!");
        } else {
            System.out.println("YOU LOSE!");
        }

        Thread.sleep(1000);
    }
}
