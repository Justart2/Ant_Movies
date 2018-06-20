package top.aezdd.www.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by jianzhou.liu on 2017/4/6.
 */

public class MovieEvaluateListView extends ListView{
    public MovieEvaluateListView(Context context) {
        super(context);
    }

    public MovieEvaluateListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieEvaluateListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int exspec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, exspec);
    }
}
