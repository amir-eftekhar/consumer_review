import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Sentiment {

    // Method to calculate sentiment values for Apple and Samsung articles

    
        public static void main(String[] args) {
            // Run the sentiment analysis for Apple and Samsung articles
            Sentiment sentiment = new Sentiment();
            sentiment.calculateAppleAndSamsungSentiment("apple.txt", "samsung.txt", "cleanSentiment.csv");
        }
    

    
        public void calculateAppleAndSamsungSentiment(String appleFile, String samsungFile, String sentimentFile) {
            // Load sentiment values from CSV file
            Map<String, Double> sentimentMap = loadSentimentValues(sentimentFile);
        
            // Load article texts from files
            String appleText = loadTextFromFile(appleFile);
            String samsungText = loadTextFromFile(samsungFile);
        
            // Split reviews using the separator "<<&&&&>>"
            String[] appleReviews = appleText.split("<<&&&&>>");
            String[] samsungReviews = samsungText.split("<<&&&&>>");
        
            // Calculate sentiment values for each article based on words found in sentiment file
            double appleSentiment = 0.0;
            for (String review : appleReviews) {
                appleSentiment += calculateSentiment(review, sentimentMap);
            }
            double samsungSentiment = 0.0;
            for (String review : samsungReviews) {
                samsungSentiment += calculateSentiment(review, sentimentMap);
            }
        
            // Calculate average sentiment scores
            double appleAverageSentiment = appleReviews.length > 0 ? appleSentiment / appleReviews.length : 0;
            double samsungAverageSentiment = samsungReviews.length > 0 ? samsungSentiment / samsungReviews.length : 0;
        
            // Output the results
System.out.println("Apple Total Sentiment Score: " + appleSentiment);
System.out.println("Apple Average Sentiment Score: " + appleAverageSentiment);
System.out.println("Samsung Total Sentiment Score: " + samsungSentiment);
System.out.println("Samsung Average Sentiment Score: " + samsungAverageSentiment);

        }
        

    // Method to load sentiment values from cleanSentiment CSV file
    public Map<String, Double> loadSentimentValues(String fileName) {
        Map<String, Double> sentimentMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String word = parts[0].trim().toLowerCase();
                    double value = Double.parseDouble(parts[1].trim());
                    sentimentMap.put(word, value);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the sentiment CSV file: " + e.getMessage());
        }
        return sentimentMap;
    }

    // Method to load text from a file
    public String loadTextFromFile(String fileName) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append(" ");
            }
        } catch (IOException e) {
            System.out.println("Error reading the text file: " + e.getMessage());
        }
        return text.toString();
    }

    // Method to calculate sentiment value of a given text using words in sentiment map
    public double calculateSentiment(String text, Map<String, Double> sentimentMap) {
        String[] lines = text.split("\\.");
        double sentimentScore = 0.0;

        // Nested loop to go through each line and then each word
        for (String line : lines) {
            String[] words = line.toLowerCase().split("\\s+");

            for (String word : words) {
                // Nested conditional to handle word processing
                if (word.length() > 1) {
                    if (word.endsWith(".") || word.endsWith(",") || word.endsWith("!") || word.endsWith("?")) {
                        word = word.substring(0, word.length() - 1); // Remove punctuation
                    }

                    // Check if the word is in the sentiment map
                    if (sentimentMap.containsKey(word)) {
                        sentimentScore += sentimentMap.get(word);
                    } else {
                        // Check if the word contains a hyphen (e.g., "well-made")
                        if (word.contains("-")) {
                            String[] subWords = word.split("-");
                            for (String subWord : subWords) {
                                if (sentimentMap.containsKey(subWord)) {
                                    sentimentScore += sentimentMap.get(subWord);
                                }
                            }
                        }
                    }
                }
            }
        }

        return sentimentScore;
    }
}
