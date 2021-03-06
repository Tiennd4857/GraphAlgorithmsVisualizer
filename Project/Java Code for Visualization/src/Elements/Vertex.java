package Elements;


import java.util.Arrays;
import java.util.Collection;

import Algorithm.Drawable;
//import javafx.animation.StrokeTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
//import javafx.util.Duration;

public class Vertex extends Circle implements Drawable{
	private static int counterID = 0;

	// Reset the private auto-incremented counter of Vertex
	public static void resetCounter() {
		counterID = 0;
	}

	private Label vertexID;
	public Vertex(double x, double y, double radius)
	{
		this(x, y, radius, Color.BLACK);
	}
	
	public Vertex(double x, double y, double radius, Color color)
	{
		super(x, y, radius);
		counterID++;
		this.setId(String.valueOf(counterID));
		this.setOpacity(0.2);
		this.draw(color);
	}
	
	public String getID()
	{
		return this.getId();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o == this ||
				(o instanceof Vertex &&
						((Vertex) o).getID().equals(this.getID()));
	}

	@Override
	public void draw(Color colour) {
		// Draw the circle
		this.setFill(colour);
		this.setStroke(Color.BLACK);
		// Draw the label
		if (this.vertexID == null)
			this.vertexID = new Label();
		
		
		this.vertexID.setLayoutX(this.getCenterX() - 6);
		this.vertexID.setLayoutY(this.getCenterY() - 6);
		vertexID.setFont(Font.font("Helvetica", FontWeight.BOLD, 14.6));
        vertexID.setTextFill(Color.ORANGERED);
        vertexID.setText(String.valueOf(this.getID()));
        vertexID.setDisable(true);
        vertexID.setOpacity(99999.0);
	}

	@Override
	public Collection<Node> drawableObjects() {
		return Arrays.asList(this, vertexID);
	}
}
