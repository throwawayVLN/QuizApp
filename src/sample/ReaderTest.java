package sample;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReaderTest {
    @Before
    public void setup() {

    }

    @Test
    public void parseInteger() throws IOException {
        // only one integer
        assertEquals(JsonReader.parse("{\n" +
                "   \"some-integer\": 123\n" +
                "}").get("some-integer"), 123);

        // multiple integers
        assertEquals(JsonReader.parse("{\n" +
                "   \"some-integer\": 123,\n" +
                "   \"another-integer\": 9123\n" +
                "}").get("another-integer"), 9123);
    }

    @Test
    void parseString() throws IOException {
        // only one string
        assertEquals(JsonReader.parse("{\n" +
                "   \"some-string\": \"hello world\"\n" +
                "}").get("some-string"), "hello world");

        // multiple strings
        assertEquals(JsonReader.parse("{\n" +
                "   \"some-string\": \"hello world\",\n" +
                "   \"another-string\": \"world\"\n" +
                "}").get("some-string"), "hello world");

        assertEquals(JsonReader.parse("{\n" +
                "   \"some-string\": \"hello world\",\n" +
                "   \"another-string\": \"world\"\n" +
                "}").get("another-string"), "world");
    }

    @Test
    public void parseEmptyString() throws IOException {
        assertEquals(JsonReader.parse("{\n" +
                "   \"empty-string\": \"\"\n" +
                "}").get("empty-string"), "");
    }

    @Test
    public void parseEmptyObject() throws IOException {
        assertEquals(JsonReader.parse("{}"),
                new HashMap<>());
    }

    @Test
    public void parseArrayOfInteger() throws IOException {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(123);

        // array with a single value
        assertEquals(JsonReader.parse("{\n" +
                "\"array\": [123] \n" +
                "}").get("array"), a);

        // array with multiple values
        a.add(234);
        assertEquals(JsonReader.parse("{\n" +
                "\"array\": [123, 234] \n" +
                "}").get("array"), a);
    }

    @Test
    void parseLargeArray() throws IOException {
        ArrayList<Integer> a = new ArrayList<>();
        a.clear();
        for (int i = 0; i < 1000; i++) {
            a.add(i);
        }
        final String input1 = "{ \"array\":" + a.toString() + "}";
        assertEquals(JsonReader.parse(input1).get("array"),
                a);

        // check with many empty arrays
        a.clear();
        ArrayList<ArrayList> arrayOfEmptyArrays = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            arrayOfEmptyArrays.add(new ArrayList<Integer>());
        }
        final String input2 = "{ \"array\":" + arrayOfEmptyArrays.toString() + "}";
        assertEquals(JsonReader.parse(input2).get("array"),
                arrayOfEmptyArrays);
    }

    @Test
    void parseArrayOfString() throws IOException {
        ArrayList<String> a = new ArrayList<>();
        a.add("I hate testing");
        a.add("asdf");

        assertEquals(JsonReader.parse("{\n" +
                "\"array\": [\"I hate testing\", \"asdf\"] \n" +
                "}").get("array"), a);
    }

    @Test
    void parseArrayOfBoolean() throws IOException {
        ArrayList<Boolean> a = new ArrayList<>();
        a.add(Boolean.FALSE);

        assertEquals(JsonReader.parse("{\n" +
                "\"array\": [false] \n" +
                "}").get("array"), a);
    }

    @Test
    void parseEmptyArray() throws IOException {
        ArrayList<Integer> a = new ArrayList<>();

        assertEquals(JsonReader.parse("{\n" +
                "\"array\": [] \n" +
                "}").get("array"), a);
    }

}
