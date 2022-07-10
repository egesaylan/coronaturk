package com.egesaylan.capstoneproject.Classes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.egesaylan.capstoneproject.Api.ApiUtilities;
import com.egesaylan.capstoneproject.Api.JsonPlaceHolderApi;
import com.egesaylan.capstoneproject.Models.Informations;
import com.egesaylan.capstoneproject.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DailyCases extends AppCompatActivity {

    public TextView active, total, recovered, death;
    private List<Informations> informationsList;
    PieChart mpiechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_cases);

        //XML dosyasında oluşturmuş olduğumuz viewları burada bir yukarıda oluşturduğumuz değerlere atayarak initialize ettik
        active = findViewById(R.id.activeCaseTV);
        recovered = findViewById(R.id.recoveredCaseTV);
        total = findViewById(R.id.totalcaseTV);
        death = findViewById(R.id.deathCaseTV);
        mpiechart = findViewById(R.id.piechart);
        informationsList = new ArrayList<>();

        //sayfa açılduığında internetten verileri çekeceğimiz fonksyonu oncreate altında çağırıyoruz
        fetchdata();

    }

    //İnternetten json verilerini çektiğimiz fonksyonu aşşağıda kodladık
    private void fetchdata() {

        ApiUtilities.getAPIInterface().getInformations().enqueue(new Callback<List<Informations>>() {
            @Override
            public void onResponse(Call<List<Informations>> call, Response<List<Informations>> response) {
                //Bütün json objelerini informationlist dediğimiz arrayliste aktardık
                informationsList.addAll(response.body());
                for (int i = 0; i < informationsList.size(); i++) {
                    //eğer json object içerisindeki değer Türkiye'ye eşitse Türkiyenin değerlerini fetch lediğimiz adımlar aşşağıda kodlandı
                    if (informationsList.get(i).getCountry().equals("Turkey")) {
                        total.setText((informationsList.get(i).getCases()));
                        active.setText((informationsList.get(i).getActive()));
                        recovered.setText((informationsList.get(i).getRecovered()));
                        death.setText((informationsList.get(i).getDeaths()));


                        //pie chartın içine atanacak değerleri tanımladığımız adımlar alt kısımda kodlandı
                        int active, total, recovered, deaths;

                        active = Integer.parseInt(informationsList.get(i).getActive());
                        total = Integer.parseInt(informationsList.get(i).getCases());
                        recovered = Integer.parseInt(informationsList.get(i).getRecovered());
                        deaths = Integer.parseInt(informationsList.get(i).getDeaths());

                        updateGraph(active, total, recovered, deaths);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Informations>> call, Throwable t) {

            }
        });
    }

    //Pie chart fonksyonu
    private void updateGraph(int active, int total, int recovered, int deaths) {

        mpiechart.clearChart();
        mpiechart.addPieSlice(new PieModel("Confirm", total, Color.parseColor("#FFC107")));
        mpiechart.addPieSlice(new PieModel("Active", active, Color.parseColor("#50C878")));
        mpiechart.addPieSlice(new PieModel("Recovered", recovered, Color.parseColor("#2196F3")));
        mpiechart.addPieSlice(new PieModel("Deaths", deaths, Color.parseColor("#000000")));
        mpiechart.startAnimation();


    }
}