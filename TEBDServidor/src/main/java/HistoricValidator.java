import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class HistoricValidator {
    public static int validXML(String xml) throws SAXException{
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        File schemaLocation = new File("historico.xsd");
        Schema schema;

        schema = factory.newSchema(schemaLocation);

        Validator validator = schema.newValidator();

        File historico = new File(xml);

        Source source = new StreamSource(historico);

        try{
            validator.validate(source);
            System.out.println(historico.getName() + " é válido");

            return 0;
        } catch (SAXException e){
            System.err.println(e);
            return 1;
        } catch (IOException e){
            System.err.println(e);
            return 2;
        } catch(RuntimeException e){
            System.out.println(e);
            return 3;
        }
    }

    public static int validRequestXML(String XML) throws SAXException{
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        File schemaLocation = new File("requisicao.xsd");
        Schema schema;

        schema = factory.newSchema(schemaLocation);

        Validator validator = schema.newValidator();

        File historico = new File(XML);

        Source source = new StreamSource(historico);

        try{
            validator.validate(source);

            return 0;
        } catch(SAXException e){
            return 3;
        } catch(IOException e){
            return 3;
        }
    }
}
