import java.util.Queue;

public class ProcessManagerModule extends Module {
    Queue<Query> queue;

    public ProcessManagerModule(){

    }

    @Override
    public double processQuery(Query aQuery) {
        return super.processQuery(aQuery);
    }
}
