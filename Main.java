package application;
	
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class Main extends Application {
	double originalPrice = 0;
	@Override
	public void start(Stage primaryStage) {
		
		try {
			BorderPane sellerView = new BorderPane();
			
			
			
			
			//SellerView -----------------------------------
			//sellerView.setPadding(new Insets(50,250,50,250));
			GridPane centerpane = new GridPane();
			centerpane.setAlignment(Pos.CENTER);
			centerpane.setMaxHeight(500);
			centerpane.setMaxWidth(300);
			centerpane.setHgap(10);
			centerpane.setVgap(10);
			centerpane.setPadding(new Insets(10));
			sellerView.setStyle("-fx-background-color: orangered;");
			centerpane.setStyle("-fx-background-color: grey;");
			Text scenetitle = new Text("Sell Your Book");
			scenetitle.setTextAlignment(TextAlignment.CENTER);
			scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
			centerpane.add(scenetitle,0,0);
			
			Text ititle = new Text("");
			ititle = new Text("Name of your book");
			TextField bookName = new TextField();
			centerpane.add(bookName, 0, 2);
			centerpane.add(ititle, 0, 1);
			
			ititle = new Text("Category");
			TextField bookCategory = new TextField();
			centerpane.add(bookCategory, 0, 5);
			centerpane.add(ititle, 0, 4);
			
			ititle = new Text("Condition");
			TextField bookCondition = new TextField();
			centerpane.add(bookCondition, 0, 8);
			centerpane.add(ititle, 0, 7);
			
			ititle = new Text("Original Price");
			TextField bookOriginalPrice = new TextField();
			centerpane.add(bookOriginalPrice, 0, 11);
			centerpane.add(ititle, 0, 10);
			
			ititle = new Text("Buying Price");
			centerpane.add(ititle, 0, 13);
			Label bookBuyingPrice = new Label("$0.00");
			bookBuyingPrice.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));;
			centerpane.add(bookBuyingPrice, 0, 14);
			
			bookOriginalPrice.textProperty().addListener((observable, oldValue, newValue) -> {
				try {
					if(bookOriginalPrice.getText() == "") {
						originalPrice = 0;
					}else {
						originalPrice = Integer.parseInt(bookOriginalPrice.getText());
					}
					//replace this with actual equation
					bookBuyingPrice.setText("$" + originalPrice*.75);
				}catch(NumberFormatException ex) {
					bookBuyingPrice.setText("invalid value entered for original price");
				}
			});
			
			Button sellConfirm = new Button("List My Book");
			sellConfirm.setOnAction((ActionEvent e )-> {
				/*
				 * = bookName.getText();
				 * = bookCategory.getText();
				 * = bookCondition.getText();
				 * = bookOriginalPrice.getText();
				 * 
				 * 
				 */
			});
			
			centerpane.add(sellConfirm, 0, 16);
			sellerView.setCenter(centerpane);
			//SellerView End----------------
			
			Scene scene = new Scene(sellerView,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
