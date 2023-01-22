/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package all;
import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 *
 * @author marc3
 */
public class OneRepMaxCalc { 
    
    public BigDecimal returnOneRepMax(double weight, double reps){  //Methode zum Berechnen des geschätzen One-Rep-Max-Wertes je Übung
     double i;
     i = weight * (1 + (reps / 30)); //nach Epley
     BigDecimal format = new BigDecimal(i).setScale(2, RoundingMode.HALF_UP);
     return format;
    }
    
}
