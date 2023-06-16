package edu.monash.humanise.smartcity;

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
            name = "df702_id_sequence",
            sequenceName = "df702_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "df702_id_sequence"
    )
    private Integer id;
    private String deviceName;
    private String data;
    private String devEUI;
    private boolean battery;
    private boolean isFull;
    private boolean fire;
    private int level;
    private int temperature;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
