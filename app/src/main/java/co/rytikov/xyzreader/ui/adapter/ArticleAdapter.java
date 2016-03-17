package co.rytikov.xyzreader.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.rytikov.xyzreader.R;
import co.rytikov.xyzreader.data.ArticleLoader;
import co.rytikov.xyzreader.data.ItemsContract;
import co.rytikov.xyzreader.ui.DynamicHeightImageView;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private Cursor mCursor;
    private Activity mActivity;

    public ArticleAdapter(Activity activity, Cursor cursor) {
        mActivity = activity;
        mCursor = cursor;
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(ArticleLoader.Query._ID);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.item_article, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW,
                        ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition()))));
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
        holder.subtitleView.setText(
                mActivity.getString(
                        R.string.article_text,
                        DateUtils.getRelativeTimeSpanString(
                                mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString(),
                        mCursor.getString(ArticleLoader.Query.AUTHOR)));

        //holder.thumbnailView.setAspectRatio(mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO));
        String imageUrl = mCursor.getString(ArticleLoader.Query.THUMB_URL);
        Glide.with(mActivity)
                .load(imageUrl)
                .crossFade()
                .placeholder(R.color.primary_light)
                .into(holder.thumbnailView);
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.thumbnail)
        DynamicHeightImageView thumbnailView;
        @Bind(R.id.article_title) TextView titleView;
        @Bind(R.id.article_subtitle) TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
