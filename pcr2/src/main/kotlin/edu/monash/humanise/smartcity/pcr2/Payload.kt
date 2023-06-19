package edu.monash.humanise.smartcity.pcr2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payload {

    @Id
    @SequenceGenerator(
            name = "pcr2_id_sequence",
            sequenceName = "pcr2_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pcr2_id_sequence"
    )
    private Integer id;
    private String deviceName;
    private String data;
    private String devEUI;
    private int ltr;
    private int rtl;
    private int cpuTemp;


    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
