package io.github.davidnest.teste.service.client;
import io.github.davidnest.teste.model.entity.Car;
import io.github.davidnest.teste.model.entity.Client;
import io.github.davidnest.teste.model.entity.Purchase;
import io.github.davidnest.teste.model.exception.car.CarAlreadySoldException;
import io.github.davidnest.teste.model.exception.client.ClientNotFoundException;
import io.github.davidnest.teste.repository.ClientRepository;
import io.github.davidnest.teste.repository.PurchaseRepository;
import io.github.davidnest.teste.service.car.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final CarService carService;
    private final PurchaseRepository purchaseRepository;

    @Transactional
    public Client save(Client client, List<UUID> carIds){
        if(carIds == null || carIds.isEmpty()){
            return clientRepository.save(client);
        }

        List<Car> cars = carIds.stream()
                .map(carService::findById)
                .collect(Collectors.toCollection(ArrayList::new));

        cars.forEach(client::addCar);

        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public Client findById(UUID id){
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado! " + id));
    }

    @Transactional(readOnly = true)
    public void deleteById(UUID id){
        findById(id);
        clientRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public void deleteAll(){
        clientRepository.deleteAll();
    }

    @Transactional
    public Client buyCar(UUID clientId, UUID carId){
        Client client = clientRepository.findByIdComTrava(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado! "
                        + clientId));
        Car car = carService.findById(carId);

        if(car.getClient() != null){
            throw new CarAlreadySoldException("Carro já pertence a outro cliente!");
        }

        client.debit(car.getPrice());
        client.addCar(car);

        Purchase purchase = new Purchase(client, car, car.getPrice(), client.getBalance());
        purchaseRepository.save(purchase);

        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public Page<Purchase> findPurchases(UUID clientId, Pageable pageable){
        findById(clientId);
        return purchaseRepository.findByClientId(clientId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Client> findAll(){ //? FUNCIONANDO
        return clientRepository.findAll();
    }
}





