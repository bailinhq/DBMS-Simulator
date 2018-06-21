package Statistics;

import Modules.QueryType;
import Modules.*;

public class ModuleStatistics {

    private int queueSize;
    private int totalQueueSize;

    private int numberOfSELECT;
    private int numberOfUPDATE;
    private int numberOfJOIN;
    private int numberOfDDL;

    private double timeOfSELECT;
    private double timeOfUPDATE;
    private double timeOfJOIN;
    private double timeOfDDL;

    private QueryType type;

    public ModuleStatistics() {
        this.queueSize = 0;
        this.totalQueueSize = 0;
        this.numberOfSELECT = 0;
        this.numberOfUPDATE = 0;
        this.numberOfJOIN = 0;
        this.numberOfDDL = 0;
        this.timeOfSELECT = 0.0;
        this.timeOfUPDATE = 0.0;
        this.timeOfJOIN = 0.0;
        this.timeOfDDL = 0.0;
    }

    //TODO tengo dudas con este metodo con los parametros que recibe, creo que solo deberia ser el tipo
    public void increaseTimeOfQuery(QueryType query, double inputTime, double outputTime) {
        double timeTemp = outputTime - inputTime;
        switch (query){
            case UPDATE:
                timeOfUPDATE += timeTemp;
                break;
            case DDL:
                timeOfDDL += timeTemp;
                break;
            case JOIN:
                timeOfJOIN += timeTemp;
                break;
            case SELECT:
                timeOfSELECT += timeTemp;
                break;
        }
    }

    public void increaseNumberOfQuery(QueryType query) {
        switch (query){
            case UPDATE:
                ++numberOfUPDATE;
                break;
            case DDL:
                ++numberOfDDL;
                break;
            case JOIN:
                ++numberOfJOIN;
                break;
            case SELECT:
                ++numberOfSELECT;
                break;
        }
    }

    public double getAverageTimeInModuleOfQuery(QueryType query) {
        double timeTemp = 0.0;
        switch (query){
            case UPDATE:
                timeTemp = timeOfUPDATE/numberOfUPDATE;
                break;
            case DDL:
                timeTemp = timeOfDDL/numberOfDDL;
                break;
            case JOIN:
                timeTemp = timeOfJOIN/numberOfJOIN;
                break;
            case SELECT:
                timeTemp = timeOfSELECT/numberOfSELECT;
                break;
        }
        return timeTemp;
    }

    //TODO No entiendo como hacerlo aun
    public double getAverageSizeQueue() {
        if(queueSize ==0)
            return 0;
        return (double)totalQueueSize/queueSize;
    }


    public void increaseTotalQueueSize(int queueSize) {
        this.totalQueueSize += queueSize;
        ++this.queueSize;
    }

    public void updateTotalQueueSize( double sizeQueue){ this.totalQueueSize+= sizeQueue;}

    public int getTotalQueries(){ return this.numberOfDDL+this.numberOfSELECT+this.numberOfJOIN+this.numberOfUPDATE; }

    public void printData() {
        System.out.println("DDL " + numberOfDDL);
        System.out.println("SEL " + numberOfSELECT);
        System.out.println("UPD " + numberOfUPDATE);
        System.out.println("JOIN " + numberOfJOIN);
    }

    public int getNumberOfSELECT() {
        return numberOfSELECT;
    }

    public int getNumberOfUPDATE() {
        return numberOfUPDATE;
    }

    public int getNumberOfJOIN() {
        return numberOfJOIN;
    }

    public int getNumberOfDDL() {
        return numberOfDDL;
    }
}
