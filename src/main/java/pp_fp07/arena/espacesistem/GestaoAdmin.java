/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pp_fp07.arena.espacesistem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/**
 *
 * @author RyanS
 */
public class GestaoAdmin {
    private static final String ARQUIVO_ADMIN = "admin_dados.txt";
    private final Criptografia criptografia;

    public GestaoAdmin() {
        this.criptografia = new Criptografia();
    }

    // Salvar admin no arquivo criptografado
    public void salvarAdmin(Admin admin) {
        try (FileOutputStream fos = new FileOutputStream(ARQUIVO_ADMIN)) {
            // Serializar o objeto Admin
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(admin);
            }

            // Criptografar os dados serializados
            String dadosCriptografados = criptografia.criptografar(Base64.getEncoder().encodeToString(baos.toByteArray()));
            fos.write(dadosCriptografados.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar administrador no arquivo: " + e.getMessage());
        }
    }
    

    // Carregar admin do arquivo criptografado
    public Admin carregarAdmin() {
        File arquivo = new File(ARQUIVO_ADMIN);
        if (!arquivo.exists()) {
            return null; // Retorna null se o arquivo não existir
        }

        try (FileInputStream fis = new FileInputStream(ARQUIVO_ADMIN)) {
            // Ler o conteúdo criptografado do arquivo
            byte[] dadosCriptografados = fis.readAllBytes();
            String dadosDecodificados = criptografia.descriptografar(new String(dadosCriptografados));

            // Decodificar e desserializar o objeto Admin
            byte[] dadosDeserializados = Base64.getDecoder().decode(dadosDecodificados);
            ByteArrayInputStream bais = new ByteArrayInputStream(dadosDeserializados);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Admin) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar administrador do arquivo: " + e.getMessage());
        }
    }
}
