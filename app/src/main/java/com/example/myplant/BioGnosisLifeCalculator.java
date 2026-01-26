package com.example.myplant;

public class BioGnosisLifeCalculator {
    private final float idealTemperature;
    private final float toleranceTemperature;
    private final float weightTemperature;
    private final int idealLuminosity;
    private final int toleranceLuminosity;
    private final int weightLuminosity;
    private final int idealHumidity;
    private final int toleranceHumidity;
    private final int weightHumidity;

    private float tempScore;
    private float lumScore;
    private float humScore;

    private float life;

    public BioGnosisLifeCalculator(float idealTemperature, float toleranceTemperature, float weightTemperature,
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

        this.life = calculateLife(weightTemperature, weightLuminosity, weightHumidity);
    }

    public float getIdealTemperature() {
        return idealTemperature;
    }

    public float getToleranceTemperature() {
        return toleranceTemperature;
    }

    public float getWeightTemperature() {
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

    public float getHumScore() {
        return humScore;
    }

    public float getLumScore() {
        return lumScore;
    }

    public float getTempScore() {
        return tempScore;
    }

    public float getLife() {
        return life;
    }

    /**
     * Função genérica para calcular o score
     *
     * @param value     Valor atual
     * @param ideal     Valor ideal
     * @param tolerance Tolerância aceitável
     * @return Score normalizado entre 0 e 1
     */
    private float calculateScore(float value, float ideal, float tolerance) {

        float diff = Math.abs(value - ideal);

        if (diff <= tolerance) {
            return 1.0f;
        }

        return (float)Math.exp(-((double) diff - (double) tolerance) / (double)tolerance);
    }

    public float calculateLife(float temperature, float luminosity, float humidity) {

        tempScore = calculateScore(temperature, getIdealTemperature(), getToleranceTemperature());
        lumScore = calculateScore(luminosity, getIdealLuminosity(), getToleranceLuminosity());
        humScore = calculateScore(humidity, getIdealHumidity(), getToleranceHumidity());

        return (tempScore * getWeightTemperature() + lumScore * getWeightLuminosity() + humScore * getWeightHumidity()) /
                (getWeightTemperature() + getWeightLuminosity() + getWeightHumidity()) * 100;
    }
}

