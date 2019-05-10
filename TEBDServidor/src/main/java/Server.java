import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int porta;
    private ServerSocket server;
    private CandidatoDAO candidatos;
    private static Server singletonServer = null;


    public static void main(String...args) throws IOException{
        Server servidor = getInstanceServer(5050);
    }

    public Server(int porta) throws IOException {
        this.porta = porta;
        this.candidatos = new CandidatoDAO();

        try{
            this.server = new ServerSocket(this.porta);
        } catch(IOException e){
            System.err.println(e);
        }

        System.out.println("Porta do servidor " + server.getInetAddress().getHostAddress() + " aberta");

        this.execute();
    }

    public static Server getInstanceServer(int porta){
        if(singletonServer == null){
            try{
                singletonServer = new Server(porta);
            } catch(IOException e){
                System.err.println(e);
            }
        }

        return singletonServer;
    }

    private void newCandidatosDAO(){
        this.candidatos = new CandidatoDAO();
    }

    public CandidatoDAO getCandidatos(){
        return this.candidatos;
    }

    private void execute(){
        Socket clientSocket;

        while(true){
            try{
                System.out.println("Aguardando cliente...");

                clientSocket = this.server.accept();

                System.out.println("Cliente está conectado");

                Client trataCliente = new Client(clientSocket, this.candidatos);

                new Thread(trataCliente).run();

                System.out.println("Execução concluída");
            } catch(IOException e){
                System.err.println(e);
            }
        }
    }

    private static class Client implements Runnable{
        private Socket client;
        private final int bufferSize = 4096;
        private final byte[] buffer = new byte[bufferSize];
        private CandidatoDAO listaCandidatos;

        private DocumentBuilder builder;
        private Document doc;
        private Document docResponse;
        private Document docHistorico;

        Client(Socket clientSocket, CandidatoDAO listaCandidatos){
            this.client = clientSocket;
            this.listaCandidatos = listaCandidatos;
        }

        private void gerarXmlResponse(int resposta) throws TransformerException, IOException, SAXException{
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            this.docResponse = this.builder.parse(new File("resposta-ex.xml"));
            this.docResponse.normalize();

            Node rootData = this.docResponse.getFirstChild();
            Node data = this.docResponse.getElementsByTagName("retorno").item(0);
            data.setTextContent(String.valueOf(resposta));

            DOMSource requestSource = new DOMSource(this.docResponse);
            StreamResult requestResult = new StreamResult(new File("response-server.xml"));
            transformer.transform(requestSource, requestResult);
        }

        private void sendResponse() throws IOException{
            OutputStream os = this.client.getOutputStream();

            int lidos = -1;

            FileInputStream inFile = new FileInputStream("response-server.xml");

            lidos = inFile.read(this.buffer, 0, this.bufferSize);
            os.write(this.buffer, 0, lidos);
        }

        public void run(){
            try{
                InputStream in = this.client.getInputStream();
                File arquivo = new File("requisicao_nova.xml");
                FileOutputStream out = new FileOutputStream(arquivo);

                int lidos = -1;

                //Recebe os dados da requisição
                lidos = in.read(buffer, 0, bufferSize);
                out.write(buffer, 0 ,lidos);

                //Inicialização dos construtores do XML
                this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                this.doc = this.builder.parse(new File("requisicao_nova.xml"));
                this.doc.getDocumentElement().normalize();

                HistoricValidator.validRequestXML("requisicao_nova.xml");

                String metodo = this.doc.getElementsByTagName("nome").item(0).getTextContent();

                System.out.println("Método " + metodo + " será executado");

                if(metodo.equals("submeter")){
                    try{
                        NodeList node = this.doc.getElementsByTagName("valor");
                        Node first = node.item(0);

                        InputSource is = new InputSource();
                        is.setCharacterStream(new StringReader(first.getTextContent()));

                        Document historico = this.builder.parse(is);

                        DOMSource source = new DOMSource(historico);
                        StreamResult result = new StreamResult(new File("historico_novo.xml"));

                        TransformerFactory transformerFactory = TransformerFactory
                                .newInstance();
                        Transformer transformer = transformerFactory.newTransformer();

                        transformer.transform(source, result);

                        HistoricValidator.validXML("historico_novo.xml");

                        this.docHistorico = this.builder.parse(new File("historico_novo.xml"));
                        this.docHistorico.normalize();

                        String cpf = this.docHistorico.getElementsByTagName("cpf").item(0).getTextContent();
                        String nome = this.docHistorico.getElementsByTagName("nome").item(0).getTextContent();
                        String cr = this.docHistorico.getElementsByTagName("crMedio").item(0).getTextContent();

                        Node universidadeData = this.docHistorico.getElementsByTagName("universidade").item(0);
                        NodeList uniDataList = universidadeData.getChildNodes();

                        String nomeUniversidade = uniDataList.item(1).getTextContent();

                        int search = this.listaCandidatos.addCandidato(nome, nomeUniversidade, cpf, Float.parseFloat(cr));

                        this.gerarXmlResponse(search);

                        this.sendResponse();
                    } catch(TransformerException e){
                        System.err.println(e);
                    }
                } else if (metodo.equals("consultaStatus")){
                    try{
                        System.out.println("Dados serão consultados");

                        String cpf = this.doc.getElementsByTagName("valor").item(0).getTextContent();

                        int valor = this.listaCandidatos.buscaCandidato(cpf);

                        this.gerarXmlResponse(valor);

                        this.sendResponse();
                    } catch(TransformerException e){
                        System.out.println(e);
                    } catch(SAXParseException e){
                        System.err.println(e);
                    }
                } else{
                    try{
                        this.gerarXmlResponse(-1);
                        this.sendResponse();
                    } catch(TransformerException e){
                        System.out.println(e);
                    }
                }
            } catch(IOException e){
                try{
                    this.gerarXmlResponse(3);
                } catch(TransformerException i){

                } catch(IOException i){

                } catch(SAXException i){

                }
                System.err.println(e);
            } catch(ParserConfigurationException e){
                System.err.println(e);
            } catch(SAXException e){
                try{
                    this.gerarXmlResponse(2);
                } catch(TransformerException i){

                } catch(IOException i){

                } catch(SAXException i){

                }
                System.err.println(e);
            }
        }
    }
}
