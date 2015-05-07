package co.hannalupi.popularnewsapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hannalupico on 5/6/15.
 */

enum DownloadStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

public class GetNYJsonData {

    private String LOG_TAG = GetNYJsonData.class.getSimpleName();
    private List<Article> mArticles;
    private String mRawUrl;
    private String mData;
    private DownloadStatus mDownloadStatus;

    //Call from MainActivity
    //Assign mRawUrl to URL passed to GetNYJsonData
    //Create Arraylist for AdapterView
    public GetNYJsonData(String mRawUrl) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mRawUrl = mRawUrl;
        this.mArticles = new ArrayList<>();
    }

    public String getmData() {
        return mData;
    }

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }

    //Pass listener through DownloadJsonData
    //Execute downloadJsonData with URL passed to GetNYJsonData
    public void execute(DataListener listener) {
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadJsonData downloadJsonData = new DownloadJsonData(listener);
        downloadJsonData.execute(mRawUrl);
    }

    //Process data returned from Download request
    public void processResult(DataListener listener) {

        if(getmDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error downloading raw file");
            return;
        }

        final String NYT_RESULTS = "results";
        final String NYT_ARTICLE_URL = "url";
        final String NYT_AUTHOR = "byline";
        final String NYT_MEDIA = "media";
        final String NYT_MEDIA_METADATA = "media-metadata";
        final String NYT_PHOTO_URL = "url";


        //Parse json data
        try {

            JSONObject jsonData = new JSONObject(getmData());
            //Create Json Array for Results
            JSONArray resultsArray = jsonData.getJSONArray(NYT_RESULTS);
            for(int i=0; i<resultsArray.length(); i++) {
                //Create Json Article object for each item in Results array
                //Get author name & article URL
                JSONObject jsonArticle = resultsArray.getJSONObject(i);
                String author = jsonArticle.getString(NYT_AUTHOR);
                Log.v(LOG_TAG, "author" + author.toString());
                String articleUrl = jsonArticle.getString(NYT_ARTICLE_URL);

                //Get media data -> use to get media metadata > photoUrl
                //Create Json object for Media
                JSONArray jsonMedia = jsonArticle.getJSONArray(NYT_MEDIA);
                Log.v(LOG_TAG, "jsonMedia" + jsonMedia.toString());

                //Create json object for MetaData
                JSONObject jsonObject1 = jsonMedia.getJSONObject(0);
                Log.v(LOG_TAG, "jsonObject1" + jsonObject1.toString());
                JSONArray jsonMediaMeta = jsonObject1.getJSONArray(NYT_MEDIA_METADATA);
                //Get url for photo from MediaMeta

                JSONObject jsonObject1ParDuex = jsonMediaMeta.getJSONObject(0);

                Log.v(LOG_TAG, "jsonObject1ParDuex" + jsonObject1ParDuex.toString());

                String photoUrl = jsonObject1ParDuex.getString(NYT_PHOTO_URL);
                Log.v(LOG_TAG, "photoUrl" + photoUrl.toString());

                //Create articleObjects and add to the list
                Article articleObject = new Article(author, articleUrl, photoUrl);
                this.mArticles.add(articleObject);


            }
            //Pass the complete list of articles to listener
            listener.onSuccess(mArticles);

            for(Article singleArticle: mArticles) {
                Log.v(LOG_TAG, singleArticle.toString());
            }

        } catch(JSONException jsone) {
            jsone.printStackTrace();
            Log.e(LOG_TAG,"Error processing Json data");
            //listener.onError();
        }
    }


    //Download data within an AsyncTask - avoids conflict with main UI
    public class DownloadJsonData extends AsyncTask<String, Void, String> {

        private final DataListener mListener;

        //Assign listener passed down from MainActivity to mListener
        public DownloadJsonData(DataListener listener){
            mListener = listener;
        }

        protected void onPostExecute(String webData) {
            mData = webData;

            //todo ---mData is being logged as null but webData shows results from NYT website in the debugger
            Log.v(LOG_TAG, "Data returned was: " + mData);
            if(mData == null) {
                if(mRawUrl == null) {
                    Log.v(LOG_TAG, "URL returned was: " + mRawUrl);
                    mDownloadStatus = DownloadStatus.NOT_INITIALISED;
                } else {
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else {
                // Success -> set DownloadStatus and process result
                mDownloadStatus = DownloadStatus.OK;
                processResult(mListener);
            }
        }

        //Request http connection, getData
        //Return string of data requested
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            if(params == null)
                return null;

            try {
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream == null) {
                    return null;
                }

                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch(IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;

                //Disconnect & close connection
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch(final IOException e) {
                        Log.e(LOG_TAG,"Error closing stream", e);
                    }
                }
            }
        }
    }

}
