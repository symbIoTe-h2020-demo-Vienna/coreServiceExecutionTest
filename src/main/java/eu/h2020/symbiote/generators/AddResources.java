package eu.h2020.symbiote.generators;

import com.google.gson.Gson;
import eu.h2020.symbiote.DEPRICATEDexecutors.models.SimplePlatform;
import eu.h2020.symbiote.DEPRICATEDexecutors.models.SimpleSensor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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

    private static final Log log = LogFactory.getLog(AddResources.class);

    private static final String DATA_FOLDER = "json_resources/";
    private static final String PLATFORM_1 = "platform1.json";
    private static final String PLATFORM_2 = "platform2.json";
    private static final String PLATFORM_3 = "platform3.json";
    private static final String PLATFORM_4 = "platform4.json";
    private static final String PLATFORM_UWE = "platformUWEDAT.json";
    private static final String RESOURCE_1 = "resource1.json";
    private static final String RESOURCE_2 = "resource2.json";
    private static final String RESOURCE_3 = "resources_multiple1.json";
    private static final String RESOURCE_4 = "resources_multiple2.json";
    private static final String RESOURCE_UWE = "resources_uwedat_test.json";

    private static final String HOST = "http://localhost:8101";


    public static void main(String[] args) {
        try {
            System.out.println("1");
            registerPlatformAndResources(PLATFORM_1, RESOURCE_1);
            System.out.println("2");
            registerPlatformAndResources(PLATFORM_2, RESOURCE_2);
            registerPlatformAndResources(PLATFORM_3, RESOURCE_3);
            registerPlatformAndResources(PLATFORM_4, RESOURCE_4);
            registerPlatformAndResources(PLATFORM_UWE, RESOURCE_UWE);
            System.out.println("Registration process completed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String registerPlatformAndResources(String platformFile, String resourceFile) throws IOException {
        String resourceID = "empty";
        log.info("Registering platform...");
        String platformID = registerPlatform(platformFile);
        log.info("Platform registered with id :" + platformID);
        if (platformID != null) {
            log.info("Registering resource with platform Id: " + platformID);
            resourceID = registerResource(platformID, resourceFile);
            log.info("Registration finished");
        }
        return resourceID;
    }

    private static String registerPlatform(String platformFile) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(HOST + "/cloud_api/platforms");

        String platformInstance = readFile(platformFile);
        StringEntity input = new StringEntity(platformInstance);
        input.setContentType("application/json");
        post.setEntity(input);
        HttpResponse response = client.execute(post);
        String sb = EntityUtils.toString(response.getEntity());
        log.info("||||||||||||||||||||||||||||||||||||||||");
        log.info("Received repsonse: " + sb);
        log.info("||||||||||||||||||||||||||||||||||||||||");
        Gson gson2 = new Gson();
        SimplePlatform result = gson2.fromJson(sb.toString(), SimplePlatform.class);
        log.info("Resource created! Id: " + result.getId());
        return result.getId();
    }

    private static String registerResource(String platformID, String resourceFile) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(HOST + "/cloud_api/platforms/" + platformID +"/resources");

        String resourceInstance = readFile(resourceFile);
        StringEntity input = new StringEntity(resourceInstance);
        input.setContentType("application/json");
        post.setEntity(input);
        HttpResponse response = client.execute(post);
        String sb = EntityUtils.toString(response.getEntity());
        log.info("||||||||||||||||||||||||||||||||||||||||");
        log.info("Received repsonse: " + sb.toString());
        log.info("||||||||||||||||||||||||||||||||||||||||");
        Gson gson2 = new Gson();
        SimpleSensor[] result = gson2.fromJson(sb.toString(), SimpleSensor[].class);
        log.info("Resource created! Id: " + result);
        return sb.toString();
    }

    private static String readFile(String filename) throws IOException {
        Path path = Paths.get(DATA_FOLDER + filename);
        log.info(path.toString());
        log.info(path.toAbsolutePath());
        return new String(Files.readAllBytes(path));
    }
}
