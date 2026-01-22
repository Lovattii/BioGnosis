package com.example.myplant;

public class BioGnosisLifeCalculator {
    private final double idealTemperature;
    private final double toleranceTemperature;
    private final int weightTemperature;
    private final int idealLuminosity;
    private final int toleranceLuminosity;
    private final int weightLuminosity;
    private final int idealHumidity;
    private final int toleranceHumidity;
    private final int weightHumidity;

    private double tempScore;
    private double lumScore;
    private double humScore;

    public BioGnosisLifeCalculator(double idealTemperature, double toleranceTemperature, int weightTemperature,
                       int idealLuminosity, int toleranceLuminosity, int weightLuminosity,
                       int idealHumidity, int toleranceHumidity, int weightHumidity) {

        this.idealTemperature = idealTemperature;
        this.toleranceTemperature = toleranceTemperature;
        this.weightTemperature = weightTemperature;
        this.idealLuminosity = idealLuminosity;
        this.toleranceLuminosity = toleranceLuminosity;
        this.weightLuminosity = weightLuminosity;
        this.idealHumidity = idealHumidity;
        this.toleranceHumidity = toleranceHumidity;
        this.weightHumidity = weightHumidity;
    }

    public double getIdealTemperature() {
        return idealTemperature;
    }

    public double getToleranceTemperature() {
        return toleranceTemperature;
    }

    public int getWeightTemperature() {
        return weightTemperature;
    }

    public int getIdealLuminosity() {
        return idealLuminosity;
    }

    public int getToleranceLuminosity() {
        return toleranceLuminosity;
    }

    public int getWeightLuminosity() {
        return weightLuminosity;
    }

    public int getIdealHumidity() {
        return idealHumidity;
    }

    public int getToleranceHumidity() {
        return toleranceHumidity;
    }

    public int getWeightHumidity() {
        return weightHumidity;
    }

    public double getHumScore() {
        return humScore;
    }

    public double getLumScore() {
        return lumScore;
    }

    public double getTempScore() {
        return tempScore;
    }

    /**
     * Função genérica para calcular o score
     *
     * @param value     Valor atual
     * @param ideal     Valor ideal
     * @param tolerance Tolerância aceitável
     * @return Score normalizado entre 0 e 1
     */
    private double calculateScore(double value, double ideal, double tolerance) {

        double diff = Math.abs(value - ideal);

        if (diff <= tolerance) {
            return 1.0;
        }

        return Math.exp(-(diff - tolerance) / tolerance);
    }

    public double calculateLife(double temperature, double luminosity, double humidity) {

        tempScore = calculateScore(temperature, getIdealTemperature(), getToleranceTemperature());
        lumScore = calculateScore(luminosity, getIdealLuminosity(), getToleranceLuminosity());
        humScore = calculateScore(humidity, getIdealHumidity(), getToleranceHumidity());

        return (tempScore * getWeightTemperature() + lumScore * getWeightLuminosity() + humScore * getWeightHumidity()) /
                (getWeightTemperature() + getWeightLuminosity() + getWeightHumidity()) * 100;
    }
}

