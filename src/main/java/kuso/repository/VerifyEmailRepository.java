package kuso.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kuso.entity.VerifyEmail;
import kuso.entity.VerifyEmailPK;

@Repository
public interface VerifyEmailRepository extends CrudRepository<VerifyEmail,VerifyEmailPK>{
	@Query(" from VerifyEmail where loginType = :loginType and userID = :userID")
	public VerifyEmail getByTypeAndID(@Param("loginType")String loginType,@Param("userID")String userID);
}
