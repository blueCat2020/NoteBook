package com.swufe.zbp.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationView;
import com.swufe.zbp.notebook.dao.NoteDAO;
import com.swufe.zbp.notebook.model.CommonValue;
import com.swufe.zbp.notebook.ui.home.HomeViewModel;

import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static String TAG="MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private Spinner spinner;
    private  SearchView searchView;
    private HomeViewModel homeViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.setNoteDAO(new NoteDAO(this));
        spinner=findViewById(R.id.note_type);
        searchView=findViewById(R.id.searchView);
        //分类列表
        TypeSelectListener(spinner);

        //为SearchView组件设置事件监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                 homeViewModel.setNoteList(homeViewModel.findByTitle(query));

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_share, R.id.nav_chart, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();

        //使用此语句报错，查询资料后改用以下两行语句
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavHostFragment navHostFragment=(NavHostFragment)getSupportFragmentManager().findFragmentById( R.id.nav_host_fragment);
        NavController navController =navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void TypeSelectListener(Spinner spinner){

        //给Spinner添加事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //当选中某一个数据项时触发该方法
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                String noteType = (String)spinner.getItemAtPosition(position);//从spinner中获取被选择的数据
                Toast.makeText(MainActivity.this, noteType, Toast.LENGTH_SHORT).show();
                homeViewModel.setNoteList(homeViewModel.getNotesData(noteType));
                Log.i(TAG, "onItemSelected: ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * 处理新建日记事件
     * @param view
     */
    public void addNote(View view) {
        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homeViewModel.setNoteList(homeViewModel.getNotesData(CommonValue.ALL_THE_NOTES));
        Log.i(TAG, "onActivityResult: ");

    }
}