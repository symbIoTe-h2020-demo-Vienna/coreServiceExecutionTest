package eu.h2020.symbiote.executors;

import com.google.gson.Gson;
import eu.h2020.symbiote.executors.models.Mapping;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Mael on 12/09/2016.
 */
public class SymbioteRegisterMapping {

    private static final String DATA_FOLDER = "resources/";

    private static final String MAPPING_AB = "mappingAB.xml";

    private static final String MODELA = "27184904365391960505654199312";
    private static final String MODELB = "27184904383838704579363750930";


    public static void main(String[] args) throws ClientProtocolException, IOException {
        System.out.println( "Registering mapping...");
        BigInteger bigInteger = registerMapping(MODELA,MODELB,MAPPING_AB);
        System.out.println("Mapping " + bigInteger + " registered!");
        //Need to register also reverse mapping for transitivity
        bigInteger = registerMapping(MODELB,MODELA,MAPPING_AB);
        System.out.println("Mapping transitive " + bigInteger + " also registered!");
    }

    private static BigInteger registerMapping( String modelA, String modelB, String modelFile ) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://localhost:8001/mapping");

        Mapping mapping = new Mapping();
        mapping.setMapping(readFile(modelFile));
        mapping.setModelId1(new BigInteger(modelA));
        mapping.setModelId2(new BigInteger(modelB));

        Gson gson          = new Gson();
        String s = gson.toJson(mapping);

        StringEntity input = new StringEntity(s);
        input.setContentType("application/json");
        post.setEntity(input);
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuilder sb = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            sb.append(line);
        }
        Gson gson2 = new Gson();
        Mapping result = gson2.fromJson(sb.toString(), Mapping.class);
        System.out.println( "Mapping created! Id: " + result.getId());
        return result.getId();
    }

    private static String readFile(String filename) throws IOException {
        Path path = Paths.get(DATA_FOLDER + filename);
        System.out.println(path.toString());
        System.out.println(path.toAbsolutePath());
        return new String(Files.readAllBytes(path));
    }

}
