import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner userIn = new Scanner(System.in);
        System.out.println("Input text you want: 1, 2, 3 (Nineteen Eighty Four), or 4 (Animal Farm)");
        int choice = userIn.nextInt();
        String path = "";
        if (choice == 1) // user input to choose which text to be counted
            path = "/Users/agrfhyl/IdeaProjects/Counting Plugged IDE/src/textOne.txt";
        else if (choice == 2)
            path = "/Users/agrfhyl/IdeaProjects/Counting Plugged IDE/src/textTwo.txt";
        else if (choice == 3)
            path = "/Users/agrfhyl/IdeaProjects/Counting Plugged IDE/src/Nineteen+eighty-four.txt";
        else if (choice == 4)
            path = "/Users/agrfhyl/IdeaProjects/Counting Plugged IDE/src/Animal+Farm.txt";
        else {
            System.out.println("Pick valid number");
            choice = userIn.nextInt();
        }
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        ArrayList<String> commons = readFile("/Users/agrfhyl/IdeaProjects/Counting Plugged IDE/src/commonWords.txt");
        ArrayList<Word> wordList = new ArrayList<>();
        while(scanner.hasNext()){ // scanning token by token in the selected texted
            String word = removePunct(scanner.next().toLowerCase()); // set the current word;
            boolean inWordList = false; //wordList is the list of words already found
            int index = 0;
            if (wordList.size() == 0) { //if wordList is empty, this must be the first word, then we always add it.
               Word a = new Word(word);
                wordList.add(a);
            } else { //if not the first word of the text
                for (int i = 0; i < wordList.size(); i++) { //determine if this word has been found before
                    if (word.equals(wordList.get(i).getWord())) {
                        inWordList = true; //update condition, determining whether word found before
                        index = i; // saves index of the found word in the array, can be used outside of this loop
                    }
                }
                if (inWordList) {
                    wordList.get(index).incrementCount(); //found word, so increment count
                } else {
                    // if word is new, check if it is part of the common word list
                    boolean inCommonList = false;
                    for (int i = 0; i < commons.size(); i++) {
                        if (word.equals(commons.get(i))) {
                            inCommonList = true;
                        }
                    }
                    if (!inCommonList) { // if not in common, we can add the word to the wordList
                        Word aWord = new Word(word);
                        wordList.add(aWord);
                    }
                }
            }
        }
        wordList = removeEmpty(wordList);
        wordList = sortWords(wordList);
        printTopFive(wordList);
    }
// reads the commonWords txt file and stores in an arrayList
    public static ArrayList<String> readFile(String fileName) throws FileNotFoundException {
        ArrayList<String> out = new ArrayList<>();
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) { // Since all words are on their own line, hasNextLine() will work here
            out.add(scan.nextLine().toLowerCase());
        }
        return out;
    }
/* I classified a word as: no punctuation in front and back of the word.
    This method tries to remove (peel) the punctuation from the front and back layer by layer until both ends are "clear"
 */
    public static String removePunct(String word) {
       // use ascii values of chars to determine whether the front and back of the token is a letter
        int asciiFirst = word.charAt(0);
        int asciiLast = word.charAt(word.length()-1);
        while (!(asciiFirst >= 97 && asciiFirst <= 122) || !(asciiLast >= 97 && asciiLast <= 122)) {
            boolean front = !(asciiFirst >= 97 && asciiFirst <= 122); //check which ones, front/back, needs a change
            boolean back = !(asciiLast >= 97 && asciiLast <= 122);
            if (word.length() > 1){
                if (front) {
                    word = word.substring(1);
                }
                if (back) {
                    word = word.substring(0, word.length()-1);
                }
            } else if (word.length() == 1){ // this word can be "a" or "I", so I see if front is a letter. if not, the word is not a word
                if (front) {
                    return "thisisnotaword";
                }
            } else { //if string is empty, it must have been an all symbol token (symbols have all been removed)
                return "thisisnotaword";
            }
            /*
            sometimes after passing through the previous condition statement, the "word" will go from for example "00" to ""
            Then a call to charAt() will give an index out of bounds exception.
            Therefore, I check again to see if the string is empty, if so, it is not a word.
            If not, I update the ascii of the first and last characters.
             */
            if (word.length() > 0) {
                asciiFirst = word.charAt(0);
                asciiLast = word.charAt(word.length()-1);
            } else {
                return "thisisnotaword";
            }
        }
        return word;
    }
    public static ArrayList<Word> removeEmpty(ArrayList<Word> arr) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).equals("thisisnotaword")) {
                arr.remove(i);
                i--;
            }
        }
        return arr;
    }
    public static ArrayList<Word> sortWords(ArrayList<Word> arr) {
        for (int i = 0; i < arr.size(); i++) { //bubble sort algorithm in descending order
            for (int j = 0; j < arr.size()-1-i; j++) {
                int count1 = arr.get(j).getCount();
                int count2 = arr.get(j+1).getCount();
                if (count1 < count2) {
                    Word tempWord = new Word(arr.get(j).getWord());
                    int tempCount = arr.get(j).getCount();
                    tempWord.setCount(tempCount);
                    arr.set(j, arr.get(j+1));
                    arr.set(j+1, tempWord);
                }
            }
        }
        return arr;
    }
// given a sorted arraylist of words (descending count), top five is just first 5 elments
    public static void printTopFive(ArrayList<Word> arr) {
        System.out.println("The top 5 most common words in this text were:");
        for (int i = 0; i < 5; i++) {
            System.out.println(i+1 + ". \""+ arr.get(i).getWord() + "\" with " + arr.get(i).getCount() + " usages.");
        }
    }
}
