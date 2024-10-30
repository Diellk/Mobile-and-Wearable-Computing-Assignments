package com.example.stepappv4;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.stepappv4.databinding.FragmentDailyBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyFragment extends Fragment {

    public int todaySteps = 0;
    TextView numStepsTextView;
    AnyChartView dailyStepsChart;

    Date cDate = new Date();
    String current_time = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

    public Map<Integer, Integer> stepsByHour = null;

    private FragmentDailyBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDailyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        System.out.println("Rooot" + root);
        System.out.println("ID:"+R.id.dailyStepsChart);

        dailyStepsChart = root.findViewById(R.id.dailyStepsChart);
        System.out.println("stepsChart:"+dailyStepsChart);
        dailyStepsChart.setProgressBar(root.findViewById(R.id.loadingBarDaily));
        dailyStepsChart.setBackgroundColor("#00000000");
        Cartesian dailyCartesian = createDailyColumnChart();
        dailyStepsChart.setChart(dailyCartesian);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public Cartesian createDailyColumnChart() {
        Map<String, Integer> stepsByDate = StepAppOpenHelper.loadStepsByDate(getContext());

        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : stepsByDate.entrySet()) {
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }

        Column column = cartesian.column(data);
        column.fill("#4CAF50");
        column.stroke("#4CAF50");

        column.tooltip()
                .position(Position.RIGHT_TOP)
                .offsetX(0d)
                .offsetY(5);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yScale().minimum(0);

        cartesian.yAxis(0).title("Number of Steps");
        cartesian.xAxis(0).title("Date");
        cartesian.background().fill("#00000000");
        cartesian.animation(true);

        return cartesian;
    }
}