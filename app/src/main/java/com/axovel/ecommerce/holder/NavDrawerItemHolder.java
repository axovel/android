package com.axovel.ecommerce.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axovel.ecommerce.R;
import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Umesh Chauhan on 31-12-2015.
 * Axovel Private Limited
 */
public class NavDrawerItemHolder extends TreeNode.BaseNodeViewHolder<NavDrawerItemHolder.IconTreeItem>{

    private TextView tvValue;
    private PrintView arrowView;

    public NavDrawerItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, NavDrawerItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.list_item_nav_drawer, null, false);

        tvValue = (TextView) view.findViewById(R.id.title);
        tvValue.setText(value.text);
        final ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        iconView.setVisibility(View.GONE);
        TextView txtCounter = (TextView) view.findViewById(R.id.counter);
        txtCounter.setVisibility(View.GONE);
        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    public static class IconTreeItem {
        public int icon;
        public String text;

        public IconTreeItem(int icon, String text) {
            this.icon = icon;
            this.text = text;
        }

        public IconTreeItem(String noIcon, String text) {
            this.text = text;
        }
    }
}
