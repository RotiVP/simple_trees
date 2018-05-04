package rot.simpletrees.guitester.ui;

import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javafx.scene.paint.Color;

import javafx.scene.layout.StackPane;

class ViewElement extends StackPane {

	private final Integer m_key;
	private Circle m_circle;

	public ViewElement(Integer key, int radius, int y, int x) {
		m_key = key;
		m_circle = new Circle(radius, Color.RED);
		getChildren().addAll( m_circle, new Text(key.toString()) );
		setLayoutX(x);
		setLayoutY(y);
	}

	public Integer getKey() {
		return m_key;
	}
}
