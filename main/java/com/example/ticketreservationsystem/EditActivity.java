/*
    Cole Howell, Manoj Bompada, Justin Le
    EditActivity.java
    ITCS 4180
 */

package com.example.ticketreservationsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    private ArrayList<Ticket> tktlst = new ArrayList<>();
    private ArrayList<String> newList = new ArrayList<>();
    private Button save, selecbtn;
    private EditText edtname, edtsource, edtdest, edtdepdate, edtdeptime, edtretdate, edtrettime;
    private RadioGroup rgtrip;
    private RadioButton rb1, rb2;
    private String editor;
    public static CharSequence[] cities = {"Albany, NY", "Atlanta, GA", "Boston, MA", "Charlotte, NC", "Chicago, IL", "Greenville, SC",
            "Houston, TX", "Las Vegas, NV", "Los Angeles, CA", "Miami, FL", "Myrtle Beach, SC", "New York, NY",
            "Portland, OR", "Raleigh, NC", "San Jose, CA", "Washington, DC"};
    final Calendar c = Calendar.getInstance();
    final static String TICKET = "Ticket";
    final static String EDITTICKETLIST = "Edit Ticket List";
    Date datedep, datereturn, timedep, timereturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

        edtname = (EditText) findViewById(R.id.namefield);
        edtsource = (EditText) findViewById(R.id.source);
        edtdest = (EditText) findViewById(R.id.destination);
        edtdepdate = (EditText) findViewById(R.id.departure);
        edtdeptime = (EditText) findViewById(R.id.deptime);
        edtretdate = (EditText) findViewById(R.id.returndate);
        edtrettime = (EditText) findViewById(R.id.rettime);
        rgtrip = (RadioGroup) findViewById(R.id.triptypegroup);
        save = (Button) findViewById(R.id.save);
        selecbtn = (Button) findViewById(R.id.editSelect);
        rb2 = (RadioButton) findViewById(R.id.round);
        edtsource.setKeyListener(null);
        edtdest.setKeyListener(null);
        edtdepdate.setKeyListener(null);
        edtdeptime.setKeyListener(null);
        edtretdate.setKeyListener(null);
        edtrettime.setKeyListener(null);

        if (getIntent().getExtras() != null) {
            tktlst = (ArrayList<Ticket>) getIntent().getExtras().getSerializable(MainActivity.TKTLIST_KEY);
            for (Ticket x : tktlst) {
                String name = x.getName().toString();
                newList.add(name);
            }
        } else {
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        rgtrip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.round == checkedId) {
                    edtretdate.setVisibility(View.VISIBLE);
                    edtrettime.setVisibility(View.VISIBLE);
                } else {
                    edtretdate.setVisibility(View.INVISIBLE);
                    edtrettime.setVisibility(View.INVISIBLE);
                }
            }
        });

        final CharSequence[] charSequenceItems = newList.toArray(new CharSequence[newList.size()]);

        AlertDialog.Builder namebuilder = new AlertDialog.Builder(this);
        namebuilder.setTitle("Select a Ticket")
                .setItems(charSequenceItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor = charSequenceItems[which].toString();
                        for (Ticket x : MainActivity.tlist) {
                            if (x.getName().equals(editor)) {
                                edtname.setText(x.getName());
                                edtsource.setText(x.getSource());
                                edtdest.setText(x.getDestination());
                                String dep[] = x.getDeparture().split(",");
                                edtdepdate.setText(dep[0]);
                                edtdeptime.setText(dep[1]);
                                String ret[] = x.getReturn().split(",");
                                edtretdate.setText(ret[0]);
                                edtrettime.setText(ret[1]);
                                if (x.isRound()) {
                                    rgtrip.check(R.id.round);

                                    edtretdate.setVisibility(View.VISIBLE);
                                    edtrettime.setVisibility(View.VISIBLE);
                                } else {
                                    rgtrip.check((R.id.oneway));
                                }
                            }
                        }
                    }
                });

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Source")
                .setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edtsource.setText(cities[which]);
                    }
                })
                .setCancelable(false);

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Destination")
                .setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (edtsource.getText().toString().equals(cities[which].toString())) {
                            Toast.makeText(EditActivity.this, "Please select correct destination", Toast.LENGTH_SHORT).show();
                        } else {
                            edtdest.setText(cities[which]);
                        }
                    }
                })
                .setCancelable(false);

        final AlertDialog namealert = namebuilder.create();
        selecbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namealert.show();
            }
        });

        final AlertDialog alert1 = builder1.create();
        final AlertDialog alert2 = builder2.create();
        edtsource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert1.show();
            }
        });
        edtdest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtname.getText().toString();
                String source = edtsource.getText().toString();
                String dest = edtdest.getText().toString();
                String deptdate = edtdepdate.getText().toString();
                String depttime = edtdeptime.getText().toString();
                String dept = deptdate + ", " + depttime;
                String retdate = edtretdate.getText().toString();
                String rettime = edtrettime.getText().toString();
                String ret = retdate + ", " + rettime;
                boolean roundtrip = false;
                Ticket ticket = new Ticket(name, source, dest, dept, ret, roundtrip);
                if (rgtrip.getCheckedRadioButtonId() == R.id.round) {
                    roundtrip = true;
                }

                if(name.isEmpty()|| source.isEmpty() || dest.isEmpty()|| deptdate.isEmpty() || depttime.isEmpty())
                {
                    Toast.makeText(EditActivity.this,"Please complete the fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    for (Ticket x : MainActivity.tlist) {
                        if (x.getName().equals(editor)) {
                            MainActivity.tlist.remove(x);
                            MainActivity.tlist.addFirst(ticket);
                            break;
                        }
                    }
                    Intent intent = new Intent(EditActivity.this, PrintActivity.class);
                    intent.putExtra(TICKET, ticket);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
            c.set( Calendar.YEAR, year );
            c.set( Calendar.MONTH, monthOfYear );
            c.set( Calendar.DAY_OF_MONTH, dayOfMonth );
            setCurrentDateOnView();
            datedep = c.getTime();
        }
    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            setCurrentDateOnView();
            timedep = c.getTime();
        }
    };

    public void datetimeOnClick(View view){
        if(view.getId() == R.id.departure){
            new DatePickerDialog(EditActivity.this, date,
                    c.get( Calendar.YEAR ), c.get( Calendar.MONTH ), c.get( Calendar.DAY_OF_MONTH ) ).show();}
        else if (view.getId() == R.id.deptime){
            new TimePickerDialog(EditActivity.this, time,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),false).show();
        }
    }

    public void setCurrentDateOnView(){
        String dateFormat = "MM-dd-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, Locale.US );

        edtdepdate.setText(sdf.format(c.getTime()));

        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat( timeFormat, Locale.US );
        edtdeptime.setText( stf.format( c.getTime() ) );
    }

    //    ----------------------
    DatePickerDialog.OnDateSetListener dateret = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
            c.set( Calendar.YEAR, year );
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            setCurrentDateOnViewret();

            datereturn = c.getTime();

//            if(datedep.after(datereturn)){
                //Toast.makeText(EditActivity.this, "Please select a proper return date", Toast.LENGTH_SHORT).show();
           // }
        }
    };

    TimePickerDialog.OnTimeSetListener timeret = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            setCurrentDateOnViewret();
            timereturn = c.getTime();
        }
    };

    public void retdatetimeOnClick(View view){
        if(view.getId() == R.id.returndate){
            new DatePickerDialog(EditActivity.this, dateret,
                    c.get( Calendar.YEAR ), c.get( Calendar.MONTH ), c.get( Calendar.DAY_OF_MONTH ) ).show();}
        else if (view.getId() == R.id.rettime){
            new TimePickerDialog(EditActivity.this, timeret,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),false).show();
        }
    }

    public void setCurrentDateOnViewret(){
        String dateFormat = "MM-dd-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, Locale.US );

        edtretdate.setText(sdf.format(c.getTime()));

        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat( timeFormat, Locale.US );
        edtrettime.setText( stf.format( c.getTime() ) );
    }
}