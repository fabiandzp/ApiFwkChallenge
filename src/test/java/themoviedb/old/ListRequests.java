package themoviedb.old;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import themoviedb.api.RestfulMoviesApi;
import themoviedb.entities.movies.MovieDetails;
import themoviedb.entities.lists.MDBList;
import themoviedb.helpers.VariableTools;
import themoviedb.http.HttpMessageSender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ListRequests {

    private static Properties props;
    private static HttpMessageSender requestSender;
    private static RestfulMoviesApi api;
    private static VariableTools variableTools = new VariableTools();


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

    @Test
    public void createList(){
        String api_key = props.getProperty("api_key");
        Response responseSessionId = api.tokenValidationSessionId(api_key);
        String sessionId = responseSessionId.then().extract().path("session_id");
        //java Faker -----

        MDBList mdbList = new MDBList("Fight Club","A Nice Movie","en");
        Response response = api.createList(mdbList, api_key, sessionId);
        response.then().log().body();
        Assert.assertEquals("The status code is different than 201", 201, response.statusCode());
    }



    @Test
    public void addMovie(){
        String api_key = props.getProperty("api_key");
        Response responseSessionId = api.tokenValidationSessionId(api_key);
        String sessionId = responseSessionId.then().extract().path("session_id");

        //Get the latest movie id
        Response responseLastMovieId = api.getLatestMovieId(api_key);
        int movieId = responseLastMovieId.then().extract().path("id");
        System.out.println("CHECK - Latest Movie id is: " + movieId);

        //Create a list
        MDBList mdbList = new MDBList("Fight Club","A Nice Movie","en");
        Response response = api.createList(mdbList, api_key, sessionId);
        int listId = response.then().extract().path("list_id");
        Assert.assertEquals("The status code is different than 201", 201, response.statusCode());

        //Add a movie to the list
        MovieDetails movieDetails = new MovieDetails(movieId);
        Response responseAddMovie = api.addMovieToList(movieDetails, api_key, sessionId, listId);
        responseAddMovie.then().log().body();
        Assert.assertEquals("The status code is different than 201", 201, responseAddMovie.statusCode());
    }


    @Test
    public void clearList(){
        String api_key = props.getProperty("api_key");
        Response responseSessionId = api.tokenValidationSessionId(api_key);
        String session_id = responseSessionId.then().extract().path("session_id");

        //Create a list
        MDBList mdbList = new MDBList("Fight Club","A Nice Movie","en");
        Response response = api.createList(mdbList, api_key, session_id);
        int listId = response.then().extract().path("list_id");
        Assert.assertEquals("The status code is different than 201", 201, response.statusCode());

        //Add movie

        //Clear a List
        Response responseCleatList = api.clearList(api_key, session_id, listId);
        responseCleatList.then().log().all();
        Assert.assertEquals("The status code is different than 201", 201, responseCleatList.statusCode());
    }

    @Test
    public void DeleteList(){
        String api_key = props.getProperty("api_key");
        Response responseSessionId = api.tokenValidationSessionId(api_key);
        String session_id = responseSessionId.then().extract().path("session_id");

        //Create a list
        MDBList mdbList = new MDBList("Fight Club","A Nice Movie","en");
        Response response = api.createList(mdbList, api_key, session_id);
        int listId = response.then().extract().path("list_id");
        Assert.assertEquals("The status code is different than 201", 201, response.statusCode());

        //Add movie

        //Clear list

        //Delete a List
        Response responseDeleteList = api.DeleteList(api_key, session_id, listId);
        responseDeleteList.then().log().all();
        Assert.assertEquals("The status code is different than 201", 500, responseDeleteList.statusCode());
    }



}
