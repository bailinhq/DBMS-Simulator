package Modules;

public class Query {
    //public int type; //1: SELECT - 2: UPDATE - 3:JOIN - 4:DDL
    public double time;
    public QueryType type;
    public Query(QueryType aType, double aTime){
        type = aType;
        time = aTime;
    }

}
