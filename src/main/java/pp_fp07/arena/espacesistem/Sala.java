/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pp_fp07.arena.espacesistem;
import pp_fp07.arena.espacesistem.Evento.TipoEvento;

/**
 *
 * @author RyanS
 */
public class Sala {
    private final String nome;
    private final int capacidade;

    public Sala(String nome, int capacidade) {
        this.nome = nome;
        this.capacidade = capacidade;
    }

    public String getNome() {
        return nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    
    public TipoEvento determinarTipoEvento(int participantes) {
        if (this.nome.equals("Sala 1") && participantes <= 100) {
            return TipoEvento.PEQUENO;
        } else if (this.nome.equals("Sala 2") && participantes > 100 && participantes <= capacidade) {
            return TipoEvento.GRANDE;
        } else if (participantes > capacidade) { // Evento exclusivo usa toda a capacidade.
            return TipoEvento.EXCLUSIVO;
        } else {
            throw new IllegalArgumentException("Número de participantes incompatível com a sala escolhida.");
        }
    }
    
    
    @Override
    public String toString() {
        return "Sala " + nome + " (Capacidade: " + capacidade + ")";
    }
}

