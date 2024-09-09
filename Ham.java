import java.util.HashMap;

public class Ham {
    /**
     * Variables
     */
    private HashMap<String, Double> hamWords;
    private double numMailHam;
    private int numCorrect;
    private int numTests = 0;

    /**
     * Constructor
     */
    public Ham(){
        hamWords = new HashMap<>();
        numMailHam = 0;
    }

    /**
     * Methods
     */
    public HashMap<String, Double> getHamWords() {
        return hamWords;
    }

    public double getNumMailHam() {
        return numMailHam;
    }

    public void setNumMailHam(double numMailHam) {
        this.numMailHam = numMailHam;
    }

    public int getNumCorrect() {
        return numCorrect;
    }

    public void setNumCorrect(int numCorrect) {
        this.numCorrect = numCorrect;
    }

    public int getNumTests() {
        return numTests;
    }

    public void setNumTests(int numTests) {
        this.numTests = numTests;
    }
}
