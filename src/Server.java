import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o servidor de chat.
 */
public class Server {
    public static final int PORT = 4000;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();

    /**
     * Inicia o servidor.
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, Thread.currentThread().getName());
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao executar na porta " + PORT + ": " + e.getMessage());
        }
    }

    /**
     * Classe interna que manipula clientes conectados ao servidor.
     */
    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        /**
         * Construtor do manipulador de cliente.
         *
         * @param socket     Socket do cliente
         * @param clientName Nome do cliente
         */
        public ClientHandler(Socket socket, String clientName) {
            this.clientSocket = socket;
            this.clientName = clientName;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(clientName);
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                String msg;
                while ((msg = in.readLine()) != null) {
                    broadcast(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
            }
        }

        /**
         * Transmite uma mensagem para todos os clientes.
         *
         * @param message Mensagem a ser transmitida
         */
        private synchronized void broadcast(String message) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }

        /**
         * Envia uma mensagem para um cliente específico.
         *
         * @param message Mensagem a ser enviada
         */
        private void sendMessage(String message) {
            out.println(message);
        }
    }

    /**
     * Método de inicialização do servidor.
     *
     * @param args Argumentos de linha de comando (não utilizado)
     */
    public static void main(String[] args) {
        Server server = new Server();
        System.out.println("Inicializando o servidor");
        server.start();
    }
}
