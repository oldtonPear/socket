import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    
    private ServerSocket ss;
    private Socket cs;

    private HashMap<String, String> resources;

    Server(){
        resources = new HashMap<>();
        initResources();  
        waitForConnession();
        
    }

    /**
     * initializes resorces from file
     */
    private void initResources(){
        try {
            BufferedReader bw = new BufferedReader(new FileReader("DatiServer.csv"));
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
            ss = new ServerSocket(1069);
            cs = ss.accept();
            SocketServer.init(cs);
            System.out.println("connession succesful!");
            online();

        } catch (IOException e) {
            e.printStackTrace();
        }
            
    }

    /**
     * sends 's' to the connected client
     * @param s
     */
    private void manageOutput(String s){

        SocketServer.out.write(s + '\n');
    }

    /**
     * listens for someone to send a message
     * @return "-1" if there is no message
     * @return "-2" if there is no user with the specified id
     * @return "0" if there is an user with the specified id
     */
    private String manageInput(){
        try {
            String s = SocketServer.in.readLine();
            if(s== null) return "-1";
            else if(s.equals("")) return "-1";
            else if(resources.get(s) == null) return "-2";
            System.out.println(s);
            
        } catch (IOException e) {
            return "-1";
        }
        return "0";
    }

    /**
     * continues to listen and processed data when needed
     * eventually calling manageOutput
     */
    public void online(){
        System.out.println("online!");
        while(!cs.isClosed()){
            String input = manageInput();
            if(input.equals("0")){
                manageOutput(resources.get(input));
            }
            else if(input.equals("-2")){
                manageOutput("null");
            }
        }
        dispose();
    }

    private void dispose(){
        try { 
            cs.close();
            waitForConnession();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
