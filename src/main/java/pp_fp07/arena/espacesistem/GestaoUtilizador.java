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
}
