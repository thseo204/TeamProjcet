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
public class ApiSearchBookList extends ConnectAPI implements APIParser{

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

    public void getApiData() {

    }

    @Override
    public ArrayList<Book> jsonAndXmlParserToArr(String bookTitle) throws ParseException {
        ArrayList<Book> bookList = new ArrayList<Book>();

        String url = responseBodyAboutApi(bookTitle);

        try {
            System.out.println(url);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

//            System.out.println(nList.getLength() + "개의 데이터 발견");

            

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                MyBatisBookRepository bookRepository = new MyBatisBookRepository();

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
//                    System.out.println(eElement.toString());
//                    System.out.println();
//                    System.out.println("책 이미지 : " + getTagvalue("cover", eElement));
//                    System.out.println("작가 : " + getTagvalue("author", eElement));
//                    System.out.println("책 링크 : " + getTagvalue("link", eElement));
//                    System.out.println("책 출판사 : " + getTagvalue("publisher", eElement));
//                    System.out.println("책 정보 : " + getTagvalue("description", eElement));
//                    System.out.println("책 제목 : " + getTagvalue("title", eElement));
//                    System.out.println("책 출판년도 : " + getTagvalue("pubDate", eElement));
//                    System.out.println();

                    String imageUrl = getTagvalue("cover", eElement);
                    String author = getTagvalue("author", eElement);
                    String buyUrl = getTagvalue("link", eElement);
                    String publisher = getTagvalue("publisher", eElement);
                    String description = getTagvalue("description", eElement);
                    String title = getTagvalue("title", eElement);
                    String pubDate = getTagvalue("pubDate", eElement);
                    String isbn = getTagvalue("isbn13", eElement);
                    Double avgStarRating = starRatingService.findAvgStarRating(isbn);
            
                    Book book = new Book(imageUrl, author, buyUrl, publisher, description, title, pubDate, isbn,avgStarRating);
//                    bookRepository.save(book);
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