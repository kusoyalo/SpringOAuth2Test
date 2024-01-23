package kuso.bean;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.context.annotation.SessionScope;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SessionScope
public class VisitBean implements Serializable{
	private static final long serialVersionUID = 9009724507183571094L;
	
	private String loginType;
	
	private String userID;
	
	private String email;
	
	private List<String> roleList;
	
//	private boolean isLogout;
}