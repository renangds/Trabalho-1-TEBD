import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String...args) throws IOException {
        int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        Socket client = new Socket("127.0.0.1", 5050);

        //String getMatricula = new Scanner(System.in).nextLine();

        System.out.println("Cliente " + client.getInetAddress().getHostAddress() + " sendo enviada");

        //Sending request to server

        File requisition = new File("submeter.xml");
        FileInputStream in = new FileInputStream(requisition);

        OutputStream out = client.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(out);

        int lidos = -1;

        while((lidos = in.read(buffer, 0, bufferSize)) != -1){
            out.write(buffer, 0, lidos);
        }

        out.flush();

        System.out.println("Mensagem enviada!");

        //Receive response from server

        InputStream is = client.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);

        DataInputStream dis = new DataInputStream(client.getInputStream());

        BufferedReader br = new BufferedReader(isr);

        File fileReceiver = new File("resposta-from-server.xml");
        FileOutputStream fos = new FileOutputStream(fileReceiver);

        lidos = -1;

        fos.flush();

        if(dis.available() == 0){
            System.out.println("Não há dados a serem recebidos");
        } else{
            while((lidos = dis.read(buffer, 0, bufferSize)) != -1){
                System.out.println(lidos);
                fos.write(buffer, 0, lidos);
            }

            System.out.println("Histórico recebido!");
        }

        client.close();

        System.out.println("Conexão terminada");
    }
}
