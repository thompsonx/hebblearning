/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hebblearning.perceptron;

import java.util.Arrays;

/**
 *
 * @author tom
 */
public class TrainSetElement extends TestSetElement {
    private double output;

    public TrainSetElement() {
            this(2);
    }

    public TrainSetElement(int inputsSize) {
            super(inputsSize);
    }

    public double getOutput() {
            return output;
    }

    public void setOutput(double output) {
            this.output = output;
    }

    public TrainSetElement(double[] inputs, double output) {
            super(inputs);
            this.output = output;
    }

    public int getIntOutput(){
            return (int)Math.round(output);
    }

    @Override
    public String toString() {
            return "TrainSetElement [output=" + output + ", getInputs()="
                            + Arrays.toString(getInputs()) + "]";
    }
}
