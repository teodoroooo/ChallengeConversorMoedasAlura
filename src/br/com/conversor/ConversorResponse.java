package br.com.conversor;

public class ConversorResponse {
    String result;
    private String base_code;
    private String target_code;
    private double conversion_rate;
    private double conversion_result;


    public double getConversion_rate() {
        return conversion_rate;
    }

    public double getConversion_result() {
        return conversion_result;
    }
}
