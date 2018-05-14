public abstract class Module {
    private double clockTime;
    private int queueLength;
    private int numberOfServers;
    private int occupability;

    public Module(){

    }

    public double processQuery(Query aQuery){
        return 0.0;
    }
}
