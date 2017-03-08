package info.redspirit.shinjukumeshi;

import org.json.JSONArray;

/**
 * Created by rj on 2017/02/15.
 */

public interface AsyncCallback {
    void onPreExecute();
    void onPostExecute(JSONArray ja);
    void onProgressUpdate(int progress);
    void onCancelled();
}
