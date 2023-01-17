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

public class DataSaving implements Serializable{
	private static final long serialVersionUID = 1;
	private static String[][] Data;
	private static File saving;
	
	public DataSaving() {
		
	}
	
	/* Daten per FileWriter/ BufferedWriter ins File eintragen, dann ein Word als EoF festlegen
	 * Dereferenzieren der Daten mit Substring, um einzelne Werte zu erlangen
	 *  */
	
	public void writeData(String date, String weight, String reps, String exercise) {
            String text = date + " " + weight + " :" + reps;
		switch(exercise) {
		case "Bankdrücken": 
                    saving = new File("dataBankdrücken.txt");
                    break;
		case "Kniebeuge": 
                    saving = new File("dataKniebeuge.txt");
                    break;
		case "Kreuzheben": 
                    saving = new File ("dataKreuzheben.txt");
                    break;
		case "Überkopfdrücken": 
                    saving = new File ("dataOverhead.txt");
                    break;
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
			if(lineCount >= 7) {
				br.reset();
				String firstLine = br.readLine();
				File temp = File.createTempFile("temp", null);
				br.reset();
				line = br.readLine();
				while(line != null) {
					try(FileWriter tempWriter = new FileWriter(temp, true); BufferedWriter bwt = new BufferedWriter(tempWriter)){
						bwt.write(line + "\n");
					} catch(Exception e) {
						e.printStackTrace();
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
					e.printStackTrace();
				}
			} else {
				bw.write(text + "\n");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String[][] readDiagramData(String exercise) {
            Data = new String[7][2];
            switch(exercise) {
                case "Bankdrücken": 
                    saving = new File("dataBankdrücken.txt");
                    break;
                case "Kniebeuge": 
                    saving = new File("dataKniebeuge.txt");
                    break;
                case "Kreuzheben": 
                    saving = new File ("dataKreuzheben.txt");
                    break;
                case "Überkopfdrücken": 
                    saving = new File ("dataOverhead.txt");
                    break;
                }
		try(FileReader fr = new FileReader(saving); BufferedReader br = new BufferedReader(fr)){
			String currLine = br.readLine();
                        int gap = 0;
                        if (currLine != null)
                            gap = currLine.indexOf(":");
			int i = 0;
			int j;
                        if (gap != 0){
                            while (currLine != null){
                            	j = 0;
				Data[i][j] = currLine.substring(0, 11);  //bei [X][0] steht jeweils das Datum
				j++;
				Data[i][j] = currLine.substring(11, gap - 1);  //bei [X][1] steht je das zugehörige Gewicht
				i ++;
                            	currLine = br.readLine();
                            }
                        }
			fr.close();
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Data;	
	}
        
        public String[][] readHistoryData(String exercise){
            Data = new String[7][2];
            switch(exercise) {
		case "Bankdrücken": 
                    saving = new File("dataBankdrücken.txt");
                    break;
                case "Kniebeuge": 
                    saving = new File("dataKniebeuge.txt");
                    break;
		case "Kreuzheben": 
                    saving = new File ("dataKreuzheben.txt");
                    break;
		case "Überkopfdrücken": 
                    saving = new File ("dataOverhead.txt");
                    break;
		}
		try(FileReader fr = new FileReader(saving); BufferedReader br = new BufferedReader(fr)){
			String currLine = br.readLine();
                        int gap = 0;
			int i = 0;
			int j;
                        while (currLine != null) {
                            gap = currLine.indexOf(":");
                            if(gap != 0){
                              j = 0;
                              Data[i][j] = currLine.substring(11, gap);  //bei [X][0] steht jeweils das Gewicht
                              j++;
                              Data[i][j] = currLine.substring(gap + 1);  //bei [X][1] steht je die zugehörige Zahl an Wiederholungen
                              i ++;
                              currLine = br.readLine();
                            }
                        }
			fr.close();
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Data;
        }
}
