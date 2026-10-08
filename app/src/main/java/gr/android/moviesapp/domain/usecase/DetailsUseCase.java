package gr.android.moviesapp.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.repoInterfaces.MovieDetailsRepository;
import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.MovieDetailsWithReviewsUi;
import gr.android.moviesapp.domain.models.ReviewsUi;
import io.reactivex.rxjava3.core.Observable;
import gr.android.moviesapp.data.model.details.basicDetails.CastMemberRemote;

public class DetailsUseCase {

    private MovieDetailsRepository movieDetailsRepository;

    @Inject
    public DetailsUseCase(MovieDetailsRepository movieDetailsRepository) {
        this.movieDetailsRepository = movieDetailsRepository;
    }

    public Observable<MovieDetailsWithReviewsUi> execute(Integer movieId) {
        Observable<DetailsBasicUi> movieDetailsObservable = movieDetailsRepository.getMovieById(movieId);
        Observable<List<ReviewsUi>> reviewsObservable = movieDetailsRepository.getReviewsById(movieId);

        return Observable.combineLatest(
                movieDetailsObservable,
                reviewsObservable,
                MovieDetailsWithReviewsUi::new
        );
    }


    public Observable<List<CastMemberRemote>> getCastByMovieId(int movieId) {
        return movieDetailsRepository.getCastById(movieId);
    }

    public Observable<List<MovieUi>> getSimilarMoviesById(int movieId) {
        return movieDetailsRepository.getSimilarMoviesById(movieId);
    }



}
