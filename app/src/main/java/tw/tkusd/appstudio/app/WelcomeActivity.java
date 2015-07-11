package tw.tkusd.appstudio.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tw.tkusd.appstudio.R;

public class WelcomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //@InjectView(R.id.btn_logout)
    //Button btnlogout;
    //-----new
    private ActionBarDrawerToggle actionbardeawertoggle;
    private DrawerLayout drawerLayout;
    private ListView navList;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        int Open,Close;
        drawerLayout=( DrawerLayout)findViewById(R.id.draglayout);
        navList=(ListView)findViewById(R.id.mavlist);
        ArrayList<String> navArray=new ArrayList<String>();
        navArray.add("homepage");
        navArray.add("setting");
        navArray.add("fragment 2");
        navArray.add("fragment 3");
        navList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,navArray);//改按下去顏色,綠色
        navList.setAdapter(adapter);
        navList.setOnItemClickListener(this);  //動作
        actionbardeawertoggle= new ActionBarDrawerToggle(this,drawerLayout,R.string.closedrawer,R.string.oppendrawer);
        drawerLayout.setDrawerListener(actionbardeawertoggle);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        fragmentmanager=getSupportFragmentManager();

        loadselection(0);//一開始=默認第一個選項
    }

    private void loadselection(int i){
        navList.setItemChecked(i,true);
        switch(i){

            case 0:
                HomeFragment homefrag=new HomeFragment();
                fragmentTransaction =fragmentmanager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,homefrag);
                fragmentTransaction.commit();
                break;
            case 1:
                Fragment1 myFragment1=new Fragment1();
                fragmentTransaction =fragmentmanager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,myFragment1);
                fragmentTransaction.commit();
                break;
            case 2:
                Fragment2 myFragment2=new Fragment2();
                fragmentTransaction =fragmentmanager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,myFragment2);
                fragmentTransaction.commit();
                break;
            case 3:
                Fragment3 myFragment3=new Fragment3();
                fragmentTransaction =fragmentmanager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,myFragment3);
                fragmentTransaction.commit();
                break;
        }

    }

    @Override
    protected  void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionbardeawertoggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id== android.R.id.home){
            if(drawerLayout.isDrawerOpen(navList)){
                drawerLayout.closeDrawer((navList));
            }else {
                drawerLayout.openDrawer(navList);
            }

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navList)) {
            drawerLayout.closeDrawer(navList);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(AdapterView<?>patent,View view,int position,long id){
        loadselection(position);
        //點了側邊攔選項有動作
        drawerLayout.closeDrawer(navList);
    }
}