package com.evansappwriter.photolist.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.evansappwriter.photolist.R;
import com.evansappwriter.photolist.core.BundledData;
import com.evansappwriter.photolist.core.PhotoListParser;
import com.evansappwriter.photolist.core.PhotoListService;
import com.evansappwriter.photolist.model.Photo;
import com.evansappwriter.photolist.util.AltArrayAdapter;
import com.evansappwriter.photolist.util.Utils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by markevans on 6/20/16.
 */
public class PhotoListFragment extends BaseListFragment {
    private static final String TAG = "PhotoListFragment";

    private BaseActivity mActivity;

    // empty public constructor
    // read here why this is needed:
    // http://developer.android.com/reference/android/app/Fragment.html
    @SuppressWarnings("unused")
    public PhotoListFragment() {

    }

    public static PhotoListFragment newInstance(Bundle b) {
        PhotoListFragment f = new PhotoListFragment();
        if (b != null) {
            f.setArguments(b);
        }
        f.setHasOptionsMenu(true);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_search_list, container, false);

        final EditText editSearch = (EditText) view.findViewById(R.id.searchText);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    CharSequence txt = editSearch.getText();
                    if (!TextUtils.isEmpty(txt) && txt.toString().trim().length() > 0) {
                        getPhotoList();
                    }
                    return true;
                }
                return false;
            }
        });

        ImageButton btnSearch = (ImageButton) view.findViewById(R.id.searchBtn);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                CharSequence txt = editSearch.getText();
                if (!TextUtils.isEmpty(txt) && txt.toString().trim().length() > 0) {
                    getPhotoList();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected AltArrayAdapter<Photo> onCreateEmptyAdapter() {
        return new AltArrayAdapter<>(mActivity,
                new int[]{R.layout.photo_row},
                null,
                new int[]{R.id.title});
    }

    @Override
    protected AltArrayAdapter.ViewBinder onCreateViewBinder() {
        return new BinderPhoto(mActivity);
    }

    protected void getPhotoList() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        disableSearchView();

        TextView tvSearch = (TextView) getView().findViewById(R.id.searchText);
        CharSequence txt = tvSearch.getText();
        String txtSearch = txt == null ? "" : txt.toString();

        Bundle params = new Bundle();
        params.putString(PhotoListService.PARAM_TEXT, txtSearch);

        PhotoListService.getInstance(mActivity).get(PhotoListService.ENDPOINT_PHOTO_SEARCH, params, new PhotoListService.OnUIResponseHandler() {
        //PhotoListService.getInstance(mActivity).getMockPhotos(mActivity, new PhotoListService.OnUIResponseHandler() {

            @Override
            public void onSuccess(String payload) {
                if (mActivity == null || mActivity.isFinishing()) {
                    return;
                }

                if (payload != null) {
                    BundledData data = new BundledData(PhotoListParser.TYPE_PARSER_PHOTOS);
                    data.setHttpData(payload);
                    PhotoListParser.parseResponse(data);
                    if (data.getAuxData() == null) {
                        Utils.printLogInfo(TAG, "Parsing error: ", data.toString());
                        mActivity.showError(getString(R.string.error_title), getString(R.string.photo_get_error), null);
                    } else {
                        if (data.getAuxData().length == 1) {
                            ArrayList<Photo> photos = (ArrayList<Photo>) data.getAuxData()[0];
                            if (photos.size() == 0) {
                                toggleEmptyListMessage(true);
                            } else {
                                Collections.sort(photos);
                                showNew(photos);
                            }
                        } else {
                            Utils.printLogInfo(TAG, "Parsing error: ", data.getAuxData()[1]);
                            mActivity.showError(getString(R.string.error_title), getString(R.string.photo_get_error), null);
                        }
                    }
                } else {
                    Utils.printLogInfo(TAG, "Payload error: ", "No Payload but a status code of 200");
                    mActivity.showError(getString(R.string.error_title), getString(R.string.photo_get_error), null);
                }
                enableSearchView();
            }

            @Override
            public void onFailure(String errorTitle, String errorText, int dialogId) {
                if (mActivity == null || mActivity.isFinishing()) {
                    return;
                }

                enableSearchView();
                mActivity.showError(getString(R.string.error_title), getString(R.string.photo_get_error), null);
            }
        });
    }

    private void disableSearchView() {
        View vLoading = getView().findViewById(R.id.progress_send);
        vLoading.setVisibility(View.VISIBLE);
        View btnSearch = getView().findViewById(R.id.searchBtn);
        btnSearch.setVisibility(View.GONE);
        View tvSearch = getView().findViewById(R.id.searchText);
        tvSearch.setEnabled(false);
    }

    private void enableSearchView() {
        View vLoading = getView().findViewById(R.id.progress_send);
        vLoading.setVisibility(View.GONE);
        View btnSearch = getView().findViewById(R.id.searchBtn);
        btnSearch.setVisibility(View.VISIBLE);
        View tvSearch = getView().findViewById(R.id.searchText);
        tvSearch.setEnabled(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {

        Photo p = (Photo) getListAdapter().getItem(pos);
        Intent i = new Intent(mActivity, PhotoActivity.class);
        i.putExtra(PhotoActivity.PHOTO_URL_KEY,p.getPhotoUrl());
        startActivity(i);
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }
}
