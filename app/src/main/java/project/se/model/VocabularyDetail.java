package project.se.model;

/**
 * Created by wiwat on 2/12/2015.
 */
public class VocabularyDetail {
    String voc_name;
    String des_name;
    String vid_name;
    String type_name;
    String img_name;
    String exam;
    String cat_name;
    String eng_voc_name;
    String eng_cat_name;
    String eng_type_name;
    String eng_des_name;
    String eng_exam;

    public VocabularyDetail(String voc_name, String des_name, String vid_name, String type_name, String img_name, String exam, String cat_name, String eng_voc_name, String eng_cat_name, String eng_type_name, String eng_des_name, String eng_exam) {
        this.voc_name = voc_name;
        this.des_name = des_name;
        this.vid_name = vid_name;
        this.type_name = type_name;
        this.img_name = img_name;
        this.exam = exam;
        this.cat_name = cat_name;
        this.eng_voc_name = eng_voc_name;
        this.eng_cat_name = eng_cat_name;
        this.eng_type_name = eng_type_name;
        this.eng_des_name = eng_des_name;
        this.eng_exam = eng_exam;
    }

    public String getVoc_name() {
        return voc_name;
    }

    public String getDes_name() {
        return des_name;
    }

    public String getVid_name() {
        return vid_name;
    }

    public String getType_name() {
        return type_name;
    }

    public String getImg_name() {
        return img_name;
    }

    public String getExam() {
        return exam;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getEng_voc_name() {
        return eng_voc_name;
    }

    public String getEng_cat_name() {
        return eng_cat_name;
    }

    public String getEng_type_name() {
        return eng_type_name;
    }

    public String getEng_des_name() {
        return eng_des_name;
    }

    public String getEng_exam() {
        return eng_exam;
    }

}
