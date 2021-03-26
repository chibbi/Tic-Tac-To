import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

class Server {

    private static DatagramSocket socket;
    private static InetAddress[] playerAddress = new InetAddress[2];
    private static int[] playerPort = new int[2];
    private static int currentPlayer = 0;
    private static byte[] buf = new byte[512];
    private static boolean isActive = true;

    public static void main(String[] args) {
        // open new Socket
        try {
            socket = new DatagramSocket(6789);
        } catch (SocketException e) {
            System.out.println("Server couldn't be created");
            System.out.println(e.getMessage());
        }
        System.out.println("Server started");
        while (isActive) {
            // clean buffer
            buf = new byte[512];
            // create packet to store message in
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            // receive message
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.out.println("Server couldn't receive Message");
                System.out.println(e.getMessage());
            }
            // find current Player
            findPlayer(packet.getAddress(), packet.getPort());
            // process message
            playerAddress[currentPlayer] = packet.getAddress();
            playerPort[currentPlayer] = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, playerAddress[currentPlayer], playerPort[currentPlayer]);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received Message: " + received);
            if (received.equals("/stopserver")) {
                isActive = false;
            }
            // clean buffer
            buf = new byte[512];
            // send message back
            packet.setAddress(playerAddress[currentPlayer]);
            packet.setPort(playerPort[currentPlayer]);
            packet.setData(("This is hello from Server to Player " + currentPlayer).getBytes());
            try {
                socket.send(packet);
            } catch (IOException e) {
                System.out.println("Server couldn't send Message");
                System.out.println(e.getMessage());
            }

        }
        System.out.println("Server stopped");
        socket.close();
    }

    private static void findPlayer(InetAddress inetAddress, int port) {
        System.out.println(inetAddress + " == " + playerAddress[currentPlayer]);
        System.out.println(arrayToString(playerAddress));
        if (playerAddress[currentPlayer] == null) {
        } else if (!inetAddress.equals(playerAddress[currentPlayer]) || port != playerPort[currentPlayer]) {
            switchPlayer();
            findPlayer(inetAddress, port);
        } else {
        }
    }

    private static void switchPlayer() {
        System.out.println(currentPlayer + " == " + playerAddress.length);
        if (currentPlayer == playerAddress.length - 1) {
            currentPlayer = 0;
        } else {
            currentPlayer++;
        }
    }

    private static <T> String arrayToString(T[] array) {
        String output = "[";
        for (T single : array) {
            if (single == null) {
                output += "null" + ",";
            } else {
                output += single.toString() + ",";
            }
        }
        output = output.substring(0, output.length() - 1) + "]";
        return output;
    }
}
