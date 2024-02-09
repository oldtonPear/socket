import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class Server {
    
    private DatagramSocket ss;
    private Socket cs;
    private byte[] buffer1;
    private DatagramPacket packet1;
    String messaggio;

    private HashMap<String, String> resources;

    Server(){
        resources = new HashMap<>();
        initResources();  
        try {
            ss = new DatagramSocket(1069);
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitForConnession();
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
     * waits until a connection is established
     */
    private void waitForConnession(){
        try {
            buffer1 = new byte[65536];
            packet1 = new DatagramPacket(buffer1, buffer1.length);
            ss.receive(packet1);
            System.out.println("connession succesful!");
            online();
        }
        catch(SocketException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
            
    }

    /**
     * sends 's' to the connected client
     * @param s
     */
    private void manageOutput(String s){
        byte[] buffer2 = messaggio.getBytes();
        InetAddress mitAddr = packet1.getAddress();
        int mittPort = packet1.getPort();
        DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, mitAddr, mittPort);
        try {
            ss.send(packet2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * listens for someone to send a message
     * @return "-1" if there is no message
     * @return "-2" if there is no user with the specified id
     * @return "-3" if the connection has been closed
     * @return "the String linked to the id" if there is an user with the specified id
     */
    private String manageInput(){
        String s = "";
        byte[] data = packet1.getData();
        messaggio = new String(data, 0, packet1.getLength());
        if(s.equals("")) return "-1";
        else if(resources.get(s) == null) return "-2";
        return s;
    }

    /**
     * continues to listen and processed data when needed
     * eventually calling manageOutput
     */
    public void online(){
        System.out.println("online!");
        while(!cs.isClosed()){

            String input = manageInput();
            
            if(input.equals("-3")) break;

            if(input.equals("-2")){
                manageOutput("null");
            }
            
            else if(!input.equals("-1")){
                System.out.println("Messaggio ricevuto!");
                manageOutput(resources.get(input));
            }
        }
        dispose();
    }

    /**
     * disposes resorces
    */
    private void dispose(){
        try { 
            System.out.println("Chiudo connessione!");
            cs.close();
            cs = null;
            waitForConnession();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
