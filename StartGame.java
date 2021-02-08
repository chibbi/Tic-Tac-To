import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

class StartGame {

    // int[y][x]
    private static int[][] gameField = new int[3][3];
    private static boolean isFinished = false;

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
            // TODO: Check Finished
            if (isFinished) {
                break;
            }
            turnKi();
            showGame();
            // TODO: Check Finished
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
            String[] splitted = input.split(",");
            // make - 1 an Option (that is just there for not so tech savvy people,
            // because the arrays start at 0, but with - 1 you can
            // count from 1)
            y = Integer.parseInt(splitted[0]) - 1;
            x = Integer.parseInt(splitted[1]) - 1;
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
    }

}