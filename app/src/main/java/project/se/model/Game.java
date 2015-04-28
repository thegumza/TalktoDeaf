package project.se.model;

/**
 * Created by wiwat on 2/20/2015.
 */
public class Game {

    String vid_name;
    String correct;
    String wrong;
    String eng_correct;
    String eng_wrong;

    public Game(String vid_name, String correct, String wrong, String eng_correct, String eng_wrong) {
        this.vid_name = vid_name;
        this.correct = correct;
        this.wrong = wrong;
        this.eng_correct = eng_correct;
        this.eng_wrong = eng_wrong;
    }

    public String getEng_correct() {
        return eng_correct;
    }

    public String getEng_wrong() {
        return eng_wrong;
    }

    public String getVid_name() {
        return vid_name;
    }

    public String getCorrect() {
        return correct;
    }

    public String getWrong() {
        return wrong;
    }
}
