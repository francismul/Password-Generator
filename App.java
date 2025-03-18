
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class App {
    public static void main(String[] args){
        System.out.println("Welcome to Password Generator App");

        List<String> lowercase = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
        List<String> uppercase = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
        List<String> digits = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        List<String> punctuation = new ArrayList<>(Arrays.asList("!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", "/", ":", ";", "<", "=", ">", "?", "@", "[", "\\", "]", "^", "_", "`", "{", "|", "}", "~"));

        int characterNumber = 20;
        Random rand = new Random();

        Collections.shuffle(lowercase, rand);
        Collections.shuffle(uppercase, rand);
        Collections.shuffle(digits, rand);
        Collections.shuffle(punctuation, rand);

        // calculate 30% & 20% of number of characters
        int part1 = Math.round(characterNumber * (30.0f / 100));
        int part2 = Math.round(characterNumber * (20.0f / 100));

        List<String> result = new ArrayList<>(20);

        for (int x = 0; x < part1; x++) {
            result.add(lowercase.get(x));
            result.add(uppercase.get(x));
        }
        
        for (int x = 0; x < part2; x++) {
            result.add(digits.get(x));
            result.add(punctuation.get(x));
        }

        System.out.println("Password: ");

        for (String pass : result){
            System.out.print(pass);
        }
        System.out.println();
    }
}