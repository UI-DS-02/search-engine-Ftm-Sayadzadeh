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

    // search operation
    public ArrayList<String> search(String command) {
        // change command to lower case
        command = command.toLowerCase();
        ArrayList<String> shouldNotBe = new ArrayList<>(); // "-"
        ArrayList<String> shouldBe = new ArrayList<>();  // nothing
        ArrayList<String> atLeast = new ArrayList<>(); // "+"
        String[] splitCommand = command.split(" ");
        for (String s : splitCommand) {
            // at least ( Or ) -----------------------------------------------------------------------------------------
            if (s.charAt(0) == '+') {
                String word = s.substring(1);
                if (dictionaries.get(word) != null) {
                    // if it is the first list , we should add all dictionary names of this list
                    if (atLeast.size() == 0)
                        atLeast.addAll(dictionaries.get(word));
                        // we should check and just add new dictionaries
                    else {
                        for (String element : dictionaries.get(word)) {
                            if (!atLeast.contains(element))
                                atLeast.add(element);
                        }
                    }
                }
            }
            // exceptions ( Not ) --------------------------------------------------------------------------------------
            else if (s.charAt(0) == '-') {
                String word = s.substring(1);
                if (dictionaries.get(word) != null) {
                    // if it is the first list , we should add all dictionary names of this list
                    if(shouldNotBe.size() == 0)
                        shouldNotBe.addAll(dictionaries.get(word));
                        // we should check and just add new dictionaries
                    else {
                        for (String element : dictionaries.get(word)) {
                            if (!shouldNotBe.contains(element))
                                shouldNotBe.add(element);
                        }
                    }
                }
            }
            // ( And ) -------------------------------------------------------------------------------------------------
            else {
                if (dictionaries.get(s) != null) {
                    if (shouldBe.size() == 0)
                        shouldBe.addAll(dictionaries.get(s));
                    else
                        shouldBe.retainAll(dictionaries.get(s));
                } else {
                    /* this word does not exist in our dictionaries
                    we should clear all data in shouldBe list :)
                     */
                    if (shouldBe.size() != 0)
                        shouldBe.clear();
                }
            }
        }
        // find result list ------------------------
        ArrayList<String> output = new ArrayList<>();
        // code
        return output;
    }
}
