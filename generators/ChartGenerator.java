/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.generators;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.DataUtil;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import lombardia2014.core.UserOperations;

/**
 *
 * @author Domek
 */
public class ChartGenerator {

    Line line1 = null, line2 = null;
    LineChart chart = null;
    String title = null;
    AxisLabels yAxis = null;
    AxisStyle axisStyle = null;
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY HH:mm");
    UserOperations operationList = null;
    // list
    List<String> increesValue = new ArrayList<>(),
            decreaseValue = new ArrayList<>();
    List<Double> dataMinus = new ArrayList<>(),
            dataPlus = new ArrayList<>();
    // this is super hack, but is work :D
    List<String[]> operations = new ArrayList<>();
    int sumIncrese = 0, sumDecrese = 0;
    double totalExpenditure = 0.0, totalEarnings = 0.0;

    public ChartGenerator(String title_) {
        title = title_;
        generateFilds();
    }

    public void generateFilds() {
        operationList = new UserOperations();
        operations.addAll(
                operationList.readOperation(title));
        // prepare paterns
        increesValue.add("Spłacono umowe kredytu za");
        increesValue.add("Produkt sprzedano za");
        decreaseValue.add("Zakupiono przedmiot za");
        decreaseValue.add("Wystawiono umowe kredytu na");

        // create axis
        for (String[] operation : operations) {
            String[] tmp = operation[2].split(":");
            if (increesValue.contains(tmp[0])) {
                dataPlus.add(Double.parseDouble(tmp[1]));
                sumIncrese++;
                totalEarnings += Double.parseDouble(tmp[1]);
            } else if (decreaseValue.contains(tmp[0])) {
                dataMinus.add(Double.parseDouble(tmp[1]));
                sumDecrese++;
                totalExpenditure += Double.parseDouble(tmp[1]);
            }
        }

    }

    public LineChart generateChart() {
        //generate one line
        line1 = Plots.newLine(DataUtil.scaleWithinRange(0, 5000, dataMinus),
                Color.YELLOW, "Minus");
        line1.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
        line1.addShapeMarkers(Shape.CIRCLE, Color.YELLOW, 10);
        line1.addShapeMarkers(Shape.CIRCLE, Color.BLACK, 7);
        line1.setFillAreaColor(Color.LIGHTYELLOW);

        //generate secound line
        line2 = Plots.newLine(DataUtil.scaleWithinRange(0, 5000, dataPlus),
                Color.LIMEGREEN, "Plus");
        line2.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
        line2.addShapeMarkers(Shape.CIRCLE, Color.LIME, 10);
        line2.addShapeMarkers(Shape.CIRCLE, Color.BLACK, 7);
        line2.setFillAreaColor(Color.LIGHTGREEN);

        //create chart
        chart = GCharts.newLineChart(line1, line2);
        chart.setSize(440, 300);

        // Defining background and chart fills.
        axisStyle = AxisStyle.newAxisStyle(Color.BLACK, 12, AxisTextAlignment.CENTER);
        yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 5000);
        yAxis.setAxisStyle(axisStyle);
        chart.addYAxisLabels(yAxis);
        chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
        return chart;
    }

    // sum increese 
    public Integer getSumIncrese() {
        return sumIncrese;
    }    
            
    // sum decrase
    public Integer getSumDecrese() {
        return sumDecrese;
    }    
    
    // total expenditure
    public Double getTotalEarnings() {
        return totalEarnings;
    }
    
    // sum of earnings
    public Double getTotalExpenditure() {
        return totalExpenditure;
    }
}
