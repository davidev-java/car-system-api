package io.github.davidnest.teste.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 30)
    private String name;

    @Column(length = 50)
    private String model;

    private Integer year;

    private Integer hp;

    @Column(nullable = false,precision = 19,scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Version
    private Long version;

    protected Car(){}

    public Car(String name, String model, Integer year, Integer hp, BigDecimal price, Client client) {
        this.name = name;
        this.model = model;
        this.year = year;
        this.hp = hp;
        this.price = price;
        this.client = client;
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

    public String getModel() {
        return model;
    }

    public void setModel(String modelo) {
        this.model = modelo;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer ano) {
        this.year = ano;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public BigDecimal getPrice() {return price;}

    public void setPrice(BigDecimal price) {this.price = price;}

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
