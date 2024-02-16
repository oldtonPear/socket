package serverStuff;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

public class Server {

    private HashMap<String, String> resources;
    private DatagramPacket packet1;
    protected static DatagramSocket ss;

    /**
     * creates the server initializing the DatagramSocket
     */
    public Server(){

        try {
            Server.ss = new DatagramSocket(1069);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        resources = new HashMap<>();
        initResources();  
        online();
    }

    /**
     * listens for someone to send a message
     * @return "-1" if there is no message
     * @return "-2" if there is no user with the specified id
     * @return "-3" if the connection has been closed
     * @return "the String linked to the id" if there is an user with the specified id
     */
    private String manageInput(){
        byte[] buffer1 = new byte[65536];
        packet1 = new DatagramPacket(buffer1, buffer1.length);
        System.out.println("checking for input");
        try {
            ss.receive(packet1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = new String(packet1.getData(), 0, packet1.getLength());
        System.out.println(s);

        if(s.equals("")) return "-1";
        else if(resources.get(s) == null) return "-2";
        
        return s;
    }

    /**
     * continues to listen and processed data when needed
     * eventually calling initThread
     */
    public void online(){
        System.out.println("online!");
        while(!ss.isClosed()){

            String input = manageInput();
            
            if(input.equals("-3")) break;

            if(input.equals("-2")){
                initThread("null");
            }
            
            else if(!input.equals("-1")){
                System.out.println("Messaggio ricevuto!");
                initThread(resources.get(input));
            }
        }
        dispose();
    }

    /**
     * disposes resorces
    */
    private void dispose(){
        System.out.println("Chiudo connessione!");
        ss = null;
        online();
    }

    /**
     * initializes resorces from file
     */
    private void initResources(){
        try {
            BufferedReader bw = new BufferedReader(new FileReader("NazioniCapitali.csv"));
            String s = bw.readLine();
            String[] arrS;
            while (s != null) {
                arrS = s.split(";");
                if(resources.get(arrS[0]) == null){
                    resources.put(arrS[0], arrS[1]);
                }
                s = bw.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * creates a new Thread to manage the output
     * @param s the message that will be sent
     */
    private void initThread(String s){
        ServerLine sl = new ServerLine(s, packet1.getAddress(), packet1.getPort());
        Thread th1 = new Thread(sl);
        th1.start();
    }
}
