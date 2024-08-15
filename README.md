# 🎯 Genel Yetenek Testi

Bu proje, organizasyonların genel yetenek testleri oluşturmasına, sorular eklemesine ve adaylarını bu testlere davet etmesine olanak tanıyan bir mikroservis mimarisi üzerine inşa edilmiş bir uygulamadır.

## 📂 Proje Yapısı

Proje, altı ana mikroservisten ve birkaç destekleyici servisten oluşmaktadır:

1. **Management Service**
2. **Question Service**
3. **Exam Service**
4. **Search Service**
5. **Mail Service**
6. **Discovery Server**
7. **Config Server**

Bu mikroservisler birbirleriyle asenkron olarak Kafka, senkron olarakta gRPC ve OpenFeign aracılığıyla haberleşmektedir.

## 🔧 Kullanılan Teknolojiler

- **Spring Boot**: Tüm mikroservisler için kullanılan ana çerçeve.
- **gRPC**: ExamService ile QuestionService arasında soru çekmek için kullanıldı.
- **OpenFeign**: Diğer servislerden Management Service'e authentication bilgilerini almak için kullanıldı.
- **Kafka**: Mikroservisler arasında asenkron mesajlaşma sağlandı.
- **MapStruct**: DTO ve entity mapping işlemleri için kullanıldı.
- **SLF4J**: Loglama işlemleri için tercih edildi ve loglar günlük olarak bir dosyaya kaydedildi.
- **Prometheus ve Grafana**: Monitoring ve gözlemleme için kullanıldı. Grafana üzerinde custom bir arayüz import edildi.
- **JUnit & Mockito**: Her servis için unit testler yazıldı.
- **MongoDB**: SearchService için verilerin saklandığı NoSQL veritabanı.
- **PostgreSQL**: Diğer servisler için kullanılan ilişkisel veritabanı.
- **Docker**: Tüm servisler ve yardımcı araçlar (Kafka, Prometheus, Grafana vb.) Docker container'ları içinde çalıştırılmaktadır.
- **Discovery Server (Eureka)**: Mikroservislerin keşfi ve yönetimi için kullanıldı.
- **Config Server**: Uygulama yapılandırmalarının merkezi bir yerden yönetilmesi için kullanıldı. Yapılandırma dosyaları GitHub'da barındırılmaktadır.

## Servisler

### 1. Management Service

Bu servis, yönetimsel işlemleri gerçekleştiren ana servistir. Admin ve Organizasyon kullanıcılarının yönetimi bu servis üzerinden yapılır.

#### Özellikler:

- **Admin İşlemleri**:

  - Yeni organizasyon oluşturma
  - Var olan organizasyonları listeleme ve güncelleme
  - Organizasyonları silme
  - Soru havuzuna anonim soru ekleme
  - Tüm soruları silme ve güncelleme
  - Test oluşturma ve güncelleme

- **Organizasyon İşlemleri**:
  - Kendi sorularını ekleyip yönetme
  - Kendi testlerini oluşturma ve güncelleme
  - Adayları e-posta ile testlere davet etme
  - Kendi kullanıcı adı ve parolasını güncelleme

### 2. Question Service

Bu servis, testlerde kullanılacak soruların yönetimini sağlar. Soruların eklenmesi, güncellenmesi ve silinmesi gibi işlemler burada gerçekleştirilir.

#### Özellikler:

- **Soru Yönetimi**:
  - Sorular eklenebilir, güncellenebilir ve silinebilir.
  - Sorular en az 2, en fazla 5 seçenek içerebilir.
  - Sorulara ve seçeneklere görsel ekleme desteği vardır.
  - Bir soru birden fazla testte kullanılabilir, ancak bir testte aynı soru yalnızca bir kez kullanılabilir.
  - Sorular, başka testlerde kullanılmışsa güncellemeye kapatılır.

### 3. Exam Service

Bu servis, sınavların oluşturulması ve yönetilmesini sağlar. Admin ve Organizasyon kullanıcıları bu servis üzerinden testler oluşturabilir ve yönetebilir.

#### Özellikler:

- **Test Yönetimi**:
  - Test oluşturma ve güncelleme
  - Testlere kurallar ekleme (Örneğin: Kamera zorunluluğu, mikrofon açık olmalıdır gibi)
  - Başlamamış testlere soru ekleme ve çıkarma
  - Testlerin başlangıç ve bitiş tarihlerine göre sınav statüsünü güncelleyen bir job (scheduled) eklenmiştir.

