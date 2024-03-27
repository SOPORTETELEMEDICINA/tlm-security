package net.amentum.security.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author marellano
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAssignedRequestView implements Serializable {

    private Long idProfile;
    private List<Long> idGroups;
}
