package graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Kol kol;
    private int label;
    private double point;
    private int outGoingSize;
    private List<Integer> inGoing; // Các đỉnh có cạnh nối vào nó

    public Node() {
        this.kol = null;
        this.label = -1;
        this.point = 0.0;
        this.outGoingSize = 0;
        this.inGoing = new ArrayList<>();
    }

    public Node(Kol kol, int label) {
        this.kol = kol;
        this.label = label;
        this.point = 0.0;
        this.outGoingSize = 0;
        this.inGoing = new ArrayList<>();
    }

    public Kol getKol() {
        return this.kol;
    }

    public void setKol(Kol kol) {
        this.kol = kol;
    }

    public int getLabel() {
        return this.label;
    }

    public void setLabel(int label){
        this.label = label;
    }

    public double getPoint() {
        return this.point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public int getOutGoingSize() {
        return outGoingSize;
    }

    public void setOutGoingSize(int outGoingSize) {
        this.outGoingSize = outGoingSize;
    }

    public List<Integer> getInGoing() {
        return this.inGoing;
    }

    // Thêm 1 cạnh có hướng vào node
    public boolean addEdge(int label){
        // label phải khác this.label
        if(label < 0 || label == this.label) {
            return false;
        }

        if(inGoing.contains(label)) { // Số cạnh khoảng xxxx nên không lo LTE (Thích thì vẫn optimize được)
            return false;
        }

        this.inGoing.add(label);
        return true;
    }

    // Bán bậc ra
    public int degOut() {
        return this.outGoingSize;
    }

    // Bán bậc vào
    public int degIn() {
        return this.inGoing.size();
    }
}
