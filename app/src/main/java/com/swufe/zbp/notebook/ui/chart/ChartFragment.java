package com.swufe.zbp.notebook.ui.chart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartView;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AAMoveOverEventMessageModel;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartAnimationType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartSymbolStyleType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AADataLabels;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAPie;
import com.google.gson.Gson;
import com.swufe.zbp.notebook.R;

public class ChartFragment extends Fragment implements AAChartView.AAChartViewCallBack{
    private static String TAG="ChartFragment";
    private ChartViewModel themeStyleViewModel;
    private AAChartModel aaChartModel;
    private AAChartView aaChartView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        themeStyleViewModel =
                new ViewModelProvider(this).get(ChartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        return root;
    }
    private void setUpAAChartView() {
        aaChartView = getActivity().findViewById(R.id.AAChartView);
        aaChartView.callBack = this;
        configureTheStyleForDifferentTypeChart("chartType");
        aaChartView.aa_drawChartWithChartModel(aaChartModel);

    }


    void configureTheStyleForDifferentTypeChart(String chartType) {
        if (chartType.equals(AAChartType.Pie)) {
            configurePieChartStyle(chartType);
        } else if (chartType.equals(AAChartType.Column) || chartType.equals(AAChartType.Bar)) {
            configureColumnChartAndBarChartStyle(chartType);
        } else if (chartType.equals(AAChartType.Line) || chartType.equals(AAChartType.Spline)) {
            configureLineChartAndSplineChartStyle(chartType);
        }

    }


    private void configureColumnChartAndBarChartStyle(String chartType) {
        aaChartModel.chartType(AAChartType.Column)
                .categories(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun","Jul", "Aug", "Sep", "Oct", "Nov", "Dec"})
                .legendEnabled(true)
                .colorsTheme(new String[]{"#fe117c","#ffc069","#06caf4","#7dffc0"})
                .animationType(AAChartAnimationType.EaseOutCubic)
                .animationDuration(1200);
    }

    private  void  configurePieChartStyle(String chartType) {
        aaChartModel
                .chartType(chartType)
                .backgroundColor("#ffffff")
                .title("LANGUAGE MARKET SHARES JANUARY,2020 TO MAY")
                .subtitle("virtual data")
                .dataLabelsEnabled(true)//是否直接显示扇形图数据
                .yAxisTitle("℃")
                .series(new AAPie[] {
                                new AAPie()
                                        .name("Language market shares")
                                        .innerSize("20%")
                                        .size(150f)
                                        .dataLabels(new AADataLabels()
                                                .enabled(true)
                                                .useHTML(true)
                                                .distance(5f)
                                                .format("<b>{point.name}</b>: <br> {point.percentage:.1f} %"))
                                        .data(new Object[][] {
                                        {"Java"  ,67},
                                        {"Swift",999},
                                        {"Python",83},
                                        {"OC"    ,11},
                                        {"Go"    ,30},
                                })
                                ,
                        }
                );
    }

    private void configureLineChartAndSplineChartStyle(String chartType) {
        aaChartModel.chartType(chartType)
                .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank)//设置折线连接点样式为:边缘白色
                .markerRadius(6f);
        if (chartType.equals(AAChartType.Spline)) {
            AASeriesElement element1 = new AASeriesElement()
                    .name("Tokyo")
                    .lineWidth(7f)
                    .data(new Object[]{50, 320, 230, 370, 230, 400,});

            AASeriesElement element2 = new AASeriesElement()
                    .name("Berlin")
                    .lineWidth(7f)
                    .data(new Object[]{80, 390, 210, 340, 240, 350,});

            AASeriesElement element3 = new AASeriesElement()
                    .name("New York")
                    .lineWidth(7f)
                    .data(new Object[]{100, 370, 180, 280, 260, 300,});

            AASeriesElement element4 = new AASeriesElement()
                    .name("London")
                    .lineWidth(7f)
                    .data(new Object[]{130, 350, 160, 310, 250, 268,});

            aaChartModel
                    .animationType(AAChartAnimationType.SwingFromTo)
                    .series(new AASeriesElement[]{element1, element2, element3, element4});

        }
    }



    @Override
    public void chartViewDidFinishLoad(AAChartView aaChartView) {
        Log.i(TAG, "chartViewDidFinishLoad: ");
    }

    @Override
    public void chartViewMoveOverEventMessage(AAChartView aaChartView, AAMoveOverEventMessageModel messageModel) {
        Gson gson = new Gson();
        System.out.println("👌👌👌👌👌move over event message " + gson.toJson(messageModel));

    }
}