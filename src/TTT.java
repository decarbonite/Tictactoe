import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game in a very
 * simple GUI window.
 *
 * @author Ahmed Romih
 * @version November 29,2017
 */

public class TTT implements ActionListener
{
    private JPanel labelsPane; //Contains all labels  
    private JPanel board; // 3x3 grid panel
    private JFrame frame;
    private JLabel status; //status of the game
    private JButton[][] xoButton;
    private ImageIcon X,O; //pictures of X and O

    public static final String PLAYER_X = "X"; // player using "X"
    public static final String PLAYER_O = "O"; // player using "O"
    public static final String EMPTY = " ";  // empty cell

    private String player;   // current player (PLAYER_X or PLAYER_O)

    private String winner;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress

    private int numFreeSquares; // number of squares still free
    private int xWin=0; //how many x won    
    private int oWin=0; //how many o won
    private int tie=0;
    private JLabel xwinLbl;
    private JLabel owinLbl;
    private JLabel tieLbl;
    private Container contentPane;


    public static void main(String[] args) {
        TTT game = new TTT();
    }

    /**
     * Constructs a new Tic-Tac-Toe board and sets up the basic
     * JFrame containing a JTextArea in a JScrollPane GUI.
     */
    public TTT()
    {
        frame = new JFrame("TicTacToe");
        contentPane = frame.getContentPane(); // get the content pane so we can put stuff in (some components go straight into the frame)
        contentPane.setLayout(new BorderLayout());

        X = new ImageIcon(getClass().getResource("x.jpg"));
        O = new ImageIcon(getClass().getResource("o.jpg"));

        winner = EMPTY;
        numFreeSquares = 9;
        player = PLAYER_X;

        //3x3 grid panel for the buttons
        board = new JPanel();
        board.setLayout(new GridLayout(3,3));
        board.setPreferredSize(new Dimension(459,459));
        contentPane.add(board,BorderLayout.CENTER);

        //adds labels and buttons and all menu components
        setBoard();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.pack();
        frame.setVisible(true); // make it visible        
        frame.setLocationRelativeTo(null); //to show the gui in the middle of the screen        
    }

