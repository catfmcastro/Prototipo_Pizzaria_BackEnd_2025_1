package backend;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class Sql {

    private final Connection connection;

    public Sql(Connection connection) {
        this.connection = connection;
    }

    // Métodos para a tabela "usuario"
    // http://localhost:8080/usuario
    public String getUsuarios() throws SQLException {
    String query = "SELECT * FROM usuario;";
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        Gson gson = new Gson();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Create a list of maps to represent rows
        List<Map<String, Object>> rows = new ArrayList<>();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                row.put(columnName, rs.getObject(i));
            }
            rows.add(row);
        }

        // Convert the list of rows to JSON using Gson
        return gson.toJson(rows);
    }
}

    public String postUsuario(String cpf, String email, String senha, String telefone) throws SQLException {
        String query = "INSERT INTO usuario (cpf, email, senha, telefone) VALUES (?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cpf);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.setString(4, telefone);
            int rowsAffected = stmt.executeUpdate();
            return "Usuário inserido com sucesso. Linhas afetadas: " + rowsAffected;
        }
    }

    public String deleteUsuario(String cpf) throws SQLException {
        String query = "DELETE FROM usuario WHERE cpf = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cpf);
            int rowsAffected = stmt.executeUpdate();
            return "Usuário deletado com sucesso. Linhas afetadas: " + rowsAffected;
        }
    }

    // Métodos para a tabela "endereco"
    // http://localhost:8080/endereco
    public String getEnderecos() throws SQLException {
        String query = "SELECT * FROM endereco;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            Gson gson = new Gson();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
    
            // Cria uma lista de mapas para representar as linhas
            List<Map<String, Object>> rows = new ArrayList<>();
    
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    row.put(columnName, rs.getObject(i));
                }
                rows.add(row);
            }
    
            // Converte a lista de linhas para JSON usando Gson
            return gson.toJson(rows);
        }
    }

    public String postEndereco(String rua, String numero, String bairro, long cep, long complemento, long idUsuario) throws SQLException {
        String query = "INSERT INTO endereco (rua, numero, bairro, cep, complemento, id_usuario) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, rua);
            stmt.setString(2, numero);
            stmt.setString(3, bairro);
            stmt.setLong(4, cep);
            stmt.setLong(5, complemento);
            stmt.setLong(6, idUsuario);
            int rowsAffected = stmt.executeUpdate();
            return "Endereço inserido com sucesso. Linhas afetadas: " + rowsAffected;
        }
    }

    public String deleteEndereco(long idEndereco) throws SQLException {
        String query = "DELETE FROM endereco WHERE id_endereco = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, idEndereco);
            int rowsAffected = stmt.executeUpdate();
            return "Endereço deletado com sucesso. Linhas afetadas: " + rowsAffected;
        }
    }

    // Métodos para a tabela "pedido"
    // http://localhost:8080/pedido
    public String getPedidos() throws SQLException {
        String query = "SELECT * FROM pedido;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            Gson gson = new Gson();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
    
            // Cria uma lista de mapas para representar as linhas
            List<Map<String, Object>> rows = new ArrayList<>();
    
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    row.put(columnName, rs.getObject(i));
                }
                rows.add(row);
            }
    
            // Converte a lista de linhas para JSON usando Gson
            return gson.toJson(rows);
        }
    }

    public String postPedido(boolean ativo, String idUsuario, long endEntrega, long idPagamento) throws SQLException {
        String query = "INSERT INTO pedido (ativo, id_usuario, end_entrega, id_pagamento) VALUES (?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBoolean(1, ativo);
            stmt.setString(2, idUsuario);
            stmt.setLong(3, endEntrega);
            stmt.setLong(4, idPagamento);
            int rowsAffected = stmt.executeUpdate();
            return "Pedido inserido com sucesso. Linhas afetadas: " + rowsAffected;
        }
    }

    public String deletePedido(long idPedido) throws SQLException {
        String query = "DELETE FROM pedido WHERE id_pedido = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, idPedido);
            int rowsAffected = stmt.executeUpdate();
            return "Pedido deletado com sucesso. Linhas afetadas: " + rowsAffected;
        }
    }

    // Métodos para a tabela "item"
    // http://localhost:8080/item
    public String getItens() throws SQLException {
        String query = "SELECT * FROM item;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            Gson gson = new Gson();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
    
            // Cria uma lista de mapas para representar as linhas
            List<Map<String, Object>> rows = new ArrayList<>();
    
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    row.put(columnName, rs.getObject(i));
                }
                rows.add(row);
            }
    
            // Converte a lista de linhas para JSON usando Gson
            return gson.toJson(rows);
        }
    }

    public String postItem(String nome, String descricao, double preco, long quantidade, long idPedido) throws SQLException {
        String query = "INSERT INTO item (nome, descricao, preco, quantidade, id_pedido) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setDouble(3, preco);
            stmt.setLong(4, quantidade);
            stmt.setLong(5, idPedido);
            int rowsAffected = stmt.executeUpdate();
            return "Item inserido com sucesso. Linhas afetadas: " + rowsAffected;
        }
    }

    public String deleteItem(long idItem) throws SQLException {
        String query = "DELETE FROM item WHERE id_item = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, idItem);
            int rowsAffected = stmt.executeUpdate();
            return "Item deletado com sucesso. Linhas afetadas: " + rowsAffected;
        }
    }

    // Métodos para a tabela "pagamento"
    // http://localhost:8080/pagamento
    public String getPagamentos() throws SQLException {
        String query = "SELECT * FROM pagamento;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            Gson gson = new Gson();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
    
            // Cria uma lista de mapas para representar as linhas
            List<Map<String, Object>> rows = new ArrayList<>();
    
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    row.put(columnName, rs.getObject(i));
                }
                rows.add(row);
            }
    
            // Converte a lista de linhas para JSON usando Gson
            return gson.toJson(rows);
        }
    }

    public String postPagamento(long tipoPagamento, String numCartao, String cvv, String apelidoCartao, String idUsuario) throws SQLException {
        String query = "INSERT INTO pagamento (tipo_pagamento, num_cartao, cvv, apelido_cartao, id_usuario) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, tipoPagamento);
            stmt.setString(2, numCartao);
            stmt.setString(3, cvv);
            stmt.setString(4, apelidoCartao);
            stmt.setString(5, idUsuario);
            int rowsAffected = stmt.executeUpdate();
            return "Pagamento inserido com sucesso. Linhas afetadas: " + rowsAffected;
        }
    }

    public String deletePagamento(long idPagamento) throws SQLException {
        String query = "DELETE FROM pagamento WHERE id_pagamento = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, idPagamento);
            int rowsAffected = stmt.executeUpdate();
            return "Pagamento deletado com sucesso. Linhas afetadas: " + rowsAffected;
        }
    }

    // Método genérico para executar SELECT
    private String get(String query) throws SQLException {
        StringBuilder resultBuilder = new StringBuilder();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Adiciona cabeçalhos das colunas
            for (int i = 1; i <= columnCount; i++) {
                resultBuilder.append(metaData.getColumnName(i)).append("\t");
            }
            resultBuilder.append("\n");

            // Adiciona os dados
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    resultBuilder.append(rs.getString(i)).append("\t");
                }
                resultBuilder.append("\n");
            }
        }
        return resultBuilder.toString();
    }
}
