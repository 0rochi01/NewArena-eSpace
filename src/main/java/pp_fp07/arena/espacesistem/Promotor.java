/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pp_fp07.arena.espacesistem;

import java.util.Scanner;

/**
 *
 * @author RyanS
 */
public class Promotor extends Utilizador {
    
    public Promotor(String nomeCompleto, String nomeDeUtilizador, String email, String password, String privilegio) {
        super(nomeCompleto, nomeDeUtilizador, email, password, privilegio);
    }
    
    // Método para listar promotor
    private void listarPromotores(GestaoUtilizador gestaoUtilizador) {
        System.out.println("\nLista de Promotores:");
        for (Promotor promotor : gestaoUtilizador.listarPromotores()) {
            System.out.println("- Nome: " + promotor.getNomeDeUtilizador());
        }
    }

    // Método para editar promotor
    private void editarPromotor(GestaoUtilizador gestaoUtilizador, Scanner scanner) {
        System.out.print("\nDigite o nome do promotor a editar: ");
        String nome = scanner.nextLine();
        System.out.print("Novo email: ");
        String novoEmail = scanner.nextLine();
        System.out.print("Nova senha: ");
        String novaSenha = scanner.nextLine();

        gestaoUtilizador.editarPromotor(nome, novoEmail, novaSenha);
        System.out.println("Promotor atualizado com sucesso.");
    }
    
    // Método para excluir promotor
    private void excluirPromotor(GestaoUtilizador gestaoUtilizador, Scanner scanner) {
        System.out.print("\nDigite o nome do promotor a excluir: ");
        String nome = scanner.nextLine();

        gestaoUtilizador.excluirPromotor(nome);
        System.out.println("Promotor excluído com sucesso.");
    }
}
