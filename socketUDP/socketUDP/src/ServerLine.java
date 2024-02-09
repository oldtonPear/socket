public class ServerLine implements Runnable{

    private String nome;
    private int index;

    
    /**
     * costruttore passando il nome del Thread
     * @param nome
     */
    public ServerLine(String nome){
        this.nome = nome;
    }

    /**
     * compito del Thread
     */
    @Override
    public void run() {
    }
    
}
