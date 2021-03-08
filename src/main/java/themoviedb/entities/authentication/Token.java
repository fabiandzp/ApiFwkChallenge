package themoviedb.entities.authentication;

public class Token {
    private String success;
    private String expires_at;
    private String request_token;

    public Token(String success, String expires_at, String request_token){
        this.success = success;
        this.expires_at = expires_at;
        this.request_token = request_token;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public String getRequest_token() {
        return request_token;
    }

    public void setRequest_token(String request_token) {
        this.request_token = request_token;
    }


}
