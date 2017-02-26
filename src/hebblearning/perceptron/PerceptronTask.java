/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hebblearning.perceptron;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tom
 */
@XmlRootElement
public class PerceptronTask {
    private Perceptron perceptron;
    private List<TrainSetElement> trainSet;
    private List<TestSetElement> testSet;
    private boolean learned = false;
    private int iteration = 0;
    private List<Double> outputs = new ArrayList<>();
    private List<Double> testOutputs = new ArrayList<>();

    
    public List<Double> getOutputs() {
        return outputs;
    }
    
    public List<Double> getTestOutputs() {
        return testOutputs;
    }

    public int getIteration() {
        return iteration;
    }


    public void storeToXML(File file) throws JAXBException{
            JAXBContext context = JAXBContext.newInstance(PerceptronTask.class);
            Marshaller m =  context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(this, file);
    }

    public static PerceptronTask loadFromXML(File file) throws JAXBException{
            JAXBContext context = JAXBContext.newInstance(PerceptronTask.class);
            Unmarshaller m = context.createUnmarshaller();
            return (PerceptronTask)m.unmarshal(file);
    }


    @XmlElement
    public Perceptron getPerceptron() {
            return perceptron;
    }


    public void setPerceptron(Perceptron perceptron) {
            this.perceptron = perceptron;
    }

    public void setTrainSet(List<TrainSetElement> trainSet) {
            this.trainSet = trainSet;
    }

    public  void setTestSet(List<TestSetElement> testSet) {
            this.testSet = testSet;
    }

    @XmlElementWrapper(name="TrainSet")
    @XmlElement(name="element")
    public List<TrainSetElement> getTrainSet() {
            return trainSet;
    }

    @XmlElementWrapper(name="TestSet")
    @XmlElement(name="element")
    public List<TestSetElement> getTestSet() {
            return testSet;
    }

    @Override
    public String toString() {
            return "PerceptronTask [perceptron=" + perceptron + ", trainSet="
                            + trainSet + ", testSet=" + testSet + "]";
    }
    
    public boolean learn()
    {
        if (this.learned == true) return false;
        
        this.outputs.clear();
        Perceptron p = this.perceptron;
        boolean changed = false;
        this.iteration++;
        
        for (TrainSetElement e : this.trainSet)
        {
            List<Double> inputs = new ArrayList<>();
            inputs.add(1.0);
            for (double d : e.getInputs()) inputs.add(d);
            double[] weights = p.getWeights();
            double sum = 0;            
            
            for (int i = 0; i < inputs.size(); i++) 
                sum += weights[i]*inputs.get(i);
            
            double y = this.signum(sum);
            this.outputs.add(y);
            
            double delta = e.getOutput() - y;
            if (delta != 0) changed = true;
//            System.out.printf("y: %f, ye: %f, delta: %f\n", y, e.getOutput(), delta);
            for (int i = 0; i < inputs.size(); i++) 
                weights[i] += p.getLerningRate() * delta * inputs.get(i);
            p.setWeights(weights);
        }
        
//        System.out.println(Boolean.toString(changed));
        
        if (!changed)
        {
            this.learned = true;
        }
        return true;
    }
    
    private double signum(double sum)
    {
        if (sum > 0) return 1;
        else return 0;
    }
}
