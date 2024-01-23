package kuso.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="USERINFO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserInfoPK.class)
public class UserInfo implements Serializable{
	private static final long serialVersionUID = 6879482906286384197L;
	
	@Id
	@Column(name="LOGIN_TYPE")
	private String loginType;
	
	@Id
	@Column(name="USER_ID")
	private String userID;
	
	@Column(name="PAZZWORD")
	private String pazzword;
	
	@Column(name="NEED_CHANGE_PAZZ")
	private String needChangePazz;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="ROLE")
	private String role;
}
