package themoviedb.entities.movies;

public class MovieDetails {
    private int movieId;
    private int rateValue;
    private int latestMovieId;

    public MovieDetails() {
    }

    public MovieDetails(int latestMovieId) {
        this.movieId = latestMovieId;
    }


    public int getmovieId() {
        return movieId;
    }

    public void setmovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getRateValue() {
        return rateValue;
    }

    public void setRateValue(int rateValue) {
        this.rateValue = rateValue;
    }

    public int getLatestMovieId() {
        return latestMovieId;
    }

    public void setLatestMovieId(int latestMovieId) {
        this.latestMovieId = latestMovieId;
    }
}
