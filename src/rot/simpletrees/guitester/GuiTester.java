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

import javafx.scene.control.ScrollPane.ScrollBarPolicy;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.ComboBox;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javafx.application.Platform;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

//tmp
import javafx.scene.shape.Circle;

public class GuiTester extends Application {

	private short utilWidth = 410;
	private byte spnrMaxWidth = 100;

	public static final SimpleIntegerProperty DELAY_PROP = new SimpleIntegerProperty();
	public final SimpleBooleanProperty disableUI = new SimpleBooleanProperty(false);

	@Override
	public void start(Stage primaryStg) throws Exception
	{

		FibTree<Integer, Integer> fibTree = new FibTree<>();	

//log
		Label logTitle = new Label("Log");
		logTitle.getStyleClass().add("label-bright");
		logTitle.setMaxWidth(Double.MAX_VALUE);
		logTitle.setAlignment(Pos.CENTER_LEFT);

		Button clearLog = new Button("Clear");	

		TextArea log = new TextArea();
		//log.setDisable(true);
		log.setEditable(false);
		log.setMaxHeight(Double.MAX_VALUE);

		clearLog.setOnAction( ae -> {
			log.clear();
		});

		//layout
		HBox logHdrPane = new HBox(logTitle, clearLog);
		HBox.setHgrow(logTitle, Priority.ALWAYS);
		logHdrPane.setPadding(new Insets(10));

//action
		CheckBox rebuildChB = new CheckBox("Rebuild to Fibonacci tree");

		//creating instance with value factory set
		Spinner<Integer> delaySpnr = new Spinner<>(0, 1000, 0, 10); 
		delaySpnr.setEditable(true);
		delaySpnr.setMaxWidth(spnrMaxWidth);
		DELAY_PROP.bind(delaySpnr.valueProperty());

		ComboBox<String> actionTypesCB = new ComboBox<>();
		actionTypesCB.getItems().setAll("remove", "search", "insert", "flush");
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

		actionPane.add(rebuildChB, 0, 0, 2, 1);

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

//status bar (bottom)
		Label statusLbl = new Label("Status: Ready");

//layout setup
		BorderPane root = new BorderPane(treeView, null, null, statusLbl, utilPane);
		root.setPadding(new Insets(10));

		Scene scene = new Scene(root);
		//System.out.println(getClass().getResource().toExternalForm());
		scene.getStylesheets().add(
			getClass().getResource("/styles/main.css").toExternalForm());

		primaryStg.setTitle("GUI Tester");
		primaryStg.setScene(scene);
		primaryStg.show();

//
		clearLog.disableProperty().bind(disableUI);
		rebuildChB.disableProperty().bind(disableUI);
		delaySpnr.disableProperty().bind(disableUI);
		actionTypesCB.disableProperty().bind(disableUI);
		actionValueSpnr.disableProperty().bind(disableUI);
		startAction.disableProperty().bind(disableUI);

		Timeline statusTl = new Timeline(
				new KeyFrame(Duration.ZERO, (ae) -> {

					String statusStr = statusLbl.getText();
					
					statusLbl.setText(
							( statusStr.endsWith(". . .") ) 
							? statusStr.replaceAll("\\. \\. \\.$", ".")
							: statusStr + " ." );
				}),
				new KeyFrame(Duration.millis(400)));
		statusTl.setCycleCount(Timeline.INDEFINITE);

//
		fibTree.m_traverser = 
			new Tree.Traverser<Integer, Integer>()
		{
			@Override
			public void selected(Tree.Data<Integer, Integer> data) 
			{
				Platform.runLater( () -> {
					treeView.selectTouch(data.m_key);
				});
				try {
					Thread.sleep(GuiTester.DELAY_PROP.getValue());
				} catch (InterruptedException e) {}
			}
		};
		
		ExecutorService executor = Executors.newFixedThreadPool(2);

		startAction.setOnAction( ae -> {

			String actionType = actionTypesCB.getValue();
			Integer actionValue = actionValueSpnr.getValue();
			boolean isRebuild = rebuildChB.isSelected();

			// gui prepare
			disableUI.setValue(true);

			statusLbl.setText("Status: Processing .");
			statusTl.play();	

			Future<?> actionStatus = executor.submit( () -> {
				
				// time marks
				long startTime, endTime;
				startTime = System.nanoTime();

				switch(actionType) {

				case "insert":
					fibTree.insert(new Tree.Data<Integer, Integer>(actionValue, 0));
					if(isRebuild) {
						fibTree.resetToFib();	
					}
					break;

				case "remove":
					fibTree.remove(actionValue);
					if(isRebuild) {
						fibTree.resetToFib();	
					}
					break;

				case "search":
					fibTree.search(actionValue);
					break;

				case "flush":
					fibTree.flush();
					break;

				default:
				}

				endTime = System.nanoTime();

				treeView.updateView();

				Platform.runLater( () -> {

					//final StringBuilder sb = new StringBuilder();
					String format = "%s %s\n";

					log.appendText(String.format(format, "Action:", actionType));
					if( actionType.equals("insert") || actionType.equals("remove") ) {
						log.appendText(String.format(format, "Value:", actionValue));
						log.appendText(String.format(format, "Rebuild to FT:", isRebuild));
					}
					if( actionType.equals("search") )
						log.appendText(String.format(format, "Delay:", delaySpnr.getValue() + " ms"));
					if( !actionType.equals("flush") )
						log.appendText(String.format(format, "Speed:", endTime - startTime + " ns"));

					log.appendText("\n");

					statusTl.stop();
					statusLbl.setText("Status: Done");
					disableUI.setValue(false);
				});
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
