package ca.ulaval.ima.ufit.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.ulaval.ima.ufit.R;
import ca.ulaval.ima.ufit.fragments.CaloriesFragment;
import ca.ulaval.ima.ufit.fragments.ScansFragment;
import ca.ulaval.ima.ufit.utils.Item;
import ca.ulaval.ima.ufit.utils.ParcelableItemList;

public class UserConsumptionActivity extends AppCompatActivity implements
    ScansFragment.OnFragmentInteractionListener,
    CaloriesFragment.OnFragmentInteractionListener {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link FragmentPagerAdapter} derivative, which will keep every
   * loaded fragment in memory. If this becomes too memory intensive, it
   * may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  private SectionsPagerAdapter mSectionsPagerAdapter;
  /**
   * The {@link ViewPager} that will host the section contents.
   */
  private ViewPager mViewPager;
  private IntentIntegrator barcode;
  private String username;
  private ParcelableItemList scansList = new ParcelableItemList();
  private ParcelableItemList caloriesList = new ParcelableItemList();
  private ScansFragment scansFragment;
  private CaloriesFragment caloriesFragment;
  private SQLiteDatabase mydatabase;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    username = getIntent().getExtras().getString("User");
    mydatabase = openOrCreateDatabase("ufit",MODE_PRIVATE,null);
    Cursor cursor = mydatabase.rawQuery("SELECT * FROM Items WHERE Username = '" + username + "'", null);
    while (cursor.moveToNext()) {
      String username = cursor.getString(0);
      String title = cursor.getString(1);
      String description = cursor.getString(2);
      String brand = cursor.getString(3);
      String image = cursor.getString(4);
      int calories = cursor.getInt(5);
      Item item = new Item(username, title, description, brand, image, calories);
      scansList.add(item);
      caloriesList.add(item);
    }
    scansFragment = ScansFragment.newInstance(scansList);
    caloriesFragment = CaloriesFragment.newInstance(caloriesList);
    setContentView(R.layout.activity_user_consumption);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
    mViewPager = (ViewPager) findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);
    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    barcode = new IntentIntegrator(this);
    getSupportActionBar().hide();
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        barcode.initiateScan();
      }
    });
  }

  @Override
  protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if(result != null) {
      if(result.getContents() == null) {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
      } else {
        final Item item = new Item(null, null, null, null, null, 0);
        final ContentValues dbItem = new ContentValues();
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.upcitemdb.com/prod/trial/lookup?upc=" + result.getContents(), null, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            try {
              JSONArray items = response.getJSONArray("items");
              JSONObject object = items.getJSONObject(0);
              JSONArray images = object.getJSONArray("images");
              String image = images.getString(0);
              String title = object.getString("title");
              title  = title.replaceAll(" ", "+");
              System.out.println(title);
              JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://api.edamam.com/api/food-database/parser?ingr=K" + title + "&app_id=7a556742&app_key=c1c6534a9b99b99df9fa9f3292b9db74", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                  try {
                    JSONArray parsed = response.getJSONArray("parsed");
                    JSONObject first = parsed.getJSONObject(0);
                    JSONObject food = first.getJSONObject("food");
                    JSONObject nutrients = food.getJSONObject("nutrients");
                    item.setCalories(nutrients.getInt("ENERC_KCAL"));
                    dbItem.put("Calories", item.getCalories());
                    scansFragment.addToItemList(item);
                    caloriesFragment.addToItemList(item);
                    mydatabase.insert("Items", null, dbItem);
                  } catch (JSONException e) {
                    e.printStackTrace();
                  }
                }
              }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
              });
              requestQueue.add(request);
              item.setUsername(username);
              item.setTitle(object.getString("title"));
              item.setDescription(object.getString("description"));
              item.setBrand(object.getString("brand"));
              item.setImage(image);
              dbItem.put("Username", username);
              dbItem.put("Title", item.getTitle());
              dbItem.put("Description", item.getDescription());
              dbItem.put("Brand", item.getBrand());
              dbItem.put("Image", item.getImage());
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
          }
        });
        requestQueue.add(jsonObjectRequest);
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_user_consumption, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onFragmentInteraction(Uri uri) {
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return scansFragment;
        case 1:
          return caloriesFragment;
        default:
          return scansFragment;
      }
    }

    @Override
    public int getCount() {
      return 2;
    }

    }
}
