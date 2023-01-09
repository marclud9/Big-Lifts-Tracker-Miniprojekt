/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author marc3
 */
package all;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author marc3
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.lang.NumberFormatException;

public class ProgressChart extends Application{
	 private static final CategoryAxis xAxis = new CategoryAxis();
	 private static final NumberAxis yAxis = new NumberAxis();
	 private static LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
	
	 public ProgressChart() {
		 initChart();
	 }
	 
	private static void initChart() {
		xAxis.setLabel("Monat");
		yAxis.setLabel("Gewicht");
		lineChart.setTitle("Progression");
	}
	
	public void start(Stage stage) {
		Scene scene = new Scene(lineChart, 800, 600);
		stage.setScene(scene);
		stage.show();
	}
	
        private static boolean checkForInt(String s){
            if(s == null) return false;
            try{
                int i = Integer.parseInt(s);
            } catch(NumberFormatException e){
                return false;
            }
            return true;
        }
        
	public void addToSeries(String[][] exerciseData) {
            XYChart.Series<String, Number> temp = new XYChart.Series<String, Number>();
            int weight;
            for (int i = 0; i < exerciseData.length; i++) {
                if(checkForInt(exerciseData[i][0]) == true && exerciseData[i][0] != null){  //Wenn readHistory aufgerufen wurde und Gewicht und Reps übergeben wurden
                    weight = Integer.parseInt(exerciseData[i][0]);
                    temp.getData().add(new XYChart.Data<String, Number>(exerciseData[i][1], weight));
                } else if (exerciseData[i][0] != null){  //wenn readDiagram aufgerufen wurde und Datum und Fewicht übergeben wurden
                    weight = Integer.parseInt(exerciseData[i][1]);
                    temp.getData().add(new XYChart.Data<String, Number>(exerciseData[i][0], weight));
                }
            }
            lineChart.getData().add(temp);
            
        }
	public LineChart<String, Number> getChart() {
		return lineChart;
	}	
	
	
}

