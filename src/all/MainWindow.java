/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package all;

/**
 *
 * @author marc3
 */
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javafx.scene.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import java.awt.event.*;
import java.util.ArrayList;

public class MainWindow implements ActionListener, ItemListener {

    private static JComboBox chooseExercise = new JComboBox();
    private static JTextField inputWeight = new JTextField(8);
    private static JTextField inputReps = new JTextField(8);
    private static JLabel oneRep = new JLabel("Estimated One-Rep-Max: -");
    private static ProgressChart chart;
    private final JFXPanel fxp = new JFXPanel(); //für das Einbinden des FX-Graphen
    private static OneRepMaxCalc calc = new OneRepMaxCalc();
    private DataSaving saveData = new DataSaving();
    private ArrayList weightsLabel = new ArrayList();
    private ArrayList repLabel = new ArrayList();
    private ArrayList dateLabelList = new ArrayList();
    private final Font changedFont = new Font("Dialog", Font.PLAIN, 17);

    public MainWindow() {

    }
	
    //Die private statische Methode ist notwendig, um die FX ProgressChart in die Swing-Gui einbinden zu können
    private static void initFX(JFXPanel fxpanel) {
        Platform.runLater(() -> {
            chart = new ProgressChart();
            Scene scene = new Scene(chart.getChart(), 700, 500);
            fxpanel.setScene(scene);
        });
    }
        
    
    @Override
    public void itemStateChanged(ItemEvent e){ //JComboBox Auswahl
       //Anpassen der letzten Werte
       String[][] tempData = saveData.readData(chooseExercise.getSelectedItem().toString());
       JLabel tempweight;
       JLabel tempreps;
       JLabel tempdate;
       String tempOneRepWeight = "";
       String tempOneRepReps = "";
       for(int i = 0; i < tempData.length; i++){
           tempweight = (JLabel) weightsLabel.get(i);
           tempreps = (JLabel) repLabel.get(i);
           tempdate = (JLabel) dateLabelList.get(i);
           tempdate.setText(tempData[i][0]);
           tempweight.setText(tempData[i][1]);
           tempreps.setText(tempData[i][2]);
           tempOneRepWeight = tempweight.getText();
           tempOneRepReps = tempreps.getText();
       }
 
       //Anzeigen des richtigen One-Rep-Maxs 
       if (tempOneRepReps != null && tempOneRepWeight != null && tempOneRepReps.isEmpty() == false && tempOneRepWeight.isEmpty() == false){
           oneRep.setText("Estimated One-Rep-Max: " + calc.returnOneRepMax(Double.parseDouble(tempOneRepWeight), Double.parseDouble(tempOneRepReps)).toString());
           oneRep.setFont(changedFont);
       } else{
           oneRep.setText("Estimated One-Rep-Max: -");
           oneRep.setFont(changedFont);
       }
      
        //Anpassen des Diagramms
        Platform.runLater(() -> {
            chart.getChart().getData().clear();
            chart.addToSeries(tempData, chooseExercise.getSelectedItem().toString());     
        });  
       
    }

    @Override
    public void actionPerformed(ActionEvent e){  //Bestätigen-Button nutzen
       String weightIn = inputWeight.getText();
       String repsIn = inputReps.getText(); 
       //Anpassen One-Rep-Max
       oneRep.setText("Estimated One-Rep-Max: " + calc.returnOneRepMax(Double.parseDouble(weightIn), Double.parseDouble(repsIn)).toString());
       oneRep.setFont(changedFont);
       LocalDate date = LocalDate.now();
       //Anpassen der letzten Werte
       saveData.writeData(date.toString(), weightIn, repsIn, chooseExercise.getSelectedItem().toString());
       String[][] temp = saveData.readData(chooseExercise.getSelectedItem().toString());
       JLabel tempweight;
       JLabel tempreps;
       JLabel tempdate;
       for(int i = 0; i < temp.length; i++){     
           tempweight = (JLabel) weightsLabel.get(i);
           tempreps = (JLabel) repLabel.get(i);
           tempdate = (JLabel) dateLabelList.get(i);
           tempdate.setText(temp[i][0]);
           tempweight.setText(temp[i][1]);
           tempreps.setText(temp[i][2]);
        }
       //Anpassen des Diagrams
        Platform.runLater(() -> {
            chart.getChart().getData().clear();
            chart.addToSeries(temp, chooseExercise.getSelectedItem().toString());
            
        });  
       
    }

