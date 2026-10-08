package gr.android.moviesapp.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie_db")
    LiveData<List<MovieEntity>> getAll();

    @Query("SELECT * FROM movie_db WHERE id = :id LIMIT 1")
    MovieEntity getMovieById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieEntity movie);

    @Delete
    void delete(MovieEntity movie);
}

