package gr.android.moviesapp.ui.detailsScreen;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import gr.android.moviesapp.common.MovieEntityMapper;
import gr.android.moviesapp.data.model.details.basicDetails.CastMemberRemote;
import gr.android.moviesapp.domain.models.MovieDetailsWithReviewsUi;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.models.ReviewsUi;
import gr.android.moviesapp.domain.repoInterfaces.MovieRepository;
import gr.android.moviesapp.domain.usecase.DetailsUseCase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class DetailsViewModel extends ViewModel {

    private final DetailsUseCase detailsUseCase;
    private final MovieRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final ExecutorService executorService;

    private final MutableLiveData<MovieDetailsWithReviewsUi> movieDetailsWithReviewsLiveData = new MutableLiveData<>();
    public final LiveData<MovieDetailsWithReviewsUi> movieDetailsLiveData = movieDetailsWithReviewsLiveData;

    private final MutableLiveData<List<ReviewsUi>> _reviewsList = new MutableLiveData<>();
    public final LiveData<List<ReviewsUi>> reviewsList = _reviewsList;

    private final MutableLiveData<List<CastMemberRemote>> _castList = new MutableLiveData<>();
    public final LiveData<List<CastMemberRemote>> castList = _castList;

    private final MutableLiveData<List<MovieUi>> _similarMovies = new MutableLiveData<>();
    public LiveData<List<MovieUi>> similarMovies = _similarMovies;

    @Inject
    public DetailsViewModel(DetailsUseCase detailsUseCase, MovieRepository repository) {
        this.detailsUseCase = detailsUseCase;
        this.repository = repository;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void fetchMovieDetailsWithReviews(Integer movieId) {
        disposable.add(
                detailsUseCase.execute(movieId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    movieDetailsWithReviewsLiveData.setValue(result);
                                    _reviewsList.setValue(result.getReviewsList());
                                },
                                throwable -> Log.e("DetailsViewModel", "Error fetching movie details", throwable)
                        )
        );
    }

    public void fetchCast(int movieId) {
        disposable.add(
                detailsUseCase.getCastByMovieId(movieId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                _castList::setValue,
                                throwable -> Log.e("DetailsViewModel", "Error fetching cast", throwable)
                        )
        );
    }

    public void fetchSimilarMovies(int movieId) {
        disposable.add(
                detailsUseCase.getSimilarMoviesById(movieId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                _similarMovies::setValue,
                                throwable -> Log.e("DetailsViewModel", "Error fetching similar movies", throwable)
                        )
        );
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
        disposable.clear();
        executorService.shutdown();
        super.onCleared();
    }
}