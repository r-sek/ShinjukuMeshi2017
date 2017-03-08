package info.redspirit.shinjukumeshi;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by rj on 2017/02/13.
 */

public class Global extends Application {
    String sort;
    ArrayList<String> nameArray;
    ArrayList<Integer> idArray;
    ArrayList<String> testArray;
    ArrayList<Integer> testIdArray;
    boolean gpsFlg;

    public void GlobalAllInit() {
        nameArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();
        testArray = new ArrayList<String>();

        testArray.add("↓Dummy↓");
        testArray.add("データなし");
        testArray.add("↑Dummy↑");

        gpsFlg = true;

    }

    public void GlobalArrayInit(){
        nameArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();
    }

}
