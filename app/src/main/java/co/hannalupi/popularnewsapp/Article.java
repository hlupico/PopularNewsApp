package co.hannalupi.popularnewsapp;

/**
 * Created by hannalupico on 5/6/15.
 */
public class Article {
    private String mAuthor;
    private String mArticleUrl;
    private String mPhotoUrl;


    public Article(String mAuthor, String mArticleUrl, String mPhotoUrl) {
        this.mAuthor = mAuthor;
        this.mArticleUrl = mArticleUrl;
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmArticleUrl() {
        return mArticleUrl;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    @Override
    public String toString() {
        return "Article{" +
                ", mAuthor='" + mAuthor + '\'' +
                ", mImage='" + mArticleUrl + '\'' +
                ", mAuthor='" + mPhotoUrl +
                '}';
    }
}
