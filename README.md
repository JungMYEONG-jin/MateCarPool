# MateCarPool
MATE 카풀 (spring boot + queryDSL + JPA + EC2 + RDS)


## QueryDSL 적용시 주의점
기존 jpa repositroy에 QueryDSL repository 추가하려면 주의점이 필요하다.
interface는 Custom+{EntityName}+Repository
구현한 class 는 Custom+{EntityName}+RepositoryImpl 로 해야 
스프링에서 인식한다. 나중에 작성시 유의!

https://github.com/ParkJiwoon/practice-codes/tree/master/spring-security-jwt/src/main/java/com/tutorial/jwtsecurity/service

# 11/14 
## 문제점
- 요일 선택이 회원가입시 필수적으로 돼야함.
- 그렇지 않으면 이전 프로젝트 구조상 에러 발생
- 회원가입시 핸드폰 번호 입력칸 필수
- front에서 server에 값 전송할때 학번이랑 패스워드 일치시켜서 보내줘야함
- 현재 로그인시 jwt 생성을 phonenumber, password 기반 생성중
- member이름 달라도 phonenumber 학번이 일치하면 토큰을 발급하는중
- 만약 사용자가 핸드폰 번호가 본인을 결정짓는 구분인걸 알고나서
- 이를 악용한다면?
- 그럴일 없을수도..?
## 기능 테스트
- 회원가입 O
- 로그인 O
- 토큰 만료 시간 O
- 토큰으로 접속 정보 가져오기 O
- 토큰 재발급 O
- 가입시 시간표 안고르고 선택해도 가입 O


# 11/15
## 헥사고날 아키텍처
- 헥사고날 아키텍쳐의 특징은 어플리케이션의 핵심인 비지니스로직이 아무런 의존성을 갖지 않는 도메인에 집중된다는 것이다.
- 즉 기술적인 코드와 분리 가능!

