package info.redspirit.shinjukumeshi;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rj on 2017/02/13.
 */

public class Global extends Application {
    String sort;
    List<String> idArray;
    List<String> nameArray;
    List<String> lineArray;
    List<String> stationArray;
    List<String> walkArray;
    List<String> categoryArray;
    List<String> imageUrlArray;
    List<String> testArray;
    List<Integer> testIdArray;
    String latitude;
    String longitude;
    boolean gpsFlg;

    public void GlobalAllInit() {
        idArray = new ArrayList<String>();
        nameArray = new ArrayList<String>();
        lineArray = new ArrayList<String>();
        stationArray = new ArrayList<String>();
        walkArray = new ArrayList<String>();
        categoryArray = new ArrayList<String>();
        imageUrlArray = new ArrayList<String>();


        testArray = new ArrayList<String>();
        testArray.add("↓Dummy↓");
        testArray.add("データなし");
        testArray.add("↑Dummy↑");

        latitude = null;
        longitude = null;
        gpsFlg = true;

    }

    public void GlobalArrayInit(){
        idArray = new ArrayList<String>();
        nameArray = new ArrayList<String>();
        lineArray = new ArrayList<String>();
        stationArray = new ArrayList<String>();
        walkArray = new ArrayList<String>();
        categoryArray = new ArrayList<String>();
        imageUrlArray = new ArrayList<String>();
    }

}
