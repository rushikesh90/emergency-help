package in.dinnernight.broadcasttest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditContact extends AppCompatActivity implements View.OnClickListener {
    private DBHelper mydb ;

    TextView name ;
    TextView phone;
    TextView email;
    TextView street;
    TextView place;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        name = (TextView) findViewById(R.id.EditTextName);
        phone = (TextView) findViewById(R.id.EditTextPhone);
        email = (TextView) findViewById(R.id.EditTextStreet);
        street = (TextView) findViewById(R.id.EditTextEmail);
        place = (TextView) findViewById(R.id.EditTextCity);

        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");

            if (Value > 0) {
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
                String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
                String stree = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_STREET));
                String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));

                if (!rs.isClosed()) {
                    rs.close();
                }

                name.setText((CharSequence) nam);
                name.setFocusable(false);
                name.setClickable(false);

                phone.setText((CharSequence) phon);
                phone.setFocusable(false);
                phone.setClickable(false);

                email.setText((CharSequence) emai);
                email.setFocusable(false);
                email.setClickable(false);

                street.setText((CharSequence) stree);
                street.setFocusable(false);
                street.setClickable(false);

                place.setText((CharSequence) plac);
                place.setFocusable(false);
                place.setClickable(false);



                Button b1 = (Button) findViewById(R.id.button1);
                Button b2 = (Button) findViewById(R.id.button2);
                Button b3 = (Button) findViewById(R.id.button3);
                b1.setOnClickListener(this);
                b2.setOnClickListener(this);
                b3.setOnClickListener(this);
                b3.setClickable(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.button1:

               name.setEnabled(true);
               name.setFocusableInTouchMode(true);
               name.setClickable(true);

               phone.setEnabled(true);
               phone.setFocusableInTouchMode(true);
               phone.setClickable(true);

               email.setEnabled(true);
               email.setFocusableInTouchMode(true);
               email.setClickable(true);

               street.setEnabled(true);
               street.setFocusableInTouchMode(true);
               street.setClickable(true);

               place.setEnabled(true);
               place.setFocusableInTouchMode(true);
               place.setClickable(true);
               Button b3 = (Button) findViewById(R.id.button3);
               b3.setClickable(true);
               Button b1 = (Button) findViewById(R.id.button1);
               b1.setClickable(false);
               //   mydb.updateContact(id_To_Update, name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), place.getText().toString());
             //  Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
               break;

               case R.id.button2:

               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               builder.setMessage(R.string.deleteContact)
                       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               mydb.deleteContact(id_To_Update);
                               Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                               startActivity(intent);
                           }
                       })
                       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               // User cancelled the dialog
                           }
                       });
               AlertDialog d = builder.create();
               d.setTitle("Are you sure");
               d.show();
                   break;

           case R.id.button3:
               mydb.updateContact(id_To_Update, name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), place.getText().toString());
               Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getApplicationContext(),MainActivity.class);
               startActivity(intent);

           default:

       }
    }
}
