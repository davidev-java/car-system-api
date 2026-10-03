package io.github.davidnest.teste.model.entity;

import io.github.davidnest.teste.model.exception.client.InsufficientBalanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClientTest {

    @Test
    @DisplayName("deve debitar o valor do saldo")
    void deveDebitarDoSaldo(){
        Client client = new Client("Maria", new BigDecimal("6000.00"), new ArrayList<>());

        client.debit(new BigDecimal("2500.00"));

        assertThat(client.getBalance()).isEqualByComparingTo("3500.00");
    }

    @Test
    @DisplayName("não deve debitar quando o saldo for insuficiente")
    void naoDeveDebitarQuandoSaldoForInsuficiente(){
        Client client = new Client("Maria", new BigDecimal("1000.00"), new ArrayList<>());

        assertThatThrownBy(() -> client.debit(new BigDecimal("3500.00")))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Saldo insuficiente");

        assertThat(client.getBalance()).isEqualByComparingTo("1000.00");
    }

}