package gr.android.moviesapp.ui.detailsScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import gr.android.moviesapp.R;
import gr.android.moviesapp.domain.models.MovieUi;

public class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.SimilarViewHolder> {

    private List<MovieUi> similarMovies;

    public void submitList(List<MovieUi> movies) {
        this.similarMovies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_similar_movie, parent, false);
        return new SimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarViewHolder holder, int position) {
        MovieUi movie = similarMovies.get(position);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return similarMovies != null ? similarMovies.size() : 0;
    }

    static class SimilarViewHolder extends RecyclerView.ViewHolder {
        final ImageView posterImageView;

        SimilarViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.similarMoviePoster);
        }
    }
}