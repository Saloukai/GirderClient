import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.lang.model.type.NullType;
import java.io.*;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

public class GirderClient {

    /************
    * Variables *
    ************/
    private String girderApiKey;
    private String girderToken;
    private String baseURL;
    private String idUserGirder;
    private OkHttpClient client;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final JSONParser parser = new JSONParser();

    /******************************************************************************************************************/

    /**************
    * Constructor *
    **************/
    public GirderClient(String girderApiKey){
        this.girderApiKey = girderApiKey;
        this.client = new OkHttpClient();
        this.baseURL = "http://localhost:8080/api/v1/";

        try {
            Object obj = parser.parse(this.getSessionToken());
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject token = (JSONObject) jsonObject.get("authToken");
            JSONObject user = (JSONObject) jsonObject.get("user");

            this.girderToken = token.get("token").toString();
            this.idUserGirder = user.get("_id").toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /******************************************************************************************************************/

    /********************
    * Getter and setter *
    ********************/

    // Get api key
    public String getGirderApiKey() {
        return girderApiKey;
    }

    // Set api key
    public void setGirderApiKey(String girderApiKey) {
        this.girderApiKey = girderApiKey;
    }

    // Get Girder token
    public String getGirderToken() {
        return this.girderToken;
    }

    // Set Girder token
    public void setGirderToken(String girderToken) {
        this.girderToken = girderToken;
    }

    // Get baseURL
    public String getBaseURL() {
        return baseURL;
    }

    // Set baseURL
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    // Get idUserGirder
    public String getIdUserGirder() {
        return idUserGirder;
    }

    // Set idUserGirder
    public void setIdUserGirder(String idUserGirder) {
        this.idUserGirder = idUserGirder;
    }

    /******************************************************************************************************************/

    /************
    * Functions *
    ************/

    /**
     * [POST] Send a post request
     *
     * @param url
     * @param data
     * @return String response
     * @throws IOException
     */
    public String post(String url, Hashtable<String, String> data) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        Set<String> keys = data.keySet();

        for (String k: keys) {
            builder.addFormDataPart(k, data.get(k));
        }

        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(this.baseURL + url)
                .post(body)
                .build();

        try (Response response = this.client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    // Method GET
    public byte[] getBytes(String url) throws IOException {
        Request request = new Request.Builder()
                .url(this.baseURL + url)
                .addHeader("Girder-Token", this.girderToken)
                .build();

        System.out.println(request.url());

        try (Response response = this.client.newCall(request).execute()) {
            return response.body().bytes();
        }
    }

    // Method GET
    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(this.baseURL + url)
                .addHeader("Girder-Token", this.girderToken)
                .build();

        try (Response response = this.client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * [POST] Create a new session token
     *
     * @return String token
     * @throws IOException
     */
    public String getSessionToken() throws IOException {

        Hashtable<String, String> data = new Hashtable<String, String>();
        data.put("key", this.girderApiKey);

        return this.post("api_key/token", data);
    }

    /**
     * [GET] Download file from Girder
     *
     * @param fileId
     * @throws IOException
     */
    public void downloadFile(String fileId) throws IOException {

        Random rand = new Random();
        int n = rand.nextInt(50) + 1;

        File targetFile = new File("src/test"+n+".txt");
        byte[] fileData = null;
        fileData = getBytes("file/" + fileId + "/download");

        OutputStream outputStream = new FileOutputStream(targetFile);
        outputStream.write(fileData);

        //responseBody.close();
        outputStream.close();

        return ;

        // Dans Grida on veut seulement retourner le tableau de bytes
        // Pour cela changer le void en byte[] et decommenter la ligne 193
        //return fileData;
    }


    /**
     * Main
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        GirderClient girderClient = new GirderClient("NGuWYueyMVHY2mRwuqmZTfMhRQ4u5m9c5PBUjAAo");

        // Download image
        girderClient.downloadFile("5ae2ea083712101d8f85e268");

        // Download text
        girderClient.downloadFile("5b1e90353712103699e0b678");
    }
}
