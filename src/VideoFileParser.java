import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
}
