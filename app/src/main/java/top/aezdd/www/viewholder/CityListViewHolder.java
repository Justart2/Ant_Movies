package top.aezdd.www.viewholder;

import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by jianzhou.liu on 2017/3/10.
 */
public class CityListViewHolder {

    private TextView cityTitle;
    private GridView cityGrid;

    public void setCityGrid(GridView cityGrid) {
        this.cityGrid = cityGrid;
    }

    public void setCityTitle(TextView cityTitle) {
        this.cityTitle = cityTitle;
    }

    public GridView getCityGrid() {
        return cityGrid;
    }

    public TextView getCityTitle() {
        return cityTitle;
    }
}
