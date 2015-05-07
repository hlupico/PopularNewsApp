package co.hannalupi.popularnewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hannalupico on 5/6/15.
 */
public class ArticleListAdapter extends BaseAdapter {

    private List<Article> articleItems;
    private Context mContext;

    public ArticleListAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        return articleItems.size();
    }

    public Object getItem(int position) {
        return articleItems.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    //Create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        Article articleItem = articleItems.get(position);
        RelativeLayout articleLayout = null;

        //Inflate video_item view, create new view if not recycled, display returned data
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            articleLayout = (RelativeLayout) inflater.inflate(R.layout.article_layout, null);
        }

        //Create thumbnail view and author for each article
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.photo);
        TextView author = (TextView) convertView.findViewById(R.id.author);

        //TODO - Import Picasso library once mData issue resolved
        //Picasso's load() method will fetch thumbnail of the video
        //.into() passes the thumbnail to ImageView
        // Picasso.with(getApplicationContext()).load(articleItem.getmPhotoUrl()).into(thumbnail);
        author.setText(articleItem.getmAuthor());
        return articleLayout;
    }

    //setArticleItems will update list with new data
    public void setArticleItems(List<Article> list){
        articleItems = list;
        notifyDataSetChanged();
    }
}
