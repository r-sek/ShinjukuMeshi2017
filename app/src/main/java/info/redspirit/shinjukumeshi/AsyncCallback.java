package info.redspirit.shinjukumeshi;

import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by rj on 2017/02/15.
 */

public interface AsyncCallback {
    void onPreExecute();
    void onPostExecute(JsonNode nodeList);
    void onProgressUpdate(int progress);
    void onCancelled();
}
