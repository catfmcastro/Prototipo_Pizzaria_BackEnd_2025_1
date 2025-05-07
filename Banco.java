package backend;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.Properties;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Banco {

    static class Conexao {
        final String url = "jdbc:postgresql://ep-holy-cherry-a81euyf1-pooler.eastus2.azure.neon.tech/neondb?sslmode=require";
        final Properties props = new Properties();
        private Connection connection;

        public Conexao() {
            props.setProperty("user", "neondb_owner");
            props.setProperty("password", "npg_3EpMu1XgjGoY");
        }

        public Connection getConexao() throws SQLException {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName("org.postgresql.Driver"); // Explicitly load the PostgreSQL driver
                } catch (ClassNotFoundException e) {
                    throw new SQLException("PostgreSQL JDBC Driver not found.", e);
                }
                connection = DriverManager.getConnection(url, props);
            }
            return connection;
        }

        public void fecharConexao() throws SQLException {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    private static final Conexao conexao = new Conexao();

    public static void main(String[] args) throws IOException, SQLException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/usuario", new UsuarioHandler());
        server.createContext("/endereco", new EnderecoHandler());
        server.createContext("/pedido", new PedidoHandler());
        server.createContext("/item", new ItemHandler());
        server.createContext("/pagamento", new PagamentoHandler());
        server.setExecutor(null); // Usa o executor padrão
        System.out.println("Servidor iniciado na porta 8080...");
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                conexao.fecharConexao();
                System.out.println("Conexão com o banco de dados encerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
    }
    static class PagamentoHandler extends BaseHandler {
        public PagamentoHandler() throws SQLException {
            super();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";

            try {
                switch (method) {
                    case "GET":
                        response = sql.getPagamentos();
                        sendResponse(exchange, response, 200);
                        break;

                    case "POST":
                        // Exemplo de dados no corpo da requisição: idPedido,valor,tipo
                        String[] postData = new String(exchange.getRequestBody().readAllBytes()).split(",");
                        response = sql.postPagamento(Long.parseLong(postData[0]), postData[1], postData[2], postData[3], postData[4]);
                        sendResponse(exchange, response, 201);
                        break;

                    case "DELETE":
                        // Exemplo de dados no corpo da requisição: idPagamento
                        long idPagamento = Long.parseLong(new String(exchange.getRequestBody().readAllBytes()));
                        response = sql.deletePagamento(idPagamento);
                        sendResponse(exchange, response, 200);
                        break;

                    default:
                        response = "Método não suportado.";
                        sendResponse(exchange, response, 405);
                        break;
                }
            } catch (SQLException e) {
                response = "Erro ao executar a operação: " + e.getMessage();
                sendResponse(exchange, response, 500);
            }
        }
    }
    
    static abstract class BaseHandler implements HttpHandler {
        protected Sql sql;

        public BaseHandler() throws SQLException {
            this.sql = new Sql(conexao.getConexao());
        }

        protected void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class UsuarioHandler extends BaseHandler {
        public UsuarioHandler() throws SQLException {
            super();
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";

            try {
                switch (method) {
                    case "GET":
                        response = sql.getUsuarios();
                        sendResponse(exchange, response, 200);
                        break;

                    case "POST":
                        // Exemplo de dados no corpo da requisição: cpf,email,senha,telefone
                        String[] postData = new String(exchange.getRequestBody().readAllBytes()).split(",");
                        response = sql.postUsuario(postData[0], postData[1], postData[2], postData[3]);
                        sendResponse(exchange, response, 201);
                        break;

                    case "DELETE":
                        // Exemplo de dados no corpo da requisição: cpf
                        String cpf = new String(exchange.getRequestBody().readAllBytes());
                        response = sql.deleteUsuario(cpf);
                        sendResponse(exchange, response, 200);
                        break;

                    default:
                        response = "Método não suportado.";
                        sendResponse(exchange, response, 405);
                        break;
                }
            } catch (SQLException e) {
                response = "Erro ao executar a operação: " + e.getMessage();
                sendResponse(exchange, response, 500);
            }
        }
    }

    static class EnderecoHandler extends BaseHandler {
        public EnderecoHandler() throws SQLException {
            super();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";

            try {
                switch (method) {
                    case "GET":
                        response = sql.getEnderecos();
                        sendResponse(exchange, response, 200);
                        break;

                    case "POST":
                        // Exemplo de dados no corpo da requisição: rua,numero,bairro,cep,complemento,idUsuario
                        String[] postData = new String(exchange.getRequestBody().readAllBytes()).split(",");
                        response = sql.postEndereco(postData[0], postData[1], postData[2], Long.parseLong(postData[3]),
                                Long.parseLong(postData[4]), Long.parseLong(postData[5]));
                        sendResponse(exchange, response, 201);
                        break;

                    case "DELETE":
                        // Exemplo de dados no corpo da requisição: idEndereco
                        long idEndereco = Long.parseLong(new String(exchange.getRequestBody().readAllBytes()));
                        response = sql.deleteEndereco(idEndereco);
                        sendResponse(exchange, response, 200);
                        break;

                    default:
                        response = "Método não suportado.";
                        sendResponse(exchange, response, 405);
                        break;
                }
            } catch (SQLException e) {
                response = "Erro ao executar a operação: " + e.getMessage();
                sendResponse(exchange, response, 500);
            }
        }
    }

    static class PedidoHandler extends BaseHandler {
        public PedidoHandler() throws SQLException {
            super();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";

            try {
                switch (method) {
                    case "GET":
                        response = sql.getPedidos();
                        sendResponse(exchange, response, 200);
                        break;

                    case "POST":
                        // Exemplo de dados no corpo da requisição: idUsuario,data,status
                        String[] postData = new String(exchange.getRequestBody().readAllBytes()).split(",");
                        response = sql.postPedido(Boolean.parseBoolean(postData[0]), postData[1], Long.parseLong(postData[2]), Long.parseLong(postData[3]));
                        sendResponse(exchange, response, 201);
                        break;

                    case "DELETE":
                        // Exemplo de dados no corpo da requisição: idPedido
                        long idPedido = Long.parseLong(new String(exchange.getRequestBody().readAllBytes()));
                        response = sql.deletePedido(idPedido);
                        sendResponse(exchange, response, 200);
                        break;

                    default:
                        response = "Método não suportado.";
                        sendResponse(exchange, response, 405);
                        break;
                }
            } catch (SQLException e) {
                response = "Erro ao executar a operação: " + e.getMessage();
                sendResponse(exchange, response, 500);
            }
        }
    }

    static class ItemHandler extends BaseHandler {
        public ItemHandler() throws SQLException {
            super();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";

            try {
                switch (method) {
                    case "GET":
                        response = sql.getItens();
                        sendResponse(exchange, response, 200);
                        break;

                    case "POST":
                        // Exemplo de dados no corpo da requisição: nome,descricao,preco
                        String[] postData = new String(exchange.getRequestBody().readAllBytes()).split(",");
                        response = sql.postItem(postData[0], postData[1], Double.parseDouble(postData[2]), Long.parseLong(postData[3]), Long.parseLong(postData[4]));
                        sendResponse(exchange, response, 201);
                        break;

                    case "DELETE":
                        // Exemplo de dados no corpo da requisição: idItem
                        long idItem = Long.parseLong(new String(exchange.getRequestBody().readAllBytes()));
                        response = sql.deleteItem(idItem);
                        sendResponse(exchange, response, 200);
                        break;

                    default:
                        response = "Método não suportado.";
                        sendResponse(exchange, response, 405);
                        break;
                }
            } catch (SQLException e) {
                response = "Erro ao executar a operação: " + e.getMessage();
                sendResponse(exchange, response, 500);
            }
        }
    }
}