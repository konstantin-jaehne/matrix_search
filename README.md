# Analyse und experimenteller Vergleich von Suchalgorithmen für sortierte Matrizen

Dieses Repository entstand im Rahmen einer Bachelorarbeit von Konstantin Jaehne an der Freien Universität Berlin, in der verschiedene auf nxm-Matrizen operierende Suchalgorithmen miteinander verglichen werden.
Prämisse ist hierbei, dass die Matrizen spalten- und zeilenweise vorsortiert sind. Aus praktischen Gründen wird oBdA weiterhin angenommen, dass n*m eine Viererpotenz ist, und alle Matrixwerte sind nichtnegative natürliche Zahlen.
Betrachtet werden insgesamt drei Suchalgorithmen:
* SELECT, welcher in der Arbeit detailliert erörtert wird
* ITERATIVE_ELIMINATION, bei dem iterativ jeweils eine Zeile oder eine Spalte der Matrix eliminiert wird, bis auf das gesuchte Element gestoßen wird
* 1D_BINSEARCH, welcher die Vorsortierung der Matrix nicht ausnutzt und stattdessen mit einem herkömmlichen Sortieralgorithmus die Matrix in eine eindimensionale Datenstruktur umwandelt und anschließend mit traditioneller Binärsuche durchsucht

Alle drei Algorithmen sind in ``Search.java`` implementiert. Es existieren außerdem Testreihen, welche diese jeweils bezüglich verschiedener Metriken wie Laufzeit, Anzahl von Vergleichen und Anzahl von Orakelaufrufen vergleichen.

# Voraussetzungen

* Java 8 oder eine neuere Version
* Start.java führt die für die Arbeit relevanten Testreihen aus