import java.security.PublicKey;
import java.util.PriorityQueue;

public class TransactionalStorageModule extends Module {
    private PriorityQueue<Query> queue;

    public TransactionalStorageModule(){

    }

    @Override
    public double processQuery(Query aQuery) {
        return super.processQuery(aQuery);
    }
}
