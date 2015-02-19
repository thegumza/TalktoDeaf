package project.se.model;

/**
 * Created by wiwat on 2/20/2015.
 */
public class Game {

    String voc_name;
    String correct;
    String wrong;

    public Game(String voc_name, String correct, String wrong) {
        this.voc_name = voc_name;
        this.correct = correct;
        this.wrong = wrong;
    }

    public String getVoc_name() {
        return voc_name;
    }

    public String getCorrect() {
        return correct;
    }

    public String getWrong() {
        return wrong;
    }
}
