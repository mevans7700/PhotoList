package com.evansappwriter.photolist.core;

import com.evansappwriter.photolist.model.Photo;
import com.evansappwriter.photolist.util.Keys;
import com.evansappwriter.photolist.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by markevans on 6/20/16.
 */
public class PhotoListParser {
    private static final String TAG = "TELMATE.PARSER";

    public static final int TYPE_PARSER_ERROR = -1;
    public static final int TYPE_PARSER_NONE = 0;
    public static final int TYPE_PARSER_PHOTOS = 1;

    private static final int MAX_NUM_PHOTOS = 10;


    // this class cannot be instantiated
    private PhotoListParser() {

    }

    public static void parseResponse(BundledData data) {
        int parserType = data.getParserType();

        Utils.printLogInfo(TAG, data.getHttpData());

        switch (parserType) {
            case TYPE_PARSER_PHOTOS:
                parsePhotoList(data);
                break;
            case TYPE_PARSER_ERROR:
                parseError(data);
                break;
            case TYPE_PARSER_NONE:
            default:
                // no parse needed
                break;
        }
    }

    private static void parsePhotoList(BundledData data) {
        if (getStringObject(data.getHttpData()) == null) {
            data.setAuxData();
            return;
        }

        try {
            // starting to parse...
            JSONObject jObject = new JSONObject(data.getHttpData());
            if (jObject.isNull(KEY_PHOTOS)) {
                data.setAuxData();
                data.setAuxData(jObject.getString(KEY_STAT), jObject.getString(KEY_MESSAGE));
                return;
            }

            JSONObject jObt = jObject.getJSONObject(KEY_PHOTOS);
            JSONArray jsonArray = jObt.getJSONArray(KEY_PHOTO);
            // ensure resources get cleaned up timely and properly
            data.setHttpData(null);

            ArrayList<Photo> photos = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                Photo photo = new Photo();
                photo.setId(jo.getString(Keys.KEY_ID));
                photo.setTitle(jo.getString(Keys.KEY_TITLE));
                photo.setFarm(jo.getString(Keys.KEY_FARM));
                photo.setOwner(jo.getString(Keys.KEY_OWNER));
                photo.setSecret(jo.getString(Keys.KEY_SECRET));
                photo.setServer(jo.getString(Keys.KEY_SERVER));
                photos.add(photo);
                if (i == MAX_NUM_PHOTOS -1) {
                    break;
                }
            }
            Collections.sort(photos);
            data.setAuxData(photos);
        } catch (Exception e) {
            Utils.printStackTrace(e);
            data.setHttpData(null);
            data.setAuxData();
        }
    }

    private static void parseError(BundledData data) {
        if (getStringObject(data.getHttpData()) == null) {
            data.setAuxData("Bad Payload", data.getHttpData());
            return;
        }

        try {
            JSONObject json = new JSONObject(data.getHttpData());

            // ensure resources get cleaned up timely and properly
            data.setHttpData(null);


            String status = "" ;
            String status_msg = "";

            data.setAuxData(status, status_msg);
        } catch (Exception e) {
            Utils.printStackTrace(e);
            data.setAuxData("Server Error", data.getHttpData());
            data.setHttpData(null);
        }
    }

    private static String getStringObject(String txt) {
        return txt == null ? null : txt.equalsIgnoreCase("null") ? null : txt;
    }

    public static final String KEY_PHOTOS = "photos";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_STAT = "stat";
    public static final String KEY_MESSAGE = "message";
}
