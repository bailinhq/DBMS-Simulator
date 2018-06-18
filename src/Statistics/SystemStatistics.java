package Statistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SystemStatistics {
    private int discardedNumberOfQuerys;
    private int numberOfQueries;
    private double timeLifeQueries;
    private ArrayList<SimulationStatistics> runsResults;

    public SystemStatistics(){
        this.runsResults = new ArrayList<>();
        discardedNumberOfQuerys = 0;
        timeLifeQueries = 0;
    }

    public void addToList(SimulationStatistics simulationStatistics){
        runsResults.add(simulationStatistics);
    }

    public void generateSystemStatistics(){
        for (int i = 0; i < runsResults.size(); ++i){
            discardedNumberOfQuerys += runsResults.get(i).getDiscardedNumberOfQueries();
            timeLifeQueries += runsResults.get(i).getTimeLifeOfQuery();
        }
        discardedNumberOfQuerys = discardedNumberOfQuerys/runsResults.size();
        timeLifeQueries = timeLifeQueries/runsResults.size();
    }

}
