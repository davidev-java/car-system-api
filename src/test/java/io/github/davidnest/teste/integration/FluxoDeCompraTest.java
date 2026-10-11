package io.github.davidnest.teste.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FluxoDeCompraTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve criar um client")
    void deveCriarUmClient() throws Exception {
        String json = """
                {"name":"Maria","balance":6000.00, "carIds":[]} 
                """;

        String resposta = mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.balance").value(6000.00))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = JsonPath.read(resposta, "$.id");
        assertThat(id).isNotBlank();
    }

    @Test
    @DisplayName("Deve fazer uma compra de um carro")
    void deveComprarUmCarro() throws Exception {
        String clientId = criarClient("Maria", "6000.00");
        String carId = criarCar("Gol", "2500.00");

        mockMvc.perform(post("/clients/{clientId}/cars/{carId}", clientId, carId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(3500.00))
                .andExpect(jsonPath("$.carIds[0]").value(carId));
    }

    private String criarClient(String name, String balance) throws Exception {
        String json = """
                {"name":"%s", "balance":%s, "carIds":[]}
                """.formatted(name, balance);

        String resposta = mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(resposta, "$.id");
    }

    private String criarCar(String name, String price) throws Exception {
        String json = """
                {"name":"%s","model":"Teste","hp":80,"year":2012,"price":%s}
                """.formatted(name, price);

        String resposta = mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(resposta, "$.id");
    }
}
