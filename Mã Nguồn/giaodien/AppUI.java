package giaodien;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
public class AppUI extends Application {
    // Hàm tạo bảng
    @SuppressWarnings("unchecked")
	public TableView<RankedKol> createTable(List<RankedKol> data) {
        TableView<RankedKol> tableView = new TableView<>();

        TableColumn<RankedKol, String> objectColumn = new TableColumn<>("KOL");
        objectColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<RankedKol, Integer> followColumn = new TableColumn<>("Followers");
        followColumn.setCellValueFactory(new PropertyValueFactory<>("followers"));
        
        TableColumn<RankedKol, Float> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<RankedKol, Integer> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));

        tableView.getColumns().addAll(objectColumn, followColumn, scoreColumn, rankColumn);

        ObservableList<RankedKol> observableData = FXCollections.observableArrayList(data);

        tableView.setItems(observableData);

        return tableView;
    }

	
    @Override
    public void start(Stage primaryStage) {
     // Tạo dữ liệu cho hai bảng
     String filepath="resource\\SortedPageRankResult.csv";
     String filepath2="resource\\SortedPageRankResult2.csv";
     
     List<RankedKol> rKOL=GetRKOL.getRankedKol(filepath);
     List<RankedKol> rKOL2=GetRKOL.getRankedKol(filepath2);
     
     GetRKOL.calculateRanks(rKOL);
     GetRKOL.calculateRanks(rKOL2);
     
     // Tạo bảng 
     TableView<RankedKol> tableA = createTable(rKOL);
     TableView<RankedKol> tableB = createTable(rKOL2);


     TextField searchField = new TextField();
     searchField.setPromptText("Nhập tên đối tượng...");

     Button searchButton = new Button("Tìm kiếm");


     Label resultLabelA = new Label();
     resultLabelA.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

     Label resultLabelB = new Label();
     resultLabelB.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

     HBox searchBox = new HBox(10, searchField, searchButton);
     searchBox.setStyle("-fx-padding: 10;");

     // Xử lý tìm kiếm khi nhấn nút
     searchButton.setOnAction(e -> {
            String searchText = searchField.getText();
            String resultA = GetRKOL.searchObject(rKOL, searchText);
            String resultB = GetRKOL.searchObject(rKOL2, searchText);

            resultLabelA.setText("Thứ hạng theo Data1: " + resultA);
            resultLabelB.setText("Thứ hạng theo Data2: " + resultB);
        });

    
     VBox sectionA = new VBox(new Label("PageRank Data1"), tableA);
     VBox sectionB = new VBox(new Label("PageRank Data2"), tableB);
     SplitPane splitPane = new SplitPane(sectionA, sectionB);

     VBox layout = new VBox(10, searchBox, resultLabelA, resultLabelB, splitPane);
     Scene scene = new Scene(new StackPane(layout), 800, 600);

     primaryStage.setTitle("Search Object with Rank");
     primaryStage.setScene(scene);
     primaryStage.show();
    }
}    