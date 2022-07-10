package com.egesaylan.capstoneproject.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.egesaylan.capstoneproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MapFragment extends Fragment {

    public GoogleMap mMap;
    public String[] ilceler;
    TextView mapTextView;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.map_fragment, container, false);

        mapTextView = root.findViewById(R.id.mapText);
        mapTextView.setSelected(true);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_maps);


        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                /*Circle circle = googleMap.addCircle(new CircleOptions()
                        .center(new LatLng(41.584299, 28.146085))
                        .radius(2000)
                        .strokeColor(getColorWithAlpha(Color.GREEN,0.4f))
                        .strokeWidth(20)
                        .fillColor(getColorWithAlpha(Color.YELLOW,0.4f)));*/

                mMap = googleMap;

                //istanbulun 4 köşesinin latlang pozisyonlarını tanımladık
                LatLng one = new LatLng(41.584299, 28.146085);
                LatLng two = new LatLng(41.141703, 29.912862);
                LatLng three = new LatLng(40.811602, 29.339939);
                LatLng four = new LatLng(41.034764, 28.006224);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                //bu köşe kordinatlarını builderın içine ekledik
                builder.include(one);
                builder.include(two);
                builder.include(three);
                builder.include(four);

                //LatLngBounds değerlerimizi bounds değişkeni içerisine attık (builderdaki değerleri)
                LatLngBounds bounds = builder.build();

                //mevcut ekranımızın(telefonun) ekranının yükseklik ve genişliğini 2 ayrı değere (width,height) eşitledik
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;

                // %0.01 padding ile başlattık
                int padding = (int) (width * 0.005);

                //google mapse boundsları setledik
                mMap.setLatLngBoundsForCameraTarget(bounds);

                //kameranın maximum göstericeği sınırları alttaki kodda tanımladık
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

                //kullanıcının yapabileceği minimum zoomu belirledik
                mMap.setMinZoomPreference(mMap.getCameraPosition().zoom);

                mMap.clear();

                ilceler = new String[]{"Adalar", "Arnavutköy", "Ataşehir", "Avcılar", "Bağcılar", "Bahçelievler", "Bakırköy", "Başakşehir",
                        "Bayrampaşa", "Beşiktaş", "Beykoz", "Beylikdüzü", "Beyoğlu", "Çekmeköy", "Esenler", "Esenyurt", "Eyüp", "Fatih", "Gaziosmanpaşa", "Güngören", "Kadıköy","Kağıthane","Kartal",
                        "Maltepe","Pendik","Sancaktepe","Sarıyer","Silivri","Sultanbeyli","Sultangazi","Şile","Şişli","Tuzla","Ümraniye","Üsküdar","Zeytinburnu"};

                for(int i = 0; i < ilceler.length; i++){
                    changeRegionHeat(googleMap,ilceler[i]);
                }

                setMapText();

                //Isı haritasını değiştiren fonksyonu çağırdık
                /*changeRegionHeat(googleMap, "Adalar");
                changeRegionHeat(googleMap, "Arnavutköy");
                changeRegionHeat(googleMap, "Ataşehir");
                changeRegionHeat(googleMap, "Avcılar");
                changeRegionHeat(googleMap, "Bağcılar");
                changeRegionHeat(googleMap, "Bahçelievler");
                changeRegionHeat(googleMap, "Bakırköy");
                changeRegionHeat(googleMap, "Başakşehir");
                changeRegionHeat(googleMap, "Bayrampaşa");
                changeRegionHeat(googleMap, "Beşiktaş");
                changeRegionHeat(googleMap, "Beykoz");
                changeRegionHeat(googleMap, "Beylikdüzü");
                changeRegionHeat(googleMap, "Beyoğlu");
                changeRegionHeat(googleMap, "Çekmeköy");
                changeRegionHeat(googleMap, "Esenler");
                changeRegionHeat(googleMap, "Esenyurt");
                changeRegionHeat(googleMap, "Eyüp");
                changeRegionHeat(googleMap, "Fatih");
                changeRegionHeat(googleMap, "Gaziosmanpaşa");
                changeRegionHeat(googleMap, "Güngören");
                changeRegionHeat(googleMap, "Kadıköy");
                changeRegionHeat(googleMap, "Kağıthane");
                changeRegionHeat(googleMap, "Kartal");
                changeRegionHeat(googleMap, "Maltepe");
                changeRegionHeat(googleMap, "Pendik");
                changeRegionHeat(googleMap, "Sancaktepe");
                changeRegionHeat(googleMap, "Sarıyer");
                changeRegionHeat(googleMap, "Silivri");
                changeRegionHeat(googleMap, "Sultanbeyli");
                changeRegionHeat(googleMap, "Sultangazi");
                changeRegionHeat(googleMap, "Şile");
                changeRegionHeat(googleMap, "Şişli");
                changeRegionHeat(googleMap, "Tuzla");
                changeRegionHeat(googleMap, "Ümraniye");
                changeRegionHeat(googleMap, "Üsküdar");
                changeRegionHeat(googleMap, "Zeytinburnu");*/


            }
        });

        return root;
    }

    //Isı haritasının renklerini belirleyen fonksyon
    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    //Isı haritasını databaseden gelen verilere göre oluşturan fonksyon
    public static void changeRegionHeat(GoogleMap map, String region) {

        DatabaseReference reference;
        GoogleMap heatMap = map;

        reference = FirebaseDatabase.getInstance().getReference("Regions");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {

                    if (ss.getKey().equals(region)) {

                        String strSickness = String.valueOf(ss.child("sickness").getValue());
                        int intSickness = Integer.parseInt(strSickness);

                        String strArea = String.valueOf(ss.child("area").getValue());
                        int intArea = Integer.parseInt(strArea);

                        String strPopulation = String.valueOf(ss.child("population").getValue());
                        double intPopulation = Double.parseDouble(strPopulation);

                        String strLat = String.valueOf(ss.child("lat").getValue());
                        String strLng = String.valueOf(ss.child("lng").getValue());

                        double lat = Double.parseDouble(strLat);
                        double lng = Double.parseDouble(strLng);

                        double proportion;
                        proportion = intSickness/(intArea * intPopulation);

                        double baseProp;
                        baseProp = intPopulation/(intPopulation * intArea);

                        double percentage;
                        percentage = (proportion * 100)/baseProp;



                        if (percentage < 10) {
                            Circle circle = heatMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .radius(1000)
                                    .strokeColor(getColorWithAlpha(Color.GREEN, 0.4f))
                                    .strokeWidth(10)
                                    .fillColor(getColorWithAlpha(Color.GREEN, 0.4f)));

                        }

                        else if (percentage > 10 && percentage < 30) {
                            Circle circle = heatMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .radius(800)
                                    .strokeColor(getColorWithAlpha(Color.GREEN, 0.4f))
                                    .strokeWidth(10)
                                    .fillColor(getColorWithAlpha(Color.GREEN, 0.4f)));

                            Circle redCircle = heatMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .radius(500)
                                    .strokeColor(getColorWithAlpha(Color.YELLOW, 0.5f))
                                    .strokeWidth(10)
                                    .fillColor(getColorWithAlpha(Color.YELLOW, 0.5f)));
                        }

                        else if (percentage > 30 && percentage < 50) {
                            Circle circle = heatMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .radius(800)
                                    .strokeColor(getColorWithAlpha(Color.YELLOW, 0.4f))
                                    .strokeWidth(10)
                                    .fillColor(getColorWithAlpha(Color.YELLOW, 0.4f)));

                            Circle redCircle = heatMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .radius(500)
                                    .strokeColor(getColorWithAlpha(Color.YELLOW, 0.4f))
                                    .strokeWidth(10)
                                    .fillColor(getColorWithAlpha(Color.RED, 0.4f)));
                        }

                        else if (percentage > 50 && percentage < 70) {

                            Circle circle = heatMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .radius(1000)
                                    .strokeColor(getColorWithAlpha(Color.YELLOW, 0.4f))
                                    .strokeWidth(10)
                                    .fillColor(getColorWithAlpha(Color.YELLOW, 0.4f)));

                            Circle redCircle = heatMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .radius(700)
                                    .strokeColor(getColorWithAlpha(Color.RED, 0.4f))
                                    .strokeWidth(10)
                                    .fillColor(getColorWithAlpha(Color.RED, 0.4f)));
                        }

                        else if (percentage > 70 && percentage < 90) {
                            Circle redCircle = heatMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .radius(1400)
                                    .strokeColor(getColorWithAlpha(Color.RED, 0.4f))
                                    .strokeWidth(10)
                                    .fillColor(getColorWithAlpha(Color.RED, 0.4f)));
                        }

                        else {
                            Circle redCircle = heatMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .radius(1600)
                                    .strokeColor(getColorWithAlpha(Color.RED, 0.6f))
                                    .strokeWidth(10)
                                    .fillColor(getColorWithAlpha(Color.RED, 0.6f)));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void setMapText(){
        ArrayList<String> texts = new ArrayList<String>();
        DatabaseReference textReference;

        textReference = FirebaseDatabase.getInstance().getReference("Regions");
        textReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ss : snapshot.getChildren()){

                    for (String s : ilceler) {

                        if (ss.getKey().equals(s)) {
                            String strSickness = String.valueOf(ss.child("sickness").getValue());
                            int intSickness = Integer.parseInt(strSickness);

                            String strArea = String.valueOf(ss.child("area").getValue());
                            int intArea = Integer.parseInt(strArea);

                            String strPopulation = String.valueOf(ss.child("population").getValue());
                            double intPopulation = Double.parseDouble(strPopulation);

                            double proportion;
                            proportion = intSickness / (intArea * intPopulation);

                            double baseProp;
                            baseProp = intPopulation / (intPopulation * intArea);

                            double percentage;
                            percentage = (proportion * 100) / baseProp;
                            String strPercentage = String.valueOf(percentage);
                            strPercentage = (new DecimalFormat("#.##").format(percentage));

                            texts.add(ss.getKey()+" %"+strPercentage +", ");

                            if(texts.size() == ilceler.length){
                                for(String t : texts){
                                    mapTextView.append(t);
                                }
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
