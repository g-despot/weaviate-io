package io.weaviate.docs.quickstart_local;

// START Import
import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.batch.api.ObjectsBatcher;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Import {
  public static void main(String[] args) throws Exception {
    Config config = new Config("http", "localhost:8080");  // Replace with your Weaviate endpoint

    WeaviateClient client = new WeaviateClient(config);

    // Get JSON data
    URL url = new URL("https://raw.githubusercontent.com/weaviate-tutorials/quickstart/main/data/jeopardy_tiny.json");
    String jsonData = new BufferedReader(new InputStreamReader(((HttpURLConnection) url.openConnection()).getInputStream()))
      .lines().reduce("", String::concat);

    // highlight-start
    // Create and execute batch
    ObjectsBatcher batcher = client.batch().objectsBatcher();

    new JSONArray(jsonData).forEach(item -> {
      JSONObject json = (JSONObject) item;
      HashMap<String, Object> properties = new HashMap<>();
      properties.put("category", json.getString("Category"));
      properties.put("question", json.getString("Question"));
      properties.put("answer", json.getString("Answer"));

      batcher.withObject(WeaviateObject.builder()
        .className("Question")
        .properties(properties)
        .build());
    });

    // Flush
    batcher.run();
    // highlight-end
  }
}
// END Import
