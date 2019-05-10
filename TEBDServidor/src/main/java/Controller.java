import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Controller {
    private CandidatoDAO candidatoDAO;

    //Atributos para manipulação do XML
    private DocumentBuilder builder;
    private Document doc;

    public Controller(){
        this.candidatoDAO = new CandidatoDAO();
    }

    //O atributo usado na implementação original será um obj. socket
    public int request(Socket client, byte[] buffer, int bufferSize) {
        try {
            InputStream in = client.getInputStream();
            File arquivo = new File("requisicao.xml");
            FileOutputStream out = new FileOutputStream(arquivo);

            int lidos = -1;

            //Recebe a requisição
            lidos = in.read(buffer, 0, bufferSize);
            out.write(buffer, 0, lidos);

            //Variáveis da validação e extração do XML
            this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.doc = this.builder.parse(new File("requisicao.xml"));
            this.doc.getDocumentElement().normalize();

            NodeList nodeList = this.doc.getElementsByTagName("requisicao");
            Node first = nodeList.item(0);

            //String contendo o nome da requisição
            String solicitacao = doc.getElementsByTagName("nome").item(0).getTextContent();

            if (solicitacao.equals("submeter")) {

                return 1;
            } else if (solicitacao.equals("consultaStatus")) {
                String cpf = doc.getElementsByTagName("cpf").item(0).getTextContent();
                int resultado = this.situacaoCandidato(cpf);

                if (resultado == -1) {

                } else {
                    //Resposta para o socket com a situação do candidato

                    return 1;
                }
            } else {

            }
        } catch (IOException e) {
            System.err.println(e);
        } catch (ParserConfigurationException e) {
            System.err.println(e);
        } catch (SAXException e) {
            System.err.println(e);
        }

        return 0;
    }



    public int situacaoCandidato(String cpf){
        return this.candidatoDAO.buscaCandidato(cpf);
    }

    public int realizarInscricao(String nome, String matricula, String cpf, float cr){
        return this.candidatoDAO.addCandidato(nome, matricula, cpf, cr);
    }
}
