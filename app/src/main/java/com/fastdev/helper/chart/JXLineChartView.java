package com.fastdev.helper.chart;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.fastdev.helper.RegexHelper;
import com.fastdev.ui.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class JXLineChartView extends LineChart {
    private YAxisMarkerView yAxisMarkerView;
    private XAxisMarkerView xAxisMarkerView;
    private HighlightPointMarkerView  pointMarkerView;
    private List<StudyPoint> mStudyData;//数据
    private int indexToday; //今天对应的下标位置
    //x轴设置
    private XAxis xAxis;
    //y轴设置
    private YAxis yAxis;
    private HighlightChangeListener highlightChangeListener;
    private boolean firstRun = true;

    public JXLineChartView(Context context) {
        super(context);
        initView(context);
    }

    public JXLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        yAxisMarkerView = new YAxisMarkerView(context);
        xAxisMarkerView = new XAxisMarkerView(context);
        pointMarkerView = new HighlightPointMarkerView(context);

        setNoDataText("");//什么都不显示
        setBackgroundColor(Color.WHITE);//背景颜色
        getAxisRight().setEnabled(false);//隐藏右边Y轴
        setScaleEnabled(false);//不允许缩放
        setDoubleTapToZoomEnabled(false);  //设置是否可以通过双击屏幕放大图表。默认是true
        getDescription().setEnabled(false);//隐藏描述
        setPinchZoom(true);//设置按比例放缩柱状图
        setDrawGridBackground(false);//不展示网格线
        setTouchEnabled(true); //有触摸事件
        setExtraBottomOffset(6);//设置底部额外的边距，不然横坐标字体太大会显示不全

        //折线图例 标签 设置
        Legend legend = getLegend();
        legend.setEnabled(false);//不显示图例

        xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//x轴对齐位置
        xAxis.setDrawGridLines(false); // 隐藏网格线
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisLineColor(ContextCompat.getColor(context, R.color.gray_eeeeee));
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.black_999999));
        xAxis.setTextSize(10);
        xAxis.setCenterAxisLabels(false);
        xAxis.setLabelCount(7, false);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setYOffset(10);

        //y轴设置
        yAxis = getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(true);// 显示网格线
        yAxis.setGridColor(ContextCompat.getColor(context, R.color.gray_eeeeee));
        yAxis.setGridDashedLine(new DashPathEffect(new float[]{5f, 5f}, 0f)); //网格线设置为虚线
        yAxis.setTextColor(ContextCompat.getColor(context, R.color.black_999999));
        yAxis.setTextSize(10);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisMinimum(0f); //保证Y轴从0开始，不然会上移一点
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
//        super.drawMarkers(canvas);
//        Log.i("markerview", "drawMarkers");
        if(!isDrawMarkersEnabled() || !valuesToHighlight()) return;

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            ILineDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;

            float[] pos = getMarkerPosition(highlight);

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue;
            // callbacks to update the content
//            mMarker.refreshContent(e, highlight);
            // draw the marker
