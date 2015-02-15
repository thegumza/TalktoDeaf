package project.se.rest;

import java.util.List;

import project.se.model.Category;
import project.se.model.Place;
import project.se.model.Place_Detail;
import project.se.model.Vocabulary;
import project.se.model.VocabularyDetail;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by wiwat on 2/12/2015.
 */
public interface ApiService {

    @GET("/place_by_name.php")
    void getPlaceDetailByIdWithCallback(@Query("place_name") String place_name,Callback<List<Place_Detail>> callback);

    @GET("/vocabulary_detail.php")
    void getVocabularyDetailByIdWithCallback(@Query("voc_name") String voc_name,Callback<List<VocabularyDetail>> callback);

    @GET("/vocabulary_by_category.php")
    void getVocabularyByMethodWithCallback(@Query("cat_name")String cat_name,Callback<List<Vocabulary>> callback);

    @GET("/place_detail.php")
    void getPlaceInfoByMethodWithCallback(Callback<List<Place>> callback);

    @GET("/category.php")
    void getCategoryByMethodWithCallback(Callback<List<Category>> callback);

}
