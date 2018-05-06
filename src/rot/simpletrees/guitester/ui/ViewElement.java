package rot.simpletrees.guitester.ui;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import javafx.scene.paint.Color;

import javafx.scene.layout.StackPane;

import javafx.scene.effect.DropShadow;

class ViewElement extends StackPane {

	public static final Color DEF_COLOR = Color.WHITE;

	private final Integer m_key;
	private Circle m_circle;
	private Text m_text;

	public ViewElement(Integer key, int radius, int y, int x) {

		m_key = key;

		m_circle = new Circle(radius, DEF_COLOR);
		//System.out.println(m_circle.getLayoutX() + m_circle.getCenterX());

		m_circle.setEffect(new DropShadow(10, 3, 3, Color.GRAY));

		m_text = new Text(key.toString());

		final Color lineColor = Color.LIGHTGRAY;
		final int lineStrokeWidth = 10;

		// left connection line
		Line leftConn = new Line(radius*2, 0, radius, radius);
		leftConn.setStroke(lineColor);
		leftConn.setStrokeWidth(lineStrokeWidth);
		leftConn.setTranslateX(-radius/2);
		leftConn.setTranslateY(radius/2);

		Line rightConn = new Line(0, 0, radius, radius);
		rightConn.setStroke(lineColor);
		rightConn.setStrokeWidth(lineStrokeWidth);
		rightConn.setTranslateX(radius/2);
		rightConn.setTranslateY(radius/2);

		getChildren().addAll( 
				leftConn,
				rightConn,
				m_circle, 
				m_text	
				);
		setLayoutX(x);
		setLayoutY(y);
	}

	public Integer getKey() {
		return m_key;
	}
	public Circle getCircle() {
		return m_circle;
	}
	public Text getText() {
		return m_text;
	}
}
