import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe que representa um cliente do chat.
 */
public class Client02 {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 4000;
    private Socket clientSocket;
    private PrintWriter out;
    private Scanner scanner;
    private String clientName = "Aguardando cliente...";

    /**
     * Construtor do cliente.
     *
     * @param clientName Nome do cliente
     */
    public Client02(String clientName) {
        this.clientName = clientName;
        scanner = new Scanner(System.in);
    }

    /**
     * Inicia o cliente.
     */
    public void start() {
        try {
            System.out.println("Cliente conectando...");
            clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectou na porta " + SERVER_PORT + " com sucesso!");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println("Cliente conectado: " + clientSocket);
            Thread.currentThread().setName(clientName);
            messageLoop();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao conectar com o servidor: " + e.getMessage());
        }
    }

    /**
     * Loop para enviar mensagens para o servidor.
     */
    private void messageLoop() {
        try {
            String msg;
            do {
                System.out.println("Digite uma mensagem (ou 'sair' para encerrar)");
                msg = scanner.nextLine();
                out.println(msg);
                System.out.println("Mensagem do cliente 1: " + msg);
            } while (!msg.equalsIgnoreCase("sair"));
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Erro durante a comunicação com o servidor: " + e.getMessage());
        }
    }

    /**
     * Método de inicialização do cliente.
     *
     * @param args Argumentos de linha de comando (não utilizado)
     */
    public static void main(String[] args) {
        Client02 client = new Client02(Thread.currentThread().getName());
        System.out.println("Inicializando o cliente");
        client.start();
    }
}
