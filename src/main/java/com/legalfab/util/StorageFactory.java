package com.legalfab.util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collection;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;

public class StorageFactory
{

    private static Storage instance = null;

    public static synchronized Storage getService () throws IOException, GeneralSecurityException
    {
        if ( instance == null )
        {
            instance = buildService ();
        }
        return instance;
    }

    private static Storage buildService () throws IOException, GeneralSecurityException
    {

        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport ();
        JsonFactory jsonFactory = new JacksonFactory ();

        InputStream credentialsJSON = StorageFactory.class.getClassLoader().getResourceAsStream(Const.JSON_KEY_FILE);
	    JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	    GoogleCredential credential = GoogleCredential.fromStream(credentialsJSON, httpTransport, JSON_FACTORY);

        if ( credential.createScopedRequired () )
        {
            Collection<String> scopes = StorageScopes.all ();
            credential = credential.createScoped ( scopes );
        }

        return new Storage.Builder ( transport, jsonFactory, credential ).setApplicationName (Const.PROJECT_ID ).build ();
    }
}