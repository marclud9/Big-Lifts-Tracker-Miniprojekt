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
    private final JFXPanel fxp = new JFXPanel();
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
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        LocalDate date = LocalDate.now();
        JLabel currentDate = new JLabel("Aktuelles Datum: " + format.format(date));
        currentDate.setFont(changedFont);
        mainWindow.add(currentDate);
        currentDate.setVisible(true);
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
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        lastValues.setLayout(new BoxLayout(lastValues, BoxLayout.X_AXIS));
        weightsPanel.setLayout(new BoxLayout(weightsPanel, BoxLayout.Y_AXIS));
        repsPanel.setLayout(new BoxLayout(repsPanel, BoxLayout.Y_AXIS));
        Font latestData = new Font("Dialog", Font.PLAIN, 15);
        
        //Block, in dem die Gewichte angezeigt werden 
        JLabel weightLabel = new JLabel("Gewicht");
        weightLabel.setFont(changedFont);
        JLabel weightsOne = new JLabel(" - ");
        weightsOne.setFont(latestData);
        JLabel weightsTwo = new JLabel(" - ");
        weightsTwo.setFont(latestData);
        JLabel weightsThree = new JLabel(" - ");
        weightsThree.setFont(latestData);
        JLabel weightsFour = new JLabel(" - ");
        weightsFour.setFont(latestData);
        JLabel weightsFive = new JLabel(" - ");
        weightsFive.setFont(latestData);
        JLabel weightsSix = new JLabel(" - ");
        weightsSix.setFont(latestData);
        JLabel weightsSeven = new JLabel(" - ");
        weightsSeven.setFont(latestData);
        weightsLabel.add(weightsSeven);
        weightsLabel.add(weightsSix);
        weightsLabel.add(weightsFive);
        weightsLabel.add(weightsFour);
        weightsLabel.add(weightsThree);
        weightsLabel.add(weightsTwo);
        weightsLabel.add(weightsOne);
        weightsPanel.add(weightLabel);
        weightsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        weightsPanel.add(weightsOne);
        weightsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        weightsPanel.add(weightsTwo);
        weightsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        weightsPanel.add(weightsThree);
        weightsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        weightsPanel.add(weightsFour);
        weightsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        weightsPanel.add(weightsFive);
        weightsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        weightsPanel.add(weightsSix);
        weightsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        weightsPanel.add(weightsSeven);
        
        //Block, in dem die Anzahl an Wiederholungen angezeigt wird
        JLabel repsLabel = new JLabel("Reps");
        repsLabel.setFont(changedFont);
        JLabel repsOne = new JLabel(" - ");
        repsOne.setFont(latestData);
        JLabel repsTwo = new JLabel(" - ");
        repsTwo.setFont(latestData);
        JLabel repsThree = new JLabel(" - ");
        repsThree.setFont(latestData);
        JLabel repsFour = new JLabel(" - ");
        repsFour.setFont(latestData);
        JLabel repsFive = new JLabel(" - ");
        repsFive.setFont(latestData);
        JLabel repsSix = new JLabel(" - ");
        repsSix.setFont(latestData);
        JLabel repsSeven = new JLabel(" - ");
        repsSeven.setFont(latestData);
        repLabel.add(repsSeven);
        repLabel.add(repsSix);
        repLabel.add(repsFive);
        repLabel.add(repsFour);
        repLabel.add(repsThree);
        repLabel.add(repsTwo);
        repLabel.add(repsOne);
        repsPanel.add(repsLabel);
        repsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        repsPanel.add(repsOne);
        repsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        repsPanel.add(repsTwo);
        repsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        repsPanel.add(repsThree);
        repsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        repsPanel.add(repsFour);
        repsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        repsPanel.add(repsFive);
        repsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        repsPanel.add(repsSix);
        repsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        repsPanel.add(repsSeven);
        
        //Block, in dem das zugehörige Datum angezeigt wird
        JLabel dateLabel = new JLabel("Datum");
        dateLabel.setFont(changedFont);
        JLabel dateOne = new JLabel(" - ");
        dateOne.setFont(latestData);
        JLabel dateTwo = new JLabel(" - ");
        dateTwo.setFont(latestData);
        JLabel dateThree = new JLabel(" - ");
        dateThree.setFont(latestData);
        JLabel dateFour = new JLabel(" - ");
        dateFour.setFont(latestData);
        JLabel dateFive = new JLabel(" - ");
        dateFive.setFont(latestData);
        JLabel dateSix = new JLabel(" - ");
        dateSix.setFont(latestData);
        JLabel dateSeven = new JLabel(" - ");
        dateSeven.setFont(latestData);
        dateLabelList.add(dateSeven);
        dateLabelList.add(dateSix);
        dateLabelList.add(dateFive);
        dateLabelList.add(dateFour);
        dateLabelList.add(dateThree);
        dateLabelList.add(dateTwo);
        dateLabelList.add(dateOne);
        datePanel.add(dateLabel);
        datePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        datePanel.add(dateOne);
        datePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        datePanel.add(dateTwo);
        datePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        datePanel.add(dateThree);
        datePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        datePanel.add(dateFour);
        datePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        datePanel.add(dateFive);
        datePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        datePanel.add(dateSix);
        datePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        datePanel.add(dateSeven);
        
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
        JPanel fxWrapper = new JPanel();
        fxWrapper.add(fxp);
        mainWindow.add(fxWrapper, gbc);
        Platform.runLater(() -> {
            initFX(fxp);
        });
    }

    public String getExercise() {
        String exercise = (String) chooseExercise.getSelectedItem();
        return exercise;
    }

    public static void main(String args[]) {
        MainWindow test = new MainWindow();
        test.buildMainWindow();
    }
}
