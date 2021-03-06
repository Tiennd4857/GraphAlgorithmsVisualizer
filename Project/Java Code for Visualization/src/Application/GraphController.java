package Application;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import Algorithm.Algorithm;
import Algorithm.AlgorithmFactory;
import Algorithm.Prim;
import Elements.*;
import javafx.animation.FillTransition;
import javafx.animation.StrokeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GraphController implements Initializable{

	// B U T T O N S !
	@FXML
	private AnchorPane anchorRoot;
	@FXML
	private Label textAlgorithm;
	@FXML
	private JFXButton backButton;
	@FXML
	private JFXButton resetButton;
	@FXML
	private JFXButton clearButton;
	@FXML
	private JFXButton runOneButton;
	@FXML
	private JFXButton runAllButton;
    @FXML
    private Group canvasGroup;
    @FXML 
    private Pane graphDrawingCanvas;
    @FXML
	private ToggleGroup addType;
    @FXML
    private Label sourceText = new Label("Source ");
    @FXML
    private Label resultText;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private ImageView backgroundimg;
    // // // // //
    ContextMenu globalMenu;		// ???? What is this?
    
    
	public GraphController() {
	}
	

	public void handle(MouseEvent event) 
	{ 
        addNodeHandler.handle(event);
    };
    
	//ADD NODE HANDLER
	// Bind this directly to vertex.setMouseReleased()
	public EventHandler<MouseEvent> addNodeHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
            //if you LEFT click into a blank space -> create new node
            if (event.getButton() == MouseButton.PRIMARY  &&
            		!event.getSource().equals(canvasGroup)) 
            {

                    Vertex vertex = new Vertex(event.getX(), event.getY(), 18);
                    addToGraph(vertex);
            }
            // Right click to un-choose every nodes
            else if (event.getButton() == MouseButton.SECONDARY) 
            	{
                    unhighlightAllVertices(selectedVertices);
                    selectedVertices.clear();
                }
            }};
		
	
	
	//ADD EDGE HANDLER
	// Bind this to vertex.setOnMousePressed()
	public EventHandler<MouseEvent> addEdgeHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent mouseEvent) {
			System.out.println("Pointed!");
			
			// TODO Auto-generated method stub
			Vertex circle = (Vertex) mouseEvent.getSource();
			
			selectedVertices.remove(circle);	// Avoid clicking one vertex twice.
			selectedVertices.add(circle);

			for (Vertex v: selectedVertices)
				System.out.println(v.getID());
			
			
			if (selectedVertices.size () == 1)	// highlight the clicked vertex
				highlight(selectedVertices);

			// Add an edge if there's not one there
			else if (!graph.isExistsEdge(selectedVertices.get(0), selectedVertices.get(1))) 
			{
					//dialog to get weight
					TextInputDialog dialog = new TextInputDialog("0");
                    dialog.setTitle(null);
                    dialog.setHeaderText("Enter Weight of the Edge :");
                    dialog.setContentText(null);

                    Optional<String> result = dialog.showAndWait();
                    int w = Integer.MIN_VALUE;
                    //parse weight
                    if (result.isPresent()) {
                    	w = Integer.parseInt(result.get());
                    } 
                    else {
                    }
                    //draw
                    if (w != Integer.MIN_VALUE)
                    {
                    	Edge newEdge = EdgeFactory.create(edgeDirection, selectedVertices.get(0), selectedVertices.get(1), w);
                    	addToGraph(newEdge);
                    }
                    
                    unhighlightAllVertices(selectedVertices);
                    selectedVertices.clear();
			}
			else
			{
				unhighlightAllVertices(selectedVertices);
				selectedVertices.clear();
			}
		}
		
	};
	

	protected void unhighlightAllVertices(List<Vertex> v)	{	for (Vertex vtx: v) unhighlight(vtx);	}
	protected void unhighlight(Vertex v) {	
		FillTransition ft1 = new FillTransition(Duration.millis(300), v);
        ft1.setToValue(Color.BLACK);
        ft1.play();
	}
	
	protected void unhighlightAllEdges(List<Edge> e)	{	for (Edge edge: e) unhighlight(edge);	}
	protected void unhighlight(Edge e) {	
		 StrokeTransition ftEdge = new StrokeTransition(Duration.millis(500), e);
         ftEdge.setToValue(Color.BLACK);
         ftEdge.play();
	}
	
	protected void highlight(List<Vertex> v)	{	for (Vertex vtx: v) highlight(vtx);	}
	protected void highlight(Vertex v)			{	v.draw(Color.RED);	}
    
	// ClearGraph handler: Wipe out all these things:
	// The graph on the screen
	// The internal graph
    @FXML
    private void ClearGraphHandle(ActionEvent event) {
        if (graphLocked)
        	unlockGraph();
        graph.resetGraph();
        canvasGroup.getChildren().clear();
        canvasGroup.getChildren().addAll(graphDrawingCanvas);
        resultText.setText("Cleared Graph");
    }

    // Reset Handle for handling Reset Algorithm Button
    // Set the Algorithm back to the first step
    @FXML
    private void ResetHandle(ActionEvent event) {
        System.out.println("IN CLEAR:" + graph.get_vertices().size());
        if (graphLocked)
        	unlockGraph();
        // Set color of all vertices and edges to color at start (BLACK)
        unhighlightAllVertices(graph.get_vertices());
        unhighlightAllEdges(graph.get_edges());
        algorithm.reset();
        resultText.setText("Algorithm reset to first step");
    }
    
    protected void addToGraph(Vertex v)
    {
        canvasGroup.getChildren().addAll(v.drawableObjects());
        graph.addVertex(v);
        v.setOnMousePressed(addEdgeHandler);
    } 
    
    protected void addToGraph(Edge e)
    {
        canvasGroup.getChildren().addAll(e.drawableObjects());
        graph.addEdge(e);
    }
    
    // Stop the user from creating any new edges/vertices
    protected void lockGraph()
    {
    	for (Vertex v: graph.get_vertices())
    		v.setOnMousePressed(e -> {});
    	graphDrawingCanvas.setOnMouseClicked(e -> {});
    	graphLocked = true;
    	
    	// Graph can't be changed anymore. 
    	// Time to create the algorithm
    	algorithm = AlgorithmFactory.create(GraphPropertyHolder.getAlgorithmName(), this.graph);
    }
    
    // Enable the user to create vertices/edges again
    protected void unlockGraph()
    {
    	for (Vertex v: graph.get_vertices())
    		v.setOnMousePressed(addEdgeHandler);
    	graphDrawingCanvas.setOnMouseClicked(addNodeHandler);
    	graphLocked = false;
    }
    
    public void runOne() {
    	if (!graphLocked)
    		lockGraph();
    	
		algorithm.runOne();
		
		resultText.setText(algorithm.toString());
		resultText.toFront();
		
	}
    
    public void runAll() {
    	if (!graphLocked)
    		lockGraph();
    	algorithm.runAll();
			resultText.setText(algorithm.toString());
			resultText.toFront();
	
	}
    
    // Setup the GraphController from GraphPropertyHolder
    // which has data passed from InputMenuController
    public void setup()
    {
    	AlgorithmName = GraphPropertyHolder.getAlgorithmName();
    	textAlgorithm.setText(AlgorithmName);
    	// Algorithm creation is deferred until the first runOne() is called
    	
    	this.edgeDirection = GraphPropertyHolder.getEdgeDirection();
    	this.graph = GraphPropertyHolder.getGraph();
    }
    
    public void loadNextScene()
    {
		Parent root;
    	try {
    		graph.resetGraph();
    		root = FXMLLoader.load(getClass().getResource("InputMenu.fxml"));
    		Main.primaryStage.setScene(new Scene(root));
		} 
    	catch (Exception e2) {
			// TODO: handle exception
		}
    }
    
   
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Initialize drawing graph");
		
		this.setup();
		//Set Back button handle
		backButton.setOnAction(e-> {loadNextScene();});
		
		if (this.graph != null) {
			canvasGroup.getChildren().addAll(this.graph.drawableObjects());
			for(Vertex vertex : this.graph.get_vertices()) {
				vertex.setOnMousePressed(addEdgeHandler);
			}
		}
		else
			graph = new Graph();
	}

	
    // What the mouse has selected so far
    private List<Vertex> selectedVertices = new ArrayList<Vertex>();
    private boolean graphLocked = false;
	private String AlgorithmName;
	private Algorithm algorithm;
    private Graph graph;
	private String edgeDirection;
}
