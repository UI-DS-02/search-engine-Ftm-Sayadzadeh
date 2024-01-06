import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class OperationManager {
    private final HashMap<String, ArrayList<String>> dictionaries;
    private final File[] files;

    public OperationManager(File[] files) {
        this.dictionaries = new HashMap<>();
        this.files = files;
    }

    // -------------------- getter ----------------------
    public HashMap<String, ArrayList<String>> getDictionaries() {
        return dictionaries;
    }

    public File[] getFiles() {
        return files;
    }

    // prepare file and delete all char except a-z and A-Z
    public void prepareFiles() throws IOException {
        for ( File f : files ){
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            String text = bufferedReader.readLine();
            if (text != null) {
                text = text.toLowerCase();
                //String[] split = text.split("[@&#$%/-^_*+<>;:?!\" ,.()\\]\\[|{}]");
                String[] split = text.split("[^a-zA-Z]");

                for(String s : split){
                    /* if map contain this word as key &
                    this file name does not add to this key value before ,
                     it should be added
                     */
                    if(dictionaries.containsKey(s)){
                        if (!dictionaries.get(s).contains(f.getName())) {
                            dictionaries.get(s).add(f.getName());
                        }
                    }
                    // if this word is new
                    else{
                        ArrayList<String> values = new ArrayList<>();
                        values.add(f.getName());
                        dictionaries.put(s , values);
                    }
                }
            }
        }
    }
}
