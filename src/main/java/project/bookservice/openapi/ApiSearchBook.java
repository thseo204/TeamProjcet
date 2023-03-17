package project.bookservice.openapi;

// 네이버 검색 API 예제 - 블로그 검색
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.book.Book;
import project.bookservice.repository.book.MybatisBookRepository;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.String;
@Getter
@Repository
public class ApiSearchBook extends ConnectAPI implements APIParser{

//    private ArrayList<JSONObject> jsonObjectArrayList;


    @Override
    public ArrayList<Book> jsonAndXmlParserToArr(String bookTitle) throws ParseException {
        ArrayList<Book> bookList = new ArrayList<Book>();
        String responseBody = responseBodyAboutApi(bookTitle);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject)parser.parse(responseBody);
        JSONArray itemArr = (JSONArray) jsonObject.get("items");

//        System.out.println("itemArr.size() = " + itemArr.size());

        
        for(int i = 0; i < itemArr.size(); i++){

            MybatisBookRepository bookRepository = new MybatisBookRepository();

            JSONObject jsonObject1 = (JSONObject)itemArr.get(i);
            String imageUrl = (String)jsonObject1.get("image"); // 이미지 링크
            String author = (String)jsonObject1.get("author"); // 저자
            String buyUrl = (String)jsonObject1.get("link"); // 구매 링크
            String publisher = (String)jsonObject1.get("publisher"); // 출판사
            String description = (String)jsonObject1.get("description");
            String title = (String)jsonObject1.get("title");
            String pubDate = (String)jsonObject1.get("pubdate"); // 출판연도
            String isbn = (String)jsonObject1.get("isbn"); // key값
            Book book = new Book(imageUrl, author, buyUrl, publisher, description, title, pubDate, isbn);
            bookRepository.save(book);
            bookList.add(book);
        }
//        object = (JSONObject) itemArr.get(0);
//        String jsonString = jsonObject.toJSONString();
        return bookList;
    }

    @Override
    public String responseBodyAboutApi(String bookTitle) {
        String clientId = "6Oj2iCxiyNiG08IN5DMS"; //애플리케이션 클라이언트 아이디
        String clientSecret = "l_PHjfyI90"; //애플리케이션 클라이언트 시크릿


        String text = null;
        try {
            text = URLEncoder.encode(bookTitle, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }


        String apiURL = "https://openapi.naver.com/v1/search/book?query=" + text;    // JSON 결과
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // XML 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);

//        System.out.println(responseBody);
        return responseBody;
    }

}
