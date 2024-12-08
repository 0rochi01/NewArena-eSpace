/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pp_fp07.arena.espacesistem;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author RyanS
 */
public class Evento {
    private final String nome;
    private final Promotor emailPromotor;
    private final Sala sala;
    private final String modalidade;
    private final LocalDateTime inicio;
    private final LocalDateTime fim;
    private final int participantes;
    private final TipoEvento tipoEvento;
    private double valorBase;
    private double valorFinal;

    public enum TipoEvento {
        PEQUENO, GRANDE, EXCLUSIVO
    }

    public Evento(String nome, Promotor emailPromotor, Sala sala, String modalidade, LocalDateTime inicio, LocalDateTime fim, int participantes) {
        this.nome = nome;
        this.emailPromotor = emailPromotor;
        this.sala = sala;
        this.modalidade = modalidade;
        this.inicio = inicio;
        this.fim = fim;
        this.participantes = participantes;
        this.tipoEvento = sala.determinarTipoEvento(participantes);
        calcularValorEvento();
    }

    private void calcularValorEvento() {
        switch (tipoEvento) {
            case PEQUENO -> {
                this.valorBase = 500; // Valor para eventos pequenos
                this.valorFinal = valorBase;
            }
            case GRANDE -> {
                this.valorBase = 1000; // Valor para eventos grandes
                this.valorFinal = valorBase * 0.9; // Desconto de 10%
            }
            case EXCLUSIVO -> {
                this.valorBase = 5000; // Valor para eventos exclusivos
                this.valorFinal = valorBase;
            }
        }
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return String.format("Evento: %s\nPromotor: %s\nSala: %s\nModalidade: %s\nInício: %s\nFim: %s\nParticipantes: %d\nTipo: %s\nValor Final: %.2f",
                nome, emailPromotor, sala.getNome(), modalidade, inicio.format(formatter), fim.format(formatter),
                participantes, tipoEvento, valorFinal);
    }
}
