package themoviedb;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import themoviedb.api.RestfulMoviesApi;
import themoviedb.entities.ValidateToken;
import themoviedb.helpers.VariableTools;
import themoviedb.http.HttpMessageSender;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthenticationLinear {

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
    public void t1_getToken() {
        String api_key = props.getProperty("api_key");
        Response response = api.getToken(api_key);
        Assert.assertEquals(200, response.statusCode());
        response.then().log().body();

        //Validate that token generation was successfully
        boolean success = response.then().extract().path("success");
        Assert.assertTrue("true", success);

        String request_token = response.then().extract().path("request_token");
        System.out.println("The Token generated is: " + request_token);

        variableTools.setRequestToken(request_token);
        System.out.println("Test" + variableTools.getRequestToken());
    }

    @Test
    public void t2_validateToken() {
        String api_key = props.getProperty("api_key");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        String request_token = variableTools.getRequestToken();
        System.out.println("----->>>>>" + request_token);

        ValidateToken validateToken = new ValidateToken(username, password, request_token);
        Response response = api.validateToken(validateToken, api_key);
        response.then().log().all();

        Assert.assertEquals("The status code is different than 200", 200, response.statusCode());
        boolean tokeValidation = response.then().extract().path("success");
        Assert.assertTrue("The token validation was successful", tokeValidation);
        System.out.println("The status of Token Validation is: " + tokeValidation);
    }


   /* @Test
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
    }*/

}
