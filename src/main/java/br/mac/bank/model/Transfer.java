package br.mac.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    private Account origin;

    @ManyToOne
    private Account destination;

    private BigDecimal amount;

    @ManyToOne
    private Currency currency;

    private String description;

    private BigDecimal taxCollected;

    private transient BigDecimal CAD;

    @ManyToOne
    private User user;

}
