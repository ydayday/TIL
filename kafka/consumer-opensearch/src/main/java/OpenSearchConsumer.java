import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class OpenSearchConsumer {

    private static final Logger log = LoggerFactory.getLogger(OpenSearchConsumer.class);

    public static RestHighLevelClient createOpenSearchClient() {

        RestHighLevelClient restHighLevelClient;
        URI connUri = URI.create("https://xs9obk4uqa:kqyuza5l5f@kafka-course-7636421393.us-east-1.bonsaisearch.net:443");
        String userInfo = connUri.getUserInfo();


        if (userInfo == null) {
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme())));
        } else {
            String[] auth = userInfo.split(":");

            CredentialsProvider cp = new BasicCredentialsProvider();
            cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme()))
                            .setHttpClientConfigCallback(
                                    httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(cp)
                                            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                            )
            );
        }
        return restHighLevelClient;
    }

    public static void main(String[] args) throws IOException {

        RestHighLevelClient openSearchClient = createOpenSearchClient();

        try(openSearchClient) {
            boolean isIndex = openSearchClient.indices().exists(new GetIndexRequest("wikimedia"), RequestOptions.DEFAULT);

            if (!isIndex) {
                CreateIndexRequest wikimedia = new CreateIndexRequest("wikimedia");
                openSearchClient.indices().create(wikimedia, RequestOptions.DEFAULT);
                log.info("created idx");
            } else {
                log.info("already idx");
            }

        }


    }
}
