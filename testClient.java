import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class testClient {

    private static DatagramSocket socket;
    private static InetAddress address;
    private static Logger logger;

    public static void main(String[] args) {
        logger = Logger.getGlobal();
        setFormatter();
        logger.info("Client started");
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException eu) {
            logger.log(Level.SEVERE, "Couldn't find localhosts address");
            logger.log(Level.SEVERE, eu.getMessage());
        } catch (SocketException es) {
            logger.log(Level.SEVERE, "Couldn't connect to Server");
            logger.log(Level.SEVERE, es.getMessage());
        }
        InputThread input = new InputThread(socket, address, logger);
        OutputThread output = new OutputThread(socket, logger);
        new Thread(input).start();
        new Thread(output).start();
        // TODO: if wanting to close, clsoe (if possible (easily, do it inside of
        // input))
        // and then do this =>
        // logger.info("Server stopped");
        // socket.close();
    }

    // TODO: format that output of logging to read it easier
    private static void setFormatter() {
        Formatter formatter = new Formatter() {

            @Override
            public String format(LogRecord logRecord) {
                return String.format("Level: %s, message: %s", logRecord.getLevel(), logRecord.getMessage());
            }

        };
        Handler handler = new Handler() {

            @Override
            public void close() throws SecurityException {
                // TODO Auto-generated method stub

            }

            @Override
            public void flush() {
                // TODO Auto-generated method stub

            }

            @Override
            public void publish(LogRecord arg0) {
                // TODO Auto-generated method stub

            }

        };
        handler.setFormatter(formatter);
        logger.addHandler(handler);
    }
}

class InputThread extends Thread {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf;
    private DatagramPacket packet;
    private Logger logger;

    InputThread(DatagramSocket socketIn, InetAddress addressIn, Logger loggerIn) {
        this.socket = socketIn;
        this.address = addressIn;
        this.logger = loggerIn;
    }

    public void run() {
        while (true) {
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
                logger.log(Level.SEVERE, "Couldn't read your Input");
                logger.log(Level.SEVERE, e.getLocalizedMessage());
            }
            if (input.equals("/stopclient")) {
                // TODO: Stop the whole Client (i may should just put the output in a thread and
                // leave input in main)
            }
            buf = input.getBytes();
            packet = new DatagramPacket(buf, buf.length, address, 5678);
            try {
                socket.send(packet);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Couldn't send your Message");
                logger.log(Level.SEVERE, e.getLocalizedMessage());
            }
        }
    }
}

class OutputThread extends Thread {
    private DatagramSocket socket;
    private byte[] buf;
    private DatagramPacket packet;
    private Logger logger;

    OutputThread(DatagramSocket socketIn, Logger loggerIn) {
        this.socket = socketIn;
        this.logger = loggerIn;
    }

    public void run() {
        while (true) {
            // clean buffer
            buf = new byte[512];
            packet = new DatagramPacket(buf, buf.length);
            // try reading
            try {
                socket.receive(packet);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Couldn't read Servers Message");
                logger.log(Level.SEVERE, e.getLocalizedMessage());
            }
            String received = new String(packet.getData(), 0, packet.getLength());
            logger.info(received);
        }
    }
}