    /**
     * Sets the board by adding all 9 buttons and the needed labels 
     */
    public void setBoard(){
        labelsPane = new JPanel();
        labelsPane.setLayout(new BoxLayout(labelsPane, BoxLayout.Y_AXIS));

        status = new JLabel("Game in progress and its X's turn");
        labelsPane.add(status);

        xwinLbl = new JLabel("X won: 0 times");
        labelsPane.add(xwinLbl);

        owinLbl = new JLabel("O won: 0 times");
        labelsPane.add(owinLbl);

        tieLbl = new JLabel("Number of ties: 0");
        labelsPane.add(tieLbl);

        contentPane.add(labelsPane,BorderLayout.SOUTH);//add it to south of XO buttons
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        JMenu fileMenu = new JMenu("Game"); // create a menu
        menubar.add(fileMenu); // and add to our menu bar

        JMenuItem newItem = new JMenuItem("New"); // create a menu item called "New"
        fileMenu.add(newItem); // and add to our menu
        newItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                clearBoard();
            }
        });

        JMenuItem quitItem = new JMenuItem("Quit"); // create a menu item called "Quit"
        fileMenu.add(quitItem); // and add to our menu
        quitItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });

        //Key listeners for CTRL+Q and CTRL+N
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); // to save typing
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));

        //creating buttons
        xoButton = new JButton[3][3];
        for(int i=0; i<3;i++){
            for(int j=0;j<3;j++){
                xoButton[i][j] = new JButton();
                xoButton[i][j].setSize(153,153);
                xoButton[i][j].addActionListener(this);
                board.add(xoButton[i][j]);
            }
        }
    }

    /**
     * Sets everything up for a new game.  Marks all squares in the Tic Tac Toe board as empty,
     * and indicates no winner yet, 9 free squares and the current player is player X.
     */
    private void clearBoard()
    {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                xoButton[i][j].setIcon(null);
                xoButton[i][j].setDisabledIcon(null);
                xoButton[i][j].setEnabled(true);
            }
        }
        winner = EMPTY;
        numFreeSquares = 9;
        player = PLAYER_X;
        status.setText("Game in progress and its X's turn");
    }

    /**
     * Returns true if filling the given square gives us a winner, and false
     * otherwise.
     *
     * @param row of square just set
     * @param col of square just set
     *
     * @return true if we have a winner, false otherwise
     */
    private boolean haveWinner(int row, int col)
    {

        // unless at least 5 squares have been filled, we don't need to go any further
        // (the earliest we can have a winner is after player X's 3rd move).

        if (numFreeSquares>5) return false;

        // Note: We don't need to check all rows, columns, and diagonals, only those
        // that contain the latest filled square.  We know that we have a winner 
        // if all 3 squares are the same, as they can't all be blank (as the latest
        // filled square is one of them).

        // check row "row"

        if ((xoButton[row][0].getIcon() != null) && (xoButton[row][1].getIcon() != null) && (xoButton[row][2].getIcon() != null) && xoButton[row][0].getIcon().equals( xoButton[row][1].getIcon())&&
                (xoButton[row][0].getIcon().equals(xoButton[row][2].getIcon()))) return true;

        // check column "col"
        if ((xoButton[0][col].getIcon() != null) && (xoButton[1][col].getIcon() != null) && (xoButton[2][col].getIcon() != null) && xoButton[0][col].getIcon().equals(xoButton[1][col].getIcon()) &&
                (xoButton[0][col].getIcon().equals(xoButton[2][col].getIcon())))  return true;

        // if row=col check one diagonal
        if (row==col)
            if ((xoButton[0][0].getIcon() != null) && (xoButton[1][1].getIcon() != null) && (xoButton[2][2].getIcon() != null) && xoButton[0][0].getIcon().equals(xoButton[1][1].getIcon()) &&
                    (xoButton[0][0].getIcon().equals(xoButton[2][2].getIcon()))) return true;

        // if row=2-col check other diagonal
        if (row==2-col)
            if ((xoButton[0][2].getIcon() != null) && (xoButton[1][1].getIcon() != null) && (xoButton[2][0].getIcon() != null) && xoButton[0][2].getIcon().equals(xoButton[1][1].getIcon())&&
                    (xoButton[0][2].getIcon().equals(xoButton[2][0].getIcon()))) return true;

        // no winner yet
        return false;
    }

    /**
     * Responds to the listeners and performs an action accordingly
     */
    public void actionPerformed(ActionEvent e)
    {
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(e.getSource() == xoButton[i][j]){
                    //player X plays
                    if(player == PLAYER_X){
                        xoButton[i][j].setIcon(X);
                        xoButton[i][j].setDisabledIcon(X);//to make it not look grayed out when clicked
                        xoButton[i][j].setEnabled(false);
                        numFreeSquares--;
                        //look for a winner
                        if(haveWinner(i,j)){
                            winner = player;
                            status.setText("Game Over, the winner is " + winner);
                            xWin++;
                            if(xWin==1){
                                xwinLbl.setText("X won: "+xWin+" time");
                            }else{
                                xwinLbl.setText("X won: "+xWin+" times");
                            }
                            JOptionPane.showMessageDialog(null, "X has Won!", "We have a WINNER!", JOptionPane.INFORMATION_MESSAGE);
                            //after a winner is declared no one can click on buttons
                            for(int k=0;k<3;k++){
                                for(int z=0;z<3;z++){
                                    xoButton[k][z].setEnabled(false);
                                }
                            }
                        }else{
                            status.setText("Game in progress and its O's turn");
                        }

                        //we dont need to add the tie to the next else statement because X is the last one to play
                        //so it will be always after X's turn 

                        //looking for a tie
                        if(numFreeSquares==0 && winner == EMPTY){
                            tie++;
                            status.setText("Its a tie!");
                            tieLbl.setText("Number of ties: "+tie);
                            JOptionPane.showMessageDialog(null, "Game ended with a Tie!", "It's a tie", JOptionPane.INFORMATION_MESSAGE);
                        }
                        player = PLAYER_O;
                    }else{
                        //player O plays
                        xoButton[i][j].setIcon(O);
                        xoButton[i][j].setDisabledIcon(O);
                        xoButton[i][j].setEnabled(false);
                        numFreeSquares--;
                        if(haveWinner(i,j)){
                            winner = player;
                            status.setText("Game Over, the winner is " + winner);
                            oWin++;
                            if(oWin==1){
                                owinLbl.setText("O won: "+oWin+" time");
                            }else{
                                owinLbl.setText("O won: "+oWin+" times");
                            }
                            JOptionPane.showMessageDialog(null, "O has Won!", "We have a WINNER!", JOptionPane.INFORMATION_MESSAGE);
                            //after a winner is declared no one can click on buttons
                            for(int k=0;k<3;k++){
                                for(int z=0;z<3;z++){
                                    xoButton[k][z].setEnabled(false);
                                }
                            }
                        }else{
                            status.setText("Game in progress and its X's turn");
                        }
                        player = PLAYER_X;
                    }
                }
            }
        }
    }
}