package net.manish.mybscitsem06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Notices extends AppCompatActivity
{
    String[] files;
    final List<FilesModel> itemsModelList = new ArrayList<>();
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("DOWNLOADED");
        ListView listView = findViewById(R.id.files_list);
        File dir = new File(getApplicationContext().getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)/");
        File[] fileList = dir.listFiles();
        if(fileList!=null)
        {
            if(fileList.length>0)
            {
                files = new String[fileList.length];
                for (int i = 0; i < files.length; i++)
                {
                    files[i] = fileList[i].getName();
                }
                for (String s : files)
                {
                    if(!s.startsWith(".") || s.contains(".temp"))
                    {
                        FilesModel itemsModel = new FilesModel(s);
                        if(itemsModel.getName().startsWith(".") || itemsModel.getName().contains(".temp"))
                        {
                            continue;
                        }
                        itemsModelList.add(itemsModel);
                    }
                }
                customAdapter = new CustomAdapter(itemsModelList);
                listView.setAdapter(customAdapter);
            }
            else
            {
                FancyToast.makeText(Notices.this, "NO DOWNLOADED FILES", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                onBackPressed();
            }
        }

        else
        {
            FancyToast.makeText(Notices.this, "NO DOWNLOADED FILES", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                customAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.searchView)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class CustomAdapter extends BaseAdapter implements Filterable
    {
        View view;
        private final List<FilesModel> itemsModelSingle;
        private List<FilesModel> itemsModelListFiltered;

        CustomAdapter(List<FilesModel> itemsModelSingle)
        {
            this.itemsModelSingle = itemsModelSingle;
            this.itemsModelListFiltered = itemsModelSingle;
        }

        @Override
        public int getCount()
        {
            return itemsModelListFiltered.size();
        }

        @Override
        public Object getItem(int position)
        {
            return itemsModelListFiltered.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            AtomicLong mLastClickTime = new AtomicLong();
            view = getLayoutInflater().inflate(R.layout.files_item, parent, false);
            TextView names = view.findViewById(R.id.title);
            final Help help = new Help();
            names.setText(itemsModelListFiltered.get(position).getName());
            view.setOnClickListener(view1 ->
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 2000)
                {
                    return;
                }
                mLastClickTime.set(SystemClock.elapsedRealtime());
                String click = itemsModelListFiltered.get(position).getName();
                FancyToast.makeText(Notices.this, click.toUpperCase(), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                Intent i = new Intent();
                i.putExtra("NAME",click.toUpperCase());
                i.putExtra("FILE",String.valueOf(Uri.parse(getApplicationContext().getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)/" + click)));
                i.putExtra("DATA",help.purchase(help.getDownload(click, getApplicationContext())));
                i.setClass(getApplicationContext(), PdfActivity.class);
                startActivity(i);
                Animatoo.animateDiagonal(Notices.this);
            });
            ImageView delete = view.findViewById(R.id.deleteFile);
            delete.setOnClickListener(v ->
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 2000)
                {
                    return;
                }
                mLastClickTime.set(SystemClock.elapsedRealtime());
                String click = itemsModelListFiltered.get(position).getName();
                AlertDialog.Builder builder = new AlertDialog.Builder(Notices.this);
                Typeface face = ResourcesCompat.getFont(Notices.this,R.font.syndor);
                builder.setMessage("ARE YOU SURE, YOU WANT TO DELETE "+click.toUpperCase()+" ??")
                        .setCancelable(false)
                        .setIcon(R.drawable.delete)
                        .setTitle(new Help().typeface(face, "DELETE FILE ???"))
                        .setPositiveButton( "OK, DELETE", (dialog, id) -> {
                            File file = new File(getApplicationContext().getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)/", click);
                            boolean deleted = file.delete();
                            if(deleted)
                            {
                                itemsModelList.remove(itemsModelListFiltered.get(position));
                                findViewById(R.id.files_list);
                                File dir = new File(getApplicationContext().getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)/");
                                File[] fileList = dir.listFiles();
                                assert fileList != null;
                                if(fileList.length>0)
                                {
                                    FancyToast.makeText(Notices.this, "FILE SUCCESSFULLY DELETED !!!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                    notifyDataSetChanged();
                                }
                                else
                                {
                                    FancyToast.makeText(Notices.this, "NO DOWNLOADED FILES", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                    startActivity(new Intent(getApplicationContext(), SampleActivity.class));
                                    Animatoo.animateDiagonal(Notices.this);
                                }
                            }
                        }).setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            });
            return view;
        }

        @Override
        public Filter getFilter()
        {
            return new Filter()
            {
                @Override
                protected FilterResults performFiltering(CharSequence constraint)
                {
                    FilterResults filterResults = new FilterResults();
                    if (constraint == null || constraint.length() == 0)
                    {
                        filterResults.count = itemsModelSingle.size();
                        filterResults.values = itemsModelSingle;
                    }
                    else
                    {
                        List<FilesModel> resultsModel = new ArrayList<>();
                        String searchStr = constraint.toString().toUpperCase();
                        for (FilesModel itemsModel : itemsModelSingle)
                        {
                            if (itemsModel.getName().contains(searchStr))
                            {
                                resultsModel.add(itemsModel);
                            }
                            filterResults.count = resultsModel.size();
                            filterResults.values = resultsModel;
                        }
                    }
                    return filterResults;
                }
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results)
                {
                    itemsModelListFiltered = (List<FilesModel>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed()
    {
       startActivity(new Intent(getApplicationContext(), SampleActivity.class));
       Animatoo.animateDiagonal(this);
       finish();
    }
}