package com.example.android.feednews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.feednews.pojo.Result;
import com.example.android.feednews.pojo.Tag;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<Result> {

    public NewsAdapter(Context context, List<Result> results) {
        super(context, 0, results);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }
        Result currentNews = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.news_title);
        title.setText(currentNews.getWebTitle());

        TextView description = (TextView) listItemView.findViewById(R.id.news_description);
        description.setText(currentNews.getFields().getTrailText());

        TextView author = (TextView) listItemView.findViewById(R.id.news_contributor);
        List<Tag> tags = currentNews.getTags();
        if(tags.size() != 0)
            author.setText(tags.get(0).getWebTitle());
        else
            author.setText("");

        TextView date = (TextView) listItemView.findViewById(R.id.news_date);
        date.setText(currentNews.getWebPublicationDate());

        ImageView image = (ImageView) listItemView.findViewById(R.id.news_image);
        Picasso.with(getContext()).load(currentNews.getFields().getThumbnail()).into(image);

        TextView section = (TextView) listItemView.findViewById(R.id.news_section);
        section.setText(currentNews.getSectionName());

        return listItemView;
    }
}
