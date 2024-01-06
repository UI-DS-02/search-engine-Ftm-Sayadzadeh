import java.io.File;
import java.io.IOException;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // input folder path :
        System.out.print("Enter folder path : ");
        String path = sc.nextLine();
        File folder = new File(path);
        try {
            File[] files = folder.listFiles();
            OperationManager operationManager = new OperationManager(files);
            try {
                operationManager.prepareFiles();
            } catch (IOException e) {
                System.out.println( " error : " + e.getMessage());
            }
            while (true) {
                System.out.print("Search  =>  ");
                String phrase = sc.nextLine();
                ArrayList<String> result = operationManager.search(phrase);
                System.out.println(result.size() + " results are founded for you ...");
                if (result.size() != 0)
                    System.out.println("Result  =>  " + result);
                else {
                    System.out.println(" NO RESULT :) ");
                }
            }
        } catch (NullPointerException e) {
            System.out.println("File not found!");
        }
    }
}