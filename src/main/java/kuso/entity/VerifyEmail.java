package kuso.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="VERIFYEMAIL")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(VerifyEmailPK.class)
public class VerifyEmail implements Serializable{
	private static final long serialVersionUID = -3684038833758996215L;

	@Id
	@Column(name="LOGIN_TYPE")
	private String loginType;
	
	@Id
	@Column(name="USER_ID")
	private String userID;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="VERIFY_CODE")
	private String verifyCode;
	
	@Column(name="VERIFY_EXPIRE_DATE")
	private Date verifyExpireDate;
}