    public void buildMainWindow() {
        JPanel mainWindow = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JDialog programmName = new JDialog();
        programmName.setTitle("Fitness-Tracking");
        programmName.setSize(1280, 720);
        programmName.add(mainWindow);
        programmName.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        programmName.setVisible(true);
        //Hinzufügen des aktuellen Datums 
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        LocalDate date = LocalDate.now();
        JLabel currentDate = new JLabel("Aktuelles Datum: " + format.format(date));
        currentDate.setFont(changedFont);
        mainWindow.add(currentDate);
        currentDate.setVisible(true);
        //Hinzufügen der Auswahlmöglichkeiten 
        chooseExercise.addItem("Bankdrücken");
        chooseExercise.addItem("Kniebeuge");
        chooseExercise.addItem("Kreuzheben");
        chooseExercise.addItem("Überkopfdrücken");
        chooseExercise.setFont(changedFont);
        chooseExercise.addItemListener(this);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.insets = new Insets(10, 20, 10, 10);
        mainWindow.add(chooseExercise, gbc);

        //Abschnitt der Eingabe
        oneRep.setFont(changedFont);
        JLabel inputX = new JLabel("x");
        inputX.setFont(changedFont);
        JLabel inputLetter = new JLabel("Eingabe: ");
        inputLetter.setFont(changedFont);
        JButton acceptInput = new JButton("Eingabe bestätigen");
        acceptInput.setFont(changedFont);
        acceptInput.addActionListener(this);
        JPanel inputContainer = new JPanel();
        //Festlegen der Positionen der Teile des Eingabebereichs
        inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.X_AXIS));
        inputContainer.add(inputLetter);
        inputContainer.add(Box.createRigidArea(new Dimension(15, 0)));
        inputContainer.add(inputWeight);
        inputContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        inputContainer.add(inputX);
        inputContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        inputContainer.add(inputReps);
        inputContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        inputContainer.add(acceptInput);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainWindow.add(inputContainer, gbc);
        gbc.gridx = 2;
        mainWindow.add(currentDate, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 20, 10, 20);
        mainWindow.add(oneRep, gbc);

        //Seitlicher Block mit den Werten der letzten Trainings
        JPanel lastValueContainer = new JPanel();
        lastValueContainer.setLayout(new BoxLayout(lastValueContainer, BoxLayout.Y_AXIS));
        JLabel lastSeven = new JLabel("Werte der letzten 7 Trainings");
        lastSeven.setFont(changedFont);
        lastSeven.setAlignmentX(Component.CENTER_ALIGNMENT);
        lastValueContainer.add(lastSeven);
        JPanel lastValues = new JPanel();
        JPanel weightsPanel = new JPanel();
        JPanel repsPanel = new JPanel();
        JPanel datePanel = new JPanel();
        //Layouts für die einzelnen Bestandteile 
        //Dabei gibt es drei "Blöcke", die untereinander angeordnet sind, die als vollständige "Blöcke" in einem übergeordnetem "Block" nebeneinander angeordnet werden
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        lastValues.setLayout(new BoxLayout(lastValues, BoxLayout.X_AXIS));
        weightsPanel.setLayout(new BoxLayout(weightsPanel, BoxLayout.Y_AXIS));
        repsPanel.setLayout(new BoxLayout(repsPanel, BoxLayout.Y_AXIS));
        Font latestData = new Font("Dialog", Font.PLAIN, 15); //Festlegen der Schriftart für die Label mit Inhalten
        
        //Block, in dem die Gewichte angezeigt werden 
        JLabel weightLabel = new JLabel("Gewicht");
        weightLabel.setFont(changedFont);
        JLabel weightsOne = new JLabel(" - ");
        JLabel weightsTwo = new JLabel(" - ");
        JLabel weightsThree = new JLabel(" - ");
        JLabel weightsFour = new JLabel(" - ");
        JLabel weightsFive = new JLabel(" - ");
        JLabel weightsSix = new JLabel(" - ");
        JLabel weightsSeven = new JLabel(" - ");
        JLabel[] weightsArray = {weightsSeven, weightsSix, weightsFive, weightsFour, weightsThree, weightsTwo, weightsOne}; //Array mit allen Elementen, um diese leichter durchlaufen zu können
        weightsPanel.add(weightLabel);
        weightsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        for(JLabel j : weightsArray){
            j.setFont(latestData);
            weightsPanel.add(j);
            weightsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            weightsLabel.add(j);
        }
        
      //Block, in dem die Anzahl an Wiederholungen angezeigt wird
        JLabel repsLabel = new JLabel("Reps");
        repsLabel.setFont(changedFont);
        JLabel repsOne = new JLabel(" - ");
        JLabel repsTwo = new JLabel(" - ");
        JLabel repsThree = new JLabel(" - ");
        JLabel repsFour = new JLabel(" - ");
        JLabel repsFive = new JLabel(" - ");
        JLabel repsSix = new JLabel(" - ");
        JLabel repsSeven = new JLabel(" - ");
        repsPanel.add(repsLabel);
        repsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel[] repsArray = {repsSeven, repsSix, repsFive, repsFour, repsThree, repsTwo, repsOne};
        for(JLabel j : repsArray){
          j.setFont(latestData);
          repsPanel.add(j);
          repsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
          repLabel.add(j);
        }
        
        //Block, in dem das zugehörige Datum angezeigt wird
        JLabel dateLabel = new JLabel("Datum");
        dateLabel.setFont(changedFont);
        JLabel dateOne = new JLabel(" - ");
        JLabel dateTwo = new JLabel(" - ");
        JLabel dateThree = new JLabel(" - ");
        JLabel dateFour = new JLabel(" - ");
        JLabel dateFive = new JLabel(" - ");
        JLabel dateSix = new JLabel(" - ");
        JLabel dateSeven = new JLabel(" - ");
        JLabel[] dateArray = {dateSeven, dateSix, dateFive, dateFour, dateThree, dateTwo, dateOne};
        datePanel.add(dateLabel);
        datePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        for(JLabel j : dateArray){
            j.setFont(latestData);
            datePanel.add(j);
            datePanel.add(Box.createRigidArea(new Dimension(0, 10)));
            dateLabelList.add(j);
        }
   
        //Zusammenfügen des Blocks, der Datum, Gewicht und Wiederholungen anzegeit
        lastValues.add(datePanel);
        lastValues.add(Box.createRigidArea(new Dimension(30, 0)));
        lastValues.add(weightsPanel);
        lastValues.add(Box.createRigidArea(new Dimension(30, 0)));
        lastValues.add(repsPanel);
        lastValueContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        lastValueContainer.add(lastValues);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 20, 20, 20);
        mainWindow.add(lastValueContainer, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weighty = 0.8;
        gbc.insets = new Insets(10, 10, 10, 10);
        //Einbinden des FX-Graphen
        JPanel fxWrapper = new JPanel();
        fxWrapper.add(fxp);
        mainWindow.add(fxWrapper, gbc);
        Platform.runLater(() -> {
            initFX(fxp);
        });
    }

    public static void main(String args[]) {
        MainWindow test = new MainWindow();
        test.buildMainWindow();
    }
}
