package tech.phlocs.histleap.util;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import tech.phlocs.histleap.R;
import tech.phlocs.histleap.model.Event;
import tech.phlocs.histleap.model.Spot;

public class JsonHandler {
    private Activity activity = null;


    public JsonHandler(Activity activity) {
        this.activity = activity;
    }

    public JSONObject makeJsonFromRawFile(int rawId) {
        JSONObject json = null;
        InputStream spotsStream = activity.getResources().openRawResource(R.raw.spots);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(spotsStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                spotsStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // 文字列をJSONObjectに変換
            json = new JSONObject(writer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    public ArrayList<JSONObject> makeArrayListFromJsonArray(JSONArray jsonArray) {
        ArrayList<JSONObject> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                list.add(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    public JSONArray getJsonArrayInJson(JSONObject obj, String key) {
        JSONArray array = null;
        try {
            array = obj.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public Spot makeSpotFromJson(JSONObject spotObj) {
        Spot spot = new Spot();
        try {
            spot.setName(spotObj.getString("spotName"));
            spot.setLatitude(makeFloatFromJson(spotObj, "spotLatitude"));
            spot.setLongitude(makeFloatFromJson(spotObj, "spotLongitude"));
            if (spotObj.has("spotAddress")) {
                spot.setAddress(spotObj.getString("spotAddress"));
            }
            if (spotObj.has("spotOverview")) {
                spot.setOverview(spotObj.getString("spotOverview"));
            }
            if (spotObj.has("spotURL")) {
                spot.setUrl(spotObj.getString("spotURL"));
            }
            if (spotObj.has("spotImageURL")) {
                spot.setImageUrl(spotObj.getString("spotImageURL"));
            }

            ArrayList<Event> eventList = new ArrayList<>();
            JSONArray eventArr = spotObj.getJSONArray("events");
            for (int i = 0; i < eventArr.length(); i++) {
                JSONObject eventObj = eventArr.getJSONObject(i);
                Event event = new Event();
                event.setStartYear(makeIntFromJson(eventObj, "eventStartYear"));
                if (eventObj.has("eventEndYear")) {
                    event.setEndYear(makeIntFromJson(eventObj, "eventEndYear"));
                }
                event.setOverview(eventObj.getString("eventOverview"));
                if (eventObj.has("eventURL")) {
                    event.setUrl(eventObj.getString("eventURL"));
                }
                eventList.add(event);
            }
            spot.setEventList(eventList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return spot;
    }
    public int makeIntFromJson(JSONObject json, String key) {
        int num = -1;
        Object value = json.opt(key);
        if (value != null) {
            if (value instanceof String) {
                num = Integer.parseInt((String) value);
            } else if (value instanceof Integer) {
                num = (int) value;
            }
        }
        return num;
    }
    public float makeFloatFromJson(JSONObject json, String key) {
        float num = -1;
        Object value = json.opt(key);
        if (value != null) {
            if (value instanceof String) {
                num = Float.parseFloat((String) value);
            } else if (value instanceof Float) {
                num = (float) value;
            }
        }
        return num;
    }

}
