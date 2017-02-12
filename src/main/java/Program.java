/**
 * Created by huynq on 2/12/17.
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import databases.Task;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import static spark.Spark.*;

public class Program {
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        get("/hello", (req, res) -> "Hello Heroku World");


        final Morphia morphia = new Morphia();

        morphia.mapPackage("databases");

        MongoClientURI uri  = new MongoClientURI("mongodb://admin:admin@ds013206.mlab.com:13206/spark-task");
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());

        final Datastore datastore = morphia.createDatastore(client, "spark-task");
        datastore.ensureIndexes();

        Task task = new Task("Haha");
        datastore.save(task);

        System.out.printf("Here");

        for (Task t : datastore.createQuery(Task.class)) {
            System.out.printf(t.toString());
        }
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
