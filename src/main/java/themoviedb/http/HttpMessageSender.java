package themoviedb.http;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import themoviedb.entities.RequestToken;
import themoviedb.entities.ValidateToken;
import themoviedb.entities.lists.AddMovie;
import themoviedb.entities.lists.LatestMovieId;
import themoviedb.entities.lists.MDBList;
import themoviedb.entities.lists.RateMovie;

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

    public Response getRequestToEndpoint(String api_key, String endpoint) {
        String requestUrl = url + endpoint;
        Response response =
                given().log().uri().
                    contentType(ContentType.JSON).with().
                    queryParam("api_key", api_key).
                when().
                    get(requestUrl).
                        andReturn();
        return response;
    }

    public Response postRequestToEndpoint(ValidateToken validateToken, String endpoint){
        String requestUrl = url + endpoint;
        return given().
                    contentType(ContentType.JSON).
                    body(validateToken).log().all().
                when().
                    post(requestUrl).
                    andReturn();
    }

    public Response postRequestToEndpoint(RequestToken requestToken, String endpoint){
        String requestUrl = url + endpoint;
        return given().
                    contentType(ContentType.JSON).
                    body(requestToken).log().all().
                when().
                    post(requestUrl).
                andReturn();
    }

    public Response postRequestToEndpoint(MDBList list, String api_key, String session_id, String endpoint){
        String requestUrl = url + endpoint;
        return given().
                    contentType(ContentType.JSON).with().
                    queryParam("api_key", api_key).
                    queryParam("session_id", session_id).
                    body(list).log().all().
                when().
                    post(requestUrl).
                andReturn();
    }

    public Response postRequestToEndpoint(AddMovie addMovie, String api_key, String session_id, String endpoint){
        String requestUrl = url + endpoint;
        return given().
                contentType(ContentType.JSON).with().
                queryParam("api_key", api_key).
                queryParam("session_id", session_id).
                body(addMovie).log().all().
                when().
                post(requestUrl).
                andReturn();
    }

    public Response postRequestToEndpoint(RateMovie rateMovie, String api_key, String gSessionId, String endpoint){
        String requestUrl = url + endpoint;
        return given().
                contentType(ContentType.JSON).with().
                queryParam("api_key", api_key).
                queryParam("guest_session_id", gSessionId).
                body(rateMovie).log().all().
                when().
                post(requestUrl).
                andReturn();
    }

    public Response postRequestToEndpoint(String api_key, String sessionId, String endpoint){
        String requestUrl = url + endpoint;
        return given().
                contentType(ContentType.JSON).with().
                queryParam("api_key", api_key).
                queryParam("session_id", sessionId).
                queryParam("confirm", true).
                log().all().
                when().
                post(requestUrl).
                andReturn();
    }

    public Response deleteRequestToEndpoint(String api_key, String session_id, String endpoint) {
        String requestUrl = url + endpoint;
        return given().
                contentType(ContentType.JSON).with().
                queryParam("api_key", api_key).
                queryParam("session_id", session_id).
                log().all().
                when().
                delete(requestUrl).
                andReturn();
    }
}
