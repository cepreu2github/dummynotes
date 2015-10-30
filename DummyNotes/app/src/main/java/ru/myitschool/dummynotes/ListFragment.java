package ru.myitschool.dummynotes;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by teacher on 21.10.15.
 */
public class ListFragment extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        // set toolbar of Activity
        ((MainActivity) getActivity()).fillMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.list_notes, container, false);
        // list adapter
        final ListView listView = (ListView) rootView.findViewById(R.id.listView);
        String sortMode = getArguments().getString("sortMode");
        String searchString = getArguments().getString("searchString");
        if (sortMode.equals("date"))
            listView.setAdapter(new NotesAdapter(getActivity(), NotesService.SortMode.DATE, searchString));
        else
            listView.setAdapter(new NotesAdapter(getActivity(), NotesService.SortMode.TITLE, searchString));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).editNote(id);
            }
        });
        // add button
        rootView.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).editNote(-1);
            }
        });
        return rootView;
    }

}
