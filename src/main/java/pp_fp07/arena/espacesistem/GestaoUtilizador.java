/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pp_fp07.arena.espacesistem;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
/**
 *
 * @author RyanS
 */
public class GestaoUtilizador {
    private static final String ARQUIVO_UTILIZADORES = "utilizadores_dados.txt";
    private final Criptografia criptografia;
    private final List<Utilizador> utilizadores; // Lista para armazenar os utilizadores em memória

    public GestaoUtilizador() {
        this.criptografia = new Criptografia();
        this.utilizadores = new ArrayList<>();
    }

    // Salvar um utilizador no arquivo
    public void salvarUtilizador(Utilizador utilizador) {
        try (FileOutputStream fos = new FileOutputStream(ARQUIVO_UTILIZADORES, true)) {
            // Serializar o objeto Utilizador
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(utilizador);
            }

            // Criptografar os dados serializados
            String dadosCriptografados = criptografia.criptografar(Base64.getEncoder().encodeToString(baos.toByteArray()));
            fos.write((dadosCriptografados + System.lineSeparator()).getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar utilizador no arquivo: " + e.getMessage());
        }
    }

    // Carregar todos os utilizadores do arquivo
    public void carregarUtilizadores() {
        utilizadores.clear(); // Limpa a lista antes de recarregar
        File arquivo = new File(ARQUIVO_UTILIZADORES);
        if (!arquivo.exists()) {
            System.out.println("Nenhum utilizador encontrado.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_UTILIZADORES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Descriptografar a linha
                String dadosDescriptografados = criptografia.descriptografar(linha);

                // Desserializar o objeto Utilizador
                byte[] dadosDeserializados = Base64.getDecoder().decode(dadosDescriptografados);
                ByteArrayInputStream bais = new ByteArrayInputStream(dadosDeserializados);
                Utilizador utilizador;
                try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                    utilizador = (Utilizador) ois.readObject();
                }

                // Adicionar o utilizador à lista
                utilizadores.add(utilizador);
            }
            System.out.println("Utilizadores carregados com sucesso!");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar utilizadores do arquivo: " + e.getMessage());
        }
    }

    // Método para procurar um utilizador pelo nome de utilizador e senha
    public Utilizador procurarUtilizador(String nomeDeUtilizador, String senha) {
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getNomeDeUtilizador().equals(nomeDeUtilizador) &&
                criptografia.verificarSenha(senha, utilizador.getPassword())) {
                return utilizador;
            }
        }
        return null;
    }
    
    public List<Promotor> listarPromotores() {
        List<Promotor> promotores = new ArrayList<>();
        for (Utilizador utilizador : utilizadores) {
            if (utilizador instanceof Promotor promotor) {
                promotores.add(promotor);
            }
        }
        return promotores;
    }
    
    
    // Método para procurar promotor por nome
    public Promotor procurarPromotorPorNome(String nome) {
        // Itera sobre a lista de promotores e verifica se algum possui o nome informado
        for (Promotor promotor : listarPromotores()) {
            if (promotor.getNomeDeUtilizador().equals(nome)) {
                return promotor; // Retorna o promotor encontrado
            }
        }
        return null; // Retorna null se nenhum promotor for encontrado com o nome informado
    }
    
    
    public void editarPromotor(String nomeDeUtilizador, String novoEmail, String novaSenha) {
        carregarUtilizadores(); // Atualiza a lista com os dados do arquivo
        for (Utilizador utilizador : utilizadores) {
            if (utilizador instanceof Promotor && utilizador.getNomeDeUtilizador().equals(nomeDeUtilizador)) {
                utilizador.setEmail(novoEmail, criptografia);
                utilizador.setPassword(criptografia.gerarHashDeSenha(novaSenha));
                salvarTodosUtilizadores();
                System.out.println("Promotor atualizado com sucesso.");
                return;
            }
        }
        System.out.println("Promotor não encontrado.");
    }
    
    public void excluirPromotor(String nomeDeUtilizador) {
        carregarUtilizadores(); // Atualiza a lista com os dados do arquivo
        Utilizador promotorParaRemover = null;
        for (Utilizador utilizador : utilizadores) {
            if (utilizador instanceof Promotor && utilizador.getNomeDeUtilizador().equals(nomeDeUtilizador)) {
                promotorParaRemover = utilizador;
                break;
            }
        }
        if (promotorParaRemover != null) {
            utilizadores.remove(promotorParaRemover);
            salvarTodosUtilizadores();
            System.out.println("Promotor removido com sucesso.");
        } else {
            System.out.println("Promotor não encontrado.");
        }
    }
    
    private void salvarTodosUtilizadores() {
        try (FileOutputStream fos = new FileOutputStream(ARQUIVO_UTILIZADORES)) {
            for (Utilizador utilizador : utilizadores) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(utilizador);
                }
                String dadosCriptografados = criptografia.criptografar(Base64.getEncoder().encodeToString(baos.toByteArray()));
                fos.write((dadosCriptografados + System.lineSeparator()).getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar todos os utilizadores no arquivo: " + e.getMessage());
        }
    }
    
}
