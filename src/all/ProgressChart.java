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
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class ProgressChart extends Application{
	 private static final NumberAxis xAxis = new NumberAxis();
	 private static final NumberAxis yAxis = new NumberAxis();
	 private static LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
	
	 public ProgressChart() {
		 initChart();
	 }
	 
	private static void initChart() {
            //Parameter des Graphen festlegen
            xAxis.setLabel("Verlauf der letzten Trainings"); //Achsenbeschriftung X-Achse
            yAxis.setLabel("Gewicht"); //Achsenbeschriftung Y-Achse
            yAxis.setAutoRanging(false);
            xAxis.setAutoRanging(false);
            //Grenzen der Achsen definieren
            yAxis.setLowerBound(0);  
            yAxis.setUpperBound(150);
            xAxis.setLowerBound(0);
            xAxis.setUpperBound(6);
            xAxis.setTickUnit(1); //Abstand der Werte der X-Achse
            lineChart.setTitle("Progression");
	}
	
         @Override
	public void start(Stage stage) {
		Scene scene = new Scene(lineChart, 800, 600);
		stage.setScene(scene);
		stage.show();
	}
        
	public void addToSeries(String[][] exerciseData, String exerciseName) { //Funktion zum Hinzufügen neuer Daten
            XYChart.Series<Number, Number> seriesData = new XYChart.Series<>();
            float weight;
            if(exerciseData != null){
                for (int i = 0; i < exerciseData.length; i++) { 
                    weight = Float.parseFloat(exerciseData[i][1]);  //Den Array-Eintrag, der das Gewicht enthäl in einen Float umwandeln, um auch Dezimalzahlen zu unterstützem
                    seriesData.getData().add(new XYChart.Data<>(i, weight));
                }
            }
            seriesData.setName(exerciseName); //Name der Datenserie auf den übergebenen Namen der Übung anpassen 
            lineChart.getData().add(seriesData);
        }
	public LineChart<Number, Number> getChart() {
		return lineChart;
	}	
}

