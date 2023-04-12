package project.bookservice.openapi;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import project.bookservice.domain.book.Book;
import project.bookservice.repository.book.MyBatisBookRepository;
import project.bookservice.service.starRating.StarRatingService;

import java.util.ArrayList;

@RequiredArgsConstructor
public class ApiSearchBookList extends ConnectAPI implements APIParser {

    public ArrayList<Book> book = new ArrayList<Book>();

    private final StarRatingService starRatingService;

    public String getTagvalue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null) {
            return null;
        }
        return nValue.getNodeValue();
    }

    @Override
    public ArrayList<Book> jsonAndXmlParserToArr(String bookTitle) {
        ArrayList<Book> bookList = new ArrayList<Book>();

        String url = responseBodyAboutApi(bookTitle);

        try {
            System.out.println(url);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                MyBatisBookRepository bookRepository = new MyBatisBookRepository();

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String imageUrl = getTagvalue("cover", eElement);
                    String author = getTagvalue("author", eElement);
                    String buyUrl = getTagvalue("link", eElement);
                    String publisher = getTagvalue("publisher", eElement);
                    String description = getTagvalue("description", eElement);
                    String title = getTagvalue("title", eElement);
                    String pubDate = getTagvalue("pubDate", eElement);
                    String isbn = getTagvalue("isbn13", eElement);
                    Double avgStarRating = starRatingService.findAvgStarRating(isbn);

                    Book book = new Book(imageUrl, author, buyUrl, publisher, description, title, pubDate, isbn, avgStarRating);
                    bookList.add(book);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    @Override
    public String responseBodyAboutApi(String bookTitle) {
        // 조회하고 싶은 리스트 명
        //ItemNewAll : 신간 전체 리스트
        //ItemNewSpecial : 주목할 만한 신간 리스트
        //ItemEditorChoice : 편집자 추천 리스트 (카테고리로만 조회 가능 - 국내도서/음반/외서만 지원)
        //Bestseller : 베스트셀러
        //BlogBest : 블로거 베스트셀러 (국내도서만 조회 가능)

        String url = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbnanovia31701002" +
                "&QueryType=" + bookTitle +
                "&MaxResults=10" +
                "&start=1" +
                "&SearchTarget=Book" +
                "&output=xml" +
                "&Version=20131101";

        return url;
    }
}