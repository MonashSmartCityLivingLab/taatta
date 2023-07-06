package edu.monash.humanise.smartcity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payload {
    @Id
    @SequenceGenerator(
            name = "df702_id_sequence",
            sequenceName = "df702_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "df702_id_sequence"
    )
    private Integer id;
    @Column(nullable = false)
    private String deviceName;
    @Column(nullable = false)
    private String data;
    @Column(nullable = false)
    private String devEUI;
    @Column(nullable = false)
    private boolean battery;
    @Column(nullable = false)
    private boolean isFull;
    @Column(nullable = false)
    private boolean fire;
    @Column(nullable = false)
    private int level;
    @Column(nullable = false)
    private int temperature;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

}
