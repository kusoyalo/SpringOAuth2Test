package kuso.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kuso.entity.UserInfo;
import kuso.entity.UserInfoPK;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo,UserInfoPK>{
	@Query(" from UserInfo where loginType = :loginType and userID = :userID")
	public UserInfo getByTypeAndID(@Param("loginType")String loginType,@Param("userID")String userID);
}
