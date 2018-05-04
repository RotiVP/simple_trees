package rot.simpletrees.guitester;

import rot.simpletrees.guitester.ui.*;
import rot.simpletrees.model.*;

import javafx.application.Application;

import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.ScrollPane;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.ComboBox;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.beans.property.SimpleIntegerProperty;

import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javafx.scene.paint.Color;

//tmp
import javafx.scene.shape.Circle;

public class GuiTester extends Application {

	private short utilWidth = 410;
	private byte spnrMaxWidth = 100;

	public static final SimpleIntegerProperty DELAY_PROP = new SimpleIntegerProperty();

	@Override
	public void start(Stage primaryStg) throws Exception
	{

		AVLTree<Integer, Color> fibTree = new FibTree<>();

		fibTree.m_traverser = 
			new Tree.Traverser<Integer, Color>()
		{
			@Override
			public void selected(Tree.Data<Integer, Color> data) 
			{
				data.m_value = Color.WHITE;
				try {
					Thread.sleep(GuiTester.DELAY_PROP.getValue());
				} catch (InterruptedException e) {}
				data.m_value = Color.BLUE;
			}
		};

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

		//creating instance with value factory set
		Spinner<Integer> delaySpnr = new Spinner<>(0, 1000, 0, 10); 
		delaySpnr.setEditable(true);
		delaySpnr.setMaxWidth(spnrMaxWidth);
		DELAY_PROP.bind(delaySpnr.valueProperty());

		ComboBox<String> actionTypesCB = new ComboBox<>();
		actionTypesCB.getItems().setAll("remove", "search", "insert", "rebuild");
		actionTypesCB.getSelectionModel().select(0);

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
		TreeView treeView = new TreeView(fibTree);	

		//VBox viewPane = new VBox(treeView);
		//viewPane.setAlignment(Pos.TOP_LEFT);
		ScrollPane viewPane = new ScrollPane();
		viewPane.setContent(treeView);

//status bar (bottom)
		Label statusLbl = new Label("Status: ready");

//layout setup
		BorderPane root = new BorderPane(viewPane, null, null, statusLbl, utilPane);
		root.setPadding(new Insets(10));

		Scene scene = new Scene(root);
		//System.out.println(getClass().getResource().toExternalForm());
		scene.getStylesheets().add(
			getClass().getResource("/styles/main.css").toExternalForm());

		primaryStg.setTitle("GUI Tester");
		primaryStg.setScene(scene);
		primaryStg.show();

//
		ExecutorService executor = Executors.newFixedThreadPool(2);

		startAction.setOnAction( ae -> {

			String actionType = actionTypesCB.getValue();
			Integer actionValue = actionValueSpnr.getValue();

			Future<?> actionStatus = executor.submit( () -> {

				switch(actionType) {
				case "insert":
					fibTree.insert(new Tree.Data<Integer, Color>(actionValue, Color.WHITE));
					break;
				default:
				}

				treeView.updateView();
			});

			/*
			executor.submit( () -> {
				do {
				} while(!actionStatus.isDone());

				try{
					actionStatus.get();
				}catch( ExecutionException ee ) { 
				}catch( InterruptedException ie) {}
			});
			*/
		});
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
