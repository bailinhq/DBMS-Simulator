package Modules;

public class RandomValueGenerator {
    RandomValueGenerator(){

    }

    public double generateNormalDistributionValue( double normalMean, double variance ){
        //Metodo de convolucion
        double sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += Math.random();
        }
        sum -= 6;

        return normalMean + (Math.sqrt(variance) * sum);
    }

    public double generateExponentialDistributionValue(double lambda){

        return -(1/lambda)*Math.log10(Math.random());
    }

    public int generateUniformDistributionValue(int a, int b){
        return (int) (a + ((b - a) * Math.random()));
    }

    public double generateDiscreteValues(){
        return Math.random();
    }

}