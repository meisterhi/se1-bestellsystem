package datamodel;

public class sID {
    private int id;

    /**
     * Creates a new sID with the specified prefix and starting ID.
     * @param prefix the prefix for the ID
     * @param startid the starting ID
     */

    protected sID(int startid) {
        this.id = startid;
    }
    protected sID() {
        this(1);
    }
    /**
     * Returns the next ID in the sequence.
     * @return the next ID
     */
    public int next() {
        add();
        return id;
    }
    public String nextString(String prefix) {
        add();
        return String.format("%s%06d", prefix,id);
    }
    /**
     * Returns the next ID in the sequence with the specified prefix.   
     * @param prefix the prefix for the ID
     * @return the next ID
     */
    public Long nextLong() {
        add();
        return (long)id;
    }
    private void add() {
        id++;
    }

}
