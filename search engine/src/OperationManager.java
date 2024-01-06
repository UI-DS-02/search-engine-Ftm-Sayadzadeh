import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

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
        for (File f : files) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            String text = bufferedReader.readLine();
            if (text != null) {
                text = text.toLowerCase();
                //String[] split = text.split("[@&#$%/-^_*+<>;:?!\" ,.()\\]\\[|{}]");
                String[] split = text.split("[^a-zA-Z]");

                for (String s : split) {
                    /* if map contain this word as key &
                    this file name does not add to this key value before ,
                     it should be added
                     */
                    if (dictionaries.containsKey(s)) {
                        if (!dictionaries.get(s).contains(f.getName())) {
                            dictionaries.get(s).add(f.getName());
                        }
                    }
                    // if this word is new
                    else {
                        ArrayList<String> values = new ArrayList<>();
                        values.add(f.getName());
                        dictionaries.put(s, values);
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
                    if (shouldNotBe.size() == 0)
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
                    // use match word manager func
                    ArrayList<String> output = this.matchWordManager(s);
                    // input 0
                    if (output == null) {
                        /* this word does not exist in our dictionaries
                          we should clear all data in shouldBe list :)
                        */
                        if (shouldBe.size() != 0)
                            shouldBe.clear();
                    }
                    // consider match word this time
                    else{
                        if (shouldBe.size() == 0)
                            shouldBe.addAll(output);
                        else
                            shouldBe.retainAll(output);
                    }
                }
            }
        }
        // find result list ------------------------
        return getResult(atLeast, shouldBe, shouldNotBe);
    }

    public ArrayList<String> getResult(ArrayList<String> atLeast, ArrayList<String> shouldBe, ArrayList<String> shouldNotBe) {
        ArrayList<String> result = new ArrayList<>();
        if (shouldBe.size() != 0 && atLeast.size() != 0) {
            if (shouldBe.size() < atLeast.size()) {
                // all shouldBe list should be added
                result.addAll(shouldBe);
                // remove elements from output list if atLeast does not contain elements
                for (int i = 0; i < result.size(); i++) {
                    if (!atLeast.contains(result.get(i)))
                        result.remove(result.get(i));
                }
            } else {
                result.addAll(atLeast);
                for (int i = 0; i < result.size(); i++) {
                    if (!shouldBe.contains(result.get(i)))
                        result.remove(result.get(i));
                }
            }
        } else {
            result.addAll(shouldBe);
            result.addAll(atLeast);
        }
        // we should remove elements which exist in notShouldBe list
        result.removeAll(shouldNotBe);
        return result;
    }

    private ArrayList<String> matchWordManager(String word) {
        System.out.println(" similar words : " + findMatchingWords(word));
        System.out.println(" * Enter one of the similar words or enter zero ! ");
        Scanner sc = new Scanner(System.in);
        System.out.print("* Match Word : ");
        String input = sc.next();
        if (!Objects.equals(input, "0"))
            return this.dictionaries.get(input);
        else
            return null;
    }

    private ArrayList<String> findMatchingWords(String word) {
        ArrayList<String> similarWords = new ArrayList<>();
        for (String element : this.dictionaries.keySet()) {
            // check words with 1 char difference ( same size )
            if (element.length() == word.length()) {
                int difference = calculateWordDifference(word, element);
                if (difference == 1) {
                    similarWords.add(element);
                }
            }
            // check words with 1 char difference ( different size )
            else if ((element.contains(word) || word.contains(element))
                    && Math.abs(element.length() - word.length()) == 1) {
                similarWords.add(element);
            }
        }
        return similarWords;
    }

    private int calculateWordDifference(String word, String matchWord) {
        int difference = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != matchWord.charAt(i)) {
                difference++;
            }
        }
        return difference;
    }
}
