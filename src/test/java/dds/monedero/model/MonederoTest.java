package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Poner() {
    cuenta.poner(1500.0);
    assertEquals(cuenta.getSaldo(), 1500.0);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500.0));
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500.0);
    cuenta.poner(456.0);
    cuenta.poner(1900.0);
    assertEquals(cuenta.getCantidadDepositosDe(LocalDate.now()), 3);
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500.0);
          cuenta.poner(456.0);
          cuenta.poner(1900.0);
          cuenta.poner(245.0);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90.0);
          cuenta.sacar(1001.0);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000.0);
      cuenta.sacar(1001.0);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500.0));
  }

  @Test
  public void MontoExtraidoHoy() {
    cuenta.setSaldo(2000.0);
    cuenta.sacar(500.0);
    cuenta.sacar(150.0);
    assertEquals(cuenta.getMontoExtraidoA(LocalDate.now()), 650.0);
  }

}