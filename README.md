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
## 로그아웃
- redis 로 구현해야 할 것 같음.
- 논의가 필요함


# ec2 scp 통신
scp -i ~/Documents/aws/shinhanMJ.pem mate.jar ubuntu@3.34.90.140:/home/ubuntu


# 스프링 API 에러 처리
에러 처리 실패시 기본 서블릿 처리까지 간다. 즉 WAS 까지 가버린다는것.
그중 api 에러 처리에는 ExceptionHandler가 가장 적합함. 커스텀을 적용할수도 있고

# 파일과 DTO 같이 받을땐
File과 Dto를 같이 받기 위해서는 @RequestPart라는 어노테이션이 필요합니다!!

# CI/CD 적용하기
도커에 젠킨스 띄우고 ec2에 스프링부트 올리게 구축하자 12/6에 할일~

# CI/CD

test server 13.209.43.209
jenkins sever 43.201.38.135
url 43.201.38.135:9000

http://13.209.43.209:8080/auth/signup

# 12/20
Error code 수정
Validation Handler에서 다 잡아버려서 중복 체크 에러가 처리되지 않음.
이를 분리해서 service에서 중복 체크하게 수정했음.
오류 코드 또한 수정했다.
RFC 7231을 보면 중복값의 경우 Conflict 409로 보낸다고 정의됨.
>The request could not be completed due to a conflict with the current state of the target resource. This code is used in situations where the user might be able to resolve the conflict and resubmit the request. The server SHOULD generate a payload that includes enough information for a user to recognize the source of the conflict.
-RFC 7231