package rot.simpletrees.guitester.ui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import rot.simpletrees.model.*;

import javafx.application.Platform;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.scene.Node;

import javafx.util.Duration;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;

public class TreeView extends /*Group*/ Pane {	

	private AVLTree<Integer, Color> m_model = null;

	private int elementRadius = 40;
	private int fadeDur = 1000;
	private int pathDur = 1000;

	public TreeView(AVLTree<Integer, Color> model)
	{
		m_model = model;
	}

	public void updateView()
	{
		Map<Integer, ElementPos> state = getState();
		Platform.runLater(() -> adaptState(state));
	}

	private Map<Integer, ElementPos> getState()
	{
		Map<Integer, ElementPos> state = new HashMap<>(0);
		
		AVLTree.DataLevelsList<Integer, Color> modelLevels = m_model.getDataLevels();
		
		int levels = modelLevels.size();
		int terms = (int) Math.pow(2, levels-1);

		for(int row = 0; row < modelLevels.size(); ++row) {

			int curLevel = row+1;
			int indent = (int)( terms/Math.pow(2, curLevel-1) ) - 1;
			int gap = indent*2+1;

			for(int col = 0; col < modelLevels.get(row).size(); ++col) {

				Tree.Data<Integer, Color> data = modelLevels.get(row).get(col);

				if( data != null ) {
					ElementPos pos = new ElementPos(
							row * elementRadius*2,
							col * elementRadius*2 );
					Integer key = modelLevels.get(row).get(col).m_key;
					
					state.put(key, pos);
				}
			}
		}

		return state;
	}
	private void adaptState(Map<Integer, ElementPos> state)
	{
		ObservableList<Node> nElements = getChildren();

		for( Node nElement : nElements ) {

			ViewElement element = (ViewElement) nElement;

			Integer elementKey = element.getKey();
			ElementPos newPos = state.get(elementKey);

			if(newPos == null ) {
				rmElement(element);	
			} else {

				System.out.println("move " + element.getKey());
				moveElement(element, newPos);
			}

			state.remove(elementKey);
		}

		Iterator<Map.Entry<Integer, ElementPos>> stateIt = state.entrySet().iterator();
		while( stateIt.hasNext() ) {

			Map.Entry<Integer, ElementPos> pair = stateIt.next();

			System.out.println("add");
			addElement(pair.getKey(), pair.getValue());	
		}
	}

	private void moveElement(ViewElement element, ElementPos pos)
	{
		//System.out.println(element.getLayoutX() + " " + element.getLayoutY() + " " + pos.m_x + " " + pos.m_y);

		Timeline tl = new Timeline();

        KeyFrame end = new KeyFrame(
				Duration.millis(pathDur),
                new KeyValue(element.layoutXProperty(), pos.m_x),
                new KeyValue(element.layoutYProperty(), pos.m_y));

        tl.getKeyFrames().add(end);
		tl.play();

		//element.setLayoutX();
		//element.setLayoutY();
	}
	private void addElement(Integer key, ElementPos pos)
	{
		ViewElement newElement = new ViewElement(key, elementRadius, pos.m_y, pos.m_x);

		FadeTransition ft = new FadeTransition(Duration.millis(fadeDur), newElement);
		newElement.setOpacity(0);
		ft.setToValue(100);

		getChildren().add(newElement);

		ft.play();
	}
	private void rmElement(final ViewElement element) 
	{
		FadeTransition ft = new FadeTransition(Duration.millis(fadeDur), element);
		ft.setToValue(0);
		ft.setOnFinished( ae -> {
			getChildren().remove(element);
			//element = null;
		});
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
