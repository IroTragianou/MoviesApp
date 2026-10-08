package gr.android.moviesapp.ui.homeScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import gr.android.moviesapp.common.MovieEntityMapper;
import gr.android.moviesapp.common.MovieMapper;
import gr.android.moviesapp.domain.repoInterfaces.MovieRepository;
import gr.android.moviesapp.data.database.MovieEntity;
import gr.android.moviesapp.domain.models.MovieUi;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import androidx.lifecycle.Observer;

@HiltViewModel
public class MovieViewModel extends ViewModel {
    private MovieRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private final ExecutorService executorService;
    private Observer<List<MovieEntity>> databaseObserver;
    private Boolean hasError = false;

    @Inject
    public MovieViewModel(MovieRepository repository) {
        this.repository = repository;
        this.executorService = Executors.newSingleThreadExecutor();
        observeDatabaseChanges();
    }

    private MutableLiveData<List<MovieUi>> moviesLiveData = new MutableLiveData<>();
    public LiveData<List<MovieUi>> getMovies() {
        refreshMovies();
        return moviesLiveData;
    }

    private void refreshMovies() {
        disposable.add(repository.getPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MovieMapper::mapToUiMovieList)
                .subscribe(
                        movies -> moviesLiveData.postValue(movies),
                        throwable -> {
                            Log.e("MovieViewModel", "Error fetching movies", throwable);
                            hasError = true;
                            observeDatabaseChanges();
                        }
                ));
    }

    private void observeDatabaseChanges() {
        LiveData<List<MovieEntity>> allMovies = repository.getAllMovies();

        databaseObserver = movieEntities -> {
            List<MovieUi> dbMovies = MovieEntityMapper.mapToUiMovieList(movieEntities);

            List<MovieUi> currentMovies = moviesLiveData.getValue();
            if (currentMovies == null || currentMovies.isEmpty()) {
                moviesLiveData.postValue(new ArrayList<>(dbMovies));
                return;
            }

            Map<Integer, MovieUi> dbMoviesMap = new HashMap<>();
            for (MovieUi movie : dbMovies) {
                dbMoviesMap.put(movie.getId(), movie);
            }

            List<MovieUi> updatedMovies = new ArrayList<>();
            boolean hasChanges = false;

            for (MovieUi currentMovie : currentMovies) {
                MovieUi dbMovie = dbMoviesMap.get(currentMovie.getId());

                boolean shouldBeFavorite;
                if (dbMovie != null) {
                    shouldBeFavorite = dbMovie.isFavorite();
                } else {
                    shouldBeFavorite = false;
                }

                MovieUi updatedMovie = new MovieUi(
                        currentMovie.getId(),
                        currentMovie.getTitle(),
                        currentMovie.getOverview(),
                        currentMovie.getPosterPath(),
                        currentMovie.getVoteAverage(),
                        shouldBeFavorite,
                        currentMovie.getBackdrop_path()
                );

                updatedMovies.add(updatedMovie);

                if (currentMovie.isFavorite() != shouldBeFavorite) {
                    hasChanges = true;
                }
            }

            if (hasChanges) {
                moviesLiveData.postValue(updatedMovies);
            }
        };

        allMovies.observeForever(databaseObserver);
    }

    public void toggleFavorite(MovieUi movie) {
        executorService.execute(() -> {
            MovieUi movieCopy = new MovieUi(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getOverview(),
                    movie.getPosterPath(),
                    movie.getVoteAverage(),
                    true,
                    movie.getBackdrop_path()
            );

            repository.toggleFavorite(MovieEntityMapper.mapToMovieEntity(movieCopy));
        });
    }

    @Override
    protected void onCleared() {
        if (databaseObserver != null) {
            repository.getAllMovies().removeObserver(databaseObserver);
        }
        disposable.dispose();
        executorService.shutdown();
        super.onCleared();
    }
}