package themoviedb.api;

//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;

import io.restassured.response.Response;
import themoviedb.entities.authentication.RequestToken;
import themoviedb.entities.authentication.ValidateToken;
import themoviedb.entities.lists.LatestMovieId;
import themoviedb.entities.lists.MDBList;
import themoviedb.entities.movies.MovieDetails;
import themoviedb.entities.movies.RateMovie;
import themoviedb.http.HttpMessageSender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

//import static org.apache.logging.log4j.LogManager.getLogger;


public class RestfulMoviesApi {
    private final String url;
    private final HttpMessageSender messageSender;
    private static Properties props;
    //private JsonParser parser = new JsonParser();
    //private static final Logger log = getLogger();

    public RestfulMoviesApi(String url) {
        this.url = url;
        this.messageSender = new HttpMessageSender(url);
    }

    // ### Authentication
    // Get Token
    public Response getToken(String api_key) {
        Response response = messageSender.getRequestToEndpoint("/authentication/token/new?api_key=" + api_key);
        return response;
    }

    // Validate Token
    public Response validateToken(ValidateToken validateToken, String api_key){
        return messageSender.postRequestToEndpoint(validateToken, "/authentication/token/validate_with_login?api_key=" + api_key);
    }


    /*public Token getNewToken() {
        props = new Properties();
        try {
            props.load(new FileInputStream("application.properties"));
        } catch (IOException e) {
            System.out.println("Error in File properties Reading");
        }

        Response response = messageSender.getRequestToEndpoint("/authentication/token/new?api_key=" + props.getProperty("api_key"));
        JsonElement jsonResponse = parser.parse(response.body().asString());

        Token token = new Gson().fromJson(jsonResponse, Token.class);
        System.out.println("This is the Token " + token.getSuccess());
        System.out.println("This is the Token " + token.getExpires_at());
        System.out.println("This is the Token " + token.getRequest_token());
        return token;
    }*/

    // Session ID
    public Response sessionId(RequestToken requestToken, String api_key){
        return messageSender.postRequestToEndpoint(requestToken, "/authentication/session/new?api_key=" + api_key);
    }

    public Response tokenValidationSessionId(String api_key) {
        props = new Properties();
        try {
            props.load(new FileInputStream("application.properties"));
        } catch (IOException e) {
            System.out.println("Error in File properties Reading");
        }

        //Generate a new token
        Response response = getToken(api_key);
        String request_token = response.then().extract().path("request_token");

        //Validate the new token
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        ValidateToken validateToken = new ValidateToken(username, password, request_token);
        validateToken(validateToken, api_key);

        //Generate the session id for create a new list
        RequestToken requestToken = new RequestToken(request_token);
        Response responseSessionId = sessionId(requestToken, api_key);
        return responseSessionId;
    }

    // ### Lists
    // Create List
    public Response createList(MDBList MDBList, String api_key, String sessionId){
        return messageSender.postRequestToEndpoint(MDBList, api_key, sessionId, "/list");
    }

    public Response getLatestMovieId(String api_key) {
        return messageSender.getRequestToEndpoint(api_key, "/movie/latest");
    }

    public Response addMovieToList(MovieDetails movieDetails, String api_key, String sessionId, int listId) {
        return messageSender.postRequestToEndpoint(movieDetails, api_key, sessionId, "/list/"+listId+"/add_item");
    }

    public Response addMovieToList(LatestMovieId latestMovieId, String api_key, String sessionId, int listId) {
        return messageSender.postRequestToEndpoint(latestMovieId, api_key, sessionId, "/list/"+listId+"/add_item");
    }


    public Response clearList(String api_key, String session_id, int listId) {
        return messageSender.postRequestToEndpoint(api_key, session_id, "/list/"+listId+"/clear");
    }

    public Response DeleteList(String api_key, String session_id, int listId) {
        return messageSender.deleteRequestToEndpoint(api_key, session_id, "/list/"+listId);
    }

    public Response createGessSessionId(String api_key) {
        return messageSender.getRequestToEndpoint(api_key, "/authentication/guest_session/new");
    }

    public Response getMovieDetails(String api_key, int movieId) {
        return messageSender.getRequestToEndpoint(api_key, "/movie/"+movieId);
    }

    public Response rateMovie(RateMovie rateMovie, String api_key, String gSessionId, int movieId) {
        return messageSender.postRequestToEndpoint(rateMovie, api_key, gSessionId, "/movie/"+movieId+"/rating");
    }

    public Response getListDetails(int listId, String apiKey) {
        return messageSender.getRequestToEndpoint(apiKey, "/list/" + listId);
    }




}
