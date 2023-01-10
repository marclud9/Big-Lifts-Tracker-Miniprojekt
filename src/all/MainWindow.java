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
    private final Font changedFont = new Font("Dialog", Font.PLAIN, 17);

    public MainWindow() {

    }
	
    //Die private statische Methode ist notwendig, um die FX ProgressChart in die Swing-Gui einbinden zu können
    private static void initFX(JFXPanel fxpanel) {
        Platform.runLater(new Runnable() {
            public void run() {
                chart = new ProgressChart();
                Scene scene = new Scene(chart.getChart(), 700, 500);
                fxpanel.setScene(scene);
            }
        });
    }
        
    
    @Override
    public void itemStateChanged(ItemEvent e){ //JComboBox Auswahl
       //Anpassen der letzten Werte
       String[][] tempData = saveData.readHistoryData(chooseExercise.getSelectedItem().toString());
       JLabel tempweight;
       JLabel tempreps;
       String tempOneRepWeight = "";
       String tempOneRepReps = "";
       for(int i = 0; i < tempData.length; i++){
           tempweight = (JLabel) weightsLabel.get(i);
           tempreps = (JLabel) repLabel.get(i);
           tempweight.setText(tempData[i][0]);
           tempreps.setText(tempData[i][1]);
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
       String[][] temp = saveData.readDiagramData(chooseExercise.getSelectedItem().toString());
       if (temp != null){
        Platform.runLater(new Runnable(){
           public void run(){
            if(temp != null){
                chart.getChart().getData().clear();
                chart.addToSeries(temp, chooseExercise.getSelectedItem().toString());
            }
           }
        });  
       }
       
    }

    @Override
    public void actionPerformed(ActionEvent e){
       String weightIn = inputWeight.getText();
       String repsIn = inputReps.getText(); 
       //Anpassen One-Rep-Max
       oneRep.setText("Estimated One-Rep-Max: " + calc.returnOneRepMax(Double.parseDouble(weightIn), Double.parseDouble(repsIn)).toString());
       oneRep.setFont(changedFont);
       LocalDate date = LocalDate.now();
       //Anpassen der letzten Werte
       saveData.writeData(date.toString(), weightIn, repsIn, chooseExercise.getSelectedItem().toString());
       String[][] currData = saveData.readHistoryData(chooseExercise.getSelectedItem().toString());
       JLabel tempweight;
       JLabel tempreps;
       for(int i = 0; i < currData.length; i++){     
           tempweight = (JLabel) weightsLabel.get(i);
           tempreps = (JLabel) repLabel.get(i);
           tempweight.setText(currData[i][0]);
           tempreps.setText(currData[i][1]);
        }
       //Anpassen des Diagrams
       String[][] currentWork = {{weightIn, date.toString()}};
       Platform.runLater(new Runnable(){
           public void run(){
               chart.addToSeries(currentWork, chooseExercise.getSelectedItem().toString());
           }
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
        JPanel weights = new JPanel();
        JPanel reps = new JPanel();
        lastValues.setLayout(new BoxLayout(lastValues, BoxLayout.X_AXIS));
        weights.setLayout(new BoxLayout(weights, BoxLayout.Y_AXIS));
        reps.setLayout(new BoxLayout(reps, BoxLayout.Y_AXIS));
        Font latestData = new Font("Dialog", Font.PLAIN, 15);
        JLabel weightLabel = new JLabel("Gewicht");
        weightLabel.setFont(changedFont);
        JLabel wone = new JLabel(" N/A ");
        wone.setFont(latestData);
        JLabel wtwo = new JLabel(" N/A ");
        wtwo.setFont(latestData);
        JLabel wthree = new JLabel(" N/A ");
        wthree.setFont(latestData);
        JLabel wfour = new JLabel(" N/A ");
        wfour.setFont(latestData);
        JLabel wfive = new JLabel(" N/A ");
        wfive.setFont(latestData);
        JLabel wsix = new JLabel(" N/A ");
        wsix.setFont(latestData);
        JLabel wseven = new JLabel(" N/A ");
        wseven.setFont(latestData);
        weightsLabel.add(wone);
        weightsLabel.add(wtwo);
        weightsLabel.add(wthree);
        weightsLabel.add(wfour);
        weightsLabel.add(wfive);
        weightsLabel.add(wsix);
        weightsLabel.add(wseven);
        weights.add(weightLabel);
        weights.add(Box.createRigidArea(new Dimension(0, 10)));
        weights.add(wone);
        weights.add(Box.createRigidArea(new Dimension(0, 10)));
        weights.add(wtwo);
        weights.add(Box.createRigidArea(new Dimension(0, 10)));
        weights.add(wthree);
        weights.add(Box.createRigidArea(new Dimension(0, 10)));
        weights.add(wfour);
        weights.add(Box.createRigidArea(new Dimension(0, 10)));
        weights.add(wfive);
        weights.add(Box.createRigidArea(new Dimension(0, 10)));
        weights.add(wsix);
        weights.add(Box.createRigidArea(new Dimension(0, 10)));
        weights.add(wseven);

        JLabel repsLabel = new JLabel("Reps");
        repsLabel.setFont(changedFont);
        JLabel rone = new JLabel(" N/A ");
        rone.setFont(latestData);
        JLabel rtwo = new JLabel(" N/A ");
        rtwo.setFont(latestData);
        JLabel rthree = new JLabel(" N/A ");
        rthree.setFont(latestData);
        JLabel rfour = new JLabel(" N/A ");
        rfour.setFont(latestData);
        JLabel rfive = new JLabel(" N/A ");
        rfive.setFont(latestData);
        JLabel rsix = new JLabel(" N/A ");
        rsix.setFont(latestData);
        JLabel rseven = new JLabel(" N/A ");
        rseven.setFont(latestData);
        repLabel.add(rone);
        repLabel.add(rtwo);
        repLabel.add(rthree);
        repLabel.add(rfour);
        repLabel.add(rfive);
        repLabel.add(rsix);
        repLabel.add(rseven);
        reps.add(repsLabel);
        reps.add(Box.createRigidArea(new Dimension(0, 10)));
        reps.add(rone);
        reps.add(Box.createRigidArea(new Dimension(0, 10)));
        reps.add(rtwo);
        reps.add(Box.createRigidArea(new Dimension(0, 10)));
        reps.add(rthree);
        reps.add(Box.createRigidArea(new Dimension(0, 10)));
        reps.add(rfour);
        reps.add(Box.createRigidArea(new Dimension(0, 10)));
        reps.add(rfive);
        reps.add(Box.createRigidArea(new Dimension(0, 10)));
        reps.add(rsix);
        reps.add(Box.createRigidArea(new Dimension(0, 10)));
        reps.add(rseven);

        lastValues.add(weights);
        lastValues.add(Box.createRigidArea(new Dimension(50, 0)));
        lastValues.add(reps);

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
        Platform.runLater(new Runnable() {
            public void run() {
                initFX(fxp);
            }
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
