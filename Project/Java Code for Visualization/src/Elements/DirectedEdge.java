package Elements;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;

public class DirectedEdge extends Edge{

	public DirectedEdge(Vertex v, Vertex u, int weight) {
		super(v, u, weight);
	}
	
	@Override 
	public boolean startsFrom(Vertex v)
	{
		return this.getBegin().equals(v);
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o == this ||			// Self Comparing
				( !(o instanceof DirectedEdge) && 	// Test for Same type 
					// Test for same two endpoints
					((DirectedEdge) o).getBegin() == this.getBegin() &&
					((DirectedEdge) o).getEnd() == this.getEnd());
	}
	

	// Draw an arrow from Begin vertex to End vertex
	@Override
	public void draw(Color colour) {
		strokeProperty().bind(fillProperty());
        setFill(colour);
        
        //Line
        getElements().add(new MoveTo(this.getBegin().getCenterX(), 
        								this.getBegin().getCenterY()));
        
        getElements().add(new LineTo(this.getEnd().getCenterX(), 
										this.getEnd().getCenterY()));
        
        //ArrowHead
        double XDiff = this.getBegin().getCenterX() - this.getEnd().getCenterX();
        double YDiff = this.getBegin().getCenterY() - this.getEnd().getCenterY();
        
        double angle = Math.atan2(YDiff, XDiff) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        
        // Set arrow wings length
        double bodyLength = Math.sqrt(XDiff*XDiff + YDiff*YDiff);
        double body_vs_ArrowHead_Ratio = 0.1;
        double arrowHeadSize =  bodyLength * body_vs_ArrowHead_Ratio;
        
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + this.getEnd().getCenterX();
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + this.getEnd().getCenterY();
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + this.getEnd().getCenterX();
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + this.getEnd().getCenterY();
        
        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        
        getElements().add(new LineTo(this.getEnd().getCenterX(), 
        								this.getEnd().getCenterY()));
    }
}
