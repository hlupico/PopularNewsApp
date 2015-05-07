package co.hannalupi.popularnewsapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;




public class MainActivity extends ActionBarActivity implements DataListener{

    //Declare the ArticleListAdapter and ListView
    ArticleListAdapter mAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set ContentView to activity_main which contains a listView
        setContentView(R.layout.activity_main);

        //Create mAdapter & listView
        mAdapter = new ArticleListAdapter(getApplicationContext());
        Log.v("mAdapter", mAdapter.toString());
        mListView = (ListView)  findViewById(R.id.listView);
        //Attach the adapter to this activity_main's ListView
        mListView.setAdapter(mAdapter);

        //Create new GetNYJsonData and call jsonData to get data
        GetNYJsonData jsonData = new GetNYJsonData("http://api.nytimes.com/svc/mostpopular/v2/mostemailed/all-sections/1.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873&offset=0");
        //Pass context as listener to GetNYJsondata
        //Listener will be used to pass data to adapter after data is downloaded
        jsonData.execute(this);

        //Call onSuccess() and pass the articles to the List to the Adapter
        //This will create the listView after the data is collected from NYT
        //onSuccess();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSuccess(List<Article> articles) {
        //Pass the list to the adapter
        mAdapter.setArticleItems(articles);
    }
}