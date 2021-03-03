package themoviedb;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import themoviedb.api.RestfulMoviesApi;
import themoviedb.entities.Auth;
import themoviedb.entities.RequestToken;
import themoviedb.entities.ValidateToken;
import themoviedb.http.HttpMessageSender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AuthenticationRequests {

    private static Properties props;
    private static HttpMessageSender requestSender;
    private static RestfulMoviesApi api;
    private static Auth auth;

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
        auth = new Auth(props.getProperty("apiKey"), props.getProperty("username"), props.getProperty("password"));

    }

    @Test
    public void getToken() {
        String api_key = auth.getApiKey();
        Response response = api.getToken(api_key);
        Assert.assertEquals(200, response.statusCode());
        response.then().log().body();

        //Validate that token generation was successfully
        boolean success = response.then().extract().path("success");
        Assert.assertTrue("true", success);

        String request_token = response.then().extract().path("request_token");
        System.out.println("The Token generated is: " + request_token);
    }

    @Test
    public void validateToken() {
        String api_key = props.getProperty("api_key");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        Response responseToken = api.getToken(api_key);
        String request_token = responseToken.then().extract().path("request_token");

        ValidateToken validateToken = new ValidateToken(username, password, request_token);
        Response response = api.validateToken(validateToken, api_key);

        Assert.assertEquals("The status code is different than 200", 200, response.statusCode());
        boolean tokeValidation = response.then().extract().path("success");
        Assert.assertTrue("The token validation was successful", tokeValidation);
        System.out.println("The status of Token Validation is: " + tokeValidation);
    }

    @Test
    public void getSessionId() {
        String api_key = props.getProperty("api_key");

        //Get a new token
        Response responseToken = api.getToken(api_key);
        String request_token = responseToken.then().extract().path("request_token");

        //Validate Token
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        ValidateToken validateToken = new ValidateToken(username, password, request_token);
        api.validateToken(validateToken, api_key);

        //Create a new session id
        RequestToken requestToken = new RequestToken(request_token);
        Response response = api.sessionId(requestToken, api_key);
        Assert.assertEquals(200, response.statusCode());
    }
}
