package upc.pe.edu.lafija;

import android.app.Fragment;
//import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import upc.pe.edu.lafija.Api.ApiService;
import upc.pe.edu.lafija.Fragments.FirstFragment;
import upc.pe.edu.lafija.Fragments.SecondFragment;
import upc.pe.edu.lafija.Fragments.ThirdFragment;
import upc.pe.edu.lafija.Models.Pokemon;
import upc.pe.edu.lafija.Models.PokemonRespuesta;

public class MainActivity extends AppCompatActivity implements FirstFragment.OnFragmentInteractionListener,SecondFragment.OnFragmentInteractionListener,ThirdFragment.OnFragmentInteractionListener{

    private Retrofit retrofit;

    ArrayList<String> listStringPokemons = new ArrayList<>();
    Spinner spinnerPokemon;

    private static final String TAG = "Pokedex";



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            //inicializa el fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.content,new FirstFragment()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.content,new SecondFragment()).commit();
                    return true;
                case R.id.navigation_notifications:
                    transaction.replace(R.id.content,new ThirdFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.v4.app.Fragment fragment = new FirstFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment).commit();

        Bundle args = new Bundle();
        args.putStringArrayList("arrayFromActivity", listStringPokemons);

        FirstFragment newframent = new FirstFragment();
        newframent.setArguments(args);

        //inicializa retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obtenerDatos();

        //ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, listStringPokemons);
        //spinnerPokemon.setAdapter(adapter);


        /*@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    namesArray.add("Android");
    namesArray.add("Iphone");
    namesArray.add("Windows Phone");
    Bundle bundle = new Bundle();
    bundle.putStringArrayList("valuesArray", namesArray);
    namesFragment myFragment = new namesFragment();
    myFragment.setArguments(bundle);
    fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.main_container, myFragment);
    fragmentTransaction.commit();
}*/
    }

   public void obtenerDatos() {
       ApiService service = retrofit.create(ApiService.class);
       Call<PokemonRespuesta> pokeApiServiceCall = service.obtenerDatos();

       pokeApiServiceCall.enqueue(new Callback<PokemonRespuesta>() {
           @Override
           public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
               if(response.isSuccessful()){
                   PokemonRespuesta pokemonRespuesta = response.body();
                   ArrayList<Pokemon> listaPokemon = pokemonRespuesta.getResults();


                   for(int i=0; i<listaPokemon.size();i++){
                       listStringPokemons.add(listaPokemon.get(i).getName());
                       //Pokemon p = listaPokemon.get(i);
                       //Log.i(TAG,"Pokemon: "+p.getName());

                   }

                    /*for(int i=0; i<19; i++){
                        listpokemons.add(listaPokemon.get(i).getName());


                    }*/



               } else {
                   Log.e(TAG,"onResponse"+response.errorBody());

               }

           }

           @Override
           public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
               Log.e(TAG,"onFailure"+t.getMessage());

           }
       });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
