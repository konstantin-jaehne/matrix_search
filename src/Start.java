import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Start {

    public static void main(String[] args) {
        // Testserie A: m wird größer
        System.out.println("Testserie A");
        for (int i = 2; i < 24; i+=2) {
            int m = (int) Math.pow(2, i);
            List<Stats> resultsPath0 = new ArrayList<>();
            List<Stats> resultsBasic = new ArrayList<>();
            List<Stats> results1Dsorting = new ArrayList<>();

            // 10 Durchgänge mit jeweils verschiedenen Matrizen
            for (int k = 0; k < 10; k++) {
                int[][] M = GenerateTestData.generateRandomSortedMatrix(4, m); // Generiere zufällige Matrix
                int value = pickValue(M); // Wähle zufälligen in Matrix enthaltenen Wert aus
                Search search = new Search(M);
                resultsPath0.add(search.search_SELECT(value));
                resultsBasic.add(search.search_ITERATIVE_ELIMINATION(value));
                results1Dsorting.add(search.search_1D_BINSEARCH(value));
            }
            printResults("SELECT", resultsPath0);
            printResults("ITERATIVE_ELIMINATION", resultsBasic);
            printResults("1D_BINSEARCH", results1Dsorting);
            System.out.println();
        }

        // Testserie B: n und m werden größer
        System.out.println("Testserie B");
        for (int i = 2; i < 13; i++) {
            int m = (int) Math.pow(2, i);
            List<Stats> resultsPath0 = new ArrayList<>();
            List<Stats> resultsBasic = new ArrayList<>();
            List<Stats> results1Dsorting = new ArrayList<>();

            // 10 Durchgänge mit jeweils verschiedenen Matrizen
            for (int k = 0; k < 10; k++) {
                int[][] M = GenerateTestData.generateRandomSortedMatrix(m, m); // Generiere zufällige Matrix
                int value = pickValue(M); // Wähle zufälligen in Matrix enthaltenen Wert aus
                Search search = new Search(M);
                resultsPath0.add(search.search_SELECT(value));
                resultsBasic.add(search.search_ITERATIVE_ELIMINATION(value));
                results1Dsorting.add(search.search_1D_BINSEARCH(value));
            }
            printResults("SELECT", resultsPath0);
            printResults("ITERATIVE_ELIMINATION", resultsBasic);
            printResults("1D_BINSEARCH", results1Dsorting);
            System.out.println();
        }

        // Testserie C: m wird größer, größeres fixes n
        System.out.println("Testserie C");
        for (int i = 5; i < 20; i+=2) {
            int m = (int) Math.pow(2, i);
            List<Stats> resultsPath0 = new ArrayList<>();
            List<Stats> resultsBasic = new ArrayList<>();
            List<Stats> results1Dsorting = new ArrayList<>();

            // 10 Durchgänge mit jeweils verschiedenen Matrizen
            for (int k = 0; k < 10; k++) {
                int[][] M = GenerateTestData.generateRandomSortedMatrix(32, m); // Generiere zufällige Matrix
                int value = pickValue(M); // Wähle zufälligen in Matrix enthaltenen Wert aus
                Search search = new Search(M);
                resultsPath0.add(search.search_SELECT(value));
                resultsBasic.add(search.search_ITERATIVE_ELIMINATION(value));
                results1Dsorting.add(search.search_1D_BINSEARCH(value));
            }
            printResults("SELECT", resultsPath0);
            printResults("ITERATIVE_ELIMINATION", resultsBasic);
            printResults("1D_BINSEARCH", results1Dsorting);
            System.out.println();
        }
    }

    /**
     * Kombiniert eine Liste von Stats-Objekten zu einem, indem die Mittelwerte der einzelnen Attribute berechnet werden.
     */
    private static Stats combine(List<Stats> stats) {
        int size = stats.size();
        Stats average = new Stats();
        average.incComparisons(stats.stream().mapToDouble(Stats::getComparisons).sum() / size);
        average.setOracleCalls(stats.stream().mapToDouble(Stats::getOracleCalls).sum() / size);
        average.setTime(stats.stream().mapToLong(Stats::getTime).sum() / size);
        if (stats.stream().anyMatch(s -> !s.isSuccessful())) {
            average.makeUnsuccessful();
        }
        return average;
    }

    /**
     * Eine Liste von Ergebnissen wird gemerged indem die Werte gemittelt werden und anschließend ausgegeben.
     */
    private static void printResults(String algorithm, List<Stats> results) {
        String FORMAT_OUTPUT = "%s: Orakel-Aufrufe = %.2f | Vergleiche = %.2f | Zeit = %sms%n";
        Stats average = combine(results);
        System.out.printf(FORMAT_OUTPUT, algorithm, average.getOracleCalls(), average.getComparisons(), average.getTime() % 1000);
        if (!average.isSuccessful()) {
            System.out.println("FEHLER: Suche war nicht erfolgreich!");
        }
    }

    /**
     * Wählt einen zufälligen in der Matrix M enthaltenen Wert aus.
     */
    private static int pickValue(int[][] M) {
        int sizeX = M.length;
        int sizeY = M[0].length;
        Random random = new Random();
        int x = random.nextInt(sizeX);
        int y = random.nextInt(sizeY);
        return M[x][y];
    }

}
