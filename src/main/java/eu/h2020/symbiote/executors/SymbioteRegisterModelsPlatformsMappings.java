package eu.h2020.symbiote.executors;


import com.google.gson.Gson;
import eu.h2020.symbiote.executors.models.InformationModel;
import eu.h2020.symbiote.executors.models.Mapping;
import eu.h2020.symbiote.executors.models.Platform;
import eu.h2020.symbiote.executors.models.RDFFormat;
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

public class SymbioteRegisterModelsPlatformsMappings {
    private static final String DATA_FOLDER = "resources/";
    private static final String MODEL_A = "platformA.ttl";
    private static final String METADATA_A = "platformA-instances.ttl";
    private static final String MODEL_B = "platformB.ttl";
    private static final String METADATA_B = "platformB-instances.ttl";
    private static final String MAPPING_AB = "mappingAB.xml";

    public static void main(String[] args) throws ClientProtocolException, IOException {
        BigInteger MODEL_A_ID = registerModelAndPlatform(MODEL_A,METADATA_A);
        BigInteger MODEL_B_ID = registerModelAndPlatform(MODEL_B,METADATA_B);

        System.out.println( "Registering mapping...");
        BigInteger mappingID = registerMapping(MODEL_A_ID, MODEL_B_ID, MAPPING_AB);
        System.out.println("Mapping " + mappingID + " registered!");
            //Need to register also reverse mapping for transitivity
        mappingID = registerMapping(MODEL_B_ID, MODEL_A_ID, MAPPING_AB);
        System.out.println("Mapping transitive " + mappingID + " also registered!");
    }

    private static BigInteger registerMapping(BigInteger modelA_ID, BigInteger modelB_ID, String MAPPING_AB) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://localhost:8001/mapping");

        Mapping mapping = new Mapping();
        mapping.setMapping(readFile(MAPPING_AB));
        mapping.setModelId1(modelA_ID);
        mapping.setModelId2(modelB_ID);

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


    private static BigInteger registerModelAndPlatform( String modelFile, String platformFile ) throws IOException {
        System.out.println( "Registering model...");
        BigInteger modelID = registerModel(modelFile);
        if ( modelID != null ) {
            System.out.println( "Creating platform with model Id: " + modelID );
            registerPlatform(modelID,platformFile);
            System.out.println( "Registration finished");
        }
        return modelID;
    }


    private static BigInteger registerModel(String modelFile ) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://localhost:8001/informationmodel");


        InformationModel model = new InformationModel();
        model.setFormat(RDFFormat.Turtle);
        model.setInstance(readFile(modelFile));
        Gson gson          = new Gson();
        String s = gson.toJson(model);

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
        InformationModel result = gson2.fromJson(sb.toString(), InformationModel.class);
        System.out.println( "Model created! Id: " + result.getId());
        return result.getId();
    }

    private static void registerPlatform ( BigInteger modelId, String platformFile ) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://localhost:8001/platform");


        Platform p = new Platform();
        p.setFormat(RDFFormat.Turtle);
        p.setModelId(modelId);
        p.setInstance(readFile(platformFile));
        Gson gson          = new Gson();
        String s = gson.toJson(p);

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
        Platform createdPlat = gson.fromJson( sb.toString(), Platform.class);
        System.out.println( "Platform created! Id: " + createdPlat.getId());
    }

    private static String readFile(String filename) throws IOException {
        Path path = Paths.get(DATA_FOLDER + filename);
        System.out.println(path.toString());
        System.out.println(path.toAbsolutePath());
        return new String(Files.readAllBytes(path));
    }
}