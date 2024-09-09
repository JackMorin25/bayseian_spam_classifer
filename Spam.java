import java.util.HashMap;

public class Spam {
    /**
     * variables
     */
    private HashMap<String, Double> spamWords;
    private double numMailSpam;
    private int numCorrect;
    private int numTests = 0;

    /**
     * Constructor
     */
    public Spam(){

        spamWords = new HashMap<>();
        numMailSpam = 0;

    }

    /**
     * Methods
     */
    public HashMap<String, Double> getSpamWords() {
        return spamWords;
    }

    public double getNumMailSpam() {
        return numMailSpam;
    }

    public void setNumMailSpam(double numMailSpam) {
        this.numMailSpam = numMailSpam;
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
