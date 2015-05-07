package co.hannalupi.popularnewsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hannalupico on 5/6/15.
 */
public class GetNYJsonData extends GetRawData {

    private String LOG_TAG = GetNYJsonData.class.getSimpleName();
    private List<Article> mArticles;
    private String mRawUrl;

    //Call super from, assign mRawUrl and create Arraylist for AdapterView
    public GetNYJsonData(String mRawUrl) {
        super(null);
        this.mRawUrl = mRawUrl;
        mArticles = new ArrayList<Article>();
    }

    public void execute() {
        super.setmRawUrl(mRawUrl);
        //Create DownloadJsonData Object
        DownloadJsonData downloadJsonData = new DownloadJsonData();
//        Log.v(LOG_TAG, "Built URI = " + mDestinationUri.toString());
        downloadJsonData.execute(mRawUrl.toString());
    }

    //Process data returned from Download request
    public void processResult() {

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


                Article articleObject = new Article(author, articleUrl, photoUrl);
                this.mArticles.add(articleObject);

            }

            for(Article singleArticle: mArticles) {
                Log.v(LOG_TAG, singleArticle.toString());
            }

        } catch(JSONException jsone) {
            jsone.printStackTrace();
            Log.e(LOG_TAG,"Error processing Json data");
        }

    }

    public class DownloadJsonData extends DownloadRawData {

        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();

        }

        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

    }

}
