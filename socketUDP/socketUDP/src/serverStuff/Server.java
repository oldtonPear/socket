package serverStuff;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Server {

    protected static HashMap<String, String> resources;

    public Server(){
        resources = new HashMap<>();
        initResources();  
        
        initThread();
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
    private void initThread(){
        ServerLine sl = new ServerLine("TH1");
        Thread th1 = new Thread(sl);
        th1.start();
    }
}
