# MateCarPool
MATE 카풀 (spring boot + queryDSL + JPA + EC2 + RDS)


## QueryDSL 적용시 주의점
기존 jpa repositroy에 QueryDSL repository 추가하려면 주의점이 필요하다.
interface는 Custom+{EntityName}+Repository
구현한 class 는 Custom+{EntityName}+RepositoryImpl 로 해야 
스프링에서 인식한다. 나중에 작성시 유의!

https://github.com/ParkJiwoon/practice-codes/tree/master/spring-security-jwt/src/main/java/com/tutorial/jwtsecurity/service