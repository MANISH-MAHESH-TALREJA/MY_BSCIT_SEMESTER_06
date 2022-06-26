package net.manish.mybscitsem06;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class SpaceItem extends DrawerItem<SpaceItem.ViewHolder>
{

    private final int spaceDp;

    SpaceItem()
    {
        this.spaceDp = 10;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        Context c = parent.getContext();
        View view = new View(c);
        int height = (int) (c.getResources().getDisplayMetrics().density * spaceDp);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height));
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {

    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    static class ViewHolder extends DrawerAdapter.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
