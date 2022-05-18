package dds.monedero.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Movimiento {
  private LocalDate fecha;
  // Nota: En ningún lenguaje de programación usen jamás doubles (es decir, números con punto flotante) para modelar dinero en el mundo real.
  // En su lugar siempre usen numeros de precision arbitraria o punto fijo, como BigDecimal en Java y similares
  // De todas formas, NO es necesario modificar ésto como parte de este ejercicio. 
  private Double monto;

  public Movimiento(LocalDate fecha, Double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public Double getMonto() {return  this.monto;}

  public Boolean esDeLaFecha(LocalDate fecha) {
    return this.getFecha().isEqual(fecha);
  }

  private LocalDate getFecha() {
    return this.fecha;
  }
}
