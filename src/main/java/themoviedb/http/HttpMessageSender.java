package themoviedb.http;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import themoviedb.entities.authentication.RequestToken;
import themoviedb.entities.authentication.ValidateToken;
import themoviedb.entities.lists.LatestMovieId;
import themoviedb.entities.movies.MovieDetails;
import themoviedb.entities.lists.MDBList;
import themoviedb.entities.movies.RateMovie;

import static io.restassured.RestAssured.given;

public class HttpMessageSender {
    private String url;

    public HttpMessageSender(String url){
        this.url = url;
    }

    public Response getRequestToEndpoint(String endpoint) {
        String requestUrl = url + endpoint;
        Response response =
                given().log().uri().
                    contentType(ContentType.JSON).
                when().
                    get(requestUrl).
                    andReturn();
        return response;
    }

    public Response getRequestToEndpoint(String apiKey, String endpoint) {
        String requestUrl = url + endpoint;
        Response response =
                given().log().uri().
                    contentType(ContentType.JSON).with().
                    queryParam("api_key", apiKey).
                when().
                    get(requestUrl).
                        andReturn();
        return response;
    }

    public Response postRequestToEndpoint(ValidateToken validateToken, String endpoint){
        String requestUrl = url + endpoint;
        return given().log().uri().
                    contentType(ContentType.JSON).
                    body(validateToken).
                //log().body().
                when().
                    post(requestUrl).
                    andReturn();
    }

    public Response postRequestToEndpoint(RequestToken requestToken, String endpoint){
        String requestUrl = url + endpoint;
        return given().log().uri().
                    contentType(ContentType.JSON).
                    body(requestToken).
                //log().body().
                when().
                    post(requestUrl).
                andReturn();
    }

    public Response postRequestToEndpoint(MDBList list, String apiKey, String sessionId, String endpoint){
        String requestUrl = url + endpoint;
        return given().log().uri().
                    contentType(ContentType.JSON).with().
                    queryParam("api_key", apiKey).
                    queryParam("session_id", sessionId).
                    body(list).
                //log().body().
                when().
                    post(requestUrl).
                andReturn();
    }

    public Response postRequestToEndpoint(MovieDetails movieDetails, String apiKey, String sessionId, String endpoint){
        String requestUrl = url + endpoint;
        return given().log().uri().
                contentType(ContentType.JSON).with().
                queryParam("api_key", apiKey).
                queryParam("session_id", sessionId).
                body(movieDetails).
                //log().body().
                when().
                post(requestUrl).
                andReturn();
    }

    public Response postRequestToEndpoint(LatestMovieId latestMovieId, String apiKey, String sessionId, String endpoint){
        String requestUrl = url + endpoint;
        return given().log().uri().
                contentType(ContentType.JSON).with().
                queryParam("api_key", apiKey).
                queryParam("session_id", sessionId).
                body(latestMovieId).
                //log().body().
                when().
                post(requestUrl).
                andReturn();
    }

    public Response postRequestToEndpoint(RateMovie rateMovie, String apiKey, String gSessionId, String endpoint){
        String requestUrl = url + endpoint;
        return given().log().uri().
                contentType(ContentType.JSON).with().
                queryParam("api_key", apiKey).
                queryParam("session_id", gSessionId).
                body(rateMovie).
                //log().body().
                when().
                post(requestUrl).
                andReturn();
    }

    public Response postRequestToEndpoint(String apiKey, String sessionId, String endpoint){
        String requestUrl = url + endpoint;
        return given().log().uri().
                contentType(ContentType.JSON).with().
                queryParam("api_key", apiKey).
                queryParam("session_id", sessionId).
                queryParam("confirm", true).
                //log().body().
                when().
                post(requestUrl).
                andReturn();
    }

    public Response deleteRequestToEndpoint(String apiKey, String sessionId, String endpoint) {
        String requestUrl = url + endpoint;
        return given().log().uri().
                contentType(ContentType.JSON).with().
                queryParam("api_key", apiKey).
                queryParam("session_id", sessionId).
                //log().body().
                when().
                delete(requestUrl).
                andReturn();
    }
}
