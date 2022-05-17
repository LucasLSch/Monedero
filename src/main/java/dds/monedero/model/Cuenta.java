package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private Double saldo = 0.0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0.0;
  }

  public Cuenta(Double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(Double cuanto) {
    this.validarOperacionNegativa(cuanto);
    this.validarNumeroDeDepositosDiarios();
    this.agregarMovimiento(LocalDate.now(), cuanto, true);
    this.sumarSaldo(cuanto);
  }

  private void validarNumeroDeDepositosDiarios() {
    if (getMovimientos().stream().filter(movimiento -> movimiento.fueDepositado(LocalDate.now())).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  private void sumarSaldo(Double monto) {
    this.saldo += monto;
  }

  public void sacar(Double cuanto) {
    this.validarOperacionNegativa(cuanto);
    this.validarExtraccion(cuanto);
    this.agregarMovimiento(LocalDate.now(), cuanto, false);
    this.restarSaldo(cuanto);
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

  public void agregarMovimiento(LocalDate fecha, Double cuanto, Boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  private void validarOperacionNegativa(Double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto de la operacion debe ser un valor positivo");
    }
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public Double getSaldo() {
    return saldo;
  }

  public void setSaldo(Double saldo) {
    this.saldo = saldo;
  }

}
