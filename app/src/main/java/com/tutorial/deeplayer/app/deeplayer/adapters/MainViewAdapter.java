package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.tutorial.deeplayer.app.deeplayer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilya.savritsky on 15.09.2015.
 */
public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.MenuDataHolder> {
    private String[] menuItems;
    private Context context;
    private ViewHolderClicks listener;
    private static final int DEFAULT_SELECTED_ITEM = -1; // no selection
    private int selectedItemIndex;

    public MainViewAdapter(Context context, String[] items, ViewHolderClicks listener) {
        super();
        this.menuItems = items;
        this.context = context;
        this.selectedItemIndex = -1;
        this.listener = listener;
    }

    @Override
    public MenuDataHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_view_item, viewGroup, false);
//        final MenuDataHolder menuHolder = new MenuDataHolder(view, listener);
//        final String text = menuItems[i];
//        menuHolder.textView.setText(text);
        return new MenuDataHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MenuDataHolder menuDataHolder, int i) {
        final String text = menuItems[i];
        menuDataHolder.position = i;
//        if (i == selectedItemIndex) {
//            menuDataHolder.textView.setSelected(true);
//            menuDataHolder.cardView.setCardElevation(5);
//            menuDataHolder.cardView.setCardBackgroundColor(R.color.primary_green);
//        } else {
//            menuDataHolder.textView.setSelected(false);
//            menuDataHolder.cardView.setCardElevation(0);
//            menuDataHolder.cardView.setCardBackgroundColor(R.color.primary_green_dark);
//        }
        menuDataHolder.textView.setChecked(i == selectedItemIndex);
        menuDataHolder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        if (menuItems == null) {
            return 0;
        }
        return menuItems.length;
    }

    public static class MenuDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.card_view)
        CardView cardView;

        @Bind(R.id.text_view)
        CheckedTextView textView;

        ViewHolderClicks listener;

        int position;

        public MenuDataHolder(View itemView, final ViewHolderClicks listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String key = textView.getText().toString();
            if (listener != null) {
                listener.onItemClick(key);
            }
        }
    }

    public void selectItemAtPos(int position) {
//        int previousIndex = selectedItemIndex;
        this.selectedItemIndex = position;
        notifyDataSetChanged();
//        if (previousIndex != -1) {
//            notifyItemChanged(previousIndex);
//        }
//        notifyItemChanged(position);
    }

    public interface ViewHolderClicks {
        void onItemClick(String key);
    }
}
