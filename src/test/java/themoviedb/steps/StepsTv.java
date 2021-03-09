package themoviedb.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import themoviedb.api.RestfulMoviesApi;
import themoviedb.entities.authentication.Auth;
import themoviedb.entities.authentication.RequestToken;
import themoviedb.entities.authentication.ValidateToken;
import themoviedb.entities.tv.Tv;
import themoviedb.http.HttpMessageSender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.apache.logging.log4j.LogManager.getLogger;

public class StepsTv {

    private static Properties props;
    private static HttpMessageSender requestSender;
    private static RestfulMoviesApi api;
    private static Auth auth;
    private static Tv tv;
    private static final Logger log = getLogger(StepsAuthentication.class.getName());

    public static void createTestEnvironment() {
        props = new Properties();
        try {
            props.load(new FileInputStream("application.properties"));
        } catch (IOException e) {
            log.debug("Error in File properties Reading");
        }
        requestSender = new HttpMessageSender(props.getProperty("url"));
        api = new RestfulMoviesApi(props.getProperty("url"));
        auth = new Auth(props.getProperty("apiKey"), props.getProperty("username"), props.getProperty("password"));
        tv = new Tv();

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

        boolean success = responseSessionId.then().extract().path("success");
        auth.setSessionId(sessionId);
        auth.setSessionValidation(success);
    }

    @Given("I am already logged at the API")
    public void loginIntoTheAPI(){
        createTestEnvironment();
        log.debug("Login and SessionId Successful: " + auth.getSessionId());
    }


    @When("A user sends a request to get the last Tv show Id")
    public void aUserSendsARequestToGetTheLastTvShowId() {
        Response response = api.getLatestTv(auth.getApiKey());
        Assert.assertEquals("The status code is different than 200", 201, response.statusCode());
        int tvId = response.then().extract().path("id");
        tv.setTvId(tvId);

    }

    @Then("A user sends a request to get the Tv Details")
    public void aUserSendsARequestToGetTheTvDetails() {
        Response response = api.getTvDetails(auth.getApiKey(), tv.getTvId());
        Assert.assertEquals("The status code is different than 200", 200, response.statusCode());
        response.then().log().body();
    }
}

