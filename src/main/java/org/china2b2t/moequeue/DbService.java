package org.china2b2t.moequeue;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.china2b2t.moequeue.crypto.Cipher;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.china2b2t.moequeue.Main.cfg;

public class DbService {
    private MongoClient client;
    private MongoDatabase db;
    private final MongoCollection<Document> collection;

    public Hashtable<String, Boolean> data = new Hashtable<>();

    public DbService(MongoClient client) {
        MongoDatabase db = client.getDatabase(cfg.getString("database.database"));

        if (db.listCollectionNames().into(new ArrayList<>()).contains(cfg.getString("database.collection"))) {
            db.createCollection(cfg.getString("database.collection"));
        }
        this.collection = db.getCollection(cfg.getString("database.collection"));

        this.client = client;
        this.db = db;
    }

    /**
     * Get the hashed password by uuid
     *
     * @param uuid uuid of the player
     * @return hashed password of the player
     */
    public String getHashedPassword(String uuid) {
        AtomicReference<String> password = new AtomicReference<>("nil");
        
        FindIterable<Document> iterable = this.collection.find(Filters.eq("uuid", uuid));
        iterable.forEach((Block<Document>) document -> {
            password.set(document.get("password").toString());
        });
        return password.get();
    }

    /**
     * Check if the player has purchased
     *
     * @param uuid uuid of the player
     * @return has the player purchased
     */
    public boolean hasPurchased(String uuid) {
        AtomicReference<Long> expire = new AtomicReference<>(0L);

        FindIterable<Document> iterable = this.collection.find(Filters.eq("uuid", uuid));
        iterable.forEach((Block<Document>) document -> {
            expire.set(document.getLong("prior-expire"));
        });
        long tExpire = expire.get();

        return System.currentTimeMillis() < tExpire;
    }

    /**
     * register
     *
     * @param uuid uuid of the player
     * @param password password
     */
    public void register(String uuid, String password) {
        if (hasRegistered(uuid)) {
            return;
        }

        Cipher cipher = new Cipher();
        Random r = new Random();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String type = String.format("%d", r.nextInt(8));
        String registerDate = dataFormat.format(new Date());
        // String lastSeen = registerDate;
        String hashed = cipher.newHash(password, type);

        Document document = new Document("uuid", uuid);
        document.append("register-date", registerDate);
        // document.append("last-seen", lastSeen);
        document.append("hash-type", type);
        document.append("password", hashed);

        this.collection.insertOne(document);
    }

    /**
     * register for a premium player
     *
     * @param uuid uuid of the player
     */
    public void register(String uuid) {
        if (hasRegistered(uuid)) {
            return;
        }

        Cipher cipher = new Cipher();
        Random r = new Random();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String type = String.format("%d", r.nextInt(8));
        String registerDate = dataFormat.format(new Date());
        // String lastSeen = registerDate;
        // String hashed = cipher.newHash(password, type);

        Document document = new Document("uuid", uuid);
        document.append("register-date", registerDate);
        // document.append("last-seen", lastSeen);
        // document.append("password", hashed);

        this.collection.insertOne(document);
    }

    public void changePassword(String uuid, String password) {
        // TODO Finish this
    }

    /**
     * Attempt logging in
     *
     * @param uuid uuid of the player
     * @param password received password
     * @return succeed or not
     */
    public boolean login(String uuid, String password) {
        Cipher cipher = new Cipher();

        if (!hasRegistered(uuid)) {
            return false;
        }

        // Get hash type
        AtomicReference<String> hashType = new AtomicReference<>();

        FindIterable<Document> iterable = this.collection.find(Filters.eq("uuid", uuid));
        iterable.forEach((Block<Document>) document -> {
            hashType.set(document.getString("hash-type"));
        });

        return cipher.checkPassword(password, hashType.get(), uuid);
    }

    public boolean hasRegistered(String uuid) {
        FindIterable<Document> iterable = this.collection.find(Filters.eq("uuid", uuid));
        return iterable.iterator().hasNext();
    }

    public boolean hasLoggedIn(String uuid) {
        return data.containsKey(uuid) ? data.get(uuid) : false;
    }
}
