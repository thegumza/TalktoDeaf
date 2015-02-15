package project.se.model;

/**
 * Created by wiwat on 2/12/2015.
 */
public class Vocabulary {

    String voc_name;
    String vid_name;

    public String getVid_name() {
        return vid_name;
    }

    public Vocabulary(String voc_name, String vid_name) {
        this.voc_name = voc_name;
        this.vid_name = vid_name;
    }

    public String getVoc_name() {
        return voc_name;
    }
}