package io.github.davidnest.teste.controller;

import io.github.davidnest.teste.controller.dto.client.ClientRequest;
import io.github.davidnest.teste.controller.dto.client.ClientResponse;
import io.github.davidnest.teste.controller.dto.purchase.PurchaseResponse;
import io.github.davidnest.teste.model.entity.Car;
import io.github.davidnest.teste.model.entity.Client;
import io.github.davidnest.teste.model.entity.Purchase;
import io.github.davidnest.teste.service.client.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @PostMapping
    public ResponseEntity<ClientResponse> saveClient(@RequestBody @Valid ClientRequest request){
        Client client = new Client(request.name(), request.balance(), new ArrayList<>());
        Client salvo = service.save(client, request.carIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findByid(@PathVariable UUID id){
        Client client = service.findById(id);
        return ResponseEntity.ok(toResponse(client));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> findAllClients(){
        List<ClientResponse> clients = service.findAll().stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(clients);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarById(@PathVariable UUID id){
         service.deleteById(id);
         return ResponseEntity.noContent().build();
    }

    @PostMapping("/{clientId}/cars/{carId}")
    public ResponseEntity<ClientResponse> buyCar(@PathVariable UUID clientId,@PathVariable UUID carId){
        Client client = service.buyCar(clientId, carId);
        return ResponseEntity.ok(toResponse(client));
    }

    @GetMapping("/{clientId}/purchases")
    public ResponseEntity<Page<PurchaseResponse>> findPurchases(@PathVariable UUID clientId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<PurchaseResponse> purchases = service.findPurchases(clientId, pageable)
                .map(this::toResponse);

        return ResponseEntity.ok(purchases);
    }

    private PurchaseResponse toResponse(Purchase purchase){
        return new PurchaseResponse(
                purchase.getId(),
                purchase.getCar().getId(),
                purchase.getCar().getName(),
                purchase.getAmount(),
                purchase.getBalanceAfter(),
                purchase.getCreatedAt());
    }


    private ClientResponse toResponse(Client client) {
        List<UUID> carIds = client.getCars()
                .stream()
                .map(Car::getId)
                .toList();
        return new ClientResponse(client.getId(), client.getName(), client.getBalance(), carIds);
    }
}
