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
            name = "tbs220_id_sequence",
            sequenceName = "tbs220_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tbs220_id_sequence"
    )
    private Integer id;
    @Column(nullable = false)
    private String deviceName;
    @Column(nullable = false)
    private String data;
    @Column(nullable = false)
    private String devEUI;
    @Column(nullable = false)
    private boolean parkFlag;
    @Column(nullable = false)
    private int frameCount;
    @Column(nullable = false)
    private int status;
    @Column(nullable = false)
    private int battery;

    @Column(nullable = false)
    private OffsetDateTime timestamp;
}
