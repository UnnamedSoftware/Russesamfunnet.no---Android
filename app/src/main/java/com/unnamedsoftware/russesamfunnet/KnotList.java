package com.unnamedsoftware.russesamfunnet;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.unnamedsoftware.russesamfunnet.RecyclerView.TempKnot;
import com.unnamedsoftware.russesamfunnet.RecyclerView.ViewKnotListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alexander Eilert Berg on 29.01.2018.
 */

public class KnotList extends AppCompatActivity
{

    private List<TempKnot> tempKnots = new ArrayList<>();
    private RecyclerView recyclerView;
    private ViewKnotListAdapter viewKnotListAdapter;

    private String url;

    // JSON Node names
    private static final String TAG_KNOTLIST = "knotList";
    private static final String TAG_TITLE = "title";
    private static final String TAG_KNOTID = "knotID";

    JSONArray knots = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knot_list);

        url = getString(R.string.url) + "knotList";


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Russesamfunnet - Knute liste");

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        this.recyclerView = findViewById(R.id.recycler_view_knot_list);
        this.viewKnotListAdapter = new ViewKnotListAdapter (tempKnots);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(viewKnotListAdapter);

/*
        try
        {
            getKnotList();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
*/
        dummy();
    }


    private void dummy()
    {
        TempKnot user = new TempKnot("Aid","Nulla mattis turpis id ullamcorper semper. Mauris rhoncus nisl sed libero dignissim congue. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus finibus nibh vitae tortor dictum pretium. Aenean nec felis mauris. Nulla non pellentesque lorem, eu feugiat mauris. Integer eget dolor felis. Nullam sapien odio, facilisis gravida interdum pellentesque, ornare pharetra ipsum. Curabitur ultrices condimentum ligula sed suscipit. Nam sed dui libero.",2);
        tempKnots.add(user);

        user = new TempKnot("Prince", "Suspendisse egestas cursus purus, quis posuere risus tincidunt vel. Ut purus nisi, ullamcorper nec ante at, volutpat vestibulum tellus. Proin bibendum ipsum diam, at condimentum sem ullamcorper nec. Aenean laoreet ligula sed ex sollicitudin mattis. Duis vehicula mi sed mi imperdiet maximus. Ut sagittis, erat vitae pretium elementum, justo dui accumsan libero, quis porta massa nisl a felis. Mauris commodo, mi in convallis convallis, nibh lorem viverra nibh, fermentum elementum felis massa nec magna. Praesent in nisl in magna ullamcorper hendrerit mattis sed dui. Duis finibus tellus vitae tortor vestibulum varius. Fusce aliquet gravida nibh, et aliquet felis mollis ut. Sed eros quam, pellentesque sed dapibus consequat, lacinia at eros. Cras luctus nisi libero, ut molestie augue efficitur eu. Nunc fermentum luctus tellus, id efficitur metus eleifend convallis. Nullam vitae odio gravida, hendrerit felis ut, ornare risus. Aliquam sagittis dui eu hendrerit congue. Aliquam nec mi malesuada, vehicula turpis sit amet, pulvinar diam.",34);
        tempKnots.add(user);

        user = new TempKnot("Remind", "Nam eu nisl leo. Duis sodales lectus justo, quis dapibus leo congue et. Morbi dignissim, massa quis venenatis mattis, velit tellus commodo diam, sed tempus lacus mauris sed justo. Cras eu nisl non diam sodales ornare in vel metus. Fusce at augue sit amet orci tincidunt laoreet sed ac felis. Aenean convallis auctor nibh, et laoreet lorem aliquam ut. Cras dignissim tortor ac leo volutpat tincidunt. Donec odio nibh, sodales vel odio sit amet, molestie scelerisque elit. Phasellus placerat aliquam neque sit amet varius. Nam ligula ante, tempor in auctor ac, pretium non justo. Fusce sed tempor turpis. Donec vitae tellus hendrerit, porta augue vel, congue leo. Sed in felis ut leo auctor mattis. Nunc posuere, risus vitae rhoncus dapibus, lectus sem porta quam, non hendrerit neque mi a tellus. Cras tempus fermentum risus, a sagittis neque blandit eu. Cras vulputate egestas eleifend.",54);
        tempKnots.add(user);

        user = new TempKnot("Seek", "Etiam commodo felis ante, eu mollis urna viverra nec. Nullam sit amet venenatis magna. Donec viverra suscipit lacinia. Cras urna risus, dapibus non consectetur id, euismod et augue. Sed in lacus vel mi ultricies mattis. Nulla ut finibus sapien, vitae dapibus nisi. Praesent urna purus, malesuada id dapibus vel, blandit in lectus. Fusce vel purus tortor. Phasellus fermentum nunc eu lorem convallis condimentum.",632);
        tempKnots.add(user);

        user = new TempKnot("Pray" ,"Quisque blandit ultrices urna non bibendum. Vestibulum ultrices orci urna, non pellentesque neque dapibus vitae. Maecenas condimentum enim ex, a aliquam est vestibulum ac. Quisque tincidunt ultricies felis vitae commodo. Ut velit felis, congue non neque eu, cursus fermentum nibh. Maecenas elementum, augue ut luctus dapibus, felis mauris suscipit massa, at dignissim leo enim a orci. Curabitur tellus turpis, interdum ac ante non, fermentum pellentesque ligula.",776543);
        tempKnots.add(user);

        user = new TempKnot("Soup", "Maecenas imperdiet sem eget arcu ornare aliquam. Suspendisse convallis ligula non lobortis eleifend. Integer laoreet tincidunt vestibulum. Curabitur vel iaculis ex. Vestibulum dictum sodales lorem, nec semper orci feugiat quis. Curabitur venenatis mauris eget massa fermentum, nec bibendum ex varius. Sed tincidunt tempor dolor. Sed suscipit ante non ante eleifend, a vulputate ex imperdiet. Sed laoreet in magna tempor tincidunt. Sed at aliquam magna. In pharetra tellus dui, aliquet maximus mi tincidunt eu. Etiam consectetur, enim sit amet accumsan rhoncus, felis arcu volutpat est, eu mattis risus velit et ligula. Sed in purus orci. Suspendisse fermentum velit accumsan ipsum ultricies, ut blandit tortor consectetur.",622);
        tempKnots.add(user);

        user = new TempKnot("Philosophy" ,"Morbi sed euismod lorem. Nullam at ante aliquam, lobortis nibh pellentesque, tempor mi. Nunc eleifend erat erat, et accumsan orci bibendum a. Vestibulum eu risus lorem. Etiam a augue sit amet enim suscipit tincidunt ac sit amet massa. Donec bibendum accumsan elementum. Vestibulum dictum erat ac venenatis mollis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.",644);
        tempKnots.add(user);

        user = new TempKnot("Trial", "Proin bibendum molestie consequat. Nulla facilisi. Mauris eu mi nec felis placerat porta. Pellentesque pellentesque turpis sit amet elit ornare vehicula. Nam nec felis nunc. In blandit euismod dignissim. Nulla eget facilisis nunc. Sed facilisis magna orci, ut molestie lacus placerat id. Proin ac mauris non erat molestie aliquam. Duis vitae posuere lacus. Mauris aliquam consequat justo sit amet tincidunt. Donec non convallis arcu, nec tempor nisi. Morbi a tortor blandit, elementum augue sed, congue ante. Cras ultrices arcu eget metus semper, at ornare ligula dignissim. Proin convallis odio sed bibendum lacinia. Praesent in vehicula lectus.",6543);
        tempKnots.add(user);
    }

    /**
     * Uses the JSONParser to request the knot list from the server.
     */
    private void getKnotList() throws IOException
    {
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = jsonParser.getJSONFromUrl(url);

        try
        {
            knots = jsonObject.getJSONArray(TAG_KNOTLIST);

            for(int i = 0; i < knots.length(); i++)
            {
                JSONObject knotsJSONObject = knots.getJSONObject(i);
                Integer knotID = Integer.valueOf(knotsJSONObject.getString(TAG_KNOTID));
                String title = knotsJSONObject.getString(TAG_TITLE);
                String description = knotsJSONObject.getString("description");

                TempKnot knot = new TempKnot(title,description,knotID);
                tempKnots.add(knot);
            }
            this.viewKnotListAdapter.notifyDataSetChanged();
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
