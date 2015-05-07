package co.hannalupi.popularnewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hannalupico on 5/6/15.
 */
public class ArticleListAdapter extends BaseAdapter {

    private final List<Article> mArticle = new ArrayList<>();
    private Context mContext;

    public ArticleListAdapter(Context c) {
        this.mContext = c;
    }

    public int getCount() {
        return mArticle.size();
    }

    public Object getItem(int position) {
        return mArticle.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ButtonView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final Article article = (Article) getItem(position);
        RelativeLayout articleLayout = null;
        //Inflate video_item view, create new view if not recycled, display returned data

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            articleLayout = (RelativeLayout) inflater.inflate(R.layout.article_layout, null);
        }

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.photo);
        TextView author = (TextView) convertView.findViewById(R.id.author);

        //Picasso's load() method will fetch thumbnail of the video
        //.into() passes the thumbnail to ImageView
//        Picasso.with(getApplicationContext()).load(article.getmPhotoUrl()).into(thumbnail);
        author.setText(article.getmAuthor());
        return articleLayout;
    }
}
