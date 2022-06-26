package net.manish.mybscitsem06;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;
import retrofit2.internal.EverythingIsNonNull;

public class Blogger extends AppCompatActivity
{
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    PostAdapter adapter;
    final List<Item> items = new ArrayList<>();
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    String token = "";
    ACProgressFlower dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogger);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
        recyclerView = findViewById(R.id.postList);
        manager = new LinearLayoutManager(this);
        adapter = new PostAdapter(this, items);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("PLEASE WAIT")
                .fadeColor(Color.DKGRAY).build();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();
                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    isScrolling = false;
                    getData();
                }
            }
        });
        getData();
    }
    String key, url;
    public PostService postService = null;
    public PostService getService()
    {
        if(postService == null)
        {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            postService = retrofit.create(PostService.class);
        }
        return postService;
    }
    public interface PostService
    {
        @GET
        Call<PostList> getPostList(@Url String url);
    }
    private void getData()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firebaseFirestore.collection("PAYMENTS").document("PAYTM");
        documentReference.get().addOnCompleteListener(task ->
        {
            if(task.isSuccessful())
            {
                final DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists())
                {
                    documentReference.addSnapshotListener((documentSnapshot, e) ->
                    {
                        if (document.get("KEY") != null && document.get("URL")!=null)
                        {
                            assert documentSnapshot != null;
                            key = documentSnapshot.getString("KEY");
                            url = documentSnapshot.getString("URL");
                            new Thread(() ->
                            {
                                Looper.prepare();
                                String blog = url + "?key=" + key;
                                String demo = "";
                                if(Objects.equals(token, null))
                                {
                                    return;
                                }
                                else if(!Objects.equals(token, demo))
                                {
                                    blog = url+ "&pageToken="+ token;
                                }
                                final Call<PostList> postList = getService().getPostList(blog);
                                postList.enqueue(new Callback<PostList>()
                                {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    @EverythingIsNonNull
                                    public void onResponse(Call<PostList> call, Response<PostList> response)
                                    {
                                        PostList list = response.body();
                                        assert list != null;
                                        token = list.getNextPageToken();
                                        items.addAll(list.getItems());
                                        adapter.notifyDataSetChanged();
                                        FancyToast.makeText(Blogger.this, "DATA FETCHED SUCCESSFULLY",FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,false).show();
                                        dialog.dismiss();
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                    }

                                    @Override
                                    @EverythingIsNonNull
                                    public void onFailure(Call<PostList> call, Throwable t)
                                    {
                                        dialog.dismiss();
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                        FancyToast.makeText(Blogger.this, "SOME ERROR OCCURRED WHILE FETCHING DATA",FancyToast.LENGTH_SHORT, FancyToast.ERROR,false).show();
                                        onBackPressed();
                                    }
                                });
                            }).start();
                        }
                        else
                        {
                            dialog.dismiss();
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                            FancyToast.makeText(getApplicationContext(), "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                            onBackPressed();
                        }
                    });
                }
            }
            else
            {
                dialog.dismiss();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                FancyToast.makeText(getApplicationContext(), "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                onBackPressed();
            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            FancyToast.makeText(getApplicationContext(), "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            onBackPressed();
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Animatoo.animateDiagonal(this);
        finish();
    }

}
