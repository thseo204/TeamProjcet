package project.bookservice.openapi;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import project.bookservice.domain.book.Book;

import java.util.ArrayList;

public interface APIParser {
    ArrayList<Book> jsonAndXmlParserToArr(String bookTitle) throws ParseException;
    String responseBodyAboutApi(String bookTitle);

}
