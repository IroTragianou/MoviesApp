package gr.android.moviesapp.domain.models;

import java.util.List;

public class MovieDetailsWithReviewsUi {

    private final DetailsBasicUi movieDetails;
    private final List<ReviewsUi> reviews;

    public MovieDetailsWithReviewsUi(DetailsBasicUi movieDetails, List<ReviewsUi> reviews) {
        this.movieDetails = movieDetails;
        this.reviews = reviews;
    }

    public DetailsBasicUi getMovieDetails() {
        return movieDetails;
    }

    public List<ReviewsUi> getReviewsList() {
        return reviews;
    }

    public ReviewsUi getReviews() {
        if (reviews != null && !reviews.isEmpty()) {
            return reviews.get(0);
        }
        return null;
    }
}
