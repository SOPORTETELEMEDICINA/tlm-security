package net.amentum.security.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventMessage implements Serializable{
    private String queue;
    private String code;
    private String toAgent;
    private List<Map<String,Object>> messages = new ArrayList<>();
}
