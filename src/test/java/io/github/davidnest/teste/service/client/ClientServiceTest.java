package io.github.davidnest.teste.service.client;

import io.github.davidnest.teste.model.entity.Car;
import io.github.davidnest.teste.model.entity.Client;
import io.github.davidnest.teste.model.entity.Purchase;
import io.github.davidnest.teste.model.exception.car.CarAlreadySoldException;
import io.github.davidnest.teste.repository.ClientRepository;
import io.github.davidnest.teste.repository.PurchaseRepository;
import io.github.davidnest.teste.service.car.CarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {


    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CarService carService;

    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private ClientService service;

    @Test
    @DisplayName("deve comprar o carro debitando o saldo e registrando a compra")
    void deveComprarCarro(){
        UUID clientId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        Client client = new Client("Maria", new BigDecimal("6000.00"), new ArrayList<>());
        Car car = new Car("Gol", "G5", 2012, 76, new BigDecimal("2500.00"), null);

        when(clientRepository.findByIdComTrava(clientId)).thenReturn(Optional.of(client));
        when(carService.findById(carId)).thenReturn(car);
        when(clientRepository.save(client)).thenReturn(client);

        Client resultado = service.buyCar(clientId, carId);

        assertThat(resultado.getBalance()).isEqualByComparingTo("3500.00");
        assertThat(car.getClient()).isSameAs(client);
        assertThat(resultado.getCars()).containsExactly(car);
        verify(purchaseRepository).save(any(Purchase.class));
    }

    @Test
    @DisplayName("não deve comprar carro que já tem dono")
    void naoDeveComprarCarroJaVendido(){
        UUID clientId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        Client comprador = new Client("Maria", new BigDecimal("6000.00"), new ArrayList<>());
        Client dono = new Client("Davi", new BigDecimal("1000.00"), new ArrayList<>());
        Car car = new Car("Gol", "G5", 2012, 76, new BigDecimal(2500.0), dono);

        when(clientRepository.findByIdComTrava(clientId)).thenReturn(Optional.of(comprador));
        when(carService.findById(carId)).thenReturn(car);

        assertThatThrownBy(() -> service.buyCar(clientId, carId))
                .isInstanceOf(CarAlreadySoldException.class);

        assertThat(comprador.getBalance()).isEqualByComparingTo("6000.00");
        verify(purchaseRepository, never()).save(any());
        verify(clientRepository, never()).save(any());

    }
}