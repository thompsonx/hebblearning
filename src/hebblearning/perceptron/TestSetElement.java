/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hebblearning.perceptron;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author tom
 */
public class TestSetElement {
    private double[] inputs;
	
    public TestSetElement() {
            this(2);
    }

    public TestSetElement(int inputsSize){
            inputs = new double[inputsSize];
    }

    @XmlElementWrapper(name="inputs")
    @XmlElement(name="value")
    public double[] getInputs() {
            return inputs;
    }

    public void setInputs(double[] inputs) {
            this.inputs = inputs;
    }

    public TestSetElement(double[] inputs) {
            super();
            this.inputs = inputs;
    }
}
