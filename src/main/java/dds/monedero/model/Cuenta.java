package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cuenta {

  private Double saldo = 0.0;
  private List<Movimiento> depositos = new ArrayList<>();
  private List<Movimiento> extracciones = new ArrayList<>();

  public Cuenta() {
    saldo = 0.0;
  }

  public Cuenta(Double montoInicial) {
    saldo = montoInicial;
  }

  public void poner(Double monto) {
    this.validarOperacionNegativa(monto);
    this.validarNumeroDeDepositosDiarios();
    this.agregarDeposito(LocalDate.now(), monto);
    this.sumarSaldo(monto);
  }

  private void validarNumeroDeDepositosDiarios() {
    if (this.getCantidadDepositosDe(LocalDate.now()) >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  private void sumarSaldo(Double monto) {
    this.saldo += monto;
  }

  public void sacar(Double monto) {
    this.validarOperacionNegativa(monto);
    this.validarExtraccion(monto);
    this.agregarExtraccion(LocalDate.now(), monto);
    this.restarSaldo(monto);
  }

  private void validarExtraccion(Double montoOperacion) {
    this.validarSaldoSuficiente(montoOperacion);
    this.validarLimiteDeExtraccionSuficiente(montoOperacion);
  }

  private void validarSaldoSuficiente(Double montoOperacion) {
    if (montoOperacion > this.getSaldo()) {
      throw  new SaldoMenorException("No puede sacar mas de " + this.getSaldo() + " $");
    }
  }

  private void validarLimiteDeExtraccionSuficiente(Double montoOperacion) {
    Double limite = 1000 - getMontoExtraidoA(LocalDate.now());
    if (montoOperacion > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
  }

  private void restarSaldo(Double monto) {
    this.saldo -= monto;
  }

  public void agregarDeposito(LocalDate fecha, Double monto) {
    this.getDepositos().add(new Deposito(fecha, monto));
  }

  public void agregarExtraccion(LocalDate fecha, Double monto){
    this.getExtracciones().add(new Extraccion(fecha, monto));
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return this.getExtracciones().stream()
        .filter(extraccion -> extraccion.esDeLaFecha(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  private void validarOperacionNegativa(Double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto de la operacion debe ser un valor positivo");
    }
  }

  public Long getCantidadDepositosDe(LocalDate fecha) {
    return this.getDepositos()
          .stream()
          .filter(deposito -> deposito.esDeLaFecha(fecha)).count();
  }

  public List<Movimiento> getExtracciones() {
    return this.extracciones;
  }

  public List<Movimiento> getDepositos() {
    return this.depositos;
  }

  public Double getSaldo() {
    return saldo;
  }

  public void setSaldo(Double saldo) {
    this.saldo = saldo;
  }

}
