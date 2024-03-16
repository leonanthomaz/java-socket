import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static final int PORT = 4000;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>(); // Lista de manipuladores de clientes

    public void start() {
        try {
            System.out.println("Conectando ao servidor");
            serverSocket = new ServerSocket(PORT); // Criação do socket do servidor
            System.out.println("Servidor em espera: " + serverSocket);
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Aceita conexões de clientes
                System.out.println("Socket Connection: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket, Thread.currentThread().getName());
                clients.add(clientHandler); // Adiciona o manipulador de cliente à lista de clientes
                clientHandler.start(); // Inicia a thread do manipulador de cliente
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao executar na porta " + PORT + ": " + e.getMessage());
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName; // Adicionando o nome do cliente

        public ClientHandler(Socket socket, String clientName) {
            this.clientSocket = socket; // Inicializa o socket do cliente
            this.clientName = clientName; // Definindo o nome do cliente ao criar o manipulador de cliente
        }

        @Override
        public void run() {
            Thread.currentThread().setName(clientName); // Definindo o nome da thread como o nome do cliente
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Cria um leitor de entrada para o cliente
                out = new PrintWriter(clientSocket.getOutputStream(), true); // Cria um escritor de saída para o cliente
                String msg;
                while ((msg = in.readLine()) != null) { // Lê mensagens do cliente
                    System.out.println("Mensagem do cliente: " + Thread.currentThread().getName() + " >> " + clientSocket.getRemoteSocketAddress() + " -> " + msg);
                    broadcast(msg); // Repassa a mensagem para todos os outros clientes
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close(); // Fecha o socket do cliente
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this); // Remove este manipulador de cliente da lista de clientes
            }
        }

        private synchronized void broadcast(String message) {
            for (ClientHandler client : clients) { // Para cada cliente na lista de clientes
                client.sendMessage(message); // Envia a mensagem
            }
        }

        private void sendMessage(String message) {
            out.println(message); // Envia a mensagem para o cliente
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        System.out.println("Inicializando o servidor");
        server.start(); // Inicia o servidor
    }
}
