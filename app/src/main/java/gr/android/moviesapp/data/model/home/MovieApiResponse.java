package gr.android.moviesapp.data.model.home;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MovieApiResponse {
    @SerializedName("results")
    private List<MovieRemote> results;

    public List<MovieRemote> getResults() {
        return results;
    }

}

