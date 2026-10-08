package gr.android.moviesapp.data.repo;

import androidx.lifecycle.LiveData;

import java.util.List;

import gr.android.moviesapp.data.model.home.MovieRemote;
import gr.android.moviesapp.data.model.home.MovieApiResponse;
import gr.android.moviesapp.data.networkServices.MovieApiService;
import gr.android.moviesapp.data.database.MovieDao;
import gr.android.moviesapp.data.database.MovieEntity;
import gr.android.moviesapp.domain.repoInterfaces.MovieRepository;
import io.reactivex.rxjava3.core.Observable;

public class MovieRepositoryImpl implements MovieRepository {
    private MovieApiService apiService;
    private MovieDao movieDao;


    public MovieRepositoryImpl(MovieApiService apiService, MovieDao movieDao) {
        this.apiService = apiService;
        this.movieDao = movieDao;
    }

    @Override
    public Observable<List<MovieRemote>> getPopularMovies() {
        return apiService.getPopularMovies()
                .map(MovieApiResponse::getResults)
                .doOnNext(movies -> {
                    for (MovieRemote movie : movies) {
                        MovieEntity localMovie = movieDao.getMovieById(movie.getId());
                        if (localMovie != null) {
                            movie.setFavorite(true);
                        }
                    }
                });
    }

    @Override
    public LiveData<List<MovieEntity>> getAllMovies() {
        return movieDao.getAll();
    }

    @Override
    public void toggleFavorite(MovieEntity movie) {
        MovieEntity existingMovie = movieDao.getMovieById(movie.getId());

        if (existingMovie != null) {
            android.util.Log.d("RoomTest", "Removing movie from DB: " + movie.getTitle());
            movieDao.delete(existingMovie);
        } else {
            android.util.Log.d("RoomTest", "Adding movie to DB: " + movie.getTitle());
            movieDao.insert(movie);
        }
    }
}
