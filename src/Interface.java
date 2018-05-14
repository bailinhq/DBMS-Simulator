public class Interface {
    Application application;
    public Interface(Application anApplication){
        application = anApplication;
    }

    public void showMenu(){

    }
    public void showStatistics(){

    }

    public void showRealTime(Event event){

    }

    public double[] askParameters(){
        double[] parameters = new double[5];
        return parameters;
    }
}
