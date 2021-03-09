package themoviedb.steps;

import io.cucumber.java.en.And;
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
import themoviedb.http.HttpMessageSender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.apache.logging.log4j.LogManager.getLogger;

public class StepsAuthentication {

    private static Properties props;
    private static HttpMessageSender requestSender;
    private static RestfulMoviesApi api;
    private static Auth auth;
    private static final Logger log = getLogger(StepsAuthentication.class.getName());

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
        log.info("Environment variables set");
    }

    @Given("I have the api key")
    public void IHaveTheApiKey() {
        createTestEnvironment();
        System.out.println("The API Key is set as: " + auth.getApiKey());
    }

    @When("A user sends a request to the authentication endpoint")
    public void aUserSendsARequestToTheAuthenticationEndpoint() {
        Response response = api.getToken(auth.getApiKey());
        Assert.assertEquals(200, response.statusCode());

        //Validate that token generation was successfully
        boolean success = response.then().extract().path("success");
        Assert.assertTrue("truesss", success);
        response.then().log().body();

        //Store the Token generated
        String requestToken = response.then().extract().path("request_token");
        auth.setRequestToken(requestToken);

    }

    @Then("A new token is generated")
    public void aANewTokenIsGenerated() {
        System.out.println("The Token generated is: " + auth.getRequestToken());
    }

    @And("Token already generated")
    public void tokenAlreadyGenerated() {
        System.out.println("The Actual Token generated is: " + auth.getRequestToken());
    }

    @When("A user sends a request to validate token endpoint")
    public void aUserSendsARequestToValidateTokenEndpoint() {
        String username = auth.getUsername();
        String password = auth.getPassword();
        String requestToken = auth.getRequestToken();
        String apiKey = auth.getApiKey();

        ValidateToken validateToken = new ValidateToken(username, password, requestToken);
        Response response = api.validateToken(validateToken, apiKey);
        response.then().log().body();
        Assert.assertEquals("The status code is different than 200", 200, response.statusCode());
        boolean tokeValidation = response.then().extract().path("success");
        auth.setTokenValidation(tokeValidation);
    }

    @Then("The Token is validated successfully")
    public void theTokenIsValidatedSuccessfully() {
        Assert.assertTrue("Token validation was failed", auth.getTokenValidation());
        System.out.println("The status of Token Validation is: " + auth.getTokenValidation());
    }


    @When("A user sends a request to generate session id endpoint")
    public void aUserSendsARequestToGenerateSessionIdEndpoint() {
        //Create a new session id
        RequestToken requestToken = new RequestToken(auth.getRequestToken());
        Response response = api.sessionId(requestToken, auth.getApiKey());
        response.then().log().body();
        Assert.assertEquals(200, response.statusCode());

        String sessionId = response.then().extract().path("session_id");
        boolean success = response.then().extract().path("success");
        auth.setSessionId(sessionId);
        auth.setSessionValidation(success);
    }

    @Then("The Session Id is generated successfully")
    public void theSessionIdIsGeneratedSuccessfully() {
        Assert.assertEquals("Session Id Generated successfully", true, auth.isSessionValidation());
    }
}

