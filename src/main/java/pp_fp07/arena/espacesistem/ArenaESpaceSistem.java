/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package pp_fp07.arena.espacesistem;

import java.util.InputMismatchException;
import java.util.Scanner;


/**
 *
 * @author RyanS
 */
public class ArenaESpaceSistem {

    public static void main(String[] args) {
        // Inicialização dos gestores
        GestaoAdmin gerirAdmin = new GestaoAdmin();
        GestaoUtilizador gerirUtilizador = new GestaoUtilizador();
        Criptografia criptografia = new Criptografia();

        // Criar ou carregar administrador padrão
        inicializarAdminPadrao(gerirAdmin, criptografia);

        // Carregar utilizadores salvos
        gerirUtilizador.carregarUtilizadores();

        // Executar o menu principal
        executaMenuPrincipal(gerirAdmin, gerirUtilizador, criptografia);
    }

    private static void inicializarAdminPadrao(GestaoAdmin gerirAdmin, Criptografia criptografia) {
        Admin adminPadrao = new Admin("admin", null, criptografia.gerarHashDeSenha("adminArena"));
        adminPadrao.setEmail("admin@arenaespace.pt", criptografia);

        if (gerirAdmin.carregarAdmin() == null) {
            gerirAdmin.salvarAdmin(adminPadrao);
            System.out.println("Administrador padrão criado com sucesso.");
        }
    }

    public static void executaMenuPrincipal(GestaoAdmin gerirAdmin, GestaoUtilizador gerirUtilizador, Criptografia criptografia) {
        try (Scanner scanner = new Scanner(System.in)) {
            OpcaoMenu1 opcaoSelecionada;
            do {
                opcaoSelecionada = mostrarMenuEDevolverOpcaoSelecionada(scanner);

                switch (opcaoSelecionada) {
                    case LOGIN -> realizarLogin(scanner, gerirAdmin, gerirUtilizador, criptografia);
                    case REGISTO -> Utilizador.cadastroDeUtilizador(gerirUtilizador);
                    case SAIR -> System.out.println("Encerrando o sistema.... Até logo!!");
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } while (opcaoSelecionada != OpcaoMenu1.SAIR);
        }
    }

    private static void realizarLogin(Scanner scanner, GestaoAdmin gerirAdmin, GestaoUtilizador gerirUtilizador, Criptografia criptografia) {
        System.out.println("=== Login ===");
        System.out.print("Digite o nome de utilizador: ");
        String nomeDeUtilizador = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        // Verificar se é um administrador
        /*Admin admin = gerirAdmin.carregarAdmin();
        if (admin != null && nomeDeUtilizador.equals(admin.getNomeDeUtilizador())
                && criptografia.verificarSenha(senha, admin.getPassword())) {
            System.out.println("Bem-vindo, administrador!");
            return;
        }*/

        // Verificar se é um utilizador
        Utilizador utilizador = gerirUtilizador.procurarUtilizador(nomeDeUtilizador, senha);
        if (utilizador != null) {
            System.out.println("Bem-vindo, " + utilizador.getNomeCompleto() + "!");
        } else {
            System.out.println("Nome de utilizador ou senha incorretos.");
        }
    }

    public static OpcaoMenu1 mostrarMenuEDevolverOpcaoSelecionada(Scanner scanner) {
        OpcaoMenu1 opcaoSelecionada = null;
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.println("\n=== Arena-eSpace Menu Principal ===");
            for (OpcaoMenu1 opcao : OpcaoMenu1.values()) {
                System.out.println(opcao.codigoMenu1 + " - " + opcao.getDescricao());
            }
            System.out.print("Escolha uma opção: ");
            try {
                int codigo = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer
                opcaoSelecionada = OpcaoMenu1.getFromCodigo(codigo);

                if (opcaoSelecionada != null) {
                    entradaValida = true;
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um número válido.");
                scanner.nextLine(); // Limpar buffer
            }
        }

        return opcaoSelecionada;
    }

    public enum OpcaoMenu1 {
        LOGIN(1, "Login"),
        REGISTO(2, "Registar Utilizador"),
        SAIR(0, "Sair");

        private final int codigoMenu1;
        private final String descricao;

        OpcaoMenu1(int codigoMenu1, String descricao) {
            this.codigoMenu1 = codigoMenu1;
            this.descricao = descricao;
        }

        public static OpcaoMenu1 getFromCodigo(int codigo) {
            for (OpcaoMenu1 opcao : OpcaoMenu1.values()) {
                if (opcao.codigoMenu1 == codigo) {
                    return opcao;
                }
            }
            return null;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
