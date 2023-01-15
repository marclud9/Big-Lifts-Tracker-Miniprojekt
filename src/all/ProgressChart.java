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
	 private static final NumberAxis xAxis = new NumberAxis();
	 private static final NumberAxis yAxis = new NumberAxis();
	 private static LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
	
	 public ProgressChart() {
		 initChart();
	 }
	 
	private static void initChart() {
		xAxis.setLabel("Vorangegangenes Training in Nummern");
		yAxis.setLabel("Gewicht");
                yAxis.setAutoRanging(false);
                yAxis.setLowerBound(0);
                yAxis.setUpperBound(150);
		lineChart.setTitle("Progression");
	}
	
         @Override
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
        
	public void addToSeries(String[][] exerciseData, String exerciseName) {
            XYChart.Series<Number, Number> seriesData = new XYChart.Series<>();
            int weight;
            for (int i = 0; i < exerciseData.length; i++) {
                if(checkForInt(exerciseData[i][0]) == true && exerciseData[i][0] != null){  //Wenn readHistory aufgerufen wurde und Gewicht und Reps übergeben wurden
                    weight = Integer.parseInt(exerciseData[i][0]);
                    seriesData.getData().add(new XYChart.Data<Number, Number>(i, weight));
                } else if (exerciseData[i][0] != null){  //wenn readDiagram aufgerufen wurde und Datum und Gewicht übergeben wurden
                    weight = Integer.parseInt(exerciseData[i][1]);
                    seriesData.getData().add(new XYChart.Data<Number, Number>(i, weight));
                }
            }
            seriesData.setName(exerciseName);
            lineChart.getData().add(seriesData);
        }
	public LineChart<Number, Number> getChart() {
		return lineChart;
	}	
	
/*	public XYChart.Series<String, Number> getSeries(){
            return seriesData;
        } */
}

