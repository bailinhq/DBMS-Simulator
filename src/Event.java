public class Event implements Comparable<Event> {
    private Query query;
    private int type;
    private CurrentState state;
    public Event(Query aQuery, int aType, CurrentState aState){
        state = aState;
    }

    private enum CurrentState{
        CLIENT,
        PROCESS,
        QUERY,
        STORAGE,
        EXECUTOR
    }

    @Override
    public int compareTo(Event o){
        return -1;
    }



}
