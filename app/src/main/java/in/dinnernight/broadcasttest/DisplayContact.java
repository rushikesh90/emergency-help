package in.dinnernight.broadcasttest;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayContact extends Activity implements View.OnClickListener {
    int from_Where_I_Am_Coming = 0;
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
        setContentView(R.layout.activity_display_contact);
        name = (TextView) findViewById(R.id.editTextName);
        phone = (TextView) findViewById(R.id.editTextPhone);
        email = (TextView) findViewById(R.id.editTextStreet);
        street = (TextView) findViewById(R.id.editTextEmail);
        place = (TextView) findViewById(R.id.editTextCity);

           mydb = new DBHelper(this);

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


                Button b = (Button)findViewById(R.id.button1);
                        b.setOnClickListener(this);







    }





   /* public void run(View view)
    {
        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            int Value = extras.getInt("id");
            if(Value>0){
                if(mydb.updateContact(id_To_Update,name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), place.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(mydb.insertContact(name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), place.getText().toString())){
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }
*/
    @Override
    public void onClick(View v) {

        if(mydb.insertContact(name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), place.getText().toString())) {
            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
