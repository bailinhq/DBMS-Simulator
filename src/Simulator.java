public class Simulator {
    private RandomValueGenerator valueGenerator;

    private ClientCommunicationsManagerModule clientCommunicationsManagerModule;
    private ProcessManagerModule processManagerModule;
    private QueryProcessorModule queryProcessorModule;
    private TransactionalStorageModule transactionalStorageModule;


    public Simulator(){
        valueGenerator = new RandomValueGenerator();
    }

    public void run(){

    }

    private Query generateQuery(){
        return new Query(0, 0);
    }

    private void simulate(){
        
    }
}
