/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package all;

/**
 *
 * @author marc3
 */
import java.io.*;

public class DataSaving {
	private static File saving;
	
	//Methode, um in das jeweilige File zu schreiben. Dabei wird die Anzahl an Wdh. mit einem : getrennt, um für das Auslesen der Daten die Position bestimmen zu können
	public void writeData(String date, String weight, String reps, String exercise) {
            String text = date + " " + weight + " :" + reps;
		switch(exercise) {
		case "Bankdrücken" -> saving = new File("dataBankdrücken.txt");
		case "Kniebeuge" -> saving = new File("dataKniebeuge.txt");
		case "Kreuzheben" -> saving = new File ("dataKreuzheben.txt");
		case "Überkopfdrücken" -> saving = new File ("dataOverhead.txt");
		}
		try(FileWriter fw = new FileWriter(saving, true); BufferedWriter bw = new BufferedWriter(fw); FileReader fr = new FileReader(saving);
				BufferedReader br = new BufferedReader(fr)){
			br.mark(300);
			String line = br.readLine();
			int lineCount = 0;
			while(line != null) {
				lineCount++;
				line = br.readLine();
			}
			if(lineCount >= 7) {  //Schleife, die sicherstellt, dass nur die lezten sieben Werte in dem File gepeichert werden
				br.reset();
				String firstLine = br.readLine();
				File temp = File.createTempFile("temp", null); //temporäres File, dass die letzten sechs Werte enthält
				br.reset();
				line = br.readLine();
				while(line != null) {
					try(FileWriter tempWriter = new FileWriter(temp, true); BufferedWriter bwt = new BufferedWriter(tempWriter)){
						bwt.write(line + "\n");
					} catch(Exception e) {
					}
					line = br.readLine();
				}
				fr.close();
				br.close();
				bw.close();
				fw.close();
				String tempString = saving.getName();
				saving.delete();
				saving = new File(tempString);
				try(FileReader tempReader = new FileReader(temp); BufferedReader brt = new BufferedReader(tempReader); 
						FileWriter nfw = new FileWriter(saving, true); BufferedWriter nbw = new BufferedWriter (nfw)){
					line = brt.readLine();
					while(line != null) {
						if (!line.equals(firstLine)) {
							nfw.write(line + "\n");
						}
						line = brt.readLine();
					}
					nfw.write(text + "\n");
					tempReader.close();
					brt.close();
					nfw.close();
					nbw.close();
				} catch (Exception e) {
				}
			} else {
				bw.write(text + "\n");
			}
		} catch(Exception e) {
		}
	}
	
	public String[][] readData(String exercise) { //Methode, die Daten aus dem jeweiligen File ausliest
            String[][] data = new String[7][3];
            switch(exercise) {
                case "Bankdrücken" -> saving = new File("dataBankdrücken.txt");
                case "Kniebeuge" -> saving = new File("dataKniebeuge.txt");
                case "Kreuzheben" -> saving = new File ("dataKreuzheben.txt");
                case "Überkopfdrücken" -> saving = new File ("dataOverhead.txt");
                }
		try(FileReader fr = new FileReader(saving); BufferedReader br = new BufferedReader(fr)){
			String currLine = br.readLine();
                        int gap = 0;
			int i = 0;
                        while (currLine != null){
                            gap = currLine.indexOf(":");
                            if (gap != 0){
                                data[i][0] = currLine.substring(0, 10);  //bei [X][0] steht jeweils das Datum
				data[i][1] = currLine.substring(11, gap - 1);  //bei [X][1] steht je das zugehörige Gewicht
                                data[i][2] = currLine.substring(gap + 1); //bei [X][2] stehen die zugehörigen Wiederholungen
				i ++;
                            	currLine = br.readLine();
                            }
                        }
			fr.close();
			br.close();
		}catch(Exception e) {
		}
		return data;	
	}
        

}
