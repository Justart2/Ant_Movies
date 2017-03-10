package top.aezdd.www.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import top.aezdd.www.entity.MovieCity;
import top.aezdd.www.entity.User;

/**
 * Created by aezdd on 2016/8/21.
 */
public class UserUtils {
    public static String city = "成都";
    public static MovieCity movieCity;
    public static User user;
    public static String getCourrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日");
        String today = sf.format(c.getTime());
        return today;
    }
    public static void setMovieCity(MovieCity movieCity) {
        UserUtils.movieCity = movieCity;
    }

    public static MovieCity getMovieCity() {
        return movieCity;
    }

    public static void setCity(String city) {
        UserUtils.city = city;
    }

    public static String getCity() {
        return city;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserUtils.user = user;
    }
}
