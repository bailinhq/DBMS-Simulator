package main.java;

public class RandomValueGenerator {

    public RandomValueGenerator(){}

    /**
     * Method that is responsible for generating random values ​​for a normal distribution,
     * using the convolution algorithm.
     * @param normalMean Average of the normal distribution.
     * @param variance variance of the normal distribution.
     * @return A random value for a normal distribution with mean "normal Mean"
     *          and variance "variance"
     */
    public double generateNormalDistributionValue( double normalMean, double variance ){
        double sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += Math.random();
        }
        sum -= 6;
        //transformation of the value to the given mean and variance.
        return normalMean + (Math.sqrt(variance) * sum);
    }

    /**
     * Method for the generation of random values ​​for a random variable with
     * an exponential distribution. Using the inverse generation algorithm.
     * @param lambda arrival rate
     * @return A random value for exponential distribution.
     */
    public double generateExponentialDistributionValue(double lambda){
        return -(1/lambda)*Math.log(Math.random());
    }

    /**
     * Generador de valores aleatorios para una variable aleatoria con distribución
     * uniforme entre [a, b]. Using the inverse generation algorithm.
     * @param a Limit "a"
     * @param b Limit "b"
     * @return A random number for the uniform distribution, in the given interval.
     */
    public int generateUniformDistributionValue(int a, int b){
        return (int) (a + ((b - a) * Math.random()));
    }


}
