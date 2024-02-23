import java.io.*;
import java.util.ArrayList;

public class StopWordManager {
    private final File[] files = new File("StopWord").listFiles();
    private final ArrayList<String> stopWords = new ArrayList<>();

    public StopWordManager() {
        fillStopWordList();
    }

    private void fillStopWordList() {
        assert files != null;
        for (File f : files) {
            try {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                //Reading until we run out of lines
                String line;
                int i = 0;
                while ((line = br.readLine()) != null) {
                    stopWords.add(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public ArrayList<String> getStopWords(){
        return this.stopWords;
    }

}
