import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

class StartGameSingle {

    // int[y][x]
    private static int[][] gameField = new int[3][3];
    private static int turn = 0;
    private static boolean isFinished = false;
    private static int winner = 0;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        initializeGame();
        showGame();
        startGame();
    }

    private static void initializeGame() {
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                gameField[i][j] = 0;
            }
        }
        System.out.println("Initialized Game");
    }

    private static void showGame() {
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                if (gameField[i][j] == 0) {
                    System.out.print("   ");
                } else if (gameField[i][j] == 1) {
                    System.out.print(" x ");
                } else if (gameField[i][j] == 2) {
                    System.out.print(" o ");
                }
                if (j + 1 < gameField[i].length) {
                    System.out.print("|");
                }
            }
            System.out.println("");
            if (i + 1 < gameField.length) {
                System.out.println("-----------");
            }
        }
        System.out.println("");
    }

    private static void startGame() {
        while (!isFinished) {
            System.out.println("It is your turn.");
            try {
                turnGameInput();
            } catch (IOException e) {
                System.out.println("Couldn't read your input");
                System.out.println(e.getMessage());
            }
            showGame();
            checkForWin();
            if (isFinished) {
                break;
            }
            turnKi();
            showGame();
            checkForWin();
        }
        if (winner == 1) {
            System.out.println(ANSI_GREEN);
            System.out.println("\nYOU WON (" + winner + ")\n");
            System.out.println(ANSI_WHITE);
        } else if (winner == 2) {
            System.out.println(ANSI_PURPLE);
            System.out.println("\nYOU LOST (" + winner + ")\n");
            System.out.println(ANSI_WHITE);
        } else {
            System.out.println(ANSI_PURPLE);
            System.out.println("\n DRAW (" + winner + ")\n");
            System.out.println(ANSI_WHITE);
        }
    }

    private static void turnKi() {
        boolean isAvailable = false;
        int i = 0;
        int j = 0;
        while (!isAvailable) {
            Random rand = new Random();
            i = rand.nextInt(2 + 1);
            j = rand.nextInt(2 + 1);
            if (gameField[i][j] == 0) {
                isAvailable = true;
            }
        }
        gameField[i][j] = 2;
    }

    private static void checkForWin() {
        System.out.println(ANSI_GREEN);
        if (checkForWinPlayer(1)) {
            isFinished = true;
            System.out.println("Pl");
            winner = 1;
            return;
        }
        if (checkForWinPlayer(2)) {
            isFinished = true;
            System.out.println("KI");
            winner = 2;
            return;
        }
        System.out.println(turn);
        if (turn >= 5) {
            isFinished = true;
            winner = 0;
        }
        System.out.println(ANSI_WHITE);
        winner = 0;
    }

    private static boolean checkForWinPlayer(int myNumber) {
        // horizontal wins
        boolean hasWin = true;
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                if (gameField[i][j] != myNumber) {
                    hasWin = false;
                }
            }
            if (hasWin) {
                System.out.println("Horizontal");
                return true;
            }
        }
        // vertical wins FIXME
        hasWin = true;
        for (int i = 0; i < gameField[0].length; i++) {
            for (int j = 0; j < gameField.length; j++) {
                System.out.printf("gameField[%s][%s] (%s) != %s %n", j, i, gameField[j][i], myNumber);
                if (gameField[j][i] != myNumber) {
                    hasWin = false;
                }
            }
            System.out.printf("\n\n\n");
            if (hasWin) {
                System.out.println("Vertical");
                return true;
            }
        }
        // top left to bottom right wins
        hasWin = true;
        for (int i = 0; i < gameField[0].length; i++) {
            if (gameField[i][i] != myNumber) {
                hasWin = false;
            }
        }
        if (hasWin) {
            System.out.println("top left to bottom right");
            return true;
        }
        // top right to bottom left wins
        hasWin = true;
        int j = 0;
        for (int i = gameField[0].length - 1; i >= 0; i--) {
            if (gameField[i][j] != myNumber) {
                hasWin = false;
            }
            j++;
        }
        if (hasWin) {
            System.out.println("top right to bottom left");
            return true;
        }
        return false;

    }

    private static void turnGameInput() throws IOException {
        // Enter data using BufferReader
        boolean isAvailable = false;
        int x = 0;
        int y = 0;
        while (!isAvailable) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();
            if (input == "") {
                input = reader.readLine();
            }
            String[] splittedInput = input.split(",");
            // make - 1 an Option (that is just there for not so tech savvy people,
            // because the arrays start at 0, but with - 1 you can
            // count from 1)
            y = Integer.parseInt(splittedInput[0]) - 1;
            x = Integer.parseInt(splittedInput[1]) - 1;
            if (x <= 2 && y <= 2) {
                if (gameField[x][y] == 0) {
                    isAvailable = true;
                } else {
                    System.out.println("You have to choose a not already chosen panel.");
                }
            } else {
                System.out.println("You can only choose between 1 and 3.");
                System.out.println("Give your  choosen panel in x and y Coordinates (Example: 2,2).");
            }
        }
        gameField[x][y] = 1;
        turn++;
    }

}
