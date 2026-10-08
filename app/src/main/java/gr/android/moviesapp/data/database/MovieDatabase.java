package gr.android.moviesapp.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(
        entities = {MovieEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}

