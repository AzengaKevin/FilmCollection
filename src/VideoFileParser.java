import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoFileParser {
    private static String serialFilename = "videos.ser";

    public static void serializeToDisk(List<Video> videos) throws IOException {
        FileOutputStream fos = new FileOutputStream(serialFilename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(videos);
        fos.close();
        oos.close();
    }

    public static List<Video> fromSerialized(String filename) throws IOException, ClassNotFoundException {

        List<Video> videoList = Collections.EMPTY_LIST;

        try (
                FileInputStream fin = new FileInputStream(filename);
                ObjectInputStream oin = new ObjectInputStream(fin);
        ) {

        	videoList = (List<Video>) oin.readObject();
        }

        return videoList;
    }
}
