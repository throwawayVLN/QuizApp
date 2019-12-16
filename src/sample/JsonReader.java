package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// A very naive implementation of a JSON parser.
public class JsonReader {
    private static PushbackReader reader;

    // prevent the class from being initialized,
    // because the only public member of this class is static.
    private JsonReader() {}

    /**
     * reads the entire input from the file and parses it as json. It expects to get a
     * valid json string and does NOT throw exception if the input is not valid.
     *
     * @param file the file to read, it must contain a valid JSON-string
     * @return the parsed JSON-string in the form of Map<String, Object>
     * @throws IOException
     */
    public static Map<String, Object> parse(File file) throws IOException {
        reader = new PushbackReader(new BufferedReader(new FileReader(file)));
        return readObject();
    }

    /**
     * reads the entire input from the file and parses it as json. It expects to get a
     * valid json string and does NOT throw exception if the input is not valid.
     *
     * @param reader the reader to read input from, it must be a valid JSON-string.
     * @return the parsed JSON-string in the form of Map<String, Object>
     * @throws IOException
     */
    public static Map<String, Object> parse(Reader reader) throws IOException {
        reader = new PushbackReader(reader);
        return readObject();
    }

    /**
     * reads the entire input from the file and parses it as json. It expects to get a
     * valid json string and does NOT throw exception if the input is not valid.
     *
     * reads the entire input from the file and parses it as json
     * @param jsonString the string to parse, it must be valid JSON
     * @return the parsed JSON-string in the form of Map<String, Object>
     * @throws IOException
     */
    public static Map<String, Object> parse(String jsonString) throws IOException {
        reader = new PushbackReader(new StringReader(jsonString));
        return readObject();
    }

    /**
     * reads the entire input from the file and parses it as json. It expects to get a
     * valid json string and does NOT throw exception if the input is not valid.
     *
     * @param bufferedReader the BufferedReader to read from, it must be valid JSON
     * @return the parsed JSON-string in the form of Map<String, Object>
     * @throws IOException
     */
    public static Map<String, Object> parse(BufferedReader bufferedReader) throws IOException {
        reader = new PushbackReader(bufferedReader);
        return readObject();
    }

    private static Map<String, Object> readObject() throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        int c = 0;

        skipWhitespace();
        reader.read(); // reads '{'

        c = reader.read();
        // check if it is an empty object '{}'
        if ((char) c == '}') {
            return result;
        } else {
            reader.unread(c);
        }

        skipWhitespace();

        do {
            // read name
            skipWhitespace();
            String name = readString();
            skipWhitespace();

            reader.read(); // read ':'
            skipWhitespace();

            // read the value of the name
            Object value = readValue();
            skipWhitespace();

            if (value != null)
                result.put(name, value);
        } while ((char) (c = reader.read()) == ',');

        //System.out.println((char) reader.read()); // reader '}'

        return result;
    }

    private static ArrayList<Object> readArray() throws IOException {
        ArrayList<Object> result = new ArrayList<Object>();

        char c = 0;

        skipWhitespace();
        reader.read(); // read '['

        c = (char) reader.read();
        // check if it is an empty object '{}'
        if (c == '}') {
            return result;
        } else {
            reader.unread(c);
        }

        skipWhitespace();

        do {
            skipWhitespace();
            Object value = readValue();
            skipWhitespace();

            if (value != null)
               result.add(value);
        } while ((c =  (char) reader.read()) == ',');

        //System.out.println("Yo: " + c);
        //reader.read(); // read ']'

        return result;
    }

    private static Boolean readBoolean() throws IOException {
        skipWhitespace();
        char c = (char) reader.read();

        if (c == 'f') {
            reader.read(); // 'a'
            reader.read(); // 'l'
            reader.read(); // 's'
            reader.read(); // 'e'
            skipWhitespace();
            return Boolean.FALSE;
        } else {
            reader.read(); // 'r'
            reader.read(); // 'u'
            reader.read(); // 'e'
            skipWhitespace();
            return Boolean.TRUE;
        }
    }

    private static Integer readNumber() throws IOException {
        StringBuilder result = new StringBuilder();
        char c;
        skipWhitespace();
        while ((c = (char) reader.read()) != ',' && c != '}' && c != ']') {
            skipWhitespace();
            result.append(c);
            skipWhitespace();
        }

        reader.unread(c); // unread ',' or '}' or ']'

        return Integer.valueOf(result.toString());
    }

    private static String readString() throws IOException {
        skipWhitespace();
        char c = (char) reader.read(); // read '"'
        StringBuilder result = new StringBuilder();
        c = (char) reader.read();
        while (c != '"') {
            if (c == '\\') {
                result.append(readEscape());
            } else {
                result.append(c);
            }
            c = (char) reader.read();
        }
        return result.toString();
    }

    private static String readEscape() throws IOException {
        char c = (char) reader.read();
        StringBuilder result = new StringBuilder();
        switch (c) {
            case '"':
            case '/':
            case '\\':
                result.append(c);
                break;
            case 'b':
                result.append('\b');
            case 'n':
                result.append('\n');
                break;
            case 't':
                result.append('\t');
                break;
            default:
        }
        return result.toString();
    }

    private static Object readValue() throws IOException {
        int c = reader.read();
        Object result = null;
        switch ((char) c) {
            case 'f':
            case 't':
                reader.unread(c);
                result = readBoolean();
                break;
            case '"':
                reader.unread(c);
                result = readString();
                break;
            case '[':
                reader.unread(c);
                result = readArray();
                break;
            case '{':
                reader.unread(c);
                result = readObject();
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                reader.unread(c);
                result = readNumber();
                break;
            default:
                reader.unread(c);
        }
        return result;
    }

    private static void skipWhitespace() throws IOException {
        char c = (char) reader.read();
        while (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
            c = (char) reader.read();
        }
        reader.unread(c);
    }
}
