/*
Jack Morin

I have neither given nor received unauthorized aid on this program.
*/
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;


public class Main {
    /**
     * main
     *
     */
    public static void main(String[] args) {

        Spam spam = new Spam();
        Ham ham = new Ham();

        HashSet<String> vocab = new HashSet<>();

        double totalMail;



        /*
         * gets files for testing and training
         */
        Scanner s = new Scanner(System.in);

        System.out.println("Enter training file for spam: " );

        String spamF = s.next();

        //trains spam
        TrainSpam(spam, spamF, vocab);


        System.out.println("Enter training file for ham: ");

        String hamF = s.next();

        //trains ham
        TrainHam(ham, hamF, vocab);


        totalMail = ham.getNumMailHam() + spam.getNumMailSpam();

        System.out.println("Enter testing file for spam: ");

        String testSpamF = s.next();


        System.out.println("Enter testing file for ham: ");

        String testHamF = s.next();

        //equalize vocabs for spam and ham
        ShareVocab(spam, ham, vocab);


        //tests

        TestSpamOrHam(spam, ham, testSpamF, vocab, totalMail);
        TestSpamOrHam(spam, ham, testHamF, vocab, totalMail);

        int totalCorrect = spam.getNumCorrect() + ham.getNumCorrect();
        int totalTests = spam.getNumTests() + ham.getNumTests();

        System.out.println("Total: " + totalCorrect + "/" + totalTests + " emails classified correctly.");

    }

    /**
        training methods
     */
    public static void TrainSpam(Spam spam, String filename, HashSet<String> vocab){

        //inputs file into code
        InputStream is = Main.class.getResourceAsStream(filename);
        if (is == null) {
            System.err.println("Bad filename");
            System.exit(1);
        }
        Scanner scan = new Scanner(is);

        while(scan.hasNextLine()){

            String line = scan.nextLine();

            //any line that doesn't start with < or blank so should only give words in the emails
            if(!line.startsWith("<") && !line.equals("")) {

                String[] lineArray = line.split(" ");

                for(String s : lineArray){

                    s = s.toLowerCase();

                    //checks any element not in vocab or in ham then adds element into each
                    if(!vocab.contains(s) || !spam.getSpamWords().containsKey(s)){

                        vocab.add(s);
                        spam.getSpamWords().put(s, 1.0);

                    }
                    else{
                        spam.getSpamWords().put(s, spam.getSpamWords().get(s) + 1);
                    }
                }

            }
            //updates at each <SUBJECT> line since it's the start of a new email
            else if(line.startsWith("<SU") && line != null){
                spam.setNumMailSpam(spam.getNumMailSpam() + 1);
            }
        }

    }

    public static void TrainHam(Ham ham, String filename, HashSet<String> vocab) {

        //inputs file into code
        InputStream is = Main.class.getResourceAsStream(filename);
        if (is == null) {
            System.err.println("Bad filename");
            System.exit(1);
        }
        Scanner scan = new Scanner(is);

        while(scan.hasNextLine()){

            String line = scan.nextLine();

            if(!line.startsWith("<") && !line.equals("")) {

                String[] lineArray = line.split(" ");

                for(String s : lineArray){

                    s = s.toLowerCase();


                    //checks any element not in vocab or in ham then adds element into each
                    if(!vocab.contains(s) || !ham.getHamWords().containsKey(s)){

                        vocab.add(s);
                        ham.getHamWords().put(s, 1.0);

                    }
                    else{
                        ham.getHamWords().put(s, ham.getHamWords().get(s) + 1);
                    }
                }

            }
            else if(line.startsWith("<SU") && line != null){
                ham.setNumMailHam(ham.getNumMailHam() + 1);
            }
        }
    }

