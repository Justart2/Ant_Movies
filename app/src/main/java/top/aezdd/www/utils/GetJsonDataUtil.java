package top.aezdd.www.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jianzhou.liu on 2017/3/10.
 */
public class GetJsonDataUtil {
    private String pinYin[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public String fileReader(InputStream inputStream) {
        BufferedReader br = null;
        try {

            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String temp = "";
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;

    }

    public List<Map<String, String>> getJsonData(String jsonStr) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", jsonObject.getString("zip"));
                map.put("name", jsonObject.getString("name"));
                map.put("pinyin", jsonObject.getString("pinyin"));
                list.add(map);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<String, List<String>> formatCityData(
            List<Map<String, String>> list) {
        Map<String, List<String>> map = new TreeMap<String, List<String>>();;
        if (list != null) {
            for (int j = 0; j < pinYin.length; j++) {
                List<String> l = new ArrayList<>();
                /*设置变量如果list没有add操作，无需map.put*/
                boolean flag = false;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).get("pinyin").substring(0, 1)
                            .equals(pinYin[j])) {
                        l.add(list.get(i).get("name"));
                        flag = true;
                    }
                }
                /*验证flag*/
                if(flag)
                map.put(pinYin[j],l);

            }

        }

        return map;
    }
}
