package com.egesaylan.capstoneproject.Classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.egesaylan.capstoneproject.Models.User;
import com.egesaylan.capstoneproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class RiskyPeopleAround extends AppCompatActivity {
    //XML kısımları ve değişkenler
    CheckBox enable, visible;
    ImageView search;
    ListView riskyList;
    String userID;
    TextView bluetoothName, riskyCount, backToMain;
    ArrayList<String> devicesList;
    ArrayAdapter<String> arrayAdapter;
    AppCompatButton showCount;

    //Bluetooth değişkenleri
    private BluetoothAdapter BA;
    String firstName;
    //private Set<BluetoothDevice> pairedDevices;


    //Firebase
    DatabaseReference bluetoothReferance;
    FirebaseUser bluetoothUser;

    //RiskyCount için int değeri
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risky_people_around);

        enable = findViewById(R.id.enableCB);
        search = findViewById(R.id.searchBluetooth);
        riskyList = findViewById(R.id.riskyPeopleList);
        visible = findViewById(R.id.visibleCB);
        devicesList = new ArrayList<String>();
        bluetoothName = findViewById(R.id.name_bt);
        riskyCount = findViewById(R.id.riskyCount);
        riskyList.setAdapter(arrayAdapter);
        showCount = findViewById(R.id.showCount);
        backToMain = findViewById(R.id.endToBluetooth);

        BA = BluetoothAdapter.getDefaultAdapter();
        firstName = BA.getName();

        changeNameIfRisky();


        makeVisibleCheckBoxVisible();

        if (BA == null) {
            Toast.makeText(RiskyPeopleAround.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (BA.isEnabled()) {
            enable.setChecked(true);
        }

        visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    getVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                    startActivityForResult(getVisible, 0);
                    Toast.makeText(RiskyPeopleAround.this, "Visible", Toast.LENGTH_SHORT).show();
                    search.setVisibility(View.VISIBLE);
                }
            }
        });

        enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    BA.disable();
                    Toast.makeText(RiskyPeopleAround.this, "Turned Off", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, 0);
                    Toast.makeText(RiskyPeopleAround.this, "Turned on", Toast.LENGTH_SHORT).show();
                }
            }
        });

        showCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String ss : devicesList) {
                    if(ss == null){
                        break;
                    }

                    else if (ss.equals("CoronaTurk")) {
                        counter = counter + 1;
                    }
                    riskyCount.setText(String.valueOf(counter));
                }
            }
        });

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RiskyPeopleAround.this,MainActivity.class));
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devicesList.clear();
                arrayAdapter.notifyDataSetChanged();
                BA.startDiscovery();
            }
        });

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, intentFilter);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, devicesList);
        riskyList.setAdapter(arrayAdapter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devicesList.add(device.getName());
                arrayAdapter.notifyDataSetChanged();

            }
        }
    };


    /*private void list(){
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices){
            list.add(bt.getName());
        }

        Toast.makeText(this, "Showing Risky People", Toast.LENGTH_SHORT).show();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
        riskyList.setAdapter(arrayAdapter);
    }*/

    /*public String getBluetoothName() {
        if (BA == null) {
            BA = BluetoothAdapter.getDefaultAdapter();
        }
        String name = BA.getName();
        if (name == null) {
            name = BA.getAddress();
        }

        return name;
    }*/

    public void makeVisibleCheckBoxVisible() {
        bluetoothUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = bluetoothUser.getUid();
        bluetoothReferance = FirebaseDatabase.getInstance().getReference("Users");
        bluetoothReferance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {
                    User userInfos = ss.getValue(User.class);

                    if (ss.getKey().equals(userID)) {
                        if (userInfos.status == 1) {
                            visible.setVisibility(View.VISIBLE);
                        } else {
                            visible.setVisibility(View.INVISIBLE);
                            search.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void changeNameIfRisky() {
        bluetoothUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = bluetoothUser.getUid();
        bluetoothReferance = FirebaseDatabase.getInstance().getReference("Users");
        bluetoothReferance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {
                    User user = ss.getValue(User.class);

                    if (ss.getKey().equals(userID)) {
                        if (user.status == 1) {
                            BA.setName("CoronaTurk");
                            bluetoothName.setText(BA.getName());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        BA.setName(firstName);
    }
}