package eu.h2020.symbiote.generators;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by mateuszl on 05.10.2016.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AddResources {

    private static final String DATA_FOLDER = "json_resources/";
    private static final String PLATFORM_1 = "platform1.json";
    private static final String PLATFORM_2 = "platform2.json";
    private static final String PLATFORM_3 = "platform3.json";
    private static final String PLATFORM_4 = "platform4.json";
    private static final String RESOURCE_1 = "resource1.json";
    private static final String RESOURCE_2 = "resource2.json";
    private static final String RESOURCE_3 = "resources_multiple1.json";
    private static final String RESOURCE_4 = "resources_multiple2.json";

    private static final String HOST = "http://localhost:8101";


    public static void main(String[] args) {
        try {
            registerPlatformAndResources(PLATFORM_1, RESOURCE_1);
            registerPlatformAndResources(PLATFORM_2, RESOURCE_2);
            registerPlatformAndResources(PLATFORM_3, RESOURCE_3);
            registerPlatformAndResources(PLATFORM_4, RESOURCE_4);
            System.out.println("Registration process completed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String registerPlatformAndResources(String platformFile, String resourceFile) throws IOException {
        String resourceID = "empty";
        System.out.println("Registering platform...");
        String platformID = registerPlatform(platformFile);
        System.out.println("Platform registered with id :" + platformID);
        if (platformID != null) {
            System.out.println("Registering resource with platform Id: " + platformID);
            resourceID = registerResource(platformID, resourceFile);
            System.out.println("Registration finished");
        }
        return resourceID;
    }

    private static String registerPlatform(String platformFile) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(HOST + "/cloud_api/platforms");

        String platformInstance = readFile(platformFile);
        System.out.println("||||||||||||||||||||||||||||||||||||||||");
        System.out.println(platformInstance);
        System.out.println("||||||||||||||||||||||||||||||||||||||||");

        StringEntity input = new StringEntity(platformInstance);
        input.setContentType("application/json");
        post.setEntity(input);
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        System.out.println("||||||||||||||||||||||||||||||||||||||||");
        System.out.println(rd);
        System.out.println("||||||||||||||||||||||||||||||||||||||||");
        String line = "";
        StringBuilder sb = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            sb.append(line);
        }
        System.out.println("||||||||||||||||||||||||||||||||||||||||");
        System.out.println(sb);
        System.out.println("||||||||||||||||||||||||||||||||||||||||");
        Gson gson2 = new Gson();
        String[] result = gson2.fromJson(sb.toString(), String[].class);
        System.out.println("Resource created! Id: " + result[0]);
        return result[0];
    }

    private static String registerResource(String platformID, String resourceFile) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(HOST + "/cloud_api/platforms/" + platformID +"/resources");

        String resourceInstance = readFile(resourceFile);
        StringEntity input = new StringEntity(resourceInstance);
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
        String result = gson2.fromJson(sb.toString(), String.class);
        System.out.println("Resource created! Id: " + result);
        return result;
    }

    private static String readFile(String filename) throws IOException {
        Path path = Paths.get(DATA_FOLDER + filename);
        System.out.println(path.toString());
        System.out.println(path.toAbsolutePath());
        return new String(Files.readAllBytes(path));
    }
}
