import java.util.List;

public class DownloadedVideo extends Video {


    private String filepath;

    public DownloadedVideo(String name, int length, String director, String genre, int releaseYear,
                           List<String> stars, String filepath) {
        super(name, length, director, genre, releaseYear, stars);

        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }
}
