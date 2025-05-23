# 📣 Starly Notification Backend

Java + Spring Boot ile geliştirilen bu servis, mobil cihazlara Firebase (Android) ve APNs (iOS) üzerinden push bildirimleri göndermek amacıyla tasarlanmıştır. Cihaz token kaydı, kullanıcı bildirim tercihleri ve zamanlanmış bildirimler gibi temel işlevleri destekler.

---

## 🚀 Özellikler

- Cihaz token kaydı (FCM + APNs)
- Tekil cihaza bildirim gönderimi
- Broadcast (tüm cihazlara) bildirim gönderimi
- Kullanıcı bildirim tercihleri (zaman dilimi, saat)
- Arka planda çalışan cron job ile zamanlanmış bildirimler

---

## 🧱 API Uç Noktaları

### `POST /device-token/register`

📌 Cihaz bilgilerini kaydeder (FCM + APNs token'ları dahil).

#### Örnek İstek:
```json
{
  "deviceId": "23044A45-6B4D-40EF-8AA0-6AE68F1A4328",
  "fcmToken": "fcm-token",
  "apnsToken": "apns-token",
  "platform": "ios"
}
```

### `POST /push/send`
📌 Belirli bir cihaza bildirim gönderir.

#### Örnek İstek:
```json
{
  "deviceId": "23044A45-6B4D-40EF-8AA0-6AE68F1A4328",
  "title": "utku selam",
  "body": "bildirim geldiyse gruptan yaz"
}
```
### `POST /notification-preferences/save`
📌 Kullanıcının saat bazlı bildirim tercihlerini kaydeder. Kullanıcı hangi yerel saat dilimindeyse o alınıp aşağıdaki gibi parametre olarak gönderilmelidir. Arkaplanda BE servis bu zaman diliminin UTC dönüşümünü yapar ve DB ye kaydeder.

#### Örnek İstek:
```json
{
  "userId": "test",
  "deviceId": "test1",
  "timeZone": "Europe/Istanbul",
  "userLocalTimes": ["08:30", "12:00", "19:45"],
  "enabled": true
}
```
### `POST /push/broadcast`
📌 Tüm uygun cihazlara toplu bildirim gönderir. platform: both, ios, android olarak verilmelidir.

#### Örnek İstek:
```json
{
  "title": "Yeni özellik yayında!",
  "body": "Uygulamanızı güncellemeyi unutmayın.",
  "platform": "both"
}
```

### `ScheduledNotificationService`
📌 Bu servis arkaplanda her dakikada bir tetiklenir. DB de o dakika için planlanmış uygun affirmation bulunması durumunda ilgili kullanıcılara bildirim gönderir.

---

## ⚙️ Yapılandırma Hakkında

Uygulama yapılandırmaları .env dosyasındaki ortam değişkenleri üzerinden alınır ve application.properties bu değişkenlere referans verir.

Firebase servis hesabı dosyası (firebase-config.json) ve application-dev.properties gibi konfigürasyon dosyaları güvenlik sebebiyle bu repoya dahil edilmemiştir. Projenin doğru şekilde çalışabilmesi için bu dosyaların manuel olarak temin edilmesi ve uygun yerlere yerleştirilmesi gerekir. 
Yani: 
Dev application properties dosyası olmadan bu projeyi lokalinizde çalıştıramazsınız.
.env dosyası olmadan bu projeyi sunucuda da çalıştıramazsınız.

Servislerde güvenlik olarak CORS kısıtlaması mevcut. Ayarlarıyla ilgili detayları sorunuz.

