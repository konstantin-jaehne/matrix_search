import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GenerateTestData {

    /**
     Diese Funktion gibt eine zufällig generierte sortierte Matrix der Dimension nxm zurück.
     Die Einträge sind nichtnegative Integers.
     Es wird uniform distribution gewährleistet und für jede Menge an Einträgen ist jede gültige Sortierung möglich.
     */
    public static int[][] generateRandomSortedMatrix(int n, int m) {
        // Init
        Random random = new Random();
        int[][] result = new int[n][m];
        int[] currentLengths = new int[n];

        // Generiere nxm Integers und sortiere diese
        List<Integer> randomNumbers = new ArrayList<>();
        for (int i = 0; i < n*m; i++) {
            randomNumbers.add(random.nextInt(Integer.MAX_VALUE));
        }
        Collections.sort(randomNumbers);

        // Setze die generierten Werte nacheinander in die Ergebnismatrix ein
        for (Integer value : randomNumbers) {
            // Wähle zufällige verfügbare Zeile zum Einsetzen
            List<Integer> availableIndices = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (currentLengths[i] < m && (i == 0 || currentLengths[i] < currentLengths[i-1])) {
                    availableIndices.add(i);
                }
            }
            int chooseLine = availableIndices.get(random.nextInt(availableIndices.size()));

            // Setze value ein und update currentLengths
            result[chooseLine][currentLengths[chooseLine]] = value;
            currentLengths[chooseLine]++;
        }

        return result;
    }

}