//            mMarker.draw(canvas, pos[0], pos[1]);
            int index = (int) e.getX();

            if(xAxisMarkerView != null){
                String desc = getXAxis().getValueFormatter().getFormattedValue(e.getX());
                xAxisMarkerView.showValue((int) e.getX(), desc);
                xAxisMarkerView.refreshContent(e, highlight);
                xAxisMarkerView.draw(canvas, pos[0] - xAxisMarkerView.getWidth() / 2f, mViewPortHandler.contentBottom() + Utils.convertDpToPixel(7));
            }

            if(mStudyData != null && mStudyData.size() > index && index >= 0){
                StudyPoint item = mStudyData.get(index);
                if(highlightChangeListener != null) highlightChangeListener.onChange(item, index <= indexToday);
                if(index <= indexToday){
                    yAxisMarkerView.showValue(index, item.mineValue());
                    yAxisMarkerView.refreshContent(e, highlight);
                    yAxisMarkerView.draw(canvas, mViewPortHandler.contentLeft() - yAxisMarkerView.getWidth(),pos[1] - yAxisMarkerView.getHeight() / 2f);

                    pointMarkerView.refreshContent(e, highlight);
                    pointMarkerView.draw(canvas, pos[0] - pointMarkerView.getWidth() / 2f, pos[1] - pointMarkerView.getHeight() / 2f);

                }else{
                    yAxisMarkerView.hide();
                }
            }else{
                String desc = String.valueOf(e.getY());
                yAxisMarkerView.showValue(index, desc);
                yAxisMarkerView.refreshContent(e, highlight);
                yAxisMarkerView.draw(canvas, mViewPortHandler.contentLeft() - yAxisMarkerView.getWidth(),pos[1] - yAxisMarkerView.getHeight() / 2f);
            }
        }
    }
    PointF downPoint = new PointF();
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint.x = evt.getX();
                downPoint.y = evt.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(evt.getX() - downPoint.x) > 5) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.onTouchEvent(evt);
    }

    @Deprecated
    public void showMultipleLineChart(LineChartBuild build){
        // 多曲线图集合
        LineData lineData = new LineData();
        List<LineDataSet> lines = build.createLinesDataSet();
        if(lines != null){
            for(LineDataSet line : lines){
                lineData.addDataSet(line);
            }
        }
        //x轴设置Lable
        XAxis xAxis = getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(build.xValue));
        setData(lineData);

        //将图表动画显示之前进行缩放
        Matrix m = new Matrix();
        getViewPortHandler().refresh(m, this, false);
        animateY(500, Easing.Linear);
        animateX(300, Easing.Linear);
    }

    public void showJXLineChart(int max, int xLabelCount, int valueKeepDecimal, int yAxisKeepDecimal, List<StudyPoint> data, String today){
        showJXLineChart(max, max / 4, max - max / 4, xLabelCount, valueKeepDecimal, yAxisKeepDecimal, data, today);
    }

    /**
     * @param max y轴显示的最大值
     * @param a 低频区间起点
     * @param b 高频区间终点
     * @param xLabelCount x轴显示的刻度个数
     * @param keepDecimal 显示数据需要保留几位小数
     * @param yAxisKeepDecimal y轴数据需要保留几位小数
     * @param data 数据列表
     * @param today 当日
     */
    public void showJXLineChart(int max, int a, int b, int xLabelCount, int keepDecimal, int yAxisKeepDecimal, List<StudyPoint> data, String today){
        this.mStudyData = data;
        this.indexToday = Integer.MAX_VALUE;
        if(data == null || data.size() == 0){
            clear();
            return;
        }
        LineData lineData = new LineData();
        List<String> xValue = new ArrayList<>();

        List<Entry> pointSetMine = new ArrayList<>();
        List<Entry> pointSet200 = new ArrayList<>();
        List<Entry> pointSet180 = new ArrayList<>();
        List<Entry> pointSetRemain = new ArrayList<>();
        TransformChartData transformData = new TransformChartData(0, a, b, max);
        for(int i = 0; i < data.size(); i++){
            StudyPoint item = data.get(i);
            item.keep_decimal = keepDecimal;
            if(i > indexToday){
                pointSetRemain.add(new Entry(i, 0f));
            }else{
                Entry minePoint = new Entry(i, transformData.toValue(item.myNum));

                pointSetRemain.add(minePoint);
                pointSetMine.add(minePoint);
            }
            if(today.equals(item.day)){
                indexToday = i;
            }
            pointSet200.add(new Entry(i, transformData.toValue(item.highNum)));
            pointSet180.add(new Entry(i, transformData.toValue(item.baseNum)));
            xValue.add(item.toDay());
        }

        if(pointSetRemain.size() > 0){
            LineDataSet lineRemain = new LineDataSet(pointSetRemain, ""){
                @Override
                public boolean isHorizontalHighlightIndicatorEnabled() {
                    if(getHighlighted() != null && getHighlighted().length > 0){
                        if(getHighlighted()[0].getX() <= indexToday){
                            return super.isHorizontalHighlightIndicatorEnabled();
                        }else{
                            return false;
                        }
                    }
                    return super.isHorizontalHighlightIndicatorEnabled();
                }
            };
            lineRemain.setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            lineRemain.setDrawValues(false); //不显示曲线点的具体数值
            lineRemain.setDrawCircles(false); //不显示曲线大圆点
            lineRemain.setHighLightColor(ContextCompat.getColor(getContext(), R.color.bule_4379ff));//十字架颜色
            lineRemain.setHighlightLineWidth(0.5f); //十字架线条宽度
            lineRemain.enableDashedHighlightLine(3, 5, 0);
            lineRemain.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置线条为折线
            lineRemain.setDrawVerticalHighlightIndicator(true);
            lineRemain.setDrawHorizontalHighlightIndicator(true);
            lineData.addDataSet(lineRemain);
        }

        LineDataSet line180 = new LineDataSet(pointSet180, "");
        line180.setColor(ContextCompat.getColor(getContext(), R.color.red_ff9e2c));
        line180.setDrawValues(false); //不显示曲线点的具体数值
        line180.setDrawCircles(false); //不显示曲线大圆点
        line180.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置线条为平滑曲线
        line180.setHighlightEnabled(false);
        lineData.addDataSet(line180);

        LineDataSet line200 = new LineDataSet(pointSet200, "");
        line200.setColor(ContextCompat.getColor(getContext(), R.color.green_42d0b6));
        line200.setDrawValues(false); //不显示曲线点的具体数值
        line200.setDrawCircles(false); //不显示曲线大圆点
        line200.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置线条为平滑曲线
        line200.setHighlightEnabled(false);
        lineData.addDataSet(line200);

        LineDataSet lineMine = new LineDataSet(pointSetMine, "");
        lineMine.setColor(ContextCompat.getColor(getContext(), R.color.bule_4379ff));
        lineMine.setDrawValues(false); //不显示曲线点的具体数值
        lineMine.setDrawCircles(false); //不显示曲线大圆点
        lineMine.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置线条为平滑曲线
        lineMine.setDrawFilled(false); //设置曲线图下方显示的填充
        lineMine.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_main_chart_fill));
        lineMine.setHighlightEnabled(false);//显示选中十字架
        lineData.addDataSet(lineMine);

        //x轴设置Lable
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValue));
        xAxis.setLabelCount(calculateScaleCount(xValue.size(), xLabelCount), true);
        yAxis.setLabelCount(5, true);
        yAxis.setAxisMaximum(max);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value == max / 4f) return RegexHelper.formatNumberValue(transformData.a, yAxisKeepDecimal);
                if(value == max / 2f) return RegexHelper.formatNumberValue(transformData.p, yAxisKeepDecimal);
                if(value == max / 4f * 3) return RegexHelper.formatNumberValue(transformData.b, yAxisKeepDecimal);
                return RegexHelper.formatNumberValue(value, yAxisKeepDecimal);
            }
        });
        setData(lineData);
        highlightValue(indexToday, 0); //设置默认高亮位置
        if (firstRun) {
            firstRun = false;
            //将图表动画显示之前进行缩放
            Matrix m = new Matrix();
            getViewPortHandler().refresh(m, this, false);
            animateY(500, Easing.Linear);
            animateX(300, Easing.Linear);
        }
    }

    private int calculateScaleCount(int totalSize, int defalutCount){
        if(totalSize < defalutCount) return totalSize;
        if(defalutCount <= 2) return 2;
        for(int i = defalutCount; i > 2; i --){
            if((totalSize - i) % (i - 1) == 0) return i;
        }
        return 2;
    }


    public static class LineChartBuild{
        // X轴真实显示lable
        List<String> xValue = new ArrayList<>();

        List<Pair<List<ILineChartData>, LineConfig>> lineSet;

        public LineChartBuild addLineChart(List<ILineChartData> pointSet, LineConfig lineConfig){
            if(lineSet == null) lineSet = new ArrayList<>();
            lineSet.add(new Pair<>(pointSet, lineConfig));
            return this;
        }

        private List<LineDataSet> createLinesDataSet(){
            if(lineSet == null || lineSet.size() == 0) return null;
            List<LineDataSet> result = new ArrayList<>(0);
            Float yMineValue = null;
            for(Pair<List<ILineChartData>, LineConfig> item : lineSet){
                if(item.first == null || item.first.size() == 0) continue;
                List<Entry> pointSet = new ArrayList<>(0);
                for(int index = 0; index < item.first.size(); index ++){
                    ILineChartData point = item.first.get(index);

                    if(!xValue.contains(point.getXAxisValue())){
                        xValue.add(point.getXAxisValue());
                    }
                    if(yMineValue == null){
                        yMineValue = point.getYAxisValue();
                    }else{
                        yMineValue = Math.min(yMineValue, point.getYAxisValue());
                    }

                    pointSet.add(new Entry(index, point.getYAxisValue()));
                }
                LineDataSet lineDataSet = new LineDataSet(pointSet, "");

                if(item.second != null){
                    lineDataSet.setColor(item.second.lineColor);//设置线条颜色
                    lineDataSet.setDrawValues(false); //不显示曲线点的具体数值
                    lineDataSet.setDrawCircles(false); //不显示曲线大圆点
                    lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置线条为平滑曲线
                    if(item.second.fillDrawable != null){
                        //填充颜色
                        lineDataSet.setDrawFilled(true); //设置曲线图下方显示的填充
                        lineDataSet.setFillDrawable(item.second.fillDrawable);
                        //选中十字架样式
                        lineDataSet.setHighlightEnabled(true);//显示选中十字架
                        lineDataSet.setHighLightColor(item.second.lineColor);//十字架颜色
                        lineDataSet.setHighlightLineWidth(0.5f); //十字架线条宽度
                        lineDataSet.enableDashedHighlightLine(3, 5, 0);  //十字架为虚线
                    }else{
                        lineDataSet.setHighlightEnabled(false);
//                        lineDataSet.setDrawHorizontalHighlightIndicator(false);
//                        lineDataSet.setDrawVerticalHighlightIndicator(true);
                    }
                }

                result.add(lineDataSet);
            }
            return result;
        }
    }

    public interface ILineChartData{
        float getYAxisValue();
        String getXAxisValue();
    }

    public static class LineConfig{
        private int lineColor;//曲线颜色
        private Drawable fillDrawable;//填充颜色

        public LineConfig(int lineColor){
            this.lineColor = lineColor;
        }
        public LineConfig(int lineColor, Drawable fillDrawable){
            this.lineColor = lineColor;
            this.fillDrawable = fillDrawable;
        }
    }

    public void setHighlightChangeListener(HighlightChangeListener listener){
        this.highlightChangeListener = listener;
    }

    public interface HighlightChangeListener{
        void onChange(StudyPoint item, boolean hasMineLine);
    }

    private static class TransformChartData{
        private final float min; //坐标系第一个刻度值; 坐标系最小值;
        private final float a;   //坐标系第二个刻度值
        private final float p;   //坐标系第三个刻度值
        private final float b;   //坐标系第四个刻度值
        private final float max; //坐标系第五个刻度值; 坐标系最大值;

        private boolean isCheck; //是否满足规则

        TransformChartData(float min, float a, float b, float max){
            this.min = min;
            this.a = a;
            this.b = b;
            this.p = (a + b) / 2f;
            this.max = max;

            isCheck = check();
        }

        float toValue(float x){
            if(x > max) x = max;
            if(!isCheck) return x;
            float granularity = (max - min) / 4f;
            if(x < min){
                return min;
            }else if(x <= a){
                return min + granularity / a * x;
            }else if(x <= p){
                return min + granularity + granularity / (p - a) * (x - a);
            }else if(x <= b){
                return min + 2 * granularity + granularity / (b - p) * (x - p);
            }else if(x <= max){
                return min + 3 * granularity + granularity / (max - b) * (x - b);
            }else{
                return max;
            }
        }

        private boolean check(){
            if(a <= min) return false;
            if(p <= a) return false;
            if(b <= p) return false;
            if(max <= b) return false;
            if(p != (a + b) / 2f) return false;
            return a != (max - min) / 4 || b != ((max - min) - (max - min) / 4);
        }
    }
}
