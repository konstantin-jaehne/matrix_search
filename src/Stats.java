public class Stats {

    private double comparisons = 0;
    private double oracleCalls = 0;
    private long time = 0L;
    private boolean successful = true;

    public boolean isSuccessful() {
        return successful;
    }

    public void makeUnsuccessful() {
        this.successful = false;
    }

    public double getComparisons() {
        return comparisons;
    }

    public double getOracleCalls() {
        return oracleCalls;
    }

    public long getTime() {
        return time;
    }

    public void incComparisons(double i) {
        comparisons += i;
    }

    public void incOracleCalls() {
        oracleCalls++;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setOracleCalls(double oracleCalls) {
        this.oracleCalls = oracleCalls;
    }

}
