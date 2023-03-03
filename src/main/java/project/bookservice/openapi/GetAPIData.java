package project.bookservice.openapi;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import project.bookservice.domain.book.Book;

import java.util.ArrayList;

public class GetAPIData {

    public ArrayList<Book> book = new ArrayList<Book>();

    public String getTagvalue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null) {
            return null;
        }
        return nValue.getNodeValue();
    }

    public void getApiData() {
        try {
            String url = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbnanovia31701002" +
                    "&QueryType=Bestseller" +
                    "&MaxResults=10" +
                    "&start=1" +
                    "&SearchTarget=Book" +
                    "&output=xml" +
                    "&Version=20131101";

            System.out.println(url);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            System.out.println(nList.getLength() + "개의 데이터 발견");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                Book b = null;

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    b = new Book(getTagvalue("title", eElement),
                            getTagvalue("description", eElement),
                            getTagvalue("cover", eElement));
                    book.add(b);
                    System.out.println("책 제목 : " + getTagvalue("title", eElement));
                    System.out.println("작가 : " + getTagvalue("author", eElement));
                    System.out.println("책 링크 : " + getTagvalue("link", eElement));
                    System.out.println("책 정보 : " + getTagvalue("description", eElement));
                    System.out.println("책 이미지 : " + getTagvalue("cover", eElement));
                    System.out.println();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        GetAPIData gad = new GetAPIData();
        gad.getApiData();

    }

}