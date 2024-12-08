/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pp_fp07.arena.espacesistem;

import java.io.Serializable;
import java.io.*;
import java.util.Base64;
import java.util.Scanner;

/**
 *
 * @author RyanS
 */
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L; // Valor fixo
    private String nomeDeUtilizador; // Nome de utilizador (username)
    private String email; // Email do utilizador
    private String password; // Senha do utilizador

    public Admin(String nomeDeUtilizador, String email, String password) {
        this.nomeDeUtilizador = nomeDeUtilizador;
        this.email = email;
        this.password = password;
    }

    public String getNomeDeUtilizador() {
        return nomeDeUtilizador;
    }

    public void setNomeDeUtilizador(String nomeDeUtilizador) {
        this.nomeDeUtilizador = nomeDeUtilizador;
    }

    public String getEmail(Criptografia criptografia) { // Armazena o email criptografado
        return criptografia.descriptografar(email); // Retorna o e-mail descriptografado
    }

    public void setEmail(String email, Criptografia criptografia) {
        this.email = criptografia.criptografar(email); // Salva o e-mail criptografado
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }   
    
    // Método para exibir o menu de administração
    public void exibirMenuAdmin(GestaoUtilizador gestaoUtilizador) {
        Scanner scanner = new Scanner(System.in);
        OpcaoMenuAdmin opcao;

        do {
            System.out.println("\n=== Menu de Administração ===");
            for (OpcaoMenuAdmin op : OpcaoMenuAdmin.values()) {
                System.out.println(op.getCodigo() + " - " + op.name());
            }
            System.out.print("Escolha uma opção: ");
            int codigo = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha

            opcao = OpcaoMenuAdmin.getFromCodigo(codigo);

            if (opcao != null) {
                switch (opcao) {
                    case LISTAR_PROMOTORES -> listarPromotores(gestaoUtilizador);
                    case EDITAR_PROMOTOR -> editarPromotor(gestaoUtilizador, scanner);
                    case EXCLUIR_PROMOTOR -> excluirPromotor(gestaoUtilizador, scanner);
                    case SAIR -> {
                        System.out.println("Retornando ao menu principal...");
                        ArenaESpaceSistem.executaMenuPrincipal(
                                new GestaoAdmin(),
                                gestaoUtilizador,
                                new Criptografia()
                        );
                    }
                }
            } else {
                System.out.println("Opção inválida, tente novamente.");
            }
        } while (opcao != OpcaoMenuAdmin.SAIR);
    }

    // Método para listar promotores
    private void listarPromotores(GestaoUtilizador gestaoUtilizador) {
        System.out.println("\nLista de Promotores:");

        // Verifica se a lista de promotores está vazia
        if (gestaoUtilizador.listarPromotores().isEmpty()) {
            System.out.println("Nenhum promotor cadastrado!");
        } else {
            // Se houver promotores, lista-os
            for (Promotor promotor : gestaoUtilizador.listarPromotores()) {
                System.out.println("- Nome: " + promotor.getNomeDeUtilizador());
            }
        }
    }

    // Método para editar promotor
    private void editarPromotor(GestaoUtilizador gestaoUtilizador, Scanner scanner) {
        // Verifica se a lista de promotores está vazia
        if (gestaoUtilizador.listarPromotores().isEmpty()) {
            System.out.println("Nenhum promotor cadastrado para editar.");
            return; // Retorna sem realizar a edição, caso não haja promotores
        }

        System.out.print("\nDigite o nome do promotor a editar: ");
        String nome = scanner.nextLine();

        // Verifica se o promotor existe
        Promotor promotorExistente = gestaoUtilizador.procurarPromotorPorNome(nome);
        if (promotorExistente == null) {
            System.out.println("Promotor não encontrado.");
            return; // Retorna se o promotor não existir
        }

        System.out.print("Novo email: ");
        String novoEmail = scanner.nextLine();

        System.out.print("Nova senha: ");
        String novaSenha = scanner.nextLine();

        // Atualiza o promotor
        gestaoUtilizador.editarPromotor(nome, novoEmail, novaSenha);
        System.out.println("Promotor atualizado com sucesso.");
    }
    
    // Método para excluir promotor
    private void excluirPromotor(GestaoUtilizador gestaoUtilizador, Scanner scanner) {
        // Verifica se a lista de promotores está vazia
        if (gestaoUtilizador.listarPromotores().isEmpty()) {
            System.out.println("Nenhum promotor cadastrado para excluir.");
            return; // Retorna sem realizar a exclusão, caso não haja promotores
        }

        System.out.print("\nDigite o nome do promotor a excluir: ");
        String nome = scanner.nextLine();

        // Verifica se o promotor existe
        Promotor promotorExistente = gestaoUtilizador.procurarPromotorPorNome(nome);
        if (promotorExistente == null) {
            System.out.println("Promotor não encontrado.");
            return; // Retorna se o promotor não existir
        }

        // Exclui o promotor
        gestaoUtilizador.excluirPromotor(nome);
        System.out.println("Promotor excluído com sucesso.");
    }

    // Classe Enum interna para representar as opções do menu
    public enum OpcaoMenuAdmin {
        LISTAR_PROMOTORES(1),
        EDITAR_PROMOTOR(2),
        EXCLUIR_PROMOTOR(3),
        LISTAR_EVENTOS_POR_PROMOTOR(4),
        SAIR(0);

        private final int codigo;

        OpcaoMenuAdmin(int codigo) {
            this.codigo = codigo;
        }

        public int getCodigo() {
            return codigo;
        }

        public static OpcaoMenuAdmin getFromCodigo(int codigo) {
            for (OpcaoMenuAdmin opcao : OpcaoMenuAdmin.values()) {
                if (opcao.getCodigo() == codigo) {
                    return opcao;
                }
            }
            return null;
        }
    }
}
