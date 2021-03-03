package themoviedb.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import sun.awt.windows.ThemeReader;
import themoviedb.api.RestfulMoviesApi;
import themoviedb.entities.Auth;
import themoviedb.entities.List;
import themoviedb.entities.RequestToken;
import themoviedb.entities.ValidateToken;
import themoviedb.entities.lists.MDBList;
import themoviedb.http.HttpMessageSender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StepsLists {

    private static Properties props;
    private static HttpMessageSender requestSender;
    private static RestfulMoviesApi api;
    private static Auth auth;
    private static List list;

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

    @Given("I am already login into the API")
    public void iAmalreadyLoginIntoTheAPI(){
        createTestEnvironment();
        System.out.println("Login and SessionId Successful: " + auth.getSessionId());
    }

    @When("A user sends a request to the Create List endpoint")
    public void createANewList(){
        MDBList mdbList = new MDBList("Fight Club","A Nice Movie","en");
        Response response = api.createList(mdbList, auth.getApiKey(), auth.getSessionId());
        response.then().log().body();
        Assert.assertEquals("The status code is different than 201", 201, response.statusCode());

        int listId =  response.then().extract().path("list_id");
        list.setListId(listId);

        boolean listCreationValidation =  response.then().extract().path("success");
        list.setListCreationValidation(listCreationValidation);
    }

    @Then("A new list is Created")
    public void listCreatedValidation(){
        Assert.assertTrue("The List creation failed", list.getListCreationValidation());
        System.out.println("The List id created is " + list.getListId());

    }









}

