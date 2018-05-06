package rot.simpletrees.guitester.ui;

import rot.simpletrees.model.*;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import javafx.application.Platform;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;

import javafx.scene.Node;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import javafx.collections.ObservableList;

import javafx.util.Duration;

import javafx.geometry.Insets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class TreeView extends ScrollPane {	

	private Pane elementContainer = new Pane();
	private Label treeType = new Label();
	private Label treeHeight = new Label();
	private Label treeSize = new Label();
	
	private CheckBox linearModeChB = new CheckBox("Linear mode");
	Map<Integer, ElementPos> oldState;

	private FibTree<Integer, Integer> m_model = null;

	private int originX = 10;
	private int originY = 10;
	private int elementRadius = 40;
	private int fadeDur = 700;
	private int pathDur = 700;
	private int fillDur = 700;

	public TreeView(FibTree<Integer, Integer> model)
	{
		m_model = model;
		treeType.getStyleClass().add("label-header");
		updateInfo();

		GridPane infoPane = new GridPane();
		//infoPane.setPadding(new Insets(10));
		infoPane.setHgap(4);
		infoPane.setVgap(6);

		infoPane.add(treeType, 0, 0, 2, 1);

		infoPane.add(new Label("height: "), 0, 1);
		infoPane.add(treeHeight, 1, 1);

		infoPane.add(new Label("size: "), 0, 2);
		infoPane.add(treeSize, 1, 2);

		infoPane.add(linearModeChB, 0, 3, 2, 1);

		VBox contentLay = new VBox(infoPane, elementContainer);

		setPadding(new Insets(10));
        //setHbarPolicy(ScrollBarPolicy.ALWAYS)
		setContent(contentLay);

		linearModeChB.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				adaptState(oldState);
			}
		});
	}

	public void updateView()
	{
		Map<Integer, ElementPos> state = getState();
		Platform.runLater(() -> { adaptState(state); updateInfo(); } );
	}

	private Map<Integer, ElementPos> getState()
	{
		Map<Integer, ElementPos> state = new HashMap<>(0);
		
		AVLTree.DataLevelsList<Integer, Integer> modelLevels = m_model.getDataLevels();
		
		int levels = modelLevels.size();
		int terms = (int) Math.pow(2, levels-1);

		for(int row = 0; row < modelLevels.size(); ++row) {

			int curLevel = row+1;
			int indent = (int)( terms/Math.pow(2, curLevel-1) ) - 1;
			int gap = indent*2+1;

			for(int col = 0; col < modelLevels.get(row).size(); ++col) {

				Tree.Data<Integer, Integer> data = modelLevels.get(row).get(col);

				if( data != null ) {
					ElementPos pos = new ElementPos(
							originX + row * elementRadius*2,
							originY + (col + gap*col + indent) * elementRadius*2 );
					Integer key = modelLevels.get(row).get(col).m_key;
					
					state.put(key, pos);
				}
			}
		}

		return state;
	}
	private void adaptState(Map<Integer, ElementPos> state)
	{	
		linearModeChB.setDisable(true);
		oldState = new HashMap<Integer, ElementPos>(state);

		ObservableList<Node> nElements = elementContainer.getChildren();

		for( Node nElement : nElements ) {

			ViewElement element = (ViewElement) nElement;

			Integer elementKey = element.getKey();
			ElementPos newPos = state.get(elementKey);

			if(newPos == null ) {
				rmElement(element);	
			} else {

				//System.out.println("move " + element.getKey());
				moveElement(element, newPos);
			}

			state.remove(elementKey);
		}

		Iterator<Map.Entry<Integer, ElementPos>> stateIt = state.entrySet().iterator();
		while( stateIt.hasNext() ) {

			Map.Entry<Integer, ElementPos> pair = stateIt.next();

			//System.out.println("add");
			addElement(pair.getKey(), pair.getValue());	
		}

		linearModeChB.setDisable(false);
	}

	private void moveElement(ViewElement element, ElementPos pos)
	{
		//System.out.println(element.getLayoutX() + " " + element.getLayoutY() + " " + pos.m_x + " " + pos.m_y);

		if(	! linearModeChB.isSelected() &&
			element.getLayoutX() == pos.m_x && element.getLayoutY() == pos.m_y) return;

		Timeline tl = new Timeline();

        KeyFrame end = new KeyFrame(
				Duration.millis(pathDur),
                new KeyValue(element.layoutXProperty(), pos.m_x),
                new KeyValue(
					element.layoutYProperty(), 
					( linearModeChB.isSelected() ) ? originY : pos.m_y));

        tl.getKeyFrames().add(end);
		tl.play();

		//element.setLayoutX();
		//element.setLayoutY();
	}
	private void addElement(Integer key, ElementPos pos)
	{
		ViewElement newElement = new ViewElement(
				key, 
				elementRadius, 
				( linearModeChB.isSelected() ) ? originY : pos.m_y, 
				pos.m_x);

		FadeTransition ft = new FadeTransition(Duration.millis(fadeDur), newElement);
		newElement.setOpacity(0);
		ft.setToValue(100);

		elementContainer.getChildren().add(newElement);

		ft.play();
	}
	private void rmElement(final ViewElement element) 
	{
		FadeTransition ft = new FadeTransition(Duration.millis(fadeDur), element);
		ft.setToValue(0);
		ft.setOnFinished( ae -> {
			elementContainer.getChildren().remove(element);
			//element = null;
		});
		ft.play();
	}

	public void selectTouch(Integer key)
	{
		ObservableList<Node> nElements = elementContainer.getChildren();

		for( Node nElement : nElements ) {

			ViewElement element = (ViewElement) nElement;

			Integer elementKey = element.getKey();

			if( elementKey.compareTo(key) == 0 ) {

				FillTransition circleAnim = new FillTransition(Duration.millis(fillDur), element.getCircle(), ViewElement.DEF_COLOR, Color.web("#00b4ef"));
				circleAnim.setCycleCount(2);
				circleAnim.setAutoReverse(true);

				FillTransition textAnim = new FillTransition(Duration.millis(fillDur), element.getText(), Color.BLACK, Color.WHITE);
				textAnim.setCycleCount(2);
				textAnim.setAutoReverse(true);
			 
				circleAnim.play();
				textAnim.play();

				break;
			}
		}
	}

	private void updateInfo() {
		treeType.setText(
				(m_model.isFT()) ? "Fibonacci tree" : "AVL tree" );
		treeHeight.setText(String.valueOf(m_model.height()));
		treeSize.setText(String.valueOf(m_model.size()));
	}
}


class ElementPos {

	public ElementPos(int y, int x)
	{
		m_y = y;
		m_x = x;
	}

	public int m_y;
	public int m_x;
}
