package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // pick appropriate instance variables for this class
    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    private final Map<String, Map<String, String>> countrylanguagemap = new HashMap<>();

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     *
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */

    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            // use the data in the jsonArray to populate your instance variables
            // Note: this will likely be one of the most substantial pieces of code you write in this lab.
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String alpha3code = jsonObject.getString("alpha3");
                Map<String, String> langaugemap = new HashMap<>();
                for (String key : jsonObject.keySet()) {
                    if (!"id".equals(key) && !"alpha2".equals(key) && !"alpha3".equals(key)) {
                        langaugemap.put(key, jsonObject.getString(key));
                    }
                }
                countrylanguagemap.put(alpha3code, langaugemap);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        for (String key : countrylanguagemap.keySet()) {
            if (key.equals(country)) {
                return new ArrayList<>(countrylanguagemap.get(key).keySet());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        // return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        return new ArrayList<>(countrylanguagemap.keySet());
    }

    @Override
    public String translate(String country, String language) {
        // Task: complete this method using your instance variables as needed
        for (String key : countrylanguagemap.keySet()) {
            if (key.equals(country)) {
                return countrylanguagemap.get(key).get(language);
            }
        }
        return "Translation failed.";
    }
}
