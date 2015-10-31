package in.dinnernight.broadcasttest;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import android.support.v4.app.ListFragment;

public class ContactList extends ListFragment {
    DBHelper mydb;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mydb = new DBHelper(getActivity());
        ArrayList array_list = mydb.getAllCotacts();

        ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, array_list);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id)
    {
        int id_To_Search = position + 1;
        Intent intent = new Intent(getActivity(),EditContact.class);
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("id", id_To_Search);
        intent.putExtras(dataBundle);
        startActivity(intent);
    }
}
