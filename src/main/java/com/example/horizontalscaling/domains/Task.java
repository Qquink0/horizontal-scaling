package com.example.horizontalscaling.domains;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task implements Serializable {

    String id;

    Integer fromServerPort;

}