    public static void ShareVocab(Spam spam, Ham ham, HashSet<String> vocab){

        for(String s : vocab){
            if(!spam.getSpamWords().containsKey(s)){
                spam.getSpamWords().put(s,0.0);
            }
            if(!ham.getHamWords().containsKey(s)){
                ham.getHamWords().put(s, 0.0);
            }
        }

    }

    /**
        testing methods
     */
    public static void TestSpamOrHam(Spam spam, Ham ham, String filename, HashSet<String> vocab, double totalMail){

        int numTest = 0;
        int numCorrect= 0;


        //priors
        double priorSpam = spam.getNumMailSpam() / totalMail;
        double priorHam = ham.getNumMailHam() / totalMail;


        priorSpam = Math.log(priorSpam);
        priorHam = Math.log(priorHam);

        //probabilities
        double spamProb = 0.0;
        double hamProb = 0.0;

        //ham is true spam is false
        String hamOrSpam;

        String rightOrWrong;

        //words in email
        HashSet<String> tempVocab = new HashSet<>();

        //inputs file into code
        InputStream is = Main.class.getResourceAsStream(filename);
        if (is == null) {
            System.err.println("Bad filename");
            System.exit(1);
        }
        Scanner scan = new Scanner(is);

        while(scan.hasNextLine()){

            String line = scan.nextLine();


            if(!line.startsWith("<") && !line.equals("")) {

                String[] lineArray = line.split(" ");

                for(String s : lineArray) {

                    s = s.toLowerCase();

                    if(!tempVocab.contains(s)) {
                        tempVocab.add(s);
                    }

                }

            }
            else if(line.startsWith("<SU") && line != null){
                numTest++;
            }
            else if(line.startsWith("</BO") && line != null) {

                int correctFeatures = 0;

                for(String s : vocab){

                    double tempVal;


                    //checks to see if word is shared between vocab and the vocab of the testing email
                    if(tempVocab.contains(s)){

                        correctFeatures++;

                        //calc prob spam
                        tempVal =  Math.abs(spam.getSpamWords().get(s) + 1) / (spam.getNumMailSpam() + 2);
                        spamProb += Math.log(tempVal);

                        //calc prob ham
                        tempVal =  Math.abs(ham.getHamWords().get(s) + 1) / (ham.getNumMailHam() + 2);
                        hamProb += Math.log(tempVal);

                    }
                    else{
                        //1 - calc prob spam
                        tempVal = Math.abs(1.0 - ((spam.getSpamWords().get(s) + 1) / (spam.getNumMailSpam() + 2)));
                        spamProb += Math.log(tempVal);

                        //1 - calc prob ham
                        tempVal = Math.abs(1.0 - ((ham.getHamWords().get(s) + 1) / (ham.getNumMailHam() + 2)));
                        hamProb += Math.log(tempVal);

                    }

                }

                //adding the ln(priors)
                spamProb += priorSpam;
                hamProb += priorHam;



                //if-statements below for formatting line to be printed
                if(spamProb > hamProb){
                    hamOrSpam = "spam";
                }
                else {
                    hamOrSpam = "ham";
                }

                if(filename.contains(hamOrSpam)){
                    rightOrWrong = "right";
                    numCorrect++;

                    if(hamOrSpam.equals("spam")) {
                        spam.setNumCorrect(numCorrect);
                    }
                    else {
                        ham.setNumCorrect(numCorrect);
                    }
                }
                else {
                    rightOrWrong = "wrong";
                }


                if(filename.contains("spam")){
                    spam.setNumTests(spam.getNumTests() + 1 );
                }
                else {
                    ham.setNumTests(ham.getNumTests() + 1);
                }

                System.out.printf("TEST " + numTest + " " + correctFeatures + "/" + vocab.size() + " features true %.3f" +
                          " %.3f" + " " + hamOrSpam + " " + rightOrWrong + "%n", spamProb, hamProb);


                //reset probs and tempVocab for next test
                spamProb = 0.0;
                hamProb = 0.0;
                tempVocab.clear();
            }


        }
    }

}