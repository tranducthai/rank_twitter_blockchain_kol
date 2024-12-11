package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csv.ReadCsv;

public class BuildGraph {
    private List<Node> node;
    private String interactionPath;
    private String followerCountPath;
    private Map<String, Integer> labelMap;

    public BuildGraph() {
        this.interactionPath = "";
        this.followerCountPath = "";
        this.node = new ArrayList<>();
        labelMap = new HashMap<>();
    }

    public BuildGraph(String interactionPath, String followerCountPath) {
        this.interactionPath = interactionPath;
        this.followerCountPath = followerCountPath;
        this.node = new ArrayList<>();
        labelMap = new HashMap<>();
    }

    public void setInteractionPath(String interactionPath){
        this.interactionPath = interactionPath;
    }

    public void setFollowerCountPath(String followerCountPath) {
        this.followerCountPath = followerCountPath;
    }

    public List<Node> getListNode() {
        return this.node;
    }

    // Thêm đỉnh vào đồ thị
    public void addNode() {
        // Cột 1 là tên, cột 2 là số folowers
        List<String> name = ReadCsv.readCsvToList(followerCountPath, "A");
        List<String> follower = ReadCsv.readCsvToList(followerCountPath, "B");

        int totalNode = name.size();
        for(int i = 0; i < totalNode; i++) {
            int followerCount = Integer.parseInt(follower.get(i));
            if(Kol.isKol(followerCount) == false) { // Số fl < 50k
                continue;
            }

            Kol currentKol = new Kol(name.get(i), followerCount);
            node.add(new Node(currentKol, i));
            labelMap.put(name.get(i), i);
        }
    }

    // Nối các cạnh lại
    public void addEdge() {
        // Cột 2 follow cột 1
        // Hoặc cột 2 cmt/repost 1 tweet của cột 1
        List<String> nameA = ReadCsv.readCsvToList(interactionPath, "A");
        List<String> nameB = ReadCsv.readCsvToList(interactionPath, "B");

        int n = nameA.size();
        for(int i = 0; i < n; i++) {
            if(labelMap.containsKey(nameA.get(i)) == false || labelMap.containsKey(nameB.get(i)) == false) continue;
            int v = labelMap.get(nameA.get(i));
            int u = labelMap.get(nameB.get(i));

            // Có 1 cạnh có huớng từ u -> v;
            node.get(v).addEdge(u);
            node.get(u).setOutGoingSize(node.get(u).getOutGoingSize() + 1); // degOut[u]++;
        }
    }

    public void build() {
        addNode();
        addEdge();
    }

    public int totalNode() {
        return this.node.size();
    }

    public int totalEdge() {
        int count = 0;
        for(Node i : node) {
            count += i.degOut();
        }
        return count;
    }

    public void println() {
        for (Node i : node) {
            System.out.println("Tên kol : " + i.getKol().getName());
            System.out.println("Số followers : " + i.getKol().getFollower());
            System.out.println("Nhãn : " + i.getLabel());
            System.out.println("Danh sách cạnh nối đến :" + i.getInGoing());
        }
    }

}
