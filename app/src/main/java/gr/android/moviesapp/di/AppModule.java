package gr.android.moviesapp.di;

import android.app.Application;

import androidx.room.Room;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import gr.android.moviesapp.domain.repoInterfaces.MovieDetailsRepository;
import gr.android.moviesapp.data.repo.MovieDetailsRepositoryImpl;
import gr.android.moviesapp.data.repo.MovieRepositoryImpl;
import gr.android.moviesapp.data.networkServices.MovieApiService;
import gr.android.moviesapp.data.database.MovieDao;
import gr.android.moviesapp.data.database.MovieDatabase;
import gr.android.moviesapp.domain.repoInterfaces.MovieRepository;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AppModule {

    @Provides
    public static MovieDatabase provideMovieDatabase(Application application) {
        return Room.databaseBuilder(application, MovieDatabase.class, "movie_db")
                .build();
    }

    @Provides
    public static MovieDao provideMovieDao(MovieDatabase database) {
        return database.movieDao();
    }

    @Provides
    public static MovieRepository provideMovieRepository(MovieApiService apiService, MovieDao movieDao) {
        return new MovieRepositoryImpl(apiService, movieDao);
    }

    @Provides
    public static MovieDetailsRepository provideMovieDetailsRepository(MovieApiService apiService) {
        return new MovieDetailsRepositoryImpl(apiService);
    }
}

