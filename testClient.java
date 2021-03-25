import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

class testClient {

    private static DatagramSocket socket;
    private static InetAddress address;

    public static void main(String[] args) {
        System.out.println("Client started");
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException eu) {
            System.out.println("Couldn't find localhosts address");
            System.out.println(eu.getMessage());
        } catch (SocketException es) {
            System.out.println("Couldn't connect to Server");
            System.out.println(es.getMessage());
        }
        while (true) {
            // clean buffer
            byte[] buf = new byte[512];
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = "";
            try {
                input = reader.readLine();
                if (input == "") {
                    input = reader.readLine();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Read input (" + input + ")");
            if (input.equals("/stopclient")) {
                break;
            }
            buf = input.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 5678);
            try {
                socket.send(packet);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            System.out.println("Send message: " + new String(packet.getData(), 0, packet.getLength()));
            // clean buffer
            buf = new byte[512];
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received Package message: " + received);
        }
        System.out.println("Server stopped");
        socket.close();
    }
}
