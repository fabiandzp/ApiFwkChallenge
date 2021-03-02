package themoviedb;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import themoviedb.api.RestfulMoviesApi;
import themoviedb.entities.lists.RateMovie;
import themoviedb.http.HttpMessageSender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MovieRequests {

    private static Properties props;
    private static HttpMessageSender requestSender;
    private static RestfulMoviesApi api;


    @BeforeClass
    public static void createTestEnvironment() {
        props = new Properties();
        try {
            props.load(new FileInputStream("application.properties"));
        } catch (IOException e) {
            System.out.println("Error in File properties Reading");
        }
        requestSender = new HttpMessageSender(props.getProperty("url"));
        api = new RestfulMoviesApi(props.getProperty("url"));
    }

    public String createGuestSession(){
        String api_key = props.getProperty("api_key");
        Response responseGuessSessionId = api.createGessSessionId(api_key);
        String gessSessionId = responseGuessSessionId.then().extract().path("guest_session_id");
        return gessSessionId;
    }

    @Test
    public void getMovieDetails(){
        String api_key = props.getProperty("api_key");

        //Get the latest movie id
        Response responseLastMovieId = api.getLatestMovieId(api_key);
        int movieId = responseLastMovieId.then().extract().path("id");
        System.out.println("CHECK - Latest Movie id is: " + movieId);

        //get Movie Details
        Response responseMovieDetails = api.getMovieDetails(api_key, movieId);
        responseMovieDetails.then().log().body();
        Assert.assertEquals("The status code is different than 200", 200, responseMovieDetails.statusCode());
    }

    @Test
    public void rateMovie(){
        String api_key = props.getProperty("api_key");
        String gSessionId = createGuestSession();

        //Get the latest movie id
        Response responseLastMovieId = api.getLatestMovieId(api_key);
        int movieId = responseLastMovieId.then().extract().path("id");
        System.out.println("CHECK - Latest Movie id is: " + movieId);

        //Rate movie
        RateMovie rateMovie = new RateMovie(1);
        Response responseRateMovie = api.rateMovie(rateMovie, api_key, gSessionId, movieId);
        responseRateMovie.then().log().all();
        Assert.assertEquals("The status code is different than 201", 201, responseRateMovie.statusCode());
    }




}
