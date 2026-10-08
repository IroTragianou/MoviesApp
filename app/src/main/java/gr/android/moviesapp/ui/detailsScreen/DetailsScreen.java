package gr.android.moviesapp.ui.detailsScreen;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;
import gr.android.moviesapp.R;
import gr.android.moviesapp.data.model.details.basicDetails.CastMemberRemote;
import gr.android.moviesapp.databinding.DetailsLayoutBinding;
import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.GenreUi;
import gr.android.moviesapp.domain.models.MovieDetailsWithReviewsUi;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.models.ReviewsUi;

@AndroidEntryPoint
public class DetailsScreen extends Fragment {

    private DetailsLayoutBinding binding;
    private DetailsViewModel detailsViewModel;
    private SimilarMoviesAdapter similarMoviesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DetailsLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        if (getArguments() != null) {
            int movieId = getArguments().getInt("MOVIE_ID", -1);
            if (movieId != -1) {
                detailsViewModel.fetchMovieDetailsWithReviews(movieId);
                detailsViewModel.fetchCast(movieId);
                detailsViewModel.fetchSimilarMovies(movieId);
            }
        }

        binding.backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        detailsViewModel.movieDetailsLiveData.observe(getViewLifecycleOwner(), this::updateUI);
        detailsViewModel.castList.observe(getViewLifecycleOwner(), this::displayCast);
        detailsViewModel.reviewsList.observe(getViewLifecycleOwner(), this::displayReviews);

        similarMoviesAdapter = new SimilarMoviesAdapter();
        binding.similarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.similarRecyclerView.setAdapter(similarMoviesAdapter);

        detailsViewModel.similarMovies.observe(getViewLifecycleOwner(), similarList -> {
            if (similarList != null && !similarList.isEmpty()) {
                binding.similarRecyclerView.setVisibility(View.VISIBLE);
                binding.similarMoviesLabel.setVisibility(View.VISIBLE);
                similarMoviesAdapter.submitList(similarList);
            } else {
                binding.similarRecyclerView.setVisibility(View.GONE);
                binding.similarMoviesLabel.setVisibility(View.GONE);
            }
        });
    }

    private void updateUI(MovieDetailsWithReviewsUi movieDetailsWithReviewsUi) {
        DetailsBasicUi details = movieDetailsWithReviewsUi.getMovieDetails();

        if (details != null) {
            binding.titleTextView.setText(details.getTitle());

            List<GenreUi> genres = details.getGenres();
            String genreText = genres.stream()
                    .map(GenreUi::getName)
                    .collect(Collectors.joining(", "));
            binding.genreTextView.setText(genreText);

            try {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = parser.parse(details.getReleaseDate());

                SimpleDateFormat formatter = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
                String formattedDate = formatter.format(date);

                binding.releaseDateTextView.setText(formattedDate);
            } catch (Exception e) {
                binding.releaseDateTextView.setText(details.getReleaseDate());
            }

            binding.ratingBar.setRating((float) (details.getVoteAverage() / 2));

            String formattedRuntime = formatRuntime(details.getRuntime());
            String fullText = "Runtime\n" + formattedRuntime;

            SpannableString spannable = new SpannableString(fullText);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#CC9200")), 8, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new android.text.style.RelativeSizeSpan(1f), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            binding.runtimeTextView.setText(spannable);
            binding.overviewTextView.setText(details.getOverview());

            Glide.with(requireContext())
                    .load("https://image.tmdb.org/t/p/w780" + details.getBackdropPath())
                    .into(binding.moviePoster);

            setupShareButton(details);
            setupFavoriteButton(details);
        }
    }

    private void setupShareButton(DetailsBasicUi details) {
        if (details.getHomepage() != null && !details.getHomepage().isEmpty()) {
            binding.shareButton.setVisibility(View.VISIBLE);
            binding.shareButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, details.getHomepage());
                startActivity(Intent.createChooser(intent, "Share via"));
            });
        } else {
            binding.shareButton.setVisibility(View.GONE);
        }
    }

    private void setupFavoriteButton(DetailsBasicUi details) {
        boolean favoriteState = getArguments() != null &&
                getArguments().containsKey("IS_FAVORITE") &&
                getArguments().getBoolean("IS_FAVORITE");

        details.setFavorite(favoriteState);
        updateFavoriteIcon(favoriteState);

        binding.favoriteIcon.setOnClickListener(v -> {
            MovieUi movieForToggle = new MovieUi(
                    details.getId(),
                    details.getTitle(),
                    details.getOverview(),
                    details.getPosterPath(),
                    details.getVoteAverage(),
                    details.isFavorite(),
                    details.getBackdropPath()
            );

            detailsViewModel.toggleFavorite(movieForToggle);

            boolean newState = !details.isFavorite();
            details.setFavorite(newState);
            updateFavoriteIcon(newState);
        });
    }

    private void displayCast(List<CastMemberRemote> castList) {
        if (castList == null || castList.isEmpty()) {
            binding.castLabel.setVisibility(View.GONE);
            binding.castTextView.setVisibility(View.GONE);
            return;
        }

        String castText = castList.stream()
                .map(CastMemberRemote::getName)
                .collect(Collectors.joining(", "));

        binding.castTextView.setText(castText);
        binding.castLabel.setVisibility(View.VISIBLE);
        binding.castTextView.setVisibility(View.VISIBLE);
    }

    private void updateFavoriteIcon(boolean isFavorite) {
        binding.favoriteIcon.setImageResource(
                isFavorite ? R.drawable.ic_favorite_selected : R.drawable.ic_favorite_unselect
        );
    }

    private void displayReviews(List<ReviewsUi> reviews) {
        binding.reviewsContainer.removeAllViews();

        if (reviews == null || reviews.isEmpty()) {
            binding.reviewsContainer.setVisibility(View.GONE);
            binding.reviewsLabel.setVisibility(View.GONE);
            return;
        }

        binding.reviewsLabel.setVisibility(View.VISIBLE);
        binding.reviewsContainer.setVisibility(View.VISIBLE);

        List<ReviewsUi> limitedReviews = reviews.size() > 3 ? reviews.subList(0, 3) : reviews;

        for (ReviewsUi review : limitedReviews) {
            SpannableString authorSpannable = new SpannableString(review.getAuthor());
            authorSpannable.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#E6B800")),
                    0,
                    authorSpannable.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            authorSpannable.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    0,
                    authorSpannable.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            TextView authorTextView = new TextView(requireContext());
            authorTextView.setText(authorSpannable);
            authorTextView.setTextSize(14);
            authorTextView.setPadding(0, 8, 0, 0);

            TextView contentTextView = new TextView(requireContext());
            contentTextView.setText(review.getContent());
            contentTextView.setTextSize(14);
            contentTextView.setPadding(0, 4, 0, 8);

            binding.reviewsContainer.addView(authorTextView);
            binding.reviewsContainer.addView(contentTextView);
        }
    }

    private String formatRuntime(int runtimeMinutes) {
        int hours = runtimeMinutes / 60;
        int minutes = runtimeMinutes % 60;
        return hours + "h " + minutes + "min";
    }
}