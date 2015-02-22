package project.se.model;

/**
 * Created by wiwat on 2/20/2015.
 */
public class Game {

    String vid_name;
    String correct;
    String wrong;

    public Game(String vid_name, String correct, String wrong) {
        this.vid_name = vid_name;
        this.correct = correct;
        this.wrong = wrong;
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
