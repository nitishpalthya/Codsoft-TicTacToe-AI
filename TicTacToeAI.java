import java.awt.*;
import javax.swing.*;

public class TicTacToeAI extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char[][] board = new char[3][3];
    private char humanPlayer = 'X';
    private char aiPlayer = 'O';
    private JLabel statusLabel;
    private boolean gameOver = false;
    
    public TicTacToeAI() {
        setTitle("Tic-Tac-Toe AI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        
        JPanel gamePanel = new JPanel(new GridLayout(3, 3, 5, 5));
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.WHITE);
                final int row = i, col = j;
                buttons[i][j].addActionListener(e -> handleMove(row, col));
                gamePanel.add(buttons[i][j]);
            }
        }
        
        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel("Your turn (X)");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusPanel.add(statusLabel);
        
        JPanel controlPanel = new JPanel();
        JButton resetBtn = new JButton("New Game");
        resetBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        resetBtn.addActionListener(e -> resetGame());
        controlPanel.add(resetBtn);
        
        add(gamePanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);
        
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void handleMove(int row, int col) {
        if (gameOver || board[row][col] != ' ') return;
        
        board[row][col] = humanPlayer;
        buttons[row][col].setText(String.valueOf(humanPlayer));
        buttons[row][col].setForeground(Color.BLUE);
        
        if (checkWinner(humanPlayer)) {
            statusLabel.setText("You win!");
            gameOver = true;
            highlightWinner(humanPlayer);
            return;
        }
        
        if (isBoardFull()) {
            statusLabel.setText("It's a draw!");
            gameOver = true;
            return;
        }
        
        statusLabel.setText("AI is thinking...");
        Timer timer = new Timer(500, e -> {
            makeAIMove();
            if (checkWinner(aiPlayer)) {
                statusLabel.setText("AI wins!");
                gameOver = true;
                highlightWinner(aiPlayer);
            } else if (isBoardFull()) {
                statusLabel.setText("It's a draw!");
                gameOver = true;
            } else {
                statusLabel.setText("Your turn (X)");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void makeAIMove() {
        int[] bestMove = minimax(board, aiPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE);
        int row = bestMove[0];
        int col = bestMove[1];
        
        board[row][col] = aiPlayer;
        buttons[row][col].setText(String.valueOf(aiPlayer));
        buttons[row][col].setForeground(Color.RED);
    }
    
    private int[] minimax(char[][] state, char player, int alpha, int beta) {
        int[] bestMove = {-1, -1, (player == aiPlayer) ? Integer.MIN_VALUE : Integer.MAX_VALUE};
        
        if (checkWinner(aiPlayer)) {
            bestMove[2] = 10;
            return bestMove;
        }
        if (checkWinner(humanPlayer)) {
            bestMove[2] = -10;
            return bestMove;
        }
        if (isBoardFull()) {
            bestMove[2] = 0;
            return bestMove;
        }
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == ' ') {
                    state[i][j] = player;
                    
                    if (player == aiPlayer) {
                        int[] currentMove = minimax(state, humanPlayer, alpha, beta);
                        if (currentMove[2] > bestMove[2]) {
                            bestMove[0] = i;
                            bestMove[1] = j;
                            bestMove[2] = currentMove[2];
                        }
                        alpha = Math.max(alpha, bestMove[2]);
                    } else {
                        int[] currentMove = minimax(state, aiPlayer, alpha, beta);
                        if (currentMove[2] < bestMove[2]) {
                            bestMove[0] = i;
                            bestMove[1] = j;
                            bestMove[2] = currentMove[2];
                        }
                        beta = Math.min(beta, bestMove[2]);
                    }
                    
                    state[i][j] = ' ';
                    
                    if (beta <= alpha) break;
                }
            }
            if (beta <= alpha) break;
        }
        
        return bestMove;
    }
    
    private boolean checkWinner(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }
    
    private void highlightWinner(char player) {
        Color winColor = new Color(144, 238, 144);
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                buttons[i][0].setBackground(winColor);
                buttons[i][1].setBackground(winColor);
                buttons[i][2].setBackground(winColor);
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                buttons[0][i].setBackground(winColor);
                buttons[1][i].setBackground(winColor);
                buttons[2][i].setBackground(winColor);
            }
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            buttons[0][0].setBackground(winColor);
            buttons[1][1].setBackground(winColor);
            buttons[2][2].setBackground(winColor);
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            buttons[0][2].setBackground(winColor);
            buttons[1][1].setBackground(winColor);
            buttons[2][0].setBackground(winColor);
        }
    }
    
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }
    
    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j].setText("");
                buttons[i][j].setBackground(Color.WHITE);
            }
        }
        gameOver = false;
        statusLabel.setText("Your turn (X)");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToeAI());
    }
}