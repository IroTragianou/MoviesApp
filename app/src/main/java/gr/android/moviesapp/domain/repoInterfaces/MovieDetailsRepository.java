package gr.android.moviesapp.domain.repoInterfaces;

import java.util.List;

import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.models.ReviewsUi;
import io.reactivex.rxjava3.core.Observable;

import gr.android.moviesapp.data.model.details.basicDetails.CastMemberRemote;


public interface MovieDetailsRepository {

    Observable<DetailsBasicUi> getMovieById(Integer movie_id);
    Observable<List<ReviewsUi>> getReviewsById(Integer movie_id);

    Observable<List<CastMemberRemote>> getCastById(int movieId);

    Observable<List<MovieUi>>getSimilarMoviesById(int movieId);

}
