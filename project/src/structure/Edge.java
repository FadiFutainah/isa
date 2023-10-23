package structure;

public class Edge {
    public int destination;
    public double length;
    public boolean impossibleToDrive;
    public String busPath;
    public double busSpeed;
    public double taxiSpeed;

    public Edge(int destination, double length, boolean impossibleToDrive, String busPath, double busSpeed, double taxiSpeed) {
        this.destination = destination;
        this.length = length;
        this.impossibleToDrive = impossibleToDrive;
        this.busPath = busPath;
        this.busSpeed = busSpeed;
        this.taxiSpeed = taxiSpeed;
    }
}
