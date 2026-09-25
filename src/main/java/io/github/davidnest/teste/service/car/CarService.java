package io.github.davidnest.teste.service.car;

import io.github.davidnest.teste.controller.dto.car.CarResponse;
import io.github.davidnest.teste.model.entity.Car;
import io.github.davidnest.teste.model.exception.car.CarNotFoundException;
import io.github.davidnest.teste.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository repository;

    public Car save(Car car){
        if(car.getHp() != null && car.getHp() <= 0){
            throw new IllegalArgumentException("HP deve ser maior que zero!");
        }

        return repository.save(car);
    }

    @Transactional
    public List<Car> saveAll(List<Car> cars){
        cars.forEach(car -> validarHp(car.getHp()));
        return repository.saveAll(cars);
    }

    @Transactional(readOnly = true)
    public Car findById(UUID id){
        return repository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Carro não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Car> findAll(){
        return repository.findAll();

    }

    public Car update(UUID id, Car newCar){
        Car car = findById(id);
        validarHp(newCar.getHp());

        car.setName(newCar.getName());
        car.setModel(newCar.getModel());
        car.setYear(newCar.getYear());
        car.setHp(newCar.getHp());
        car.setPrice(newCar.getPrice());
        car.setClient(newCar.getClient());

        return repository.save(car);
    }

    @Transactional(readOnly = true)
    public void deleteById(UUID id){
        findById(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public void deleteAll(){
        repository.deleteAll();
    }

    private void validarHp(Integer hp){
        if(hp != null && hp <= 0){
            throw new IllegalArgumentException("HP deve ser maior que zero!");
        }
    }
}

