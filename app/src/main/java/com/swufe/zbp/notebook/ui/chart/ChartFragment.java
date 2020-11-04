package com.swufe.zbp.notebook.ui.chart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.swufe.zbp.notebook.MainActivity;
import com.swufe.zbp.notebook.R;
import com.swufe.zbp.notebook.model.CommonValue;
import com.swufe.zbp.notebook.model.Note;
import com.swufe.zbp.notebook.ui.home.HomeViewModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ChartFragment extends Fragment implements AAChartView.AAChartViewCallBack{
    private static String TAG="ChartFragment";
    private List<Note> noteList;
    private AAChartModel aaChartModel;
    private AAChartView aaChartView;
    private HomeViewModel homeViewModel;
    private Spinner spinner;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        aaChartView = getActivity().findViewById(R.id.AAChartView);
        spinner=getActivity().findViewById(R.id.chart_type);
        homeViewModel= new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        
        noteList=homeViewModel.getNoteList().getValue();
        aaChartView.callBack = this;
        aaChartModel = new AAChartModel();
        //configurePieChartStyle();
        configureColumnChartAndBarChartStyle();
        aaChartView.aa_drawChartWithChartModel(aaChartModel);
        TypeSelectListener(spinner);

    }

    void configureTheStyleForDifferentTypeChart(int position) {
        switch(position){
            case 0:
                configurePieChartStyle();
                break;
            case 1:
                configureColumnChartAndBarChartStyle();
                break;
            case 2:
                configureLineChartAndSplineChartStyle();
                break;
            default:
                configurePieChartStyle();
                break;
        }



    }


    private void configureColumnChartAndBarChartStyle() {
        String[] types=getResources().getStringArray(R.array.noteType);
        long total_notes_number=noteList.size();
        long[] nums_array=new long[types.length];
        String[] colorsTheme=new String[types.length];
        Object[][] nums_colock=new Object[types.length][24];
        AASeriesElement[] elements = new AASeriesElement[types.length];
        String[] clocks=new String[24];
        for (int i=0;i<clocks.length;i++){
            clocks[i]=String.valueOf(i+1);
        }
        for(int i=0;i<nums_array.length;i++){
            for(int j=0;j<clocks.length;j++){
                nums_colock[i][j]=0;

            }
        }
        for (Note note : noteList) {

                nums_colock[0][note.getClock()-1]=(int)nums_colock[0][note.getClock()-1]+1;
                for(int i=0;i<nums_array.length;i++){
                    if(note.getNoteType().equals(types[i])){
                        nums_colock[i][note.getClock()-1]=(int)nums_colock[i][note.getClock()-1]+1;
                        break;
                    }

                }


        }
        for(int i=0;i<nums_array.length;i++){
            nums_array[i]=0;
            elements[i]=new AASeriesElement().name(types[i]).data(nums_colock[i]);

        }

        for(int i=0;i<colorsTheme.length;i++){
            colorsTheme[i]= CommonValue.COLORTHEME[i][1];
        }
        aaChartModel.chartType(AAChartType.Column)
                .backgroundColor("#8470FF")
                .colorsTheme(colorsTheme)
                .title("Note moment statistics bar chart")
                .subtitle("total data")
                .categories(clocks)
                .legendEnabled(true)
                .series(elements)
                .animationType(AAChartAnimationType.EaseOutCubic)
                .animationDuration(1200);
    }

    private  void  configurePieChartStyle() {
        String[] types=getResources().getStringArray(R.array.noteType);
        long total_notes_number=noteList.size();
        long[] nums_array=new long[types.length-1];
        Object[][] data=new Object[types.length-1][2];

        for(int i=0;i<nums_array.length;i++){
            nums_array[i]=0;
            data[i][0]=types[i+1];

        }
        for (Note note : noteList) {
            for(int i=0;i<nums_array.length;i++){
                if(note.getNoteType().equals(types[i])){
                    nums_array[i]++;
                    break;
                }
            }
        }
        for(int i=0;i<nums_array.length;i++){
            data[i][1]=100*nums_array[i]/total_notes_number;
        }
        String[] colorsTheme=new String[types.length-1];
        for(int i=0;i<colorsTheme.length;i++){
            colorsTheme[i]= CommonValue.COLORTHEME[i][1];
        }
        aaChartModel
                .chartType(AAChartType.Pie)
                .backgroundColor("#8470FF")
                .colorsTheme(colorsTheme)
                .title("Note classification ratio fan chart")
                .subtitle("total data")
                .dataLabelsEnabled(true)//是否直接显示扇形图数据
                .yAxisTitle("℃")
                .series(new AAPie[] {
                                new AAPie()
                                        .name("Note type shares")
                                        .innerSize("20%")
                                        .size(150f)
                                        .dataLabels(new AADataLabels()
                                                .enabled(true)
                                                .useHTML(true)
                                                .distance(5f)
                                                .format("<b>{point.name}</b>: <br> {point.percentage:.1f} %"))
                                        .data(data)
                                ,
                        }
                );
    }

    private void configureLineChartAndSplineChartStyle() {
        String[] types=getResources().getStringArray(R.array.noteType);
        long total_notes_number=noteList.size();
        long[] nums_array=new long[types.length];
        String[] colorsTheme=new String[types.length];
        Object[][] nums_month=new Object[types.length][12];
        AASeriesElement[] elements = new AASeriesElement[types.length];
      
        for(int i=0;i<nums_array.length;i++){
            for(int j=0;j<12;j++){
                nums_month[i][j]=0;

            }
        }
        for (Note note : noteList) {

            nums_month[0][note.getMonth()-1]=(int) nums_month[0][note.getMonth()-1]+1;
            for(int i=0;i<nums_array.length;i++){
                if(note.getNoteType().equals(types[i])){
                    nums_month[i][note.getMonth()-1]=(int) nums_month[i][note.getMonth()-1]+1;
                    break;
                }

            }


        }
        for(int i=0;i<nums_array.length;i++){
            nums_array[i]=0;
            elements[i]=new AASeriesElement().name(types[i]).data( nums_month[i]);

        }

        for(int i=0;i<colorsTheme.length;i++){
            colorsTheme[i]= CommonValue.COLORTHEME[i][1];
        }
        aaChartModel.chartType(AAChartType.Line)
                .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank)//设置折线连接点样式为:边缘白色
                .backgroundColor("#8470FF")
                .markerRadius(6f)
                .categories(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun","Jul", "Aug", "Sep", "Oct", "Nov", "Dec"})
                .animationType(AAChartAnimationType.SwingFromTo)
                .series(elements);;

    }

    private void TypeSelectListener(Spinner spinner){

        //给Spinner添加事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //当选中某一个数据项时触发该方法
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
               // String chartType = (String)spinner.getItemAtPosition(position);//从spinner中获取被选择的数据
                configureTheStyleForDifferentTypeChart(position );
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
                Log.i(TAG, "onItemSelected: "+position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
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