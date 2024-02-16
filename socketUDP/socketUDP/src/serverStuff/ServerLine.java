package serverStuff;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class ServerLine implements Runnable{

    private String message;
    private InetAddress mitAddr;
    private int mittPort;

    /**
     * costruttore passando il message del Thread
     * @param message
     */
    public ServerLine(String message, InetAddress mitAddr, int mittPort){
        super();
        this.message = message;
        this.mitAddr = mitAddr;
        this.mittPort = mittPort;
    }
    

    /**
     * sends 's' to the connected client
     * @param s
     */
    private void manageOutput(){
        byte[] buffer2 = message.getBytes();
        DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, mitAddr, mittPort);
        try {
            Server.ss.send(packet2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * compito del Thread
     */
    @Override
    public void run() {
        manageOutput();
    }
}
