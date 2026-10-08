package gr.android.moviesapp.common;

import java.util.List;
import java.util.stream.Collectors;

import gr.android.moviesapp.data.model.home.MovieRemote;
import gr.android.moviesapp.data.database.MovieEntity;
import gr.android.moviesapp.domain.models.MovieUi;

public class MovieEntityMapper {

    public static MovieEntity mapToMovieEntity(MovieUi movie) {
        return new MovieEntity(
                movie.getId(),
                movie.getTitle(),
                movie.getOverview(),
                movie.getPosterPath(),
                movie.getVoteAverage(),
                movie.isFavorite(),
                movie.getBackdrop_path()
        );
    }

    public static MovieUi mapToMovieUi(MovieEntity movie) {
        return new MovieUi(
                movie.getId(),
                movie.getTitle(),
                movie.getOverview(),
                movie.getPosterPath(),
                movie.getVoteAverage(),
                movie.isFavorite(),
                movie.getBackdrop_path()
        );
    }

    public static List<MovieUi> mapToUiMovieList(List<MovieEntity> movieEntities) {
        return movieEntities.stream()
                .map(MovieEntityMapper::mapToMovieUi)
                .collect(Collectors.toList());
    }
}

