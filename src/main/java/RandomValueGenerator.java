package main.java;

public class RandomValueGenerator {
    public RandomValueGenerator(){
    }

    public double generateNormalDistributionValue( double normalMean, double variance ){
        double sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += Math.random();
        }
        sum -= 6;

        return normalMean + (Math.sqrt(variance) * sum);
    }

    public double generateExponentialDistributionValue(double lambda){
        return -(1/lambda)*Math.log(Math.random());
    }

    public int generateUniformDistributionValue(int a, int b){
        return (int) (a + ((b - a) * Math.random()));
    }

    public double generateDiscreteValues(){
        return Math.random();
    }

}
