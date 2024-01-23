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
public class VerifyEmailPK implements Serializable{
	private static final long serialVersionUID = -1158117881221794344L;
	
	private String loginType;
	private String userID;
}
