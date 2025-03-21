# 만들게된 계기
코인 노래방을 친구들과 같이 자주 가는데, 나만의 애창곡을 검색하고 즐겨찾기로 추가해서 관리할수 있는 앱이 좋겠다 생각이 들어서 만들어 보게 되었습니다.

# favorite-song-backend

# 서버정보
- ec2
- Route 53
- nginx
- lets encrypt
- docker-compose
- backend -> 8080, fronend & nginx -> 80, 443, mariadb -> 3306
- https://favorite-song.store (aws 요금으로 평소에 꺼놓음, 사용할때만 킴)

# 네트워크 구성도
![image](https://github.com/user-attachments/assets/0798d765-0913-4433-863b-da31f3dfebf4)

# 개발환경
- inteliJ
- Spring boot
- Gradle
- h2 (local)
- mariadb (prod)

# 사용기술
- spring data jpa
- querydsl
- oauth2
- spring-boot-starter-mail
- spring security
- jwt
- spring-boot-starter-validation
- p6spy

# openApi karaoke
- https://api.manana.kr/v2/karaoke/search.json
- https://api.manana.kr/v2/karaoke/release.json

# frontend repository url
- https://github.com/aamoos/favorite-song.git

# backend repository url
- https://github.com/aamoos/favoriteSongBackend.git

# Android repository url
- https://github.com/aamoos/favortie-song-webview-android.git

# 화면
## 로그인
![image](https://github.com/aamoos/favorite-song/assets/37327676/a23031dd-53ee-45ba-94e8-f9b681b9af67)
- oauth2 (카카오, 네이버, 구글) 소셜 로그인과, 회원가입으로 앱에서 따로 가입한 회원들 대상으로 로그인이 가능하게 구현하였습니다.

## 회원가입
![image](https://github.com/aamoos/favorite-song/assets/37327676/aaf1c30d-3dd3-4b32-b356-853b0cfcee07)
- vuelidate를 사용해서 validation 처리를 하였으며, 정상적으로 회원가입시 메일로 인증번호가 발송이 됩니다.

## 인증번호 입력창
![image](https://github.com/aamoos/favorite-song/assets/37327676/ecd51847-e2c0-4e7e-94e7-167ed16be83e)
- 5분안에 입력을 해야하며 메일로 발송된 인증번호를 맞게 입력해야 회원가입이 진행됩니다.

## 인증번호 메일
![image](https://github.com/aamoos/favorite-song/assets/37327676/2a3612ea-c8a5-4459-92ea-0c92ba0d4109)

## 패스워드 찾기
![image](https://github.com/aamoos/favorite-song/assets/37327676/5d8886bc-2474-4a42-9b30-56a15782738e)
- 회원기입한 이메일, 사용자이름을 작성해서 비밀번호 찾기 클릭시, 인증번호창이 나옵니다.

## 패스워드 찾기 인증번호 창
![image](https://github.com/aamoos/favorite-song/assets/37327676/bef88445-fff0-4ba0-ac04-34b9b92df150)
- 메일로 발송된 인증번호를 올바르게 입력하면 임시비밀번호가 메일로 발송되고 변경이 됩니다.

## 임시 비밀번호 메일
![image](https://github.com/aamoos/favorite-song/assets/37327676/df820a72-a290-42c9-bfb8-dd0861685dc5)

## 출시일 검색
![image](https://github.com/aamoos/favorite-song/assets/37327676/a559b5df-dc1a-42c6-baca-24fa8c29cd5a)
- 출시일을 조건으로 검색시 해당 YYYYMM에 발표한 곡들이 조회가 됩니다.

## 노래찾기
![image](https://github.com/aamoos/favorite-song/assets/37327676/182aa8ac-74e9-41ca-92ea-db12cc261f65)
- 금영, tj, dam, joysound를 대상으로 제목, 가수명, 곡번호를 조회조건으로 해당하는 노래를 검색할수 있습니다.

## 즐겨찾기
![image](https://github.com/aamoos/favorite-song/assets/37327676/700d1e62-97ca-476d-9f1a-00000f518cd2)
- 출시일 검색, 노래찾기에서 검색한 항목을 즐겨찾기한 항목들을 볼수있습니다.

## 공통 기능
- 유튜브 버튼 클릭시 해당 곡을 유튜브에 검색해서 창이 열립니다.
- Like 버튼 클릭시 즐겨찾기 메뉴에서 볼수 있습니다.



