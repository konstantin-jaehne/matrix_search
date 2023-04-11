import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Search {

    // Fixe Werte
    private final int[][] startMatrix; // int[zeile][spalte]
    private final int n; // Zeilen
    private final int m; // Spalten, m >= n

    public Search(int[][] startMatrix) {
        this.startMatrix = startMatrix;
        this.n = startMatrix.length;
        this.m = startMatrix[0].length;
    }

    public Stats search_SEARCH(int value) {
        // Init Stats und Interval
        Stats stats = new Stats();
        long startTime = System.currentTimeMillis();
        float L1 = startMatrix[0][0];
        float L2 = Integer.MAX_VALUE;

        // Teile die Startmatrix in 4 Teilmatrizen auf
        List<int[][]> subMatrices = split(stats, Collections.singletonList(startMatrix), L1, L2);

        while (!subMatrices.isEmpty()) {
            // Initialisiere R
            List<Integer> R = new ArrayList<>();
            if (subMatrices.get(0).length == 1 && subMatrices.get(0)[0].length == 1) {
                R = subMatrices.stream().map(M -> M[0][0]).collect(Collectors.toList());
            } else {
                for (int[][] M : subMatrices) {
                    R.add(getMin(M));
                    R.add(getMax(M));
                }
            }

            for (int i = 0; i < 2; i++) {
                // Entferne alle Werte außerhalb des Intervals (L1,L2) aus R -> Rp
                stats.incComparisons(2 * R.size());
                float finalL = L1;
                float finalL1 = L2;
                List<Integer> Rp = R.stream().filter(v -> v > finalL && v < finalL1).collect(Collectors.toList());

                if (!Rp.isEmpty()) {
                    // Wähle den Median -> L
                    float L = getMedian(stats, Rp);

                    // Orakelaufruf (L1 oder L2 wird angepasst)
                    if (L > value) {
                        L2 = L;
                    } else {
                        L1 = L;
                    }
                    stats.incOracleCalls();
                }
            }

            // Entferne alle Matrizen mit Werten außerhalb des Intervals (L1,L2)
            float finalL2 = L1;
            float finalL3 = L2;
            subMatrices.removeIf(M -> !isValidMatrix(stats, M, finalL2, finalL3));

            // Falls die Matrizen mehr als ein Element enthalten, teile sie jeweils in 4 Submatrizen auf
            if (!subMatrices.isEmpty() && (subMatrices.get(0).length > 1 || subMatrices.get(0)[0].length > 1)) {
                subMatrices = split(stats, subMatrices, L1, L2);
            }
        }

        // Überprüfung des Ergebnisses (es sollte L1 = value gelten)
        long endTime = System.currentTimeMillis();
        stats.setTime(endTime - startTime);
        if (L1 == value) {
        } else {
            stats.makeUnsuccessful();
        }
        return stats;
    }

    public Stats search_ITERATIVE_ELIMINATION(int value) {
        // Init Stats
        Stats stats = new Stats();
        long startTime = System.currentTimeMillis();

        // Algorithmus
        int i = 0;
        int k = m - 1;

        while (i < n && k >= 0) {
            // Orakelaufruf
            stats.incOracleCalls();
            if (startMatrix[i][k] > value) {
                k--;
            } else if (startMatrix[i][k] < value) {
                i++;
            } else {
                break;
            }
        }

        // Überprüfung des Ergebnisses
        long endTime = System.currentTimeMillis();
        stats.setTime(endTime - startTime);
        if (startMatrix[i][k] == value) {
        } else {
            stats.makeUnsuccessful();
        }
        return stats;
    }

    public Stats search_1D_BINSEARCH(int value) {
        // Init Stats
        Stats stats = new Stats();
        long startTime = System.currentTimeMillis();

        // 2D -> 1D und sortiere
        List<Integer> flattenedList = new ArrayList<>();
        for (int[] row : startMatrix) {
            for (int element : row) {
                flattenedList.add(element);
            }
        }
        Collections.sort(flattenedList);
        stats.incComparisons((int) (n * m * Math.log(n * m))); // Liste der Länge nm kann in nmlog(nm) Vergleichen sortiert werden, Orakelaufrufe sind nicht nötig

        // Binärsuche
        int LEFT = 0;
        int RIGHT = flattenedList.size() - 1;
        stats.incOracleCalls();
        while (LEFT <= RIGHT) {
            int MIDDLE = (LEFT + RIGHT) / 2;
            stats.incOracleCalls();
            if (flattenedList.get(MIDDLE) > value) {
                RIGHT = MIDDLE - 1;
            } else if (flattenedList.get(MIDDLE) < value) {
                LEFT = MIDDLE + 1;
            } else {
                LEFT = MIDDLE;
                RIGHT = MIDDLE;
                break;
            }
        }

        // Überprüfung des Ergebnisses
        long endTime = System.currentTimeMillis();
        stats.setTime(endTime - startTime);
        if (flattenedList.get(LEFT) == value && flattenedList.get(RIGHT) == value) {
        } else {
            stats.makeUnsuccessful();
        }
        return stats;
    }

    /**
     * Gibt das kleinste Element in der sortierten Matrix M zurück.
     */
    private int getMin(int[][] M) {
        return M[0][0];
    }

    /**
     * Gibt das größte Element in der sortierten Matrix M zurück.
     */
    private int getMax(int[][] M) {
        return M[M.length-1][M[0].length-1];
    }

    /**
     * Gibt die Submatrix zwischen (x1,y1) und (x2,y2) zurück.
     */
    private int[][] makeSubmatrix(int[][] M, int x1, int y1, int x2, int y2) {
        int[][] subM = new int[x2-x1][y2-y1];
        for (int i = 0; i < x2-x1; i++) {
            subM[i] = Arrays.copyOfRange(M[x1+i], y1, y2);
        }
        return subM;
    }

    /**
     * Nimmt an dass Matrixdimensionen 2er-Potenzen sind und dass Input nichtleer ist.
     * Es wird in 4 gleich große Teilmatrizen gesplittet. Matrizen außerhalb von (L1,L2) werden direkt entfernt.
     * Für dicke Matrizen wird in beiden Dimensionen in der Mitte gesplittet.
     * Für dünne Matrizen wird in einer Dimension gevierteilt.
     */
    private List<int[][]> split(Stats stats, List<int[][]> matrices, float L1, float L2) {
        List<int[][]> newList = new ArrayList<>();
        int sizeX = matrices.get(0).length;
        int sizeY = matrices.get(0)[0].length;
        // Falls Matrizen dick:
        if (sizeX > 1) {
            for (int[][] m : matrices) {
                int[][] submatrix1 = makeSubmatrix(m, 0, 0, sizeX/2, sizeY/2);
                if (isValidMatrix(stats, submatrix1, L1, L2)) {
                    newList.add(submatrix1);
                }
                int[][] submatrix2 = makeSubmatrix(m, 0, sizeY/2, sizeX/2, sizeY);
                if (isValidMatrix(stats, submatrix2, L1, L2)) {
                    newList.add(submatrix2);
                }
                int[][] submatrix3 = makeSubmatrix(m, sizeX/2, 0, sizeX, sizeY/2);
                if (isValidMatrix(stats, submatrix3, L1, L2)) {
                    newList.add(submatrix3);
                }
                int[][] submatrix4 = makeSubmatrix(m, sizeX/2, sizeY/2, sizeX, sizeY);
                if (isValidMatrix(stats, submatrix4, L1, L2)) {
                    newList.add(submatrix4);
                }
            }
        // Falls Matrizen dünn:
        } else {
            for (int[][] m : matrices) {
                int[][] submatrix1 = makeSubmatrix(m, 0, 0, 1, sizeY/4);
                if (isValidMatrix(stats, submatrix1, L1, L2)) {
                    newList.add(submatrix1);
                }
                int[][] submatrix2 = makeSubmatrix(m, 0, sizeY/4, 1, sizeY/2);
                if (isValidMatrix(stats, submatrix2, L1, L2)) {
                    newList.add(submatrix2);
                }
                int[][] submatrix3 = makeSubmatrix(m, 0, sizeY/2, 1, 3*sizeY/4);
                if (isValidMatrix(stats, submatrix3, L1, L2)) {
                    newList.add(submatrix3);
                }
                int[][] submatrix4 = makeSubmatrix(m, 0, 3*sizeY/4, 1, sizeY);
                if (isValidMatrix(stats, submatrix4, L1, L2)) {
                    newList.add(submatrix4);
                }
            }
        }
        return newList;
    }

    /**
     * Prüft ob eine Matrix nicht vollständig außerhalb des Intervals (L1,L2) liegt.
     */
    private boolean isValidMatrix(Stats stats, int[][] M, float L1, float L2) {
        stats.incComparisons(2);
        return !(getMax(M) <= L1 || getMin(M) >= L2);
    }

    /**
     * Berechnet den Median einer Liste von Integers.
     * Im Falle einer geraden Anzahl von Elementen wird das arithmetische Mittel der beiden Kandidaten zurückgegeben.
     * Hinweis: Die vorliegende Implementierung ist nicht O(n), dies wäre jedoch mit dem Median of Medians Algorithmus möglich.
     */
    private float getMedian(Stats stats, List<Integer> L) {
        List<Integer> sortedList = L.stream().sorted().collect(Collectors.toList());
        stats.incComparisons(L.size()); // Linear viele Vergleiche unter Annahme des effizienteren Algorithmus
        if (L.size() % 2 == 1) {
            return sortedList.get((L.size()-1)/2);
        } else {
            return ((float)sortedList.get(L.size()/2) + sortedList.get(L.size()/2-1)) / 2;
        }
    }

}
