import org.junit.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class Validations {
    private DocumentBuilder builder;
    private Document doc;
    private Document docResponse;
    private Document docConsulta;

    @Before
    public void initTests() throws SAXException, IOException, ParserConfigurationException {
        this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        this.doc = this.builder.parse(new File("submeter.xml"));
        this.docResponse = this.builder.parse(new File("resposta-ex.xml"));
        this.docConsulta = this.builder.parse(new File("submeter.xml"));

        this.doc.getDocumentElement().normalize();
    }

    @Test
    public void lerXmlNovo(){
        NodeList nodeList = this.doc.getElementsByTagName("metodo");
        Node first = nodeList.item(0);

        System.out.println(this.doc.getElementsByTagName("nome").item(0).getTextContent());

        assertEquals("submeter", this.doc.getElementsByTagName("nome").item(0).getTextContent());
    }

    //Inserir um valor na resposta
    @Test
    public void insertValueInXmlResponse() throws TransformerException {
        Node rootData = this.docResponse.getFirstChild();
        Node data = this.docResponse.getElementsByTagName("retorno").item(0);
        data.setTextContent("3");

        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(this.docResponse);
        StreamResult result = new StreamResult(new File("resposta-ex.xml"));
        transformer.transform(source, result);

        assertEquals("3", this.docResponse.getElementsByTagName("retorno").item(0).getTextContent());
    }

    @Test
    public void getCandidateData(){
        String cpf = this.doc.getElementsByTagName("cpf").item(0).getTextContent();
        String nome = this.doc.getElementsByTagName("aluno").item(0).getTextContent();

        assertEquals(cpf, "00000000002");
        assertEquals(nome, "Lívia de Azevedo da Silva");

        String cr = this.doc.getElementsByTagName("crPeriodo").item(0).getTextContent();

        assertEquals(cr, "8.45");
    }

}