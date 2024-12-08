/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pp_fp07.arena.espacesistem;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author RyanS
 */
public class Utilizador {
    // Atributos da classe Utilizador, que armazenam informações sobre o utilizador
    private String nomeCompleto; // Nome completo do utilizador
    private String nomeDeUtilizador; // Nome de utilizador (username)
    private String email; // Email do utilizador(armazenado criptografado)
    private String password; // Senha do utilizador(armazenada como hash)
    private String privilegio; // Privilégio do utilizador (admin, promotor, líder de equipa, atleta, espetador) armazenado criptografado

    public Utilizador(String nomeCompleto, String nomeDeUtilizador, String email, String password, String privilegio) {
        this.nomeCompleto = nomeCompleto;
        this.nomeDeUtilizador = nomeDeUtilizador;
        this.email = email;
        this.password = password;
        this.privilegio = privilegio;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeDeUtilizador() {
        return nomeDeUtilizador;
    }

    public String getEmail(Criptografia criptografia) {
        return criptografia.descriptografar(email); // Retorna o e-mail descriptografado
    }

    public void setEmail(String email, Criptografia criptografia) {
        this.email = criptografia.criptografar(email); // Criptografa o e-mail antes de armazenar
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // Apenas o hash será armazenado
    }

    public String getPrivilegio(Criptografia criptografia) {
        return criptografia.descriptografar(privilegio); // Retorna o privilégio descriptografado
    }

    public void setPrivilegio(String privilegio, Criptografia criptografia) {
        this.privilegio = criptografia.criptografar(privilegio); // Criptografa o privilégio
    }

    
    public static Utilizador cadastroDeUtilizador(GestaoUtilizador gerirUtilizador) {
        Scanner menu = new Scanner(System.in);
        Criptografia criptografia = new Criptografia();

        System.out.println("=== Cadastro de Utilizador ===");

        // Nome Completo
        String nomeCompleto;
        while(true){
        System.out.print("Digite o seu Nome Completo: ");
        nomeCompleto = menu.nextLine();
        
         try {
            // Tenta formatar o nome e verificar se é válido
            nomeCompleto = formatarNomeCompleto(nomeCompleto);
            break; // Se o nome for válido, sai do loop
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // Exibe erro caso a validação falhe
        }
    }
        // Nome de Utilizador
        String nomeDeUtilizador;
        while (true) {
            System.out.print("Digite o seu Nome de Utilizador: ");
            nomeDeUtilizador = menu.nextLine();

            if (isNomeDeUtilizadorUnico(nomeDeUtilizador)) {
                break;
            } else {
                System.out.println("Nome de utilizador já existe! Por favor, escolha outro.");
            }
        }

        // Email
        String email;
        while (true) {
            System.out.print("Digite o seu email: ");
            email = menu.nextLine();

            if (isEmailValido(email)) {
                break;
            } else {
                System.out.println("Email inválido! Por favor, digite um email válido.");
            }
        }

        // Criptografar o e-mail
        String emailCriptografado = criptografia.criptografar(email);

        // Password
        String password = obterPass(menu);
        String senhaHasheada = criptografia.gerarHashDeSenha(password); // Hash da senha

        // Privilégio
        System.out.println("Selecione um tipo de utilizador:");
        System.out.println("1. " + Privilegio.PROMOTOR.name());
        System.out.println("2. " + Privilegio.ATLETA.name());
        System.out.println("3. " + Privilegio.LIDER_DE_EQUIPA.name());
        System.out.println("4. " + Privilegio.ESPETADOR.name());

        int opcaoPrivilegio;
        try {
            opcaoPrivilegio = Integer.parseInt(menu.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Por favor, digite o número correspondente ao privilégio.");
            return null;
        }
        
        // Obtém a opção correspondente ao número digitado
        Privilegio privilegio = Privilegio.getFromCodigo(opcaoPrivilegio); // Chama método para obter o privilégio a partir do código
        if (privilegio == null || (privilegio != Privilegio.PROMOTOR || privilegio != Privilegio.ATLETA || privilegio != Privilegio.LIDER_DE_EQUIPA || privilegio != Privilegio.ESPETADOR)) {  // Verifica se o privilégio é válido (se não é nulo e se é um dos tipos permitidos)
            System.out.println("Privilégio inválido!!"); // Mensagem de erro para privilégio inválido
            return null; // Retorna null indicando que o cadastro não foi bem-sucedido
        }

        // Criptografar o privilégio antes de armazenar
        String privilegioCriptografado = criptografia.criptografar(privilegio.name());

        // Exibir o privilégio descriptografado ao usuário
        System.out.println("Privilégio selecionado: " + privilegio.name());

        // Armazenar o privilégio criptografado
        // (Salve o privilegioCriptografado no banco ou arquivo)

        // Criar o utilizador e salvar
        Utilizador utilizador = new Utilizador(nomeCompleto, nomeDeUtilizador, emailCriptografado, senhaHasheada, privilegioCriptografado);
        gerirUtilizador.salvarUtilizador(utilizador);

        System.out.println("Utilizador cadastrado com sucesso!");
        return utilizador;
    }
    
    
    // Método para formatar o nome completo
    public static String formatarNomeCompleto(String nomeCompleto) {
        // Valida se o nome completo não está vazio e contém pelo menos 2 palavras
        if (nomeCompleto == null || nomeCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome completo não pode ser vazio.");
        }

        String[] partes = nomeCompleto.trim().split("\\s+"); // Divide o nome em partes por espaços

        if (partes.length < 2) {
            throw new IllegalArgumentException("O nome completo deve conter pelo menos um nome e um sobrenome.");
        }

        // Formata cada parte do nome, capitalizando a primeira letra e deixando as demais em minúsculas
        StringBuilder nomeFormatado = new StringBuilder();
        for (String parte : partes) {
            nomeFormatado.append(parte.substring(0, 1).toUpperCase()) // Primeira letra em maiúscula
                         .append(parte.substring(1).toLowerCase())   // Restante da palavra em minúsculas
                         .append(" ");                              // Adiciona espaço entre as palavras
        }

        return nomeFormatado.toString().trim(); // Retorna o nome formatado
    }
    
    
    // Verifica se o nome de utilizador já existe
    public static boolean isNomeDeUtilizadorUnico(String nomeDeUtilizador) {
        Criptografia criptografia = new Criptografia();
        File arquivo = new File("utilizadores.txt"); // Nome do arquivo de armazenamento

        if (!arquivo.exists()) {
            return true; // Arquivo não existe, então o nome de utilizador é único
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Descriptografar linha e deserializar o utilizador
                String dadosDescriptografados = criptografia.descriptografar(linha);
                byte[] dadosDeserializados = Base64.getDecoder().decode(dadosDescriptografados);
                ByteArrayInputStream bais = new ByteArrayInputStream(dadosDeserializados);
                Utilizador utilizador;
                try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                    utilizador = (Utilizador) ois.readObject();
                }

                // Comparar nome de utilizador
                if (utilizador.getNomeDeUtilizador().equals(nomeDeUtilizador)) {
                    return false; // Nome de utilizador já existe
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao verificar nome de utilizador: " + e.getMessage());
        }

        return true; // Nome de utilizador é único
    }
    
    
    // Verifica se o email é válido
    public static boolean isEmailValido(String email) {
        String regex = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }
    
    
    // Método para obter a senha com ocultação e validação de senha forte
    public static String obterPass(Scanner menu) {
        Console console = System.console();
        String password = null;
        if (console != null) {
            // Usar Console para ler a senha de forma oculta
            char[] senha = console.readPassword("Digite a sua password (mínimo 8 caracteres, uma letra maiúscula, uma minúscula, um número e um caractere especial): ");
            password = new String(senha);
        } else {
            // Console não disponível, usar Scanner
            System.out.print("Digite a sua password (mínimo 8 caracteres, uma letra maiúscula, uma minúscula, um número e um caractere especial): ");
            password = menu.nextLine();
        }

        // Validar se a senha é forte
        while (!validarPassForte(password)) {
            System.out.println("Senha fraca! A senha deve atender aos seguintes requisitos:");
            System.out.println("- Mínimo de 8 caracteres");
            System.out.println("- Pelo menos uma letra maiúscula");
            System.out.println("- Pelo menos uma letra minúscula");
            System.out.println("- Pelo menos um número");
            System.out.println("- Pelo menos um caractere especial (@, #, $, %, etc.)");
            System.out.print("Digite uma senha válida: ");
            password = menu.nextLine();
        }

        // Exibir a senha como "******"
        System.out.println("Senha cadastrada com sucesso!");

        return password;
    }
    
    // Método para validar se a senha é forte
    public static boolean validarPassForte(String senha) {
        // Requisito 1: Mínimo de 8 caracteres
        if (senha.length() < 8) {
            return false;
        }

        // Requisito 2: Pelo menos uma letra maiúscula
        Pattern maiuscula = Pattern.compile("[A-Z]");
        Matcher matcherMaiuscula = maiuscula.matcher(senha);
        if (!matcherMaiuscula.find()) {
            return false;
        }

        // Requisito 3: Pelo menos uma letra minúscula
        Pattern minuscula = Pattern.compile("[a-z]");
        Matcher matcherMinuscula = minuscula.matcher(senha);
        if (!matcherMinuscula.find()) {
            return false;
        }

        // Requisito 4: Pelo menos um número
        Pattern numero = Pattern.compile("[0-9]");
        Matcher matcherNumero = numero.matcher(senha);
        if (!matcherNumero.find()) {
            return false;
        }

        // Requisito 5: Pelo menos um caractere especial
        Pattern especial = Pattern.compile("[@#$%^&+=!]");
        Matcher matcherEspecial = especial.matcher(senha);
        return matcherEspecial.find();
    }
    
    
    // Classe enum para representar o privilégio do utilizador
    public enum Privilegio {

        PROMOTOR(1), // Privilégio de Promotor
        
        ATLETA(2), // Privilégio de Atleta
        
        LIDER_DE_EQUIPA(3), // Privilégio de Líder de Equipa
        
        ESPETADOR(4); // Privilégio de Espetador


        private final int codigoPrivilegio; // Código associado ao privilégio

        // Construtor do enum
        Privilegio(int codigoPrivilegio) {
            this.codigoPrivilegio = codigoPrivilegio;
        }

        // Método para obter o código do privilégio
        public int getCodigoPrivilegio() {
            return codigoPrivilegio;
        }

        // Método estático para obter um privilégio a partir de um código
        public static Privilegio getFromCodigo(int codigoPrivilegio) {
            for (Privilegio privilegio : values()) {
                // Verifica se o código fornecido corresponde a um dos privilégios
                if (privilegio.codigoPrivilegio == codigoPrivilegio) {
                    return privilegio; // Retorna o privilégio correspondente
                }
            }
            return null; // Retorna null se o código não corresponder a nenhum privilégio
        }
    }
    
}
