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
import themoviedb.entities.lists.LatestMovieId;
import themoviedb.entities.lists.List;
import themoviedb.entities.lists.MDBList;
import themoviedb.entities.movies.MovieDetails;
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
    public void LoginIntoTheAPI(){
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
    public void listValidation(){
        Assert.assertTrue("The List creation failed", list.getListCreationValidation());
        System.out.println("The List id created is " + list.getListId());
    }

    @When("A user sends a request to the Get Details endpoint")
    public void aUserSendsARequestToTheGetDetailsEndpoint() {
        Response response = api.getListDetails(list.getListId(), auth.getApiKey());
        Assert.assertEquals("The status code is different than 201", 200, response.statusCode());
        String listDescription = response.then().extract().path("description");
        String createdBy = response.then().extract().path("created_by");
        list.setDescription(listDescription);
        list.setCreatedBy(createdBy);
    }

    @Then("The response shows the list id description and created by fields")
    public void theResponseShowsTheListIdDescriptionAndCreatedByFields() {
        Assert.assertNotEquals("Descrition OK", null, list.getDescription());
        Assert.assertNotEquals("Created By OK", null, list.getCreatedBy());
        System.out.println("List Id : "  + list.getListId());
        System.out.println("List Description: " + list.getDescription());
        System.out.println("Created By: " + list.getCreatedBy());
    }

    @When("A user sends a request to Add Movie endpoint")
    public void aUserSendsARequestToAddMovieEndpoint() {
        Response responseLatestMovie = api.getLatestMovieId(auth.getApiKey());
        int latestMovieId = responseLatestMovie.then().extract().path("id");

        LatestMovieId latestMovieId1 = new LatestMovieId(latestMovieId);
        Response response = api.addMovieToList(latestMovieId1, auth.getApiKey(), auth.getSessionId(), list.getListId());

        response.then().log().body();
        boolean addMovieValidation =  response.then().extract().path("success");
        Assert.assertEquals(true, addMovieValidation);
    }

    @Then("The movie listed in the List Details endpoint")
    public void theMovieIsAddedToTheList() {
        Response response = api.getListDetails(list.getListId(), auth.getApiKey());
        Assert.assertEquals("The status code is different than 201", 200, response.statusCode());
        response.then().log().body();

        String listDescription = response.then().extract().path("description");
        String createdBy = response.then().extract().path("created_by");
        //int movieId = response.then().extract().path("items.id");

        //movieDetails.setmovieId(movieId);

        list.setDescription(listDescription);
        list.setCreatedBy(createdBy);

        Assert.assertEquals("The actual movie does not match", movieDetails.getLatestMovieId(), movieDetails.getmovieId());
    }


    @When("The user sends request to Clear List")
    public void theUserSendsRequestToClearList() {
        Response response = api.clearList(auth.getApiKey(), auth.getSessionId(), list.getListId());
        Assert.assertEquals("The status code is different than 201", 201, response.statusCode());
    }


    @Then("The List is empty")
    public void theListIsEmpty() {
        Response response = api.getListDetails(list.getListId(), auth.getApiKey());
        Assert.assertEquals("The status code is different than 201", 200, response.statusCode());

        //Pendiente validacion de lista nula

    }

    @When("The user sends request to Delete List")
    public void theUserSendsRequestToDeleteList() {
        Response response = api.DeleteList(auth.getApiKey(),auth.getSessionId(), list.getListId());
    }

    @Then("The List is Deleted")
    public void theListIsDeleted() {
        Response response = api.getListDetails(list.getListId(), auth.getApiKey());
        Assert.assertEquals("The status code is different than 404", 404, response.statusCode());
        response.then().log().body();

        String status_message = response.then().extract().path("status_message");
        Assert.assertEquals("Status Message is wrong", "The resource you requested could not be found.", status_message);
    }
}

