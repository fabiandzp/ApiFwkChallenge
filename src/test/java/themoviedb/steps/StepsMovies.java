package themoviedb.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import themoviedb.api.RestfulMoviesApi;
import themoviedb.entities.authentication.Auth;
import themoviedb.entities.authentication.RequestToken;
import themoviedb.entities.authentication.ValidateToken;
import themoviedb.entities.lists.List;
import themoviedb.entities.movies.LatestMovie;
import themoviedb.entities.movies.MovieDetails;
import themoviedb.entities.movies.RateMovie;
import themoviedb.http.HttpMessageSender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StepsMovies {

    private static Properties props;
    private static HttpMessageSender requestSender;
    private static RestfulMoviesApi api;
    private static Auth auth;
    private static List list;
    private static MovieDetails movieDetails;
    private static LatestMovie latestMovie;

    public static void createTestEnvironment() {
        props = new Properties();
        try {
            props.load(new FileInputStream("application.properties"));
        } catch (IOException e) {
            System.out.println("Error in File properties Reading");
        }
        requestSender = new HttpMessageSender(props.getProperty("url"));
        api = new RestfulMoviesApi(props.getProperty("url"));
        auth = new Auth(props.getProperty("apiKey"), props.getProperty("username"), props.getProperty("password"));
        list = new List();
        movieDetails = new MovieDetails();
        latestMovie = new LatestMovie();

        Response responseToken = api.getToken(auth.getApiKey());
        String requestToken = responseToken.then().extract().path("request_token");
        auth.setRequestToken(requestToken);

        ValidateToken validateToken = new ValidateToken(auth.getUsername(), auth.getPassword(), requestToken);
        Response responseTokenValidation = api.validateToken(validateToken, auth.getApiKey());
        responseTokenValidation.then().log().body();

        //Realizar validacion de token

        RequestToken reqToken = new RequestToken(auth.getRequestToken());
        Response responseSessionId = api.sessionId(reqToken, auth.getApiKey());
        responseSessionId.then().log().body();
        String sessionId = responseSessionId.then().extract().path("session_id");
        //System.out.println("testtttt" + sessionId);
        boolean success = responseSessionId.then().extract().path("success");
        auth.setSessionId(sessionId);
        auth.setSessionValidation(success);
    }

    @Given("I am already logged into the API")
    public void LoginIntoTheAPI(){
        createTestEnvironment();
        System.out.println("Login and SessionId Successful: " + auth.getSessionId());
    }


    @When("A user sends a request to get the last Movie Id")
    public void aUserSendsARequestToGetTheLastMovieId() {
        Response responseLatestMovie = api.getLatestMovieId(auth.getApiKey());
        Assert.assertEquals("The status code is different than 200", 200, responseLatestMovie.statusCode());
        responseLatestMovie.then().log().body();

        int movieId = responseLatestMovie.then().extract().path("id");
        latestMovie.setLatestMovieId(movieId);
    }

    @Then("A new sends a request to get the Movie Details")
    public void aNewSendsARequestToGetTheMovieDetails() {
        Response responseMovieDetails = api.getMovieDetails(auth.getApiKey(), latestMovie.getLatestMovieId());
        Assert.assertEquals("The status code is different than 200", 200, responseMovieDetails.statusCode());

        responseMovieDetails.then().log().body();
    }

    @Then("The user sends request to Rate the Movie")
    public void theUserSendsRequestToRateTheMovie() {
        Response responseMovieDetails = api.getMovieDetails(auth.getApiKey(), latestMovie.getLatestMovieId());
        Assert.assertEquals("The status code is different than 200", 200, responseMovieDetails.statusCode());

        responseMovieDetails.then().log().body();

        RateMovie rateMovie = new RateMovie(9);
        Response responseRateMovie = api.rateMovie(rateMovie, auth.getApiKey(), auth.getSessionId(), latestMovie.getLatestMovieId());
        responseRateMovie.then().log().body();
        Assert.assertEquals("The status code is different than 201", 201, responseRateMovie.statusCode());

    }



}