### 4. Search Service

Bu servis, QuestionService ve ExamService'den gelen kayıtların Kafka üzerinden alınarak MongoDB'ye kaydedilmesini sağlar. Ayrıca, dinamik sorgularla bu kayıtlar üzerinde arama yapılmasına olanak tanır.

#### Özellikler:

- **Veri Yönetimi**:
  - Kafka ile gelen veriler MongoDB'ye kaydedilir.
  - Dinamik sorgularla MongoDB üzerinden kayıtlar aranabilir.

### 5. Mail Service

Bu servis, organizasyon kullanıcılarının adayları testlere davet etmesini sağlar. Davetler e-posta yoluyla iletilir.

#### Özellikler:

- **E-posta Gönderimi**:
  - Adayları testlere davet etmek için e-posta gönderimi
  - E-posta gönderim işlemi Kafka ile asenkron olarak gerçekleştirilir.

### 6. Discovery Server

Bu servis, tüm mikroservislerin birbirini bulmasını ve tanımasını sağlar. Eureka Server olarak yapılandırılmıştır.

#### Özellikler:

- **Mikroservis Keşfi**:
  - Tüm servislerin birbirini bulmasını sağlar.
  - Dinamik olarak mikroservislerin IP adreslerini ve portlarını yönetir.

### 7. Config Server

Bu servis, mikroservislerin yapılandırma dosyalarını merkezi bir yerden yönetir. Yapılandırmalar GitHub'da barındırılmaktadır.

#### Özellikler:

- **Merkezi Yapılandırma Yönetimi**:
  - Tüm mikroservisler için yapılandırma dosyalarını merkezi bir yerden sağlar.
  - Yapılandırma dosyaları GitHub'da tutulur ve her servis başlatıldığında bu dosyaları alır.

## Ortak Yapılar

Projemizde ortak yapılar için **core** ve **common** paketleri kullanılmıştır. Bu paketler diğer mikroservislerde bağımlılık olarak eklenmiştir. Bu paketler, genel amaçlı yardımcı sınıflar, DTO'lar ve diğer ortak bileşenleri içerir.

## Veri Tabanı

- **PostgreSQL**: QuestionService, ExamService ve ManagementService için ilişkisel veritabanı olarak kullanılır.
- **MongoDB**: SearchService için NoSQL veritabanı olarak kullanılır.

## Docker

Projede tüm servisler ve ilgili bileşenler (Kafka, Prometheus, Grafana vb.) Docker container'ları içerisinde çalıştırılmaktadır.

## 📦 Kurulum ve Çalıştırma

1. **Projeyi Klonlayın**:

   ```bash
   git clone https://github.com/omer-ylmz/General-Aptitude-Test.git
   cd General-Aptitude-Test
   ```

2. **Docker Compose ile Yapıları Başlatın**:
   Projedeki Kafka, Prometheus, Grafana ve diğer bağımlılıkları Docker Compose kullanarak başlatabilirsiniz.
   ```bash
   docker-compose up -d
   ```

## 📊 Monitoring ve Gözlemleme

1. Prometheus ve Grafana entegrasyonuyla sisteminize ait verileri kolayca izleyebilir ve analiz edebilirsiniz. Aşağıda Prometheus ve Grafana'dan bir ekran görüntüsü bulunmaktadır:

## PROMETHEUS

![image](https://github.com/user-attachments/assets/da4956a8-362a-4830-ab20-ad7ca65fc7f7)

## GRAPHANA

![image](https://github.com/user-attachments/assets/e2479697-6f7c-434e-977b-2c6b4762889c)

![image](https://github.com/user-attachments/assets/10d6c806-7dc0-46f1-9d3a-f55536fb7740)

2. **Eureka Dashboard'u Kontrol Edin**:
   Discovery Server üzerinde tüm mikroservislerin sağlıklı bir şekilde başlatıldığını doğrulamak için Eureka Dashboard'u ziyaret edebilirsiniz.

   ![image](https://github.com/user-attachments/assets/51e4c434-dbfb-4c20-920f-1dd66ae5221d)

## 👥 Katkıda Bulunma

Katkıda bulunmak isterseniz, lütfen bir pull request gönderin. Hataları bildirmek için bir issue açabilirsiniz.
