import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.validation.Schema;
import java.io.File;
import java.io.IOException;

public class HistoricValidator {
    public static void validXML(String xml) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        File schemaLocation = new File("historico.xsd");
        Schema schema;

        schema = factory.newSchema(schemaLocation);

        Validator validator = schema.newValidator();

        File historico = new File(xml);

        Source source = new StreamSource(historico);

        validator.validate(source);
        System.out.println(historico.getName() + " é válido");
    }

    public static void validRequestXML(String XML) throws SAXException, IOException{
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        File schemaLocation = new File("requisicao.xsd");
        Schema schema;

        schema = factory.newSchema(schemaLocation);

        Validator validator = schema.newValidator();

        File historico = new File(XML);

        Source source = new StreamSource(historico);
        validator.validate(source);
    }
}
