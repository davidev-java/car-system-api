package io.github.davidnest.teste.model.entity;

import io.github.davidnest.teste.model.exception.client.InsufficientBalanceException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 30)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Version
    private Long version;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Car> cars = new ArrayList<>();

    protected Client(){
    }

    public Client(String name, BigDecimal balance, List<Car> cars) {
        this.name = name;
        this.balance = balance;
        this.cars = cars;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {return balance;}

    public List<Car> getCars() {
        return Collections.unmodifiableList(cars);
    }

    public void addCar(Car car){
         cars.add(car);
         car.setClient(this);
    }

    public void removeCar(Car car){
        cars.remove(car);
        car.setClient(null);
    }

    public void debit(BigDecimal amount){
        if(balance.compareTo(amount) < 0){
            throw new InsufficientBalanceException("Saldo insuficiente para comprar este carro!");
        }
        balance = balance.subtract(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Client other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
