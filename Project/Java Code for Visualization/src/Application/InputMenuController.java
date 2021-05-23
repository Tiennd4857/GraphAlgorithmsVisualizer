package Application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;

public class InputMenuController implements Initializable{
	@FXML
	ToggleGroup GraphType;
	@FXML
	RadioButton directedBox, undirectedBox;
	@FXML
	ComboBox AlgorithmBox;
	@FXML
	TextArea graphData;
	public static void showAlert(String title, String message)
	{
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
	public void validate()
	{
		if (directedBox.isSelected())
		{
			if (AlgorithmBox.getSelectionModel().getSelectedItem().toString().contains("Kruskal")||
					AlgorithmBox.getSelectionModel().getSelectedItem().toString().contains("Prim"))
			{
				showAlert("Invalid algorithm", "Only Dijkstra can be implemented with directed graph");
			}
		}
		if (undirectedBox.isSelected())
		{

		}
		try {
			String graphText = graphData.getText().trim();
			if (!graphText.equals(""))
			{
				String[] splitdata = graphText.split("\\s+");
				int number_of_vertices = Integer.valueOf(splitdata[0]);
				int number_of_edges = Integer.valueOf(splitdata[1]);
				if (splitdata.length > (3*number_of_edges + 2))
				{
					showAlert("Invalid data", "(First line has 2 number N,M - number of vertices and number of edges; the next M lines each have a combination u, v, w: begin - end - weight)");
				}
				for (int i = 0; i < number_of_edges; i++)
				{
					int u, v;
					double w;
					u = Integer.valueOf(splitdata[2 + 3*i]);
					v = Integer.valueOf(splitdata[3 + 3*i]);
					w = Integer.valueOf(splitdata[4 + 3*i]);
				}
			}
		}
		catch (Exception exception)
		{
			showAlert("Invalid data", "(First line has 2 number N,M - number of vertices and number of edges; the next M lines each have a combination u, v, w: begin - end - weight)");
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		String[] algo = {"Dijkstra", "Kruskal MST - for undirected only", "Prim MST - for undirected only"};
		AlgorithmBox.getItems().clear();
		AlgorithmBox.setItems(FXCollections.observableArrayList(algo));
		graphData.setText("");
	}
}