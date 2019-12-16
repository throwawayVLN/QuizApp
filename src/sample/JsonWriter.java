package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class JsonWriter {
    private File file;
    private Map<String, Object> jsonObject;

    /**
     * parses a the json-object given in the form of Map<String, Object> to a JSON string and
     * writes it to the file.
     *
     * @param file the file to write the json-string from parsing the jsonObject
     * @param jsonObject the json-object to parse
     * @throws IOException
     */
    public JsonWriter(File file, Map<String, Object> jsonObject) throws IOException {
        this.file = file;
        this.jsonObject = jsonObject;
        update();
    }

    /**
     * updates the JSON object given to it, and then updates the content of the file.
     *
     * @param jsonObject the new value of the json-object
     * @return itself
     * @throws IOException
     */
    public JsonWriter updateJsonObject(Map<String, Object> jsonObject) throws IOException {
        this.jsonObject = jsonObject;
        update();
        return this;
    }

    /**
     * updates the file based on the current value of its json-object
     *
     * @throws IOException
     */
    public void update() throws IOException {
        String jsonString = objectToString(jsonObject);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(jsonString);
        writer.close();
    }

    private static String arrayToString(ArrayList<Object> array) {
        StringBuilder result = new StringBuilder();

        result.append("[\n");

        for (Object item: array) {
            if (item != null)
                result.append(valueToString(item));
        }

        if (result.toString().contains(",")) {
            result.deleteCharAt(result.lastIndexOf(","));
        }
        result.append("]\n");

        return result.toString();
    }

    private static String objectToString(Map<String, Object> object) {
        StringBuilder result = new StringBuilder();

        result.append("{\n");

        for (String name: object.keySet()) {
            result.append('"' + name + '"' + " : ");
            Object item = object.get(name);
            result.append(valueToString(item));
        }

        if (result.toString().contains(",")) {
            result.deleteCharAt(result.lastIndexOf(","));
        }        result.append("}\n");

        return result.toString();
    }

    private static String valueToString(Object item) {
        StringBuilder result = new StringBuilder();
        if (item instanceof ArrayList) {
            result.append(arrayToString((ArrayList<Object>) item));
        } else if (item instanceof Map) {
            result.append(objectToString((Map<String, Object>) item));
        } else if (item instanceof String) {
            result.append('"');
            result.append(item);
            result.append('"');
        } else if (item instanceof Integer) {
            result.append(item.toString());
        } else {
            if (item.equals(Boolean.FALSE)) {
                result.append("false");
            } else {
                result.append("true");
            }
        }
        result.append(",\n");
        return result.toString();
    }
}
