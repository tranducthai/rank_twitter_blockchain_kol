package mainrunner;

import javax.swing.JOptionPane;

import crawler.Crawler;
import giaodien.AppUI;
import graph.BuildGraph;
import javafx.application.Application;
import pagerank.PageRank;

public class Main {
    public static void main(String[] args) throws InterruptedException {
    	String[] options = {"Crawler", "Build Graph", "Build Graph and PageRank", "Statistics"};
    	String choice = (String) JOptionPane.showInputDialog(
    		    null,
    		    "Chọn chức năng bạn muốn thực hiện:",
    		    "Menu Chức Năng",
    		    JOptionPane.INFORMATION_MESSAGE,
    		    null,
    		    options,
    		    options[0]
    		);
    	
    	if(choice.equals("Crawler")) {
    		 Crawler.crawler();
             javax.swing.JOptionPane.showMessageDialog(null, "Hoàn thành!", "Thông báo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    		 return;
    	}
    	
    	if(choice.equals("Build Graph")) {
    		BuildGraph graph = new BuildGraph("resource\\Interaction.csv","resource\\FollowerCount.csv");
    		graph.build();
    		return;
    	}
    	
    	if(choice.equals("Build Graph and PageRank")) {
    		BuildGraph graph = new BuildGraph("resource\\Interaction.csv", "resource\\FollowerCount.csv");
    		graph.build();
    		PageRank pageRank = new PageRank(graph);
    	    pageRank.calculatePageRank();
    	    pageRank.writePageRankToCSV("resource\\SortedPageRankResult.csv");
    	    return;
    	}
    	
    	if(choice.equals("Statistics")) {
    		Application.launch(AppUI.class, args);
    	}
    }
    
}
