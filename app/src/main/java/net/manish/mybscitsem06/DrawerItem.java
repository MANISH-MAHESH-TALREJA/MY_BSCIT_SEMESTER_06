package net.manish.mybscitsem06;

import android.view.ViewGroup;

public abstract class DrawerItem<T extends DrawerAdapter.ViewHolder>
{

    boolean isChecked;

    public abstract T createViewHolder(ViewGroup parent);

    public abstract void bindViewHolder(T holder);

    @SuppressWarnings("rawtypes")
    DrawerItem setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
        return this;
    }

    boolean isChecked() {
        return isChecked;
    }

    public boolean isSelectable() {
        return true;
    }

}
