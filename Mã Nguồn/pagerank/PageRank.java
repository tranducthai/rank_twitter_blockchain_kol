package pagerank;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import csv.WriteCsv;
import graph.BuildGraph;
import graph.Node;

public class PageRank {
    private BuildGraph graph;
    private double[] pageRanks; 		 // Lưu giá trị PageRank của mỗi node
    private final static double DAMPING_FACTOR = 0.85; // Hệ số giảm
    private final static int MAX_ITERATIONS = 1000;    // Số lần lặp tối đa
    private final static double TOLERANCE = 1.0e-6;   // Độ chính xác hội tụ

    public PageRank() {
    }

    public PageRank(BuildGraph graph) {
        this.graph = graph;
        this.pageRanks = new double[graph.totalNode()];
    }

    public void assignPageRanksToGraph() {
        List<Node> nodeList = graph.getListNode(); // Lấy danh sách các Node từ graph

        for (int i = 0; i < nodeList.size(); i++) {
            nodeList.get(i).setPoint(pageRanks[i]); // Gán giá trị PageRank vào thuộc tính point
        }
    }

    public void calculatePageRank() {
        int n = graph.totalNode();
        double[] tempPageRanks = new double[n];
        boolean isConverged;
        List<Node> nodeList = graph.getListNode();

        // Khởi tạo giá trị PageRank ban đầu
        for (int i = 0; i < n; i++) {
            pageRanks[i] = 1.0 / n;
        }

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            isConverged = true;

            // Tính tổng giá trị PageRank của các node treo (dangling nodes)
            double danglingRankSum = 0.0;
            for (int i = 0; i < n; i++) {
                Node node = graph.getListNode().get(i);
                if (node.degOut() == 0) {
                    danglingRankSum += pageRanks[i];
                }
            }

            for (int i = 0; i < n; i++) {
                Node node = nodeList.get(i);
                List<Integer> adjacencyList = node.getInGoing();

                double rankSum = 0.0;

                // Tính tổng giá trị PageRank từ các node liên kết đến node này
                for (int neighbor : adjacencyList) {
                    Node neighborNode = nodeList.get(neighbor);
                    rankSum += pageRanks[neighbor] / neighborNode.degOut();
                }

                // Tính pagerank của node theo công thức (đã tính đến việc có node treo)
                tempPageRanks[i] = (1 - DAMPING_FACTOR) / n + DAMPING_FACTOR * (rankSum + danglingRankSum/n);

                // Kiểm tra hội tụ
                if (Math.abs(tempPageRanks[i] - pageRanks[i]) > TOLERANCE) {
                    isConverged = false;
                }
            }

            // Cập nhật giá trị PageRank
            System.arraycopy(tempPageRanks, 0, pageRanks, 0, n);

            // Dừng nếu hội tụ
            if (isConverged) {
                break;
            }
        }

        assignPageRanksToGraph();
    }

    public List<Node> getSortedRank() {
        List<Node> nodeList = new ArrayList<>(graph.getListNode());

        nodeList.sort(Comparator.comparingDouble(Node::getPoint).reversed());

        return nodeList;
    }

    public void printSortedRank() {
        List<Node> nodeList = getSortedRank();

        System.out.println("Danh sách các node theo thứ tự PageRank giảm dần:");

        DecimalFormat formatter = new DecimalFormat("0.###E0");

        for (Node node : nodeList) {
            String nodeName = node.getKol().getName();
            double pageRank = node.getPoint();
            String formattedRank = formatter.format(pageRank);
            System.out.println(nodeName + " - PageRank: " + formattedRank);
        }
    }

    public void writePageRankToCSV(String filePath) {
        List<Node> nodeList = getSortedRank(); // Lấy danh sách các node từ BuildGrap

        //Định dạng pagerank khi viết ra file
        DecimalFormat formatter = new DecimalFormat("0.###E0");

        // Ghi thông tin từng node
        for (Node node : nodeList) {
            String nodeName = node.getKol().getName(); 		// Tên node (Kol)
            int followers = node.getKol().getFollower(); 	//Số followers của kol đó
            double pageRank = node.getPoint(); 				// Giá trị PageRank
            String formattedRank = formatter.format(pageRank); // Định dạng PageRank

            String[] line = {nodeName, formattedRank, String.valueOf(followers)};
            WriteCsv.writeListToCSV(filePath, line);
        }

        System.out.println("Đã tạo file CSV");
    }
}
