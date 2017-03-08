package info.redspirit.shinjukumeshi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.*;

import java.util.Iterator;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ItemFragment extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Global global;
    CardRecyclerView crv;
    CardRecyclerAdapter crva;
    LocationManager locationManager;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressDialog waitDialog;
    private OnFragmentInteractionListener mListener;
    private View v;
    private String latitude;
    private String longitude;
    private Double nowLat;
    private Double nowLng;

    public ItemFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Handler handler = new Handler();
        final LayoutInflater fInrlater = inflater;
        final ViewGroup fcontainer = container;

        v = fInrlater.inflate(R.layout.fragment_item, fcontainer, false);
        return v;
    }

    @Override
    public void onStart() {
        Log.i("fragment", "onStart");
        crv = (CardRecyclerView) v.findViewById(R.id.cardRecyclerView1);

        TextView tv = (TextView) getActivity().findViewById(R.id.sortTxt);

        global = (Global) getActivity().getApplication();
        global.GlobalArrayInit();

        if (ActivityCompat.checkSelfPermission(getActivity().getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            /** fine location のリクエストコード（値は他のパーミッションと被らなければ、なんでも良い）*/
            final int requestCode = 1;

            // いずれも得られていない場合はパーミッションのリクエストを要求する
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);
            return;
        }

        // 位置情報を管理している LocationManager のインスタンスを生成する
        locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        String locationProvider = null;

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPSが利用可能になっているかどうかをチェック
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // GPSプロバイダーが有効になっていない場合は基地局情報が利用可能になっているかをチェック
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            // いずれも利用可能でない場合は、GPSを設定する画面に遷移する
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            return;
        }

        /** 位置情報の通知するための最小時間間隔（ミリ秒） */
        final long minTime = 500;
        /** 位置情報を通知するための最小距離間隔（メートル）*/
        final long minDistance = 1;

        // 利用可能なロケーションプロバイダによる位置情報の取得の開始
        // FIXME 本来であれば、リスナが複数回登録されないようにチェックする必要がある

        locationManager.requestLocationUpdates(locationProvider, minTime, minDistance, this);
        // 最新の位置情報
        Location location = locationManager.getLastKnownLocation(locationProvider);

        try {
            Log.i("LocationProvider", String.valueOf(locationProvider));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // アクセスキー
        String acckey = "9888fa439f5f71db7d78db34c2acab78";
        // 緯度
        String lat = global.latitude;
        // 経度
        String lon =  global.longitude;
        // 範囲
        String range = "1";
        // 返却形式
        String format = "json";
        // エンドポイント
        String gnaviRestUri = "https://api.gnavi.co.jp/RestSearchAPI/20150630/";
        String prmKeyid = "?keyid=" + acckey;
        String prmFormat = "&format=" + format;
        String prmLat = "&latitude=" + lat;
        String prmLon = "&longitude=" + lon;
        String prmRange = "&range=" + range;

        // URI組み立て
        StringBuffer uri = new StringBuffer();
        uri.append(gnaviRestUri);
        uri.append(prmKeyid);
        uri.append(prmFormat);
        uri.append(prmLat);
        uri.append(prmLon);
        uri.append(prmRange);

        HttpResponsAsync hra = new HttpResponsAsync(new AsyncCallback() {
            @Override
            public void onPreExecute() {
                // プログレスダイアログの設定
                waitDialog = new ProgressDialog(getContext());
                // プログレスダイアログのメッセージを設定します
                waitDialog.setMessage("NOW LOADING...");
                // 円スタイル（くるくる回るタイプ）に設定します
                waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                waitDialog.setIndeterminate(true);
                // プログレスダイアログを表示
                waitDialog.show();

            }

            @Override
            public void onPostExecute(JsonNode nodeList) {

                if(nodeList != null){
                    //トータルヒット件数
                    Log.i("hitcount",nodeList.path("total_hit_count").asText());
                    //restのみ取得
                    JsonNode restList = nodeList.path("rest");
                    Iterator<JsonNode> rest = restList.iterator();
                    //店舗番号、店舗名、最寄の路線、最寄の駅、最寄駅から店までの時間、店舗の小業態を出力
                    while(rest.hasNext()){
                        JsonNode r = rest.next();
                        String id = r.path("id").asText();
                        String name = r.path("name").asText();
                        String line = r.path("access").path("line").asText();
                        String imageUrl = r.path("image_url").path("shop_image1").asText();
                        String station = r.path("access").path("station").asText();
                        String walk    = r.path("access").path("walk").asText() + "分";
                        String categorys = "";

                        global.idArray.add(id);
                        global.nameArray.add(name);
                        global.lineArray.add(line);
                        global.imageUrlArray.add(imageUrl);
                        global.stationArray.add(station);
                        global.walkArray.add(walk);
                        global.categoryArray.add(categorys);

                        for(JsonNode n : r.path("code").path("category_name_s")){
                            categorys += n.asText();
                        }
                        Log.i("Node",id + "¥t" + name + "¥t" + line + "¥t" + station + "¥t" + walk + "¥t" + categorys + "¥t" +imageUrl);
                    }
                }

//                try {
//                    for (int i = 0; i < ja.length(); i++) {
//                        JSONObject eventObj = ja.getJSONObject(i);
//                        String id = eventObj.getString("spot_id");
//                        String name = eventObj.getString("spot_name");
//                        global.idArray.add(Integer.parseInt(id));
//                        global.nameArray.add(name);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                Log.i("ArraySize", String.valueOf(global.nameArray.size()));
                if (global.nameArray.size() != 0) {
                    crv.setRecyclerAdapterB(getContext(), global.nameArray, global.walkArray);
                } else {
//                    crv.setRecyclerAdapterB(getContext(),global.testArray);
                }
                //プログレスダイアログ消す
                if (waitDialog.isShowing()) {
                    waitDialog.dismiss();
                }

            }

            @Override
            public void onProgressUpdate(int progress) {

            }

            @Override
            public void onCancelled() {

            }
        });
        hra.execute(uri.toString());

//        if (global.nameArray.size() != 0) {
//            crv.setRecyclerAdapterB(getContext(), global.nameArray, global.idArray);
//        }

        super.onStart();
    }

    //位置情報が通知されるたびにコールバックされるメソッド
    @Override
    public void onLocationChanged(Location location) {
        nowLat = location.getLatitude();
        nowLng = location.getLongitude();
        Log.i("Location", String.valueOf(nowLat) + ":" + String.valueOf(nowLng));
        global.latitude = String.valueOf(nowLat);
        global.longitude = String.valueOf(nowLng);
    }

    //ロケーションプロバイダが利用不可能になるとコールバックされるメソッド
    @Override
    public void onProviderDisabled(String provider) {
        //ロケーションプロバイダーが使われなくなったらリムーブする必要がある
    }

    //ロケーションプロバイダが利用可能になるとコールバックされるメソッド
    @Override
    public void onProviderEnabled(String provider) {
        //プロバイダが利用可能になったら呼ばれる
    }

    //ロケーションステータスが変わるとコールバックされるメソッド
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // 利用可能なプロバイダの利用状態が変化したときに呼ばれる
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
