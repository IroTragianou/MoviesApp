package gr.android.moviesapp.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_db")
public class MovieEntity {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "poster_path")
    private String posterPath;


    @ColumnInfo(name = "vote_average")
    private double voteAverage;

    @ColumnInfo(name = "backdrop_path")
    private String backdrop_path;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    // Constructor
    public MovieEntity(int id, String title, String overview, String posterPath, double voteAverage, boolean isFavorite, String backdrop_path) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.isFavorite = isFavorite;
        this.backdrop_path = backdrop_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}

