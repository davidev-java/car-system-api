package io.github.davidnest.teste.controller;

import io.github.davidnest.teste.controller.dto.car.CarRequest;
import io.github.davidnest.teste.controller.dto.car.CarResponse;
import io.github.davidnest.teste.model.entity.Car;
import io.github.davidnest.teste.model.entity.Client;
import io.github.davidnest.teste.service.car.CarService;
import io.github.davidnest.teste.service.client.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService service;
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<CarResponse> save(@RequestBody @Valid CarRequest request) {
        Car salvo = service.save(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
    }

    @PostMapping("/lote")
    public ResponseEntity<List<CarResponse>> saveAll(@RequestBody List<@Valid CarRequest> requests){
        List<Car> cars = requests.stream().map(this::toEntity).toList();
        List<CarResponse> response = service.saveAll(cars).stream().map(this::toResponse).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> findCarById(@PathVariable UUID id) {
        Car car = service.findById(id);
        return ResponseEntity.ok(toResponse(car));
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> findAllCars() {
        List<CarResponse> cars = service.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(cars);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable UUID id, @RequestBody @Valid CarRequest request) {
        Car car = service.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarById(@PathVariable UUID id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCars(){
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }

    private CarResponse toResponse(Car car) {
        UUID clientId = car.getClient() == null ? null : car.getClient().getId();
        return new CarResponse(car.getId(), car.getName(), car.getModel(), car.getHp(), car.getYear(), car.getPrice(), clientId);
    }

    private Car toEntity(CarRequest request) {
        Client client = request.clientId() == null ? null : clientService.findById(request.clientId());
        return new Car(request.name(), request.model(), request.year(), request.hp(), request.price(), client);
    }
}
