package io.github.davidnest.teste.model.exception.car;

public class CarAlreadySoldException extends RuntimeException {
    public CarAlreadySoldException(String message) {
        super(message);
    }
}
