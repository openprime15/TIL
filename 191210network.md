### 네트워크의 이해

* 이더넷 어댑터

  * = LAN 카드
  * = NIC(Network Interface Controller)

* Ethernet

  * LAN 영역에서 사용하는 통신기술 중 하나
  * LAN 영역에서 사용하는 기술 중 사실상 표준

* IPv4 주소

  * 총 32비트(0.0.0.0 ~ 255.255.255.255)로 구성된 주소체계
  * 2<sup>32</sup>개의 주소 표현이 가능 

* IPv6 주소

  * 총 128 비트
  * 2<sup>128</sup>개의 주소 포현이 가능

* IP(Internet Protocol)

  * 인터넷 공간에서 자기 PC가 사용하는 고유 식별자를 의미
  * IP 주소의 클래스(등급)
    * A 클래스 : 1~126 = 0000 0001 ~ 0111 1110
    * B 클래스 : 128~191 = 1000 0000 ~ 1011 1111
    * C 클래스 : 192~223 = 1100 0000 ~ 1101 1111
    * 구글에서 제공하는 DNS 서버의 IP 주소 = 8.8.8.8 → A 클래스
    * KT에서 제공하는 DNS 서버의 IP 주소 = 168.126.63.1 → B 클래스
    * 127.0.0.1 → 어떤 클래스에도 속하지 않음 → 자기가 사용하는 LAN카드 자신을 의미 = Loopback address

* 서브넷 마스크(Subnet mask)

  * IP 주소를 서브넷 마스크를 이용해 표기하는 방식

  * IP 주소를 네트워크 ID와 호스트 ID로 구분

    * | IP          | Subnet Mask | Network ID | Host ID             |
      | ----------- | ----------- | ---------- | ------------------- |
      | 10.10.10.10 | 255.0.0.0   | 10 (국번)  | 10.10.10 (전화번호) |

  * 게이트웨이(Gateway) = 라우터(Router) = 각기 다른 네트워크 ID를 사용하는 LAN영역을 연결

* LAN 영역

  * 동일한 네트워크 ID를 공유하는 장치들의 집합
  * 동일한 게이트웨이 주소를 사용하는 장치들의 집합

* 라우팅(Routing)  ⇒ 다른 네트워크 ID를 사용하는 LAN영역을 연결

* 스위칭(Swiching) ⇒ LAN 영역에서 MAC 주소에 기반한 내부 통신

* MAC 주소 = 물리적 주소 (ipconfig /all로 확인가능)

  * LAN 카드에 부여된 주소로 LAN 영역에서 내부 통신을 수행하기 위해 필요한 주소
  * 48비트 = OUI + 일련번호

* DHCP(Dynamic Host Configuration Protocol) ⇒ 유동 IP 환경

  * 사용할 IP 주소 범위를 서버에 미리 등록하면 PC 사용자에게 IP주소, 서브넷마스크, 게이트웨이 주소, KNS 주소 등을 자동으로 할당해주는 서비스

* DNS(Domain Name System) 서버

  * 도메인 이름과 IP 주소의 대응 관계를 데이터베이스 형태로 저장해 사용하는 서버

* IP = 32비트 = 네트워크 ID + 호스트 ID ⇒ IP 주소 기반에 라우팅

* MAC = 48비트 = OUI + 일련번호 ⇒ MAC 주소 기반에 스위칭

* ping

  * 출발지 호스트(내 pc)와 목적지 호스트(8.8.8.8) 사이에서 회선의 연결 상태나 목적지 운영체제 의 동작 여부를 점검하기 위한 도구

* ettercap

  * LAN 환경에서 중간자 공격을 수행할 수 있도록 구현한 프로그램
  * GUI와 다양한 플러그인을 제공

* nmap

  * 네트워크에 연결되어 있는 호스트의 정보를 파악하는 도구
  * 서버의 열린 포트
  * 서비스하는 소프트웨어 버전

* nmap을 이용한 포트 스캐닝

  * 타겟 서버의 포트 상태를 확인

* TCP open Scan

  * 정상적인 TCP 3-way handshaking 과정을 통해서 사용중인 포트를 확인
  * 포트가 열려 있으면         : SYN → SYN/ACK→ACK
  * 포트가 열려있지 않으면 : SYN → RST/ACK
  * 연결에 대한 로그가 남기 때문에 안전하지 않은 방법

* Stealth Scan

  * 3 way handshaking 과정을 거치지 않기 때문에 로그가 남지 않는다.
  * TCP half open scan = TCP SYN open scan ⇒ -sS 
  * FIN scan, XMAS scan, NULL scan - 비정상적인 TCP 플래그를 전달
    * FIN : FIN                                                             ⇒  -sF
    * XMAS : FIN, PSH, URG 태그가 옴                    ⇒ -sX
    * NULL : 아무것도 설정되지 않는 상태에서 옴 ⇒ -sN
    * → 포트가 열려 있으면 → 무응답
    * → 포트가 닫혀 있으면 → RST/ACK