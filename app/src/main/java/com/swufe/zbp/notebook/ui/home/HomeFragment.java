package com.swufe.zbp.notebook.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.swufe.zbp.notebook.NoteActivity;
import com.swufe.zbp.notebook.R;
import com.swufe.zbp.notebook.dao.NoteDAO;
import com.swufe.zbp.notebook.model.CommonValue;
import com.swufe.zbp.notebook.model.Note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {
    private static String TAG="HomeFragment";
    private HomeViewModel homeViewModel;
    private List<Note> noteList;
    private List<HashMap<String,String>> note_list;
    private SwipeMenuListView listView;
    private SimpleAdapter adapter;
    private  SearchView searchView;
    private  Spinner spinner;
    private NoteDAO noteDAO;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.setNoteDAO(new NoteDAO(getContext()));
        homeViewModel.getNoteList().observe(getViewLifecycleOwner(), notes -> {

            Log.i(TAG, "onChanged: ");
            noteList=homeViewModel.getNoteList().getValue();
            setNote_list();
            setListView();
        });
        Log.i(TAG, "onCreateView: ");
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        noteDAO=new NoteDAO(getContext());
        listView= getActivity().findViewById(R.id.noteList);
        spinner=getActivity().findViewById(R.id.note_type);
        searchView=getActivity().findViewById(R.id.searchView);
        noteList=homeViewModel.getNotesData(CommonValue.ALL_THE_NOTES);
        setNote_list();
        setListView();

/*
        setNoteList(CommonValue.ALL_THE_NOTES);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteType = (String)spinner.getSelectedItem();
                setNoteList(noteType);
                setNote_list();
                setListView();
            }
        });
        //为SearchView组件设置事件监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                noteList=noteDAO.findByTitle(query);
                setNote_list();
                setListView();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        TypeSelectListener(spinner);

 */
        setOnItemClickListener(listView);
        setMenuItemClickListener(listView);
        Log.i(TAG, "onActivityCreated: ");
    }
    private void setNoteList(String noteType){

        if( noteType.equals(CommonValue.ALL_THE_NOTES)){
            noteList=noteDAO.getScrollData();
        }else{
            noteList=noteDAO.getScrollData(noteType);
        }


    }
    private  void  setNote_list(){
        note_list=new ArrayList<HashMap<String,String>>();
        for (Note note : noteList) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("title", note.getTitle());
            map.put("id", String.valueOf(note.getId()));
            String time=note.getYear()+"年"+ note.getMonth()+"月"+ note.getDay()+"日"+ note.getClock()+"时";
            map.put("time", time);
            note_list.add(map);
        }
    }
    private void setListView(){

        adapter = new SimpleAdapter
                (getContext(),note_list, R.layout.notelist_item, new String[]{"title", "id","time"}, new int[]{R.id.label_note_title, R.id.label_note_Id,R.id.label_note_time});
        listView.setAdapter(adapter);
        listView.setEmptyView(getView().findViewById(R.id.noData));

        Log.i(TAG, "setListView: ");
    }
    private  void setMenuItemClickListener(SwipeMenuListView listView){
        SwipeMenuCreator menuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#3CADE7")));//设置背景
                deleteItem.setWidth(150);//设置滑出 项 宽度

                deleteItem.setIcon(R.drawable.ic_delete);
                //没有删除俩字而是直接一个删除图标时.
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // 为ListView设置创建器
        listView.setMenuCreator(menuCreator);
        // 第2步：为ListView设置菜单项点击监听器，来监听菜单项的点击事件
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //position:列表项的下标。如：0，1，2，3，4，...
                //index:菜单项的下标。如：0，1，2，3，4，...
                //待完成：弹出提示框，是否删除
                long id=noteList.get(position).getId();
                note_list.remove(position);
                //更新适配器
                adapter = new SimpleAdapter
                        (getContext(),note_list, R.layout.notelist_item, new String[]{"title", "id","time"}, new int[]{R.id.label_note_title, R.id.label_note_Id,R.id.label_note_time});

                listView.setAdapter(adapter);
                noteDAO.detele(id);
                // true：其他已打开的列表项的菜单状态将保持原样，不会受到其他列表项的影响而自动收回
                // false:已打开的列表项的菜单将自动收回
                return false;
            }
        });

    }
    private void setOnItemClickListener(ListView listView){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                Note note=noteList.get(position);
                String time=note.getYear()+"年"+ note.getMonth()+"月"+ note.getDay()+"日"+ note.getClock()+"时";
                Intent intent = new Intent(getContext(), NoteActivity.class);
                intent.putExtra("id", note.getId()+"");
                intent.putExtra("type", note.getNoteType());
                intent.putExtra("title", note.getTitle());
                intent.putExtra("context", note.getContext());
                intent.putExtra("time", time);
                startActivityForResult(intent,2);
            }
        });
        Log.i(TAG, "setOnItemClickListener: ");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        TAG=null;
    }
}