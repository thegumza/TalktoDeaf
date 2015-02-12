package project.se.rest;

import java.util.List;

import project.se.model.Category;
import project.se.model.Vocabulary;
import project.se.model.VocabularyDetail;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by wiwat on 2/12/2015.
 */
public interface ApiService {

    @GET("/vocabulary_detail.php")
    void getVocabularyDetailByIdWithCallback(@Query("voc_name") String voc_name,Callback<List<VocabularyDetail>> callback);

    @GET("/vocabulary.php")
    void getVocabularyByMethodWithCallback(Callback<List<Vocabulary>> callback);

    @GET("/category.php")
    void getCategoryByMethodWithCallback(Callback<List<Category>> callback);

}
