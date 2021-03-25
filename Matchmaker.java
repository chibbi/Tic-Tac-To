import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

class Matchmaker {

    private static DatagramSocket socket;
    private static InetAddress[] playerAddress = new InetAddress[2];
    private static int[] playerPort = new int[2];
    private static int currentPlayer = 0;
    private static byte[] buf = new byte[512];

    public static void main(String[] args) {
        // open new Socket
        try {
            socket = new DatagramSocket(5678);
        } catch (SocketException e) {
            System.out.println("Server couldn't be created");
            System.out.println(e.getMessage());
        }
        System.out.println("Server started");
        while (true) {
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
            // process message
            playerAddress[currentPlayer] = packet.getAddress();
            playerPort[currentPlayer] = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, playerAddress[currentPlayer], playerPort[currentPlayer]);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received Message: " + received);
            if (received.equals("/stopserver")) {
                break;
            }
            // clean buffer
            buf = new byte[512];
            // send message back
            packet.setAddress(playerAddress[currentPlayer]);
            packet.setPort(playerPort[currentPlayer]);
            packet.setData("This is hello from Server".getBytes());
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
}
