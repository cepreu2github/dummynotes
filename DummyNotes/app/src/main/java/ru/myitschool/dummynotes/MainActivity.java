package ru.myitschool.dummynotes;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // use this icons: http://icons4android.com

    private Context mContext = this;
    private Toolbar mToolbar;
    private SharedPreferences mPrefs;
    private String sortMode = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // determine action
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // Search work
            String query = intent.getStringExtra(SearchManager.QUERY);
            mToolbar.setSubtitle("Searching: " + query);
            if (savedInstanceState == null)
                refreshList(query);
        } else {
            // place list of notes
            if (savedInstanceState == null)
                refreshList("");
        }
    }

    public void clearMenu(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.getMenu().clear();
    }

    public void editNote(long id){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EditFragment editFragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        editFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.controls, editFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void refreshList(String searchString){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ListFragment listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("sortMode", sortMode);
        bundle.putString("searchString", searchString);
        listFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.controls, listFragment);
        fragmentTransaction.commit();
    }

    public void fillMenu(){
        clearMenu();
        mToolbar.inflateMenu(R.menu.menu_main);
        Menu menu = mToolbar.getMenu();
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
    }

    public void showList(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.getBackStackEntryCount() == 0)
            fillMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort){
            if (sortMode.equals("date"))
                sortMode = "title";
            else
                sortMode = "date";
            Toast.makeText(mContext, "Sort by " + sortMode, Toast.LENGTH_SHORT).show();
            refreshList("");
        }
        if (id == android.R.id.home) {
            showList();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.getBackStackEntryCount() != 0) {
            showList();
        } else {
            super.onBackPressed();
        }
    }

}
