package themoviedb.entities.lists;

public class AddMovie {
    private int media_id;

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public AddMovie(int media_id) {
        this.media_id = media_id;
    }

}
