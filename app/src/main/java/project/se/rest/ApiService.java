package project.se.rest;

import java.util.List;

import project.se.model.Book;
import project.se.model.Book_Detail;
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


    @GET("/book_detail_by_name.php")
    void getBookDetailByNameWithCallback(@Query("book_name") String book_name,Callback<Book_Detail> callback);

    @GET("/place_detail_by_name.php")
    void getPlaceDetailByNameWithCallback(@Query("place_name") String place_name,Callback<Place_Detail> callback);

    @GET("/place_info.php")
    void getPlaceInfoByMethodWithCallback(Callback<List<Place>> callback);

    @GET("/book_info.php")
    void getBookInfoByMethodWithCallback(Callback<List<Book>> callback);

    @GET("/vocabulary_detail_by_name.php")
    void getVocabularyDetailByNameWithCallback(@Query("voc_name") String voc_name,Callback<VocabularyDetail> callback);

    @GET("/vocabulary_by_category.php")
    void getVocabularyByMethodWithCallback(@Query("cat_name")String cat_name,Callback<List<Vocabulary>> callback);

    @GET("/search_category.php")
    void getCategoryBySearchWithCallback(@Query("cat_name")String cat_name,Callback<List<Category>> callback);

    @GET("/category.php")
    void getCategoryByMethodWithCallback(Callback<List<Category>> callback);

}
