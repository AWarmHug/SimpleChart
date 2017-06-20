package me.rebi.simplechart;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import me.rebi.simplechartlibrary.BarChart;
import me.rebi.simplechartlibrary.LineChart;
import me.rebi.simplechartlibrary.PieChart;
import me.rebi.simplechartlibrary.Value;

public class MainActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Value value = new Value("玩游戏", 6, Color.BLUE);
        Value value1 = new Value("吃饭", 9, Color.CYAN);
        Value value2 = new Value("看书", 12, Color.DKGRAY);
        Value value3 = new Value("写代码", 9, Color.BLACK);
        Value value4 = new Value("睡觉", 80, Color.RED);
        Value value5 = new Value("发呆", 70, Color.GRAY);
        List<Value> values = new ArrayList<>();
        values.add(value);
        values.add(value1);
        values.add(value2);
        values.add(value3);
        values.add(value4);
        values.add(value5);
        values.add(value);
        values.add(value1);
        values.add(value2);
        values.add(value3);
        values.add(value4);
        values.add(value5);
        values.add(value);
        values.add(value1);
        values.add(value2);
        values.add(value3);
        values.add(value4);
        values.add(value5);

        pieChart= (PieChart) this.findViewById(R.id.pieChart);
        pieChart.initValues(values);

        barChart= (BarChart) this.findViewById(R.id.barChart);
        barChart.initValues(values);

        lineChart= (LineChart) this.findViewById(R.id.lineChart);
        lineChart.initValues(values);
    }
}
