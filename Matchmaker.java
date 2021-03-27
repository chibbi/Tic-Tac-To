import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

class Matchmaker {

    private static DatagramSocket socket;
    private static InetAddress[] playerAddress = new InetAddress[2];
    private static Integer[] playerPort = new Integer[2];
    private static int currentPlayer = 0;
    private static byte[] buf = new byte[512];
    private static boolean isActive = true;

    public static void main(String[] args) {
        // open new Socket
        try {
            socket = new DatagramSocket(5678);
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
            try {
                sendMessage(packet, ("Player " + currentPlayer + " :" + received));
            } catch (IOException e) {
                System.out.println("Server couldn't send Message");
                System.out.println(e.getMessage());
            }
            // TODO: this is not automatic, if i'd change Player size i would have to change
            // this as well
            // please make it so that is not needed (i will forget this for sure xD)
            if (playerAddress[1] != null) {
                System.out.println("Sending Players onto a Game Server");
                // TODO: have a list of all servers, find a free one, give them the IP of that
                // Server
                try {
                    sendMessage(packet, ("New Server:" + "127.0.0.1,6789"));
                } catch (IOException e) {
                    System.out.println("Server couldn't send Message");
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Server stopped");
        socket.close();
    }

    private static void sendMessage(DatagramPacket packet, String message) throws IOException {
        int i = 0;
        System.out.println(arrayToString(playerPort));
        for (InetAddress player : playerAddress) {
            // clean buffer
            buf = new byte[512];
            // send message back
            if (player != null) {
                packet.setAddress(player);
                packet.setPort(playerPort[i]);
                packet.setData(message.getBytes());
                socket.send(packet);
            }
            i++;
        }
    }

    private static void findPlayer(InetAddress inetAddress, int port) {
        // find a Player
        int i = 0;
        for (InetAddress player : playerAddress) {
            System.out.println(port + "==" + playerPort[i]);
            if (inetAddress.equals(player) && port == playerPort[i]) {
                currentPlayer = i;
                return;
            } else if (null == player && null == playerPort[i]) {
                currentPlayer = i;
                return;
            }
            i++;
        }
        switchPlayer();
    }

    private static void switchPlayer() {
        // switch to the next Player (just go through the Array)
        System.out.println(currentPlayer + " == " + playerAddress.length);
        if (currentPlayer == playerAddress.length - 1) {
            currentPlayer = 0;
        } else {
            currentPlayer++;
        }
    }

    private static <T> String arrayToString(T[] array) {
        // so i can see the content of an Array of generic type
        // (string[],Integer[],InetAddress[], ...)
        // helpful for debugging
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
