package eu.h2020.symbiote.DEPRICATEDexecutors;

import com.google.gson.Gson;
import eu.h2020.symbiote.DEPRICATEDexecutors.models.Ontology;
import eu.h2020.symbiote.DEPRICATEDexecutors.models.SearchObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Mael on 12/09/2016.
 */
public class SymbioteQuery {

    private static final String DATA_FOLDER = "resources/";
    private static final String QUERY_A = "queryA.sparql";
    private static final String modelA_ID = "27197365727749257496298336307"; //There you have to provide the model ID for Model A
                                                                            //(You can get it from localhost:8001/informationmodel page) for now

    private static final String QUERY_B = "queryB.sparql";
    private static final String modelB_ID = "27197365727749257496298336309"; //   -||-

    private static final String QUERY_CORE = "queryCore.sparql";
    private static final String MODEL_ID = "none";

    public static void main(String[] args) throws ClientProtocolException, IOException {
        searchUsingQuery(QUERY_A, modelA_ID);
        searchUsingQuery(QUERY_B, modelB_ID);
        searchUsingQuery(QUERY_CORE, MODEL_ID);
    }

    public static void searchUsingQuery(String query, String model) throws IOException {
        System.out.println("-Results for: \n query: " + query + "\n model id: " + model +" :\n");
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://localhost:8120/search");

        SearchObject p = new SearchObject(Ontology.getModelGraphURI(modelA_ID), readFile(QUERY_A));
        Gson gson          = new Gson();
        String s = gson.toJson(p);
        StringEntity input = new StringEntity(s);
        input.setContentType("application/json");
        post.setEntity(input);
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("\n");
    }



    private static String readFile(String filename) throws IOException {
        Path path = Paths.get(DATA_FOLDER + filename);
        System.out.println(path.toString());
        System.out.println(path.toAbsolutePath());
        return new String(Files.readAllBytes(path));
    }
}
