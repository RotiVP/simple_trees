package rot.simpletrees.guitester;

import rot.simpletrees.guitester.ui.*;

import javafx.application.Application;

import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.ComboBox;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class GuiTester extends Application {

	private static int counter;

	private short utilWidth = 410;
	private byte spnrMaxWidth = 100;

	@Override
	public void start(Stage primaryStg) throws Exception
	{
//log
		Label logTitle = new Label("Log");
		logTitle.getStyleClass().add("label-bright");
		logTitle.setMaxWidth(Double.MAX_VALUE);
		logTitle.setAlignment(Pos.CENTER_LEFT);

		Button clearLog = new Button("Clear");

		TextArea log = new TextArea();
		log.setDisable(true);
		log.setMaxHeight(Double.MAX_VALUE);

		//layout
		HBox logHdrPane = new HBox(logTitle, clearLog);
		HBox.setHgrow(logTitle, Priority.ALWAYS);
		logHdrPane.setPadding(new Insets(10));

//action
		CheckBox autoFTChB = new CheckBox("Auto rebuild to Fibonacci tree");

		Spinner<Integer> delaySpnr = new Spinner<>(0, 1000, 0, 10); //creating instance with value factory set
		delaySpnr.setEditable(true);
		delaySpnr.setMaxWidth(spnrMaxWidth);

		ComboBox<String> actionTypesCB = new ComboBox<>();
		actionTypesCB.getItems().addAll("remove", "search", "insert", "rebuild");

		Spinner<Integer> actionValueSpnr = new Spinner<>(1, 99, 1);
		actionValueSpnr.setEditable(true);
		actionValueSpnr.setMaxWidth(spnrMaxWidth);

		Button startAction = new Button("Start");
	
		//layout
		GridPane actionPane = new GridPane();
		actionPane.setPadding(new Insets(10));
		actionPane.setHgap(4);
		actionPane.setVgap(8);

		actionPane.add(autoFTChB, 0, 0, 2, 1);

		actionPane.add(new Label("Delay: "), 0, 1);
		actionPane.add(delaySpnr, 1, 1);

		actionPane.add(new Label("Action: "), 0, 2);
		actionPane.add(actionTypesCB, 1, 2);
		actionPane.add(new Label("Value: "), 3, 2);
		actionPane.add(actionValueSpnr, 4, 2);

		actionPane.add(startAction, 0, 3, 4, 1);

//util (left)
		VBox utilPane = new VBox(logHdrPane, log, actionPane);
		utilPane.setPrefWidth(utilWidth);
		VBox.setVgrow(log, Priority.ALWAYS);

//tree view (right)
		VBox viewPane = new VBox(new Label("View"));
		viewPane.setAlignment(Pos.TOP_LEFT);

//status bar (bottom)
		Label statusLbl = new Label("Status: ready");

//layout setup
		BorderPane root = new BorderPane(null, null, viewPane, statusLbl, utilPane);
		root.setPadding(new Insets(10));

		Scene scene = new Scene(root);
		//System.out.println(getClass().getResource().toExternalForm());
		scene.getStylesheets().add(
			getClass().getResource("/styles/main.css").toExternalForm());

		primaryStg.setTitle("GUI Tester");
		primaryStg.setScene(scene);
		primaryStg.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
