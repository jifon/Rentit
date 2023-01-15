package com.neobis.rentit.model.idclasses;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class FollowerId  implements Serializable {

    private Long userIdFollower;
    private Long userIdFollowed;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FollowerId that = (FollowerId) o;

        if (!Objects.equals(userIdFollower, that.userIdFollower))
            return false;
        return Objects.equals(userIdFollowed, that.userIdFollowed);
    }

    @Override
    public int hashCode() {
        int result = userIdFollower != null ? userIdFollower.hashCode() : 0;
        result = 31 * result + (userIdFollowed != null ? userIdFollowed.hashCode() : 0);
        return result;
    }


}
