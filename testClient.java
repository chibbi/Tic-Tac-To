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
    private static boolean isActive = true;

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
        InputThread input = new InputThread(socket, address);
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (isActive) {
            new Thread(input).start();
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
            System.out.println(received);
        }
        System.out.println("Server stopped");
        socket.close();
    }
}

class InputThread extends Thread {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf;
    private DatagramPacket packet;

    InputThread(DatagramSocket socketIn, InetAddress addressIn) {
        this.socket = socketIn;
        this.address = addressIn;
    }

    public void run() {
        // clean buffer
        buf = new byte[512];
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
        if (input.equals("/stopclient")) {
            // TODO: Stop the whole Client (i may should just put the output in a thread and
            // leave input in main)
        }
        buf = input.getBytes();
        packet = new DatagramPacket(buf, buf.length, address, 5678);
        try {
            socket.send(packet);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
