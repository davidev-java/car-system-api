package io.github.davidnest.teste.controller;

import io.github.davidnest.teste.model.entity.Client;
import io.github.davidnest.teste.model.exception.client.ClientNotFoundException;
import io.github.davidnest.teste.service.client.ClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import io.github.davidnest.teste.configuration.SecurityConfig;

@Import(SecurityConfig.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean
    private io.github.davidnest.teste.service.car.CarService carService;

    @Test
    @DisplayName("deve decolver 404 com o erro padronizado quando o cliente não existe")
    void deveDevolver404QuandoClienteNapExiste() throws Exception {
        UUID id = UUID.randomUUID();
        when(clientService.findById(id))
                .thenThrow(new ClientNotFoundException("Cliente não encontrado! " + id));

        mockMvc.perform(get("/clients/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado! " + id))
                .andExpect(jsonPath("$.errors").isEmpty());

    }


    @Test
    @DisplayName("deve devolver 200 com os dados do cliente")
    void deveDevolver404QuandoClienteNaoExiste() throws Exception {
        UUID id = UUID.randomUUID();
        Client client = new Client("Maria", new BigDecimal("6000.00"), new ArrayList<>());
        when(clientService.findById(id)).thenReturn(client);


        mockMvc.perform(get("/clients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.balance").value(6000.00));

    }

}