package upc.pe.edu.lafija.Api;



import retrofit2.Call;
import retrofit2.http.GET;
import upc.pe.edu.lafija.Models.PokemonRespuesta;

public interface ApiService {
    @GET("pokemon")
    Call<PokemonRespuesta> obtenerDatos();
}
