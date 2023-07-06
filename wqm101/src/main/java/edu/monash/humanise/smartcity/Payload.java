package edu.monash.humanise.smartcity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payload {

    @Id
    @SequenceGenerator(
            name = "wqm101_id_sequence",
            sequenceName = "wqm101_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "wqm101_id_sequence"
    )
    private Integer id;
    @Column(nullable = false)
    private String deviceName;
    @Column(nullable = false)
    private String data;
    @Column(nullable = false)
    private String devEUI;
    @Column(nullable = false)
    private double temperature;
    @Column(nullable = false)
    private double tds;

    @Column(nullable = false)
    private OffsetDateTime timestamp;
}
