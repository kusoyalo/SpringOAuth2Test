package kuso.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoPK implements Serializable{
	private static final long serialVersionUID = 5741936502076329533L;

	private String loginType;
	private String userID;
}